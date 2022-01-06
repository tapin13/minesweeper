import java.awt.*;

class Cell {
    private boolean isOpen, isMine, isFlag;
    private int countBombNear;
    private int block_size;

    final String FLAG = "F";
    final int[] COLORS = { 0x0000FF, 0x008000, 0xFF0000, 0x800000, 0x000000 };

    Cell(int block_size) {
        this.block_size = block_size;
    }

    void open() {
        isOpen = true;
    }

    boolean isNotOpen() {
        return !isOpen;
    }

    boolean isMined() {
        return isMine;
    }

    void mine() {
        isMine = true;
    }

    void inverseFlag() {
        isFlag = !isFlag;
    }

    void setCountBomb(int count) {
        countBombNear = count;
    }

    int getCountBomb() {
        return countBombNear;
    }

    void paintBomb(Graphics g, int x, int y, Color color) {
        g.setColor(color);
        g.fillRect(x * block_size + 7, y * block_size + 10, 18, 10);
        g.fillRect(x * block_size + 11, y * block_size + 6, 10, 18);
        g.fillRect(x * block_size + 9, y * block_size + 8, 14, 14);
        g.setColor(Color.WHITE);
        g.fillRect(x * block_size + 11, y * block_size + 10, 4, 4);
    }

    void paintString(Graphics g, String string, int x, int y, Color color) {
        g.setColor(color);
        g.setFont(new Font("", Font.BOLD, block_size));
        g.drawString(string, x * block_size + 8, y * block_size + 26);
    }

    void paint(Graphics g, int x, int y, boolean bangMine, boolean win) {
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(x * block_size, y * block_size, block_size, block_size);
        if(!isOpen) {
            if((bangMine || win) && isMine) {
                paintBomb(g, x, y, Color.BLACK);
            } else {
                g.setColor(Color.LIGHT_GRAY);
                g.fill3DRect(x * block_size, y * block_size, block_size, block_size, true);
                if(isFlag) {
                    paintString(g, FLAG, x, y, Color.RED);
                }
            }
        } else {
            if(isMine) {
                paintBomb(g, x, y, bangMine ?  Color.RED :  Color.BLACK);
            } else {
                if(countBombNear > 0) {
                    paintString(g, Integer.toString(countBombNear), x, y, new Color(COLORS[countBombNear - 1]));
                }
            }
        }
    }
}