package match;

import android.renderscript.Sampler;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Game {
    // the host of the game
    public static String host;
    // the player of the game
    public static String player;
    // the game reference: /GAME/game_id
    public static String gameReference;
    // check if the user is the host
    public static boolean isHost;
    // the name of the host
    public static String hostName;
    // the name of the player
    public static String playerName;

    // default constructor
    public Game() {
    }

    // parameterized constructor: isHost check if the user is the host
    public Game(String gameReference, boolean isHost, String name){
        this.gameReference = gameReference;
        this.isHost = isHost;

        if (isHost){
            host = FirebaseAuth.getInstance().getCurrentUser().getUid();
            hostName = name;
            FirebaseDatabase.getInstance().getReference(gameReference).child(host).child("isHost").setValue(true);
            FirebaseDatabase.getInstance().getReference(gameReference).child(host).child("name").setValue(hostName);
        }
        else{
            player = FirebaseAuth.getInstance().getCurrentUser().getUid();
            playerName = name;
            FirebaseDatabase.getInstance().getReference(gameReference).child(player).child("isHost").setValue(false);
            FirebaseDatabase.getInstance().getReference(gameReference).child(player).child("name").setValue(playerName);
        }
    }

    public void printGame(){
        ValueEventListener enemyNameEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()){
                    System.out.println(data);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
    }
}
