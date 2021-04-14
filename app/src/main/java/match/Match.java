package match;

// contains player match information in the matching process
public class Match {

    // reference to the host that created the game
    private String host;

    // reference to the game
    private String gameReference;

    // get the host of the game
    public String getHost() {
        return host;
    }

    // set the host in the game
    public void setHost(String host) {
        this.host = host;
    }

    // set the game path
    public void setGameReference(String gameReference) {
        this.gameReference = gameReference;
    }

    // get the game path
    public String getGameReference() {
        return gameReference;
    }

    // default constructor that's needed for firebase
    public Match() {

    }

    // parameterized constructor for our won purpose
    public Match(String host, String gameReference) {
        this.host = host;
        this.gameReference = gameReference;
    }
}
