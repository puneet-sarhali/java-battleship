package gridPackage;

import com.example.myapplication.imageAdapter;

public class Grid {
    Square[][] sea;
    int row;
    int column;

    private int getRow(int position) {
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

    public void setColumn(int column) {
        this.column = column;
    }
    

}
