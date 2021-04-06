package match;

// contains player match information in the matching process
public class Match {

    // reference to the host that created the game
    private String host;

    // reference to the game
    private String gameReference;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setGameReference(String gameReference) {
        this.gameReference = gameReference;
    }

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
