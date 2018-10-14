package infotainment.presenter;

import java.util.Random;

public class RpmGenerator
{
    public static boolean direction; /* up == true, down == false */
    public static int prev_int;
    public static Random random;
    public static double magnitude = 18d;

    public RpmGenerator()
    {
        prev_int = 22;
        random = new Random();
    }

    public static int rndInt()
    {
        if (prev_int > 80)
            direction = false;
        else if (prev_int < 0)
            direction = true;

        if (direction)
            prev_int += (int)(random.nextDouble() * magnitude);
        else
            prev_int +=  (int)(random.nextDouble() * magnitude * -1);

        return prev_int;
    }


}