package match;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseGame {
    public interface onCompletion {
        void onSuccess();
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

    public static ValueEventListener mValueEventListener;

    // default constructor
    public FirebaseGame() {
    }

    public static void setUserValue(boolean isHost, String auth, String gameReference, String name){
        FirebaseGame.gameReference = gameReference;
        FirebaseGame.isHost = isHost;

        if (FirebaseGame.isHost){
            FirebaseGame.hostUID = auth;
            FirebaseGame.currentUID = auth;
            FirebaseGame.hostName = name;
            FirebaseGame.currentName = name;
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.hostUID).child("isHost").setValue(true);
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.hostUID).child("name").setValue(name);
            FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).child(FirebaseGame.hostUID).child("isReady").setValue(false);
        }
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

        mValueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data1 : snapshot.getChildren()){
                    for (DataSnapshot data2 : data1.getChildren()){
                        if (data2.getKey().equals("isHost") && data2.getValue().equals(!(FirebaseGame.isHost))){
                            if (FirebaseGame.isHost){
                                FirebaseGame.playerUID = data1.getKey();
                                FirebaseGame.opponentUID = data1.getKey();
                            }
                            else{
                                FirebaseGame.hostUID = data1.getKey();
                                FirebaseGame.opponentUID = data1.getKey();
                            }
                        }
                        String auth = (isHost) ? hostUID : playerUID;
                        if (!(data1.getKey().equals(auth)) && data2.getKey().equals("name")){
                            if(isHost){
                                FirebaseGame.playerName = data2.getValue().toString();
                                FirebaseGame.opponentName = data2.getValue().toString();
                            }
                            else{
                                FirebaseGame.hostName = data2.getValue().toString();
                                FirebaseGame.opponentName = data2.getValue().toString();
                            }
                        }
                    }
                }
                mOnGetValueEventListener.onSuccess();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Failed value event listener for getAuth");
                mOnGetValueEventListener.onFailure();
            }
        };

        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).addValueEventListener(mValueEventListener);
    }

    public static void removeOpponentValueListener(){
        FirebaseDatabase.getInstance().getReference(FirebaseGame.gameReference).removeEventListener(mValueEventListener);
    }

    public static void printValue(){
        System.out.println(hostUID);
        System.out.println(playerUID);
        System.out.println(gameReference);
        System.out.println(isHost);
        System.out.println(hostName);
        System.out.println(playerName);
    }
}
