package match;

import android.annotation.SuppressLint;

// data analysis
public class Analysis {
    public static int hitCounter;
    public static int missCounter;
    public static float hitRate;

    static {
        hitCounter = 0;
        missCounter = 0;
        hitRate = 0.0f;
    }

    // default constructor
    public Analysis(){
    }

    @SuppressLint("DefaultLocale")
    public static String getHitRate(){
        if (hitCounter + missCounter == 0){
            return "0";
        }
        else{
            hitRate = (float) hitCounter / ((float) hitCounter + (float) missCounter) * 100;
            return String.format("%.2f", hitRate);
        }

    }

    public static void resetCounter(){
        hitCounter = 0;
        missCounter = 0;
        hitRate = 0.0f;
    }
}
