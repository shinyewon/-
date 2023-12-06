package teamproject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class SongMaker extends JFrame {
    private final JSlider intervalSlider;
    private static final int ROWS = 8;
    private static final int COLS = 30;
    private final JButton[][] buttons; // 버튼을 담을 2차원 배열

    private final Color[] rowColors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK, Color.MAGENTA, Color.BLACK};

    public SongMaker() {
        setTitle("SongMaker");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        // 상단 패널 (제목)
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Compose melody");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        // 중간 패널 (8x30 배열 버튼)
        JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLS));
        buttons = new JButton[ROWS][COLS];

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(10, 10));  // 셀 크기 조정
                cellButton.addActionListener(new CellClickListener(row, col));
                gridPanel.add(cellButton);

                buttons[row][col] = cellButton; // 버튼을 배열에 추가
            }
        }

        // 하단 패널 (재생, 리셋 버튼)
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        // 재생 버튼
        JButton playButton = new JButton("Play");
        playButton.addActionListener(new PlayButtonClickListener());
        bottomPanel.add(playButton);

        // 리셋 버튼
        JButton resetButton = new JButton("Reset");
        resetButton.addActionListener(new ResetButtonClickListener());
        bottomPanel.add(resetButton);

        //

        intervalSlider = new JSlider(JSlider.HORIZONTAL, 0, 10, 3);
        bottomPanel.add(intervalSlider);
        initializeSlider(intervalSlider);

        // 전체 레이아웃 설정
        add(titlePanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void initializeSlider(JSlider intervalSlider) {
        intervalSlider.setMajorTickSpacing(1);
        intervalSlider.setPaintTicks(true);
        intervalSlider.setPaintLabels(true);

        // 슬라이더 값 변경 이벤트를 처리하는 리스너 추가
        intervalSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // 슬라이더 값이 변경될 때의 추가 로직
                // 예: 변경된 값을 출력
                System.out.println("Slider Value: " + intervalSlider.getValue());
            }
        });
    }

    private String melodyFind(int row) {
        // WAV 파일 재생
        return switch (row) {
            case 0 -> "C:\\Users\\LG\\Downloads\\do.wav";
            case 1 -> "C:\\Users\\LG\\Downloads\\re.wav";
            case 2 -> "C:\\Users\\LG\\Downloads\\mi.wav";
            case 3 -> "C:\\Users\\LG\\Downloads\\fa.wav";
            case 4 -> "C:\\Users\\LG\\Downloads\\sol.wav";
            case 5 -> "C:\\Users\\LG\\Downloads\\la.wav";
            case 6 -> "C:\\Users\\LG\\Downloads\\si.wav";
            case 7 -> "C:\\Users\\LG\\Downloads\\do2.wav";
            // Add more cases for other columns as needed
            default -> ""; // Set a default value or handle it according to your needs
        };

    }
    private void playWAV(String wavFilePath) {
        try {
            File soundFile = new File(wavFilePath);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            clip.start();

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private class CellClickListener implements ActionListener {
        private final int row;
        private final int col;
        private Color originalColor;

        public CellClickListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton clickedButton = (JButton) e.getSource();
            String wavFilePath = melodyFind(row);

            if (originalColor == null) {
                // Save the original color before changing
                originalColor = clickedButton.getBackground();
                clickedButton.setBackground(rowColors[row]);
                playWAV(wavFilePath);
            } else {
                // Restore the original color
                clickedButton.setBackground(originalColor);
                originalColor = null;
            }

            System.out.println("Clicked on cell: (" + row + ", " + col + ")");
        }
    }

    private class PlayButtonClickListener implements ActionListener {
        private int currentColumn = 0;
        private final Color[][] previousColors; // 이전 색을 저장하기 위한 배열

        public PlayButtonClickListener() {
            previousColors = new Color[ROWS][COLS];
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("Play button clicked");
            currentColumn = 0; // Reset column count

            // Stop the timer when all columns are played
            Timer timer = new Timer(1000 - intervalSlider.getValue() * 100, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (currentColumn >= COLS) {
                        restorePreviousColors(currentColumn);
                        ((Timer) e.getSource()).stop(); // Stop the timer when all columns are played
                    } else {
                        playColumnSounds(currentColumn);
                        updateButtonColors(currentColumn);
                        currentColumn++;
                    }
                }
            });

            timer.start();
        }

        private void playColumnSounds(int col) {
            for (int row = 0; row < ROWS; row++) {
                JButton button = buttons[row][col];
                Color buttonColor = button.getBackground();

                if (buttonColor == rowColors[row]) {
                    String wavFilePath = melodyFind(row);
                    playWAV(wavFilePath);
                }
            }
        }

        private void updateButtonColors(int col) {
            for (int row = 0; row < ROWS; row++) {
                JButton button = buttons[row][col];
                Color buttonColor = button.getBackground();
                Color transparentSkyBlue = new Color(135, 206, 235, 150);
                Color combinedColor = new Color(
                        (buttonColor.getRed() + transparentSkyBlue.getRed()) / 2,
                        (buttonColor.getGreen() + transparentSkyBlue.getGreen()) / 2,
                        (buttonColor.getBlue() + transparentSkyBlue.getBlue()) / 2
                );

                // 이전 색을 저장하고 현재 색으로 업데이트
                previousColors[row][col] = buttonColor;
                button.setBackground(combinedColor);
            }

            // 이전 색을 복원
            restorePreviousColors(col);
        }

        private void restorePreviousColors(int col) {
            if (col > 0) {
                for (int row = 0; row < ROWS; row++) {
                    JButton button = buttons[row][col - 1];
                    button.setBackground(previousColors[row][col - 1]);
                }
            }
        }
    }


    private class ResetButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Reset 버튼 동작 구현
            Component[] buttons = getContentPane().getComponents();
            for (Component component : buttons) {
                if (component instanceof JPanel) {
                    Component[] panelComponents = ((JPanel) component).getComponents();
                    for (Component panelComponent : panelComponents) {
                        if (panelComponent instanceof JButton button) button.setBackground(null);
                    }
                }
            }
            System.out.println("Reset button clicked");
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(SongMaker::new);
    }
}