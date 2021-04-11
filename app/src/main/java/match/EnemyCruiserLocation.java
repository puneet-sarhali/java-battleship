package match;

public class EnemyCruiserLocation {
    public int[][] location = new int[3][2];

    public EnemyCruiserLocation(){
    }

    // parameterized
    public EnemyCruiserLocation(int row1, int column1, int row2, int column2, int row3, int column3){
        location[0][0] = row1;
        location[0][1] = column1;
        location[1][0] = row2;
        location[1][1] = column2;
        location[2][0] = row3;
        location[2][1] = column3;
    }
}
