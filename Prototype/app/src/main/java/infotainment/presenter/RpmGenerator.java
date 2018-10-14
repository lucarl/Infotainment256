package infotainment.presenter;

import java.util.Random;

public class RpmGenerator
{
    public static boolean direction; /* up == true, down == false */
    public static int prev_int;
    public static Random random;

    public RpmGenerator()
    {
        prev_int = 2000;
        random = new Random();
    }

    protected static int rndInt()
    {
        if (prev_int > 6000)
            direction = false;
        else if (prev_int < 1200)
            direction = true;

        if (direction)
            prev_int += (int)(random.nextDouble() * 100);
        else
            prev_int +=  (int)(random.nextDouble() * 100 * -1);

        return prev_int;
    }


}