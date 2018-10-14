package infotainment.presenter;

import android.app.Activity;
import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarSensorEvent;
import android.car.hardware.CarSensorManager;
import android.car.hardware.hvac.CarHvacManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;

import infotainment.Model.DataFilter;
import infotainment.Model.MainActivityModel;
import infotainment.Model.db.Db;
import infotainment.Model.db.LogDb;
import infotainment.contract.MainActivityContract;

import static android.graphics.Color.parseColor;

public class MainActivityPresenter extends AppCompatActivity implements MainActivityContract.Presenter{

    private MainActivityContract.View mView;
    private MainActivityContract.Model mModel;
    private DataFilter dataFilter;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {

        new LogDb(context);
        //this.dataFilter = new DataFilter();

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

    public MainActivityPresenter(MainActivityContract.View view, Context context) {

        this.context = context;
        mView = view;
        initPresenter();
    }

    private void initPresenter() {

        mModel = new MainActivityModel();
        mView.initView();

        double resArr[] = new double[10];   //storleken bestämmer hur stor snittet är i ecoCal
        double ecoPointsRef=36;             //Ansatt referensvärde
        Arrays.fill(resArr,ecoPointsRef);
        double lambda=10;                   //  l/100km
        double tilt = 0;                    //grader lutning

        mModel.setecoCal(lambda, tilt, resArr, ecoPointsRef);

        RPMStateChangeListener = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {

                CarSensorEvent.RpmData rpmData = carSensorEvent.getRpmData(null);

                if(rpmData.rpm!=0.0) {

                    Date currentTime = Calendar.getInstance().getTime();

                    Log.d("CAR", "RPM stuff " + currentTime);

                    //------------->PUT EVENT HANDLING FOR RPM HERE<-------------

                    TextView logTextView = mView.getResultTextView();

                    if (logTextView.getLineCount() >= 10) {
                        String text = logTextView.getEditableText().toString();
                        logTextView.setText(text.substring(text.indexOf(System.getProperty("line.separator"))+1));
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
                    logTextView.setText(text.substring(text.indexOf(System.getProperty("line.separator"))+1));
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
                    logTextView.setText(text.substring(text.indexOf(System.getProperty("line.separator"))+1));
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
                        Log.d("CAR","Sensor manager received connected");
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

    @Override
    public void onClick(android.view.View view) {

        String data = mModel.getData();

        for (Object d : Db.getData(Db.EntryType.RPM, 0)){
            System.out.println(d);
        }

        mView.setViewData(data);

        mView.setColor(mModel.getecoCal());
    }
    
}