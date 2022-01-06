import javax.swing.*;
import java.awt.*;

class Canvas extends JPanel {
    private int field_size;
    private Cell[][] field;
    private boolean win, bangMine;

    Canvas(int field_size, Cell[][] field) {
        this.field_size = field_size;
        this.field = field;
    }

    void setWin(boolean win) {
        this.win = win;
    }

    void setBangMine(boolean bangMine) {
        this.bangMine = bangMine;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        for(int x = 0; x < field_size; x++) {
            for(int y = 0; y < field_size; y++) {
                field[y][x].paint(g, x, y, bangMine, win);
            }
        }
    }
}