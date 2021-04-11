package match;

public class EnemyBattleshipLocation {
    // 4 squares, 0 index is row, 1 index is column
    // the value of the location ranges from 0 to 7
    public int[][] location = new int[4][2];

    // default
    public EnemyBattleshipLocation(){
    }

    // parameterized
    public EnemyBattleshipLocation(int row1, int column1, int row2, int column2, int row3, int column3,
                                   int row4, int column4){
        location[0][0] = row1;
        location[0][1] = column1;
        location[1][0] = row2;
        location[1][1] = column2;
        location[2][0] = row3;
        location[2][1] = column3;
        location[3][0] = row4;
        location[3][1] = column4;
    }
}
