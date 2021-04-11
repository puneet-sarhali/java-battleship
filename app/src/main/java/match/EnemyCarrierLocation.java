package match;

public class EnemyCarrierLocation {
    public int[][] location = new int[5][2];

    public EnemyCarrierLocation(){
    }

    // parameterized
    public EnemyCarrierLocation(int row1, int column1, int row2, int column2, int row3, int column3,
                                   int row4, int column4, int row5, int column5){
        location[0][0] = row1;
        location[0][1] = column1;
        location[1][0] = row2;
        location[1][1] = column2;
        location[2][0] = row3;
        location[2][1] = column3;
        location[3][0] = row4;
        location[3][1] = column4;
        location[4][0] = row5;
        location[4][1] = column5;
    }
}
