package match;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// the class stores all the user and game info that's also in the database, but not grid info
public class FirebaseGame {
    // a callback interface that will be called when the event listener finish reading data
    public interface onCompletion {
        // run this once successfully reading the data
        void onSuccess();
        // run this once failed reading the data
        void onFailure();
    }

    // the host of the game
    public static String hostUID;
    // the player of the game
    public static String playerUID;
    // the current UID
    public static String currentUID;
    // the opponent's UID
    public static String opponentUID;
    // the game reference: /GAME/game_id
    public static String gameReference;
    // check if the user is the host
    public static boolean isHost;
    // the name of the host
    public static String hostName;
    // the name of the player
    public static String playerName;
    // the name of the current player
    public static String currentName;
    // the name of the opponent player
    public static String opponentName;
    // the event listener attached in Firebase
    public static ValueEventListener mValueEventListener;

    // default constructor
    public FirebaseGame() {
    }

    // set the user values in the static field, and store them in Firebase database
    public static void setUserValue(boolean isHost, String auth, String gameReference, String name){
        // assign the game path and whether or not the user is host
        FirebaseGame.gameReference = gameReference;
        FirebaseGame.isHost = isHost;

        // if the user is host, assign the host ID, name to the user, and store the info in the database
        if (FirebaseGame.isHost){
            FirebaseGame.hostUID = auth;
            FirebaseGame.currentUID = auth;
            FirebaseGame.hostName = name;
            FirebaseGame.currentName = name;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.hostUID).child("isHost").setValue(true);
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.hostUID).child("name").setValue(name);
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.hostUID).child("isReady").setValue(false);
        }
        // if the user is not host, assign the player ID, name to the user, and store them in the database
        else{
            FirebaseGame.playerUID = auth;
            FirebaseGame.currentUID = auth;
            FirebaseGame.playerName = name;
            FirebaseGame.currentName = name;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.playerUID).child("isHost").setValue(false);
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.playerUID).child("name").setValue(name);
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.playerUID).child("isReady").setValue(false);
        }
    }

    // the method get the opponent value from the database and set it in the variable
    public static void setOpponentValue(onCompletion mOnGetValueEventListener){

        // initialize the value event listener in the firebase so that it can be easily removed later
        mValueEventListener = new ValueEventListener() {

            // call this once the data changed
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // in the database, go to the opponent UID node, and get the info from there
                for (DataSnapshot data1 : snapshot.getChildren()){
                    for (DataSnapshot data2 : data1.getChildren()){
                        // get the opponent UID
                        if (data2.getKey().equals("isHost") && data2.getValue().equals(!(FirebaseGame.isHost))){
                            // if the user is host, then assign the player UID to the opponent
                            if (FirebaseGame.isHost){
                                FirebaseGame.playerUID = data1.getKey();
                                FirebaseGame.opponentUID = data1.getKey();
                            }
                            // if the user is not host, then assign the host UID to the opponent
                            else{
                                FirebaseGame.hostUID = data1.getKey();
                                FirebaseGame.opponentUID = data1.getKey();
                            }
                        }
                        // set the auth variable to the opponent UID
                        String auth = (isHost) ? hostUID : playerUID;
                        // get the opponent's name
                        if (!(data1.getKey().equals(auth)) && data2.getKey().equals("name")){
                            // if the user is host, assign the player name to the opponent
                            if(isHost){
                                FirebaseGame.playerName = data2.getValue().toString();
                                FirebaseGame.opponentName = data2.getValue().toString();
                            }
                            // if the user is not host, assign the host name to the opponent
                            else{
                                FirebaseGame.hostName = data2.getValue().toString();
                                FirebaseGame.opponentName = data2.getValue().toString();
                            }
                        }
                    }
                }
                // call the callback interface onSuccess methods once finishing reading the data
                mOnGetValueEventListener.onSuccess();
            }

            // if the event listener failed, return an error to the developer
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed value event listener for getAuth");
                mOnGetValueEventListener.onFailure();
            }
        };

        // add the value event listener to the game reference
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).addValueEventListener(mValueEventListener);
    }

    // remove the value event listener set in the opponent's game node in the database
    public static void removeOpponentValueListener(){
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).removeEventListener(mValueEventListener);
    }

    // for testing purpose, we can print the values of the fields
    public static void printValue(){
        System.out.println(hostUID);
        System.out.println(playerUID);
        System.out.println(gameReference);
        System.out.println(isHost);
        System.out.println(hostName);
        System.out.println(playerName);
    }
}
