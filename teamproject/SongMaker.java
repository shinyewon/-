package teamproject;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SongMaker extends JFrame {
    private static final int ROWS = 8;
    private static final int COLS = 30;

    private final Color[] rowColors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK, Color.MAGENTA, Color.BLACK};

    public SongMaker() {
        setTitle("SongMaker");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 상단 패널 (제목)
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Compose melody");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // 중간 패널 (8x30 배열 버튼)
        JPanel gridPanel = new JPanel(new GridLayout(ROWS, COLS));
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton cellButton = new JButton();
                cellButton.setPreferredSize(new Dimension(10, 10));  // 셀 크기 조정
                cellButton.addActionListener(new CellClickListener(row, col));
                gridPanel.add(cellButton);
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

        // 전체 레이아웃 설정
        setLayout(new BorderLayout());
        add(titlePanel, BorderLayout.NORTH);
        add(gridPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
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

            if (originalColor == null) {
                // Save the original color before changing
                originalColor = clickedButton.getBackground();
                clickedButton.setBackground(rowColors[row]);
            } else {
                // Restore the original color
                clickedButton.setBackground(originalColor);
                originalColor = null;
            }

            System.out.println("Clicked on cell: (" + row + ", " + col + ")");
        }
    }

    private static class PlayButtonClickListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: 재생 버튼이 클릭되었을 때의 동작 추가
            System.out.println("Play button clicked");
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
