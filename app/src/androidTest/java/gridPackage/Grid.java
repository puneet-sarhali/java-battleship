package gridPackage;

public class Grid {
    Square[][] sea;
    int row;
    int column;

    private int getRow() {
        return row;
    }

    private int getColumn() {
        return column;
    }

    public Square getSquare(int r, int c){
        return sea[r][c];
    }

    private void setRow(int row) {
        this.row = row;
    }

    private void setColumn(int column) {
        this.column = column;
    }

}
