package match;

public class EnemySubmarineLocation {
    public int[][] location = new int[2][2];

    public EnemySubmarineLocation(){
    }

    // parameterized
    public EnemySubmarineLocation(int row1, int column1, int row2, int column2){
        location[0][0] = row1;
        location[0][1] = column1;
        location[1][0] = row2;
        location[1][1] = column2;
    }
}
