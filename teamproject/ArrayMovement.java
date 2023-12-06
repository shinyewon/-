package teamproject;
import javax.swing.*;
import java.awt.*;

public class ArrayMovement extends JFrame {
    private static final int ROWS = 8;
    private static final int COLS = 30;
    private static final char EMPTY = '.';
    private static final char MOVING = '*';

    private final char[][] array;
    private JButton[][] buttons; // 배열에 대응하는 버튼 배열
    private int currentCol = 0;

    public ArrayMovement() {
        array = new char[ROWS][COLS];
        initializeArray();
        initializeButtons();

        setTitle("Array Movement Animation");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Timer timer = new Timer(1000, e -> {
            moveArray();
            updateButtonColors();
            repaint();
        });

        timer.start();
    }

    private void initializeArray() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                array[i][j] = EMPTY;
            }
        }
    }

    private void initializeButtons() {
        buttons = new JButton[ROWS][COLS];

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                buttons[i][j] = new JButton();
                buttons[i][j].setPreferredSize(new Dimension(20, 20));
                add(buttons[i][j]);
            }
        }
    }

    private void updateButtonColors() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                if (array[i][j] == MOVING) {
                    buttons[i][j].setBackground(Color.CYAN);
                } else {
                    buttons[i][j].setBackground(Color.WHITE);
                }
            }
        }
    }

    private void moveArray() {
        if (currentCol > 0) {
            for (int row = 0; row < ROWS; row++) {
                array[row][currentCol - 1] = EMPTY;
            }
        }

        for (int row = 0; row < ROWS; row++) {
            array[row][currentCol] = MOVING;
        }

        currentCol++;

        if (currentCol >= COLS) {
            currentCol = 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ArrayMovement arrayMovement = new ArrayMovement();
            arrayMovement.setLayout(new GridLayout(ROWS, COLS));
            arrayMovement.setVisible(true);
        });
    }
}
