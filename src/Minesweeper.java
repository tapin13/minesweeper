import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;

public class Minesweeper extends JFrame {

    final String TITLE = "Minesweeper";
    final int LOCATION = 200;
    final int FIELD_SIZE = 9;
    final int BLOCK_SIZE = 30;
    final int FIELD_DX = 6;
    final int FIELD_DY = 28;
    final int FIELD_DY_TIMER = 17;
    int OS_FIELD_SIZE = 0;
    final int MOUSE_BUTTON_LEFT = 1;
    final int MOUSE_BUTTON_RIGHT = 3;
    final int MINES = 10;
    final Cell[][] field = new Cell[FIELD_SIZE][FIELD_SIZE];
    Random random = new Random();
    int openCells;
    boolean win, bangMine;
    final Canvas canvas;
    final String OS = System.getProperty("os.name").toLowerCase();

    public static void main(String[] args) {
        new Minesweeper();
    }

    Minesweeper() {
        if(OS.startsWith("windows")) {
            OS_FIELD_SIZE = 11;
        }

        System.out.println(OS);
        System.out.println(OS.startsWith("Windows"));

        setTitle(TITLE);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBounds(LOCATION, LOCATION,
                FIELD_SIZE * BLOCK_SIZE + FIELD_DX + OS_FIELD_SIZE,
                FIELD_SIZE * BLOCK_SIZE + FIELD_DY + OS_FIELD_SIZE + FIELD_DY_TIMER);
        setResizable(false);
        final TimerLabel timerLabel = new TimerLabel();
        timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        canvas = new Canvas(FIELD_SIZE, field);
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent mouseEvent) {
                super.mouseReleased(mouseEvent);

                int x = mouseEvent.getX() / BLOCK_SIZE;
                int y = mouseEvent.getY() / BLOCK_SIZE;

                if(mouseEvent.getButton() == MOUSE_BUTTON_LEFT && !bangMine && !win) {
                    if(field[y][x].isNotOpen()) {
                        openCell(x, y);
                        win = (openCells == (FIELD_SIZE * FIELD_SIZE - MINES));
                        canvas.setWin(win);
                    }
                }

                if(mouseEvent.getButton() == MOUSE_BUTTON_RIGHT) {
                    field[y][x].inverseFlag();
                }

                if(bangMine || win) {
                    timerLabel.stop();
                }

                canvas.repaint();
            }
        });
        add(BorderLayout.CENTER, canvas);
        add(BorderLayout.SOUTH, timerLabel);
        setVisible(true);
        initField();
    }

    void initField() {
        this.initCells(FIELD_SIZE, FIELD_SIZE);

        this.initMines(FIELD_SIZE, FIELD_SIZE);

        this.countBombNear(FIELD_SIZE, FIELD_SIZE);
    }

    void openCell(int x, int y) {
        if(x < 0 || y < 0 || x > FIELD_SIZE - 1 || y > FIELD_SIZE - 1) {
            return;
        }

        if(!field[y][x].isNotOpen()) {
            return;
        }

        field[y][x].open();
        bangMine = field[y][x].isMined();
        canvas.setBangMine(bangMine);
        if(!bangMine) {
            openCells++;
        }

        if(field[y][x].isMined() || field[y][x].getCountBomb() != 0) {
            return;
        }

        for(int i = -1; i < 2; i++) {
            for(int j = -1; j < 2; j++) {
                if(i == 0 && j == 0) {
                    continue;
                }
                openCell(x + i, y + j);
            }
        }
    }

    void initCells(int field_size_x, int field_size_y) {
        for(int x = 0; x < field_size_x; x++) {
            for(int y = 0; y < field_size_y; y++) {
                field[y][x] = new Cell(BLOCK_SIZE);
            }
        }
    }

    void initMines(int field_size_x, int field_size_y) {
        int x, y, mines = 0;

        while (mines < MINES) {
            do {
                x = random.nextInt(field_size_x);
                y = random.nextInt(field_size_y);
            } while (field[y][x].isMined());
            field[y][x].mine();
            mines++;
        }
    }

    void countBombNear(int field_size_x, int field_size_y) {
        for(int x = 0; x < FIELD_SIZE; x++) {
            for (int y = 0; y < FIELD_SIZE; y++) {
                if(!field[y][x].isMined()) {
                    int count = 0;
                    for(int dx = -1; dx < 2; dx++) {
                        for(int dy = -1; dy < 2; dy++) {
                            int nX = x + dx;
                            int nY = y + dy;
                            if(nX < 0 || nY < 0 || nX > FIELD_SIZE - 1 || nY > FIELD_SIZE - 1) {
                                nX = x;
                                nY = y;
                            }
                            count += (field[nY][nX].isMined()) ? 1 : 0;
                        }
                    }
                    field[y][x].setCountBomb(count);
                }
            }
        }
    }

}
