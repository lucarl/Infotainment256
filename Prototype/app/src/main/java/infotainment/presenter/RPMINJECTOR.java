package infotainment.presenter;

import java.util.Timer;

public class RPMINJECTOR extends Timer implements Runnable
{
    Thread timerThread;

    public RPMINJECTOR(int generatedPerSec)
    {
        timerThread = new Thread(this);



        /* Class init vars */


        /* Starts run */
        timerThread.run();
    }

    public void run()
    {
        while (true)
        {
            schedule(new rpmtimertask(), 0);
            try {
                timerThread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /* Scheduele Timertask x times a second*/
        }
    }

}