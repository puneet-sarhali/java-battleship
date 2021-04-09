package match;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;

import java.util.Random;

// handle the match making process for the player
public class PlayerMatchMaking {
    // a callback interface that defines what happens after players are matched
    public interface MatchMadeCallback {
        public void run(PlayerMatchMaking matchMaking);
    }

    // an interface that describes what happens when failing to find matches
    public interface FailFindingMatch {
        void whenFail();
    }

    // constants that will be stored in the database
    public final static String RANDOM_ROOM = "/RANDOM_ROOM/";
    public final static String ROOM = "/WAITING_ROOM_ID/";
    public static final String PLAYER_GAME_MOVES = "/GAME/";

    // reference to the game room
    private DatabaseReference mRoomReference;
    // reference to the game challenge
    private DatabaseReference mGameChallengeReference;

    // reference to the host
    public String mHost;
    // reference to the game path
    public String mGameLocation;
    // If player created the game, it's 1, else it's 0
    public int mPlayerIdentity;
    // a boolean that checks if the user is the host of the game
    private boolean mIsHost;
    // the callback interface
    private MatchMadeCallback mMatchMade;

    // return a random number string from 0 - 999
    public static String getRandom(){
        Random rand = new Random();
        return String.valueOf(rand.nextInt(1000));

    }

    // parameterized constructor to create the object
    private PlayerMatchMaking(DatabaseReference roomReference, MatchMadeCallback MatchMaking) {
        mRoomReference = roomReference;
        mMatchMade = MatchMaking;
    }

    // create an instance for matching, game initializer and game deleter
    private Matching mMatching;
    private MatchInitializer mMatchInitializer;
    private MatchDeleter mMatchDeleter;

    // search for match
    public void searchMatch() {
        // the method runs in a different thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // create a match not found variable that describes what happens when failing finding match
                FailFindingMatch matchNotFound = new FailFindingMatch() {
                    @Override
                    // when failing finding match, we make the matching variable null, but create
                    // a challenge initializer and wait for others to match to us
                    public void whenFail() {
                        mMatching = null;
                        mMatchInitializer = new MatchInitializer();
                        mRoomReference.runTransaction(mMatchInitializer);
                    }
                };

                // give matching the fail finding match interface and run the transaction for matching
                mMatching = new Matching(matchNotFound);
                mRoomReference.runTransaction(mMatching);
            }
        }).start();  // start the thread immediately after calling the methods
    }

    // describes what happens when the match making halts
    public void halt() {
        // if there's no challenge initializer and there's already a deleter, don't do anything
        if (mMatchInitializer == null ||
                mMatchDeleter != null) {
            return;
        }

        // else: create a new challenge deleter that delete the matching info
        mMatchDeleter = new MatchDeleter(mMatchInitializer);
        mRoomReference.runTransaction(mMatchDeleter);
    }

    // the method is used to create an instance of the class
    public static PlayerMatchMaking createInstance(String userRoom, MatchMadeCallback matchMaking) {
        String room = ROOM + userRoom;
        return new PlayerMatchMaking(FirebaseDatabase.getInstance().getReference(room), matchMaking);
    }
    
    // check if the user is host
    public boolean isUserHost() {
        return mIsHost;
    }
    
    // describes what happens when the match is found
    private void matchFound(boolean isHost) {
        // see if the user is host
        mIsHost = isHost;
        // if the user is host, he has identity 1, else 0
        mPlayerIdentity = (isHost) ? 1 : 0;
        // run the match making 
        mMatchMade.run(this);
    }
    
    // Matching inner class describes the function of matching to other player
    private class Matching implements Transaction.Handler{
        // create an instance of match
        private Match mMatch = null;
        // create a fail finding match instance
        private final FailFindingMatch mFailFindingMatch;
        // parameterized constructor that takes in fail find match interface 
        Matching(@Nullable FailFindingMatch failCallback) {
            mFailFindingMatch = failCallback;
        }

        @NonNull
        @Override
        // does transaction
        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
            // find each match data in the firebase, match to the first data, and delete the first data
            for (MutableData matchData : mutableData.getChildren()) {

                mMatch = matchData.getValue(Match.class);;
                matchData.setValue(null);
                return Transaction.success(mutableData);
                
            }

            // if no data is found, mutableData is null
            return Transaction.success(mutableData);
        }

        @Override
        // run this method after finishing the transaction
        public void onComplete(@Nullable DatabaseError databaseError,
                               boolean b, @Nullable DataSnapshot dataSnapshot) {
            // if there's a person matched to the player, matching is complete
            if (mMatch != null) {
                mHost = mMatch.getHost();
                mGameLocation = mMatch.getGameReference();
                // call match found and tell it the player is not the host
                matchFound(false);
            } else if (mFailFindingMatch != null) {
                // if failing finding match, call the when fail method
                mFailFindingMatch.whenFail();
            }
        }
    }

    // the inner method initialize the match when no match is found in the database
    private class MatchInitializer implements Transaction.Handler, ValueEventListener{

        // create a match object, a reference to the match, and a reference to the game
        private final Match mMatch;
        private DatabaseReference mMatchReference;
        private DatabaseReference mGameReference;

        // default constructor
        private MatchInitializer() {
            mMatch = new Match(
                    FirebaseAuth.getInstance().getCurrentUser().getUid(), null);
        }

        @NonNull
        @Override
        // do the transaction
        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
            // game reference is pointed to the player game moves
            mGameReference = FirebaseDatabase.getInstance().getReference().child(PLAYER_GAME_MOVES).push();
            // set the match game reference
            mMatch.setGameReference(PLAYER_GAME_MOVES + mGameReference.getKey());
            // set the game path the match's reference
            mGameLocation = mMatch.getGameReference();

            // set the match reference the child of room reference, then set the match there
            mMatchReference = mRoomReference.push();
            mMatchReference.setValue(mMatch);
            mMatchReference.addValueEventListener(this);

            return Transaction.success(mutableData);
        }

        @Override
        // call this when the transaction is complete
        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
        }

        @Override
        // call this when the data changed in the firebase reference location
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            // if the value of the location is null, that means a player matched to us
            // set the match, game reference and match initializer to null
            if (dataSnapshot.getValue() == null) {
                mMatchReference = null;
                mGameReference = null;
                mMatchInitializer = null;
                // call the match found method and tell it the player is the host
                matchFound(true);
            }
        }

        @Override
        // call this when failing to initializing match
        public void onCancelled(@NonNull DatabaseError databaseError) {
            System.out.println("Initializing match failed");
        }
    }

    // the inner describes the deletion of a match
    private class MatchDeleter implements Transaction.Handler{
        // create an instance of match initializer
        private final MatchInitializer mMatchInitializer;

        // constructor that set the match initializer to the one defined in the parent class
        private MatchDeleter(MatchInitializer matchInitializer) {
            mMatchInitializer = matchInitializer;
        }

        @NonNull
        @Override
        // do the transaction
        public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
            // remove the event listener once since we added that event listener in match initializer once
            mMatchInitializer.mMatchReference.removeEventListener(mMatchInitializer);
            
            // set the string match key to the key of the match reference
            final String matchKey = mMatchInitializer.mMatchReference.getKey();

            // if the data in the game reference equals to the match key, delete that key
            for (MutableData matchNode : mutableData.getChildren()) {
                if (matchNode.getKey().contentEquals(matchKey)) {
                    matchNode.setValue(null);
                }
            }

            return Transaction.success(mutableData);
        }

        @Override
        // run this when complete the transaction
        public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {
            // set the match reference in the match initializer to null
            // set the game reference to null if it's not already done so
            mMatchInitializer.mMatchReference = null;
            final DatabaseReference gameReference = mMatchInitializer.mGameReference;

            if (gameReference != null) {
                gameReference.setValue(null);
            }
        }
    }
}
