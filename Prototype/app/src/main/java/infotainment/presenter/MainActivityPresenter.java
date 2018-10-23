package infotainment.presenter;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarSensorEvent;
import android.car.hardware.CarSensorManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import infotainment.Model.DataFilter;
import infotainment.Model.MainActivityModel;
import infotainment.Model.db.Db;
import infotainment.Model.db.LogDb;
import infotainment.contract.LogDbContract;
import infotainment.contract.MainActivityContract;

import static infotainment.Model.MainActivityModel.ecoPointsRef;

public class MainActivityPresenter extends AppCompatActivity implements MainActivityContract.Presenter {

    private MainActivityContract.View mView;
    private MainActivityContract.Model mModel;
    private DataFilter dataFilter;

    Car car;
    Handler handler;
    ServiceConnection serviceConnection;

    //CarHvacManager carHvacManager;
    //CarHvacManager.CarHvacEventCallback carHvacEventCallback;
    CarSensorManager sensorManager;

    CarSensorManager.OnSensorChangedListener FuelLevelStateChangeListener;
    CarSensorManager.OnSensorChangedListener RPMStateChangeListener;
    CarSensorManager.OnSensorChangedListener gearMonitor;

    boolean useFuelData = false;
    boolean useSpeedData = false;
    private static final int speedDataPermissionMagicNumber = 42;
    private Context context;


    double lambda;
    double tilt;

    private Timer eventTimer;
    private TimerTask eventTask;
    private boolean timerAlive = false;

    int testSpeed = 10;
    int testGear = 1;
    int testRPM = 10;
    int variation = 1;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {

        this.context = context;
        mView = view;
        initPresenter();
        new LogDb(context);
        //this.dataFilter = new DataFilter();
    }

    private void initPresenter () {

        mModel = new MainActivityModel();
        mView.initView();



        this.lambda = 10;                   //  l/100km
        this.tilt = 0;                    //grader lutning

        mModel.setecoCal(lambda, tilt);

        dataFilter = new DataFilter();

        RPMStateChangeListener = new CarSensorManager.OnSensorChangedListener() {
                @Override
                public void onSensorChanged(CarSensorEvent carSensorEvent) {
                    CarSensorEvent.RpmData rpmData = carSensorEvent.getRpmData(null);

                    if (rpmData.rpm != 0.0) {

                        Date currentTime = Calendar.getInstance().getTime();

                        Log.d("CAR", "RPM stuff " + currentTime);

                        //------------->PUT EVENT HANDLING FOR RPM HERE<-------------

                        TextView logTextView = mView.getResultTextView();

                        if (logTextView.getLineCount() >= 10) {
                            String text = logTextView.getEditableText().toString();
                            logTextView.setText(text.substring(text.indexOf(System.getProperty("line.separator")) + 1));
                        }

                        logTextView.append("\nRPM data: " + rpmData.rpm);
                    }
                }
        };

        gearMonitor = new CarSensorManager.OnSensorChangedListener() {
                @Override
                public void onSensorChanged(CarSensorEvent carSensorEvent) {
                    Log.d("CAR", "Gear data event...");

                    CarSensorEvent.GearData gearData = carSensorEvent.getGearData(null);

                    //------------->PUT STATE HANDLING FOR GEAR HERE<-------------

                    TextView logTextView = mView.getResultTextView();

                    if (logTextView.getLineCount() >= 10) {
                        String text = logTextView.getEditableText().toString();
                        logTextView.setText(text.substring(text.indexOf(System.getProperty("line.separator")) + 1));
                    }

                    logTextView.append("\nGear data: " + gearData.gear);
                }
        };

        FuelLevelStateChangeListener = new CarSensorManager.OnSensorChangedListener() {
                @Override
                public void onSensorChanged(CarSensorEvent carSensorEvent) {

                    CarSensorEvent.FuelLevelData fuelData = carSensorEvent.getFuelLevelData(null);

                    Log.d("CAR", "Fuel stuff");

                    //------------->PUT EVENT HANDLING FOR FUEL HERE<-------------

                    TextView logTextView = mView.getResultTextView();

                    if (logTextView.getLineCount() >= 10) {
                        String text = logTextView.getEditableText().toString();
                        logTextView.setText(text.substring(text.indexOf(System.getProperty("line.separator")) + 1));
                    }

                    logTextView.append("\nRPM data: " + fuelData.range);
                }
        };

            // get permissions
        /*
        if (ContextCompat.checkSelfPermission(context, Car.PERMISSION_FUEL)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("CAR", "Requesting permission to use fuel events.");
            ActivityCompat.requestPermissions((Activity)mView,
                    new String[] {Car.PERMISSION_FUEL}, 101);
        } else {
            // permission already given
            Log.d("CAR", "Permission available to use fuel events.");
            useFuelData = true;
        }*/

        /*
        if (ContextCompat.checkSelfPermission(this, Car.PERMISSION_SPEED)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("CAR", "Requesting permission to use fuel events.");
            ActivityCompat.requestPermissions(this,
                    new String[] {Car.PERMISSION_SPEED}, speedDataPermissionMagicNumber);
        } else {
            // permission already given
            Log.d("CAR", "Permission available to use speed events.");
            useSpeedData = true;
        }*/

        serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                    Log.d("CAR", "Service connected");

                    try {
                        // connect
                        sensorManager = (CarSensorManager) car.getCarManager(Car.SENSOR_SERVICE);
                        //carHvacManager = (CarHvacManager) car.getCarManager(Car.HVAC_SERVICE);

                        // report connections
                        if (sensorManager != null)
                            Log.d("CAR", "Sensor manager received connected");
                    /*if (carHvacManager != null)
                        Log.d("CAR", "HVAC manager received connected");
                    */

                        // hook up handlers
                        //carHvacManager.registerCallback(carHvacEventCallback);

                        sensorManager.registerListener(RPMStateChangeListener,
                                CarSensorManager.SENSOR_TYPE_RPM,
                                CarSensorManager.SENSOR_RATE_NORMAL);

                    /*sensorManager.registerListener(gearMonitor,
                            CarSensorManager.SENSOR_TYPE_GEAR,
                            CarSensorManager.SENSOR_RATE_NORMAL);*/

                        //check for permission to use fuel data
                        if (useFuelData)
                            sensorManager.registerListener(FuelLevelStateChangeListener,
                                    CarSensorManager.SENSOR_TYPE_FUEL_LEVEL,
                                    CarSensorManager.SENSOR_RATE_NORMAL);

                        //check for permission to use speed data
                    /*if (useSpeedData) {
                        sensorManager.registerListener(speedMonitor,
                                CarSensorManager.SENSOR_TYPE_CAR_SPEED,
                                CarSensorManager.SENSOR_RATE_NORMAL);
                    } else {
                        Log.d("CAR", "speedMonitor not registering...");
                    }*/

                    } catch (CarNotConnectedException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    Log.d("CAR", "Service disconnected");

                }
        };

