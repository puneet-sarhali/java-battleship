package match;

public class EnemyDestroyerLocation {
    public int[][] location = new int[1][2];

    public EnemyDestroyerLocation(){
    }

    // parameterized
    public EnemyDestroyerLocation(int row1, int column1){
        location[0][0] = row1;
        location[0][1] = column1;
    }
}
