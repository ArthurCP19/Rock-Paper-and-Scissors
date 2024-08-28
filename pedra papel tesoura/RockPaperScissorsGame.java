import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class RockPaperScissorsGame extends JFrame implements ActionListener {
    private CardLayout cardLayout;
    private JPanel mainPanel, menuPanel, gamePanel, scorePanel, bottomPanel, creditsPanel;
    private JButton startButton, rockButton, paperButton, scissorsButton, creditsButton, backToMenuButton, exitButton;
    private JLabel resultLabel, playerChoiceLabel, cpuChoiceLabel;
    private JLabel playerScoreLabel, cpuScoreLabel, totalGamesLabel, drawsLabel;
    private int playerScore = 0;
    private int cpuScore = 0;
    private int totalGames = 0;
    private int draws = 0;
    private Random random = new Random();
    private Clip backgroundMusic;
    private Clip rockSound, paperSound, scissorsSound;
    private JSlider volumeSlider;
    private JButton volumeButton;
    private JLabel volumeIcon;

    public RockPaperScissorsGame() {
        setTitle("Pedra, Papel e Tesoura");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Painel do Menu
        menuPanel = new JPanel();
        menuPanel.setBackground(new Color(30, 30, 30));
        menuPanel.setLayout(new GridBagLayout());

        startButton = new JButton("Start Game");
        styleButton(startButton);
        startButton.setPreferredSize(new Dimension(300, 80));
        startButton.addActionListener(this);

        creditsButton = new JButton("Credits");
        styleButton(creditsButton);
        creditsButton.setPreferredSize(new Dimension(300, 80));
        creditsButton.addActionListener(this);

        exitButton = new JButton("EXIT");
        styleButton(exitButton);
        exitButton.setPreferredSize(new Dimension(100, 50));
        exitButton.addActionListener(e -> System.exit(0));

        // Painel de Volume
        JPanel volumePanel = new JPanel();
        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.X_AXIS));
        volumePanel.setBackground(new Color(30, 30, 30));

        volumeIcon = new JLabel(new ImageIcon("volume_icon.png")); // Caminho da imagem do ícone
        volumeIcon.setPreferredSize(new Dimension(50, 50));

        volumeSlider = new JSlider(0, 100, 50);
        styleSlider(volumeSlider);
        volumeSlider.setPreferredSize(new Dimension(200, 50));

        volumeButton = new JButton("Volume");
        styleVolumeButton(volumeButton);
        volumeButton.setPreferredSize(new Dimension(120, 50));

        volumeButton.addActionListener(e -> volumeSlider.setVisible(!volumeSlider.isVisible()));

        volumePanel.add(volumeIcon);
        volumePanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espaço entre a imagem e o botão
        volumePanel.add(volumeButton);
        volumePanel.add(Box.createRigidArea(new Dimension(10, 0))); // Espaço entre o botão e o slider
        volumePanel.add(volumeSlider);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        menuPanel.add(startButton, gbc);
        gbc.gridy = 1;
        menuPanel.add(creditsButton, gbc);
        gbc.gridy = 2;
        menuPanel.add(volumePanel, gbc);
        gbc.gridy = 3;
        menuPanel.add(exitButton, gbc); // Adiciona o botão EXIT ao menu

        // Painel de Créditos
        creditsPanel = new JPanel();
        creditsPanel.setBackground(new Color(30, 30, 30));
        creditsPanel.setLayout(new BorderLayout());

        JLabel creditsLabel = new JLabel("<html><div style='text-align: center;'>Créditos:<br>Arthur Cardim <br>Diego Gabriel <br>Vinicius Bassio</div></html>");
        creditsLabel.setForeground(Color.WHITE);
        creditsLabel.setFont(new Font("Arial", Font.BOLD, 24));
        creditsLabel.setHorizontalAlignment(SwingConstants.CENTER);
        creditsPanel.add(creditsLabel, BorderLayout.CENTER);

        JButton backButton = new JButton("Back");
        styleButton(backButton);
        backButton.setPreferredSize(new Dimension(150, 50));
        backButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));
        creditsPanel.add(backButton, BorderLayout.SOUTH);

        // Painel do Jogo
        gamePanel = new JPanel();
        gamePanel.setLayout(new BorderLayout(20, 20));
        gamePanel.setBackground(new Color(30, 30, 30)); // Cor igual à do menu iniciar

        // Labels para escolhas e resultado
        playerChoiceLabel = new JLabel("Your Choice: ");
        styleLabel(playerChoiceLabel);

        cpuChoiceLabel = new JLabel("CPU Choice: ");
        styleLabel(cpuChoiceLabel);

        resultLabel = new JLabel("Result: ");
        styleLabel(resultLabel);
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Painel de Pontuação no topo
        scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(1, 2, 20, 20));
        scorePanel.setBackground(new Color(30, 30, 30));
        scorePanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        playerScoreLabel = new JLabel("<html><div style='text-align: center;'>Your Score:<br><font size='6'>" + playerScore + "</font></div></html>");
        styleLabel(playerScoreLabel);

        cpuScoreLabel = new JLabel("<html><div style='text-align: center;'>CPU Score:<br><font size='6'>" + cpuScore + "</font></div></html>");
        styleLabel(cpuScoreLabel);

        scorePanel.add(playerScoreLabel);
        scorePanel.add(cpuScoreLabel);

        // Painel de Botões no centro
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 3, 20, 0));
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));

        rockButton = new JButton(new ImageIcon("rock_icon.png"));
        paperButton = new JButton(new ImageIcon("paper_icon.png"));
        scissorsButton = new JButton(new ImageIcon("scissors_icon.png"));

        styleButton(rockButton);
        styleButton(paperButton);
        styleButton(scissorsButton);

        rockButton.setPreferredSize(new Dimension(150, 150));
        paperButton.setPreferredSize(new Dimension(150, 150));
        scissorsButton.setPreferredSize(new Dimension(150, 150));

        rockButton.addActionListener(this);
        paperButton.addActionListener(this);
        scissorsButton.addActionListener(this);

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);

        // Painel Inferior para Total de Partidas e Empates
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new GridLayout(2, 1, 0, 10));
        bottomPanel.setBackground(new Color(30, 30, 30));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(20, 100, 50, 100));

        totalGamesLabel = new JLabel("<html><div style='text-align: center;'>Total Games:<br><font size='6'>" + totalGames + "</font></div></html>");
        styleLabel(totalGamesLabel);

        drawsLabel = new JLabel("<html><div style='text-align: center;'>Draws:<br><font size='6'>" + draws + "</font></div></html>");
        styleLabel(drawsLabel);

        bottomPanel.add(totalGamesLabel);
        bottomPanel.add(drawsLabel);

        // Criar o botão "Voltar ao Menu"
        backToMenuButton = new JButton("Voltar ao Menu");
        styleButton(backToMenuButton);  // Reutiliza o método de estilo já existente
        backToMenuButton.setPreferredSize(new Dimension(150, 50));

        // Adicionar ActionListener para voltar ao menu
        backToMenuButton.addActionListener(e -> cardLayout.show(mainPanel, "Menu"));

        // Adicionar o botão ao canto inferior direito do gamePanel
        JPanel bottomRightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomRightPanel.setBackground(new Color(30, 30, 30)); // Cor de fundo igual à do gamePanel
        bottomRightPanel.add(backToMenuButton);

        // Adicionar o painel ao gamePanel
        gamePanel.add(scorePanel, BorderLayout.NORTH);
        gamePanel.add(buttonPanel, BorderLayout.CENTER);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);
        gamePanel.add(bottomRightPanel, BorderLayout.SOUTH);

        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(gamePanel, "Game");
        mainPanel.add(creditsPanel, "Credits");

        add(mainPanel);
        cardLayout.show(mainPanel, "Menu");

        loadBackgroundMusic();
        loadSoundEffects(); // Carregar os efeitos sonoros
        setVisible(true);
    }

    private void loadBackgroundMusic() {
        try {
            File musicFile = new File("background_music.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void loadSoundEffects() {
        try {
            rockSound = AudioSystem.getClip();
            rockSound.open(AudioSystem.getAudioInputStream(new File("rock_sound.wav")));
            paperSound = AudioSystem.getClip();
            paperSound.open(AudioSystem.getAudioInputStream(new File("paper_sound.wav")));
            scissorsSound = AudioSystem.getClip();
            scissorsSound.open(AudioSystem.getAudioInputStream(new File("scissors_sound.wav")));
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playSound(Clip clip) {
        if (clip != null) {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    private void adjustVolume() {
        if (backgroundMusic != null) {
            FloatControl volumeControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = (float) volumeSlider.getValue() / 100;
            float dB = (float) (Math.log(volume == 0 ? 0.0001 : volume) / Math.log(10.0) * 20.0);
            volumeControl.setValue(dB);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(0, 100, 255));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 20));
    }

    private void styleLabel(JLabel label) {
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 24));
        label.setHorizontalAlignment(SwingConstants.CENTER);
    }

    private void styleSlider(JSlider slider) {
        slider.setBackground(new Color(30, 30, 30));
        slider.setForeground(Color.WHITE);
        slider.addChangeListener(e -> adjustVolume());
    }

    private void styleVolumeButton(JButton button) {
        button.setBackground(new Color(70, 70, 70));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 16));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            cardLayout.show(mainPanel, "Game");
        } else if (e.getSource() == creditsButton) {
            cardLayout.show(mainPanel, "Credits");
        } else if (e.getSource() == rockButton) {
            playSound(rockSound);
            playGame("Rock");
        } else if (e.getSource() == paperButton) {
            playSound(paperSound);
            playGame("Paper");
        } else if (e.getSource() == scissorsButton) {
            playSound(scissorsSound);
            playGame("Scissors");
        }
    }

    private void playGame(String playerChoice) {
        String[] choices = {"Rock", "Paper", "Scissors"};
        String cpuChoice = choices[random.nextInt(choices.length)];
        playerChoiceLabel.setText("Your Choice: " + playerChoice);
        cpuChoiceLabel.setText("CPU Choice: " + cpuChoice);

        if (playerChoice.equals(cpuChoice)) {
            resultLabel.setText("Result: Draw!");
            draws++;
        } else if ((playerChoice.equals("Rock") && cpuChoice.equals("Scissors")) ||
                (playerChoice.equals("Paper") && cpuChoice.equals("Rock")) ||
                (playerChoice.equals("Scissors") && cpuChoice.equals("Paper"))) {
            resultLabel.setText("Result: You Win!");
            playerScore++;
        } else {
            resultLabel.setText("Result: You Lose!");
            cpuScore++;
        }

        totalGames++;
        updateScores();
    }

    private void updateScores() {
        playerScoreLabel.setText("<html><div style='text-align: center;'>Your Score:<br><font size='6'>" + playerScore + "</font></div></html>");
        cpuScoreLabel.setText("<html><div style='text-align: center;'>CPU Score:<br><font size='6'>" + cpuScore + "</font></div></html>");
        totalGamesLabel.setText("<html><div style='text-align: center;'>Total Games:<br><font size='6'>" + totalGames + "</font></div></html>");
        drawsLabel.setText("<html><div style='text-align: center;'>Draws:<br><font size='6'>" + draws + "</font></div></html>");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RockPaperScissorsGame::new);
    }
}
