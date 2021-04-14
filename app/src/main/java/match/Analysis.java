package match;

import android.annotation.SuppressLint;

// data analysis on the game
public class Analysis {
    // a counter that counts how many times the user hit the target
    public static int hitCounter;
    // a counter that counts how many times the user miss the target
    public static int missCounter;
    // calculate the hit rate of the user
    public static float hitRate;

    // upon creation of the class, run the static initialization block
    static {
        // set every field to 0
        hitCounter = 0;
        missCounter = 0;
        hitRate = 0.0f;
    }

    // default constructor
    public Analysis(){
    }

    // get the hit rate by with calculation from the hit counter and miss counter
    @SuppressLint("DefaultLocale")
    public static String getHitRate(){
        // if the user hasn't hit anything return 0
        // this is to prevent division by 0 exception
        if (hitCounter + missCounter == 0){
            return "0";
        }
        // if the denominator is not 0, calculate the hit rate
        else{
            hitRate = (float) hitCounter / ((float) hitCounter + (float) missCounter) * 100;
            return String.format("%.2f", hitRate);
        }

    }

    // reset the counters once the game finish
    public static void resetCounter(){
        hitCounter = 0;
        missCounter = 0;
        hitRate = 0.0f;
    }
}