        car = Car.createCar(context, serviceConnection, handler);
        Log.d("CAR", "Car created");

        car.connect();
        Log.d("CAR", "Car connected");
    }

    //skapar en java timer som genererar fake event via ui tråden
    private void setupTimer(){
        eventTimer = new Timer();
        eventTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        //random minor braking
                        if(Math.random()*40 <= 1){
                            if(testGear != 1){
                                testGear -= 1;
                            }
                            testRPM = testRPM/2;
                        }

                        //randomly generate positive or negative tilt
                        if(Math.random()*5 <= 1){
                            //keep values within -15 to 15 range
                            tilt = Math.max(-15, (Math.min(15, tilt + Math.random()*0.3 - Math.random()*0.3)));
                        }

                        //accelerate with minor variation til we reach acceptable highway speed
                        if (testRPM < 3000){
                            testRPM += 100+Math.random()*100;
                            lambda = Math.max(0, Math.min(100, lambda + Math.random()*3 - Math.random()*3));
                        } else if (testGear < 5){
                            testRPM = testSpeed*100 / testGear+1;
                            testGear += 1;
                            lambda = Math.max(0, Math.min(100, lambda + Math.random()*1 - Math.random()*3));
                        }

                        //the "oh shit" catch
                        if(lambda>ecoPointsRef/2){
                            variation = 2;
                        } else {
                            variation = 1;
                        }

                        //clamp our value within 0-100 range
                        lambda = Math.max(0, Math.min(100, lambda + Math.random()*5 - Math.random()*3*variation));


                        testSpeed = testRPM*testGear / 120;

                        System.out.println("-----TEST-----");
                        System.out.println("Speed is: " + testSpeed);
                        System.out.println("Gear is: " + testGear);
                        System.out.println("RPM is: " + testRPM);
                        System.out.println("Current lamdba is: " + lambda);
                        System.out.println("Current tilt is: " + tilt);

                        testUpdater();
                    }
                });
            }
        };

        eventTimer.schedule(eventTask, 500, 250);
        timerAlive = true;
    }

    //samlingsplats för skickandet av data från timern
    private void testUpdater(){
        mModel.setecoCal(lambda, tilt);
        //skickar relevant data till filtret
        //dataFilter.dataInput(Db.EntryType.RPM, testRPM);

        /* DataFilter needs to be tweaked in order for it to work here... */
        
        //dataFilter.dataInput(Db.EntryType.ECOSCORE, mModel.getecoCal());
        Db.insertData(new Pair<>(Db.EntryType.ECOSCORE, mModel.getecoCal()));
        mView.setColor(mModel.getecoCal());
    }

    @Override
    public void onClick (android.view.View view){

        /*
        String data = mModel.getData();

        for (Object d : Db.getData(Db.EntryType.RPM, Db.cTimeMinusMinutes(1))) {
            System.out.println(d);
        }
        */

        //om vi inte har en timer så skapar vi en. om en finns så dödar vi den
        if(!timerAlive){
            setupTimer();
        } else {
            eventTask.cancel();
            eventTimer.cancel();
            timerAlive = false;
        }
    }
}