package match;

import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.TransitionRes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

// handles the player being ready event in the ship placement section
public class Ready {

    // describes what happens when both players are ready
    public interface ReadyMatchComplete{
        void run();
    }

    ReadyMatchComplete mReadyMatchComplete;
    ValueEventListener mValueEventListener;

    // a interface that describes what happens when the opponent's not ready yet
    public interface ReadyMatchFail{
        void onFail();
    }

    // a reference to the user's isReady node under user's UID
    DatabaseReference userIsReadyReference;
    // a reference to the opponent's isReady node under opponent's UID
    DatabaseReference opponentIsReadyReference;

    // whether the opponent is ready or not
    Boolean readyValue;

    // create instances for inner classes
    ReadyMatch mReadyMatch;
    ReadyInitializer mReadyInitializer;

    // a default constructor
    public Ready(){
    }

    // a parameterized constructor
    public Ready(DatabaseReference userIsReadyReference, DatabaseReference opponentIsReadyReference,
                 ReadyMatchComplete mReadyMatchComplete){
        this.userIsReadyReference = userIsReadyReference;
        this.opponentIsReadyReference = opponentIsReadyReference;
        this.mReadyMatchComplete = mReadyMatchComplete;
    }

    // be ready for the game
    public void beReady(){
        // run on a different thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // set a interface that describes what happens when opponent is not ready
                ReadyMatchFail opponentNotReady = new ReadyMatchFail() {
                    @Override
                    public void onFail() {
                        // the match state is set to null, but we initialize the ready state
                        mReadyMatch = null;
                        mReadyInitializer = new ReadyInitializer();
                        opponentIsReadyReference.runTransaction(mReadyInitializer);
                    }
                };
                mReadyMatch = new ReadyMatch(opponentNotReady);
                opponentIsReadyReference.runTransaction(mReadyMatch);
            }
        }).start();
    }

    // what happens when both players are ready
    private void onReady(){
        mReadyMatchComplete.run();
    }

    // initialize the ready event if the other
    public class ReadyInitializer implements Transaction.Handler, ValueEventListener {

        // default constructor
        public ReadyInitializer(){
        }

        @NonNull
        @Override
        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
            userIsReadyReference.setValue(true);
            opponentIsReadyReference.addValueEventListener(this);

            return Transaction.success(currentData);
        }

        @Override
        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            if (snapshot.getValue().equals(true)) {
                onReady();
                opponentIsReadyReference.removeEventListener(this);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    }

    public class ReadyMatch implements Transaction.Handler{

        ReadyMatchFail mReadyMatchFail;

        // parameterized
        public ReadyMatch(ReadyMatchFail readyMatchFail){
            mReadyMatchFail = readyMatchFail;
        }

        @NonNull
        @Override
        public Transaction.Result doTransaction(@NonNull MutableData currentData) {
            // ready value is either true or false
            readyValue = currentData.getValue(Boolean.class);
            userIsReadyReference.setValue(true);
            return Transaction.success(currentData);
        }

        @Override
        public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {
            if (readyValue){
                onReady();
            } else {
                mReadyMatchFail.onFail();
            }
        }
    }
}
