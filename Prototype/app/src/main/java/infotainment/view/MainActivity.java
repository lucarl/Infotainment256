package infotainment.view;

import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarSensorEvent;
import android.car.hardware.CarSensorManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import java.security.InvalidParameterException;

import infotainment.contract.MainActivityContract;
import infotainment.contract.StatisticsActivityContract;
import infotainment.presenter.MainActivityPresenter;

import static android.graphics.Color.parseColor;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    /*Car car;
    Handler handler;
    ServiceConnection serviceConnection;
    CarSensorManager sensorManager;
    CarHvacManager carHvacManager;*/

    private TextView resultTextView;
    private Button helloButton;
    private Button statisticsButton;
    private ConstraintLayout Clayout;
    private MainActivityContract.Presenter mPresenter;
    private StatisticsActivityContract.Presenter statisticsPresenter;

    private int ecoLevel = 100;
    int[] RGBcolor1;
    int[] RGBcolor2;


    Car car;
    Handler handler;
    ServiceConnection serviceConnection;

    CarSensorManager.OnSensorChangedListener FuelLevelStateChangeListener;
    CarSensorManager.OnSensorChangedListener RPMStateChangeListener;
    CarSensorManager sensorManager;



    /*CarSensorManager.OnSensorChangedListener ignitionStateChangedListener;
    /* CarSensorManager.OnSensorChangedListener testStateChanged; */
    //CarSensorManager.OnSensorChangedListener gearMonitor;
    //CarSensorManager.OnSensorChangedListener speedMonitor;

    //CarHvacManager.CarHvacEventCallback carHvacEventCallback;

    private static final int speedDataPermissionMagicNumber = 42;
    boolean useFuelData = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainActivityPresenter(this);

        //sätter standard röd-grön skala
        configureEcoColors(new int[]{255,255,10,10}, new int[]{255,10,255,10});

        RPMStateChangeListener = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {

                CarSensorEvent.RpmData rpmData = carSensorEvent.getRpmData(null);

                if(rpmData.rpm!=0.0) {

                    Log.d("CAR", "RPM stuff");

                    if (rpmData.rpm > 3500.0) {
                        adjustEcoLevel(-2);
                    } else if (rpmData.rpm > 2500.0) {
                        adjustEcoLevel(-1);
                    } else if (rpmData.rpm < 2000.0) {
                        adjustEcoLevel(1);
                    } else if (rpmData.rpm < 1500.0) {
                        adjustEcoLevel(2);
                    }

                    if (resultTextView.getLineCount() >= 10) {
                        String text = resultTextView.getEditableText().toString();
                        resultTextView.setText(text.substring(text.indexOf(System.getProperty("line.separator"))+1));

                        //t.setText((t.getEditableText().toString().split("\n"))[1]);
                        //t.getEditableText().delete(0, 28);
                    }
                    resultTextView.append("\nRPM data: " + rpmData.rpm + " level: " + ecoLevel);
                }
            }
        };

        FuelLevelStateChangeListener = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {

                CarSensorEvent.FuelLevelData fuelData = carSensorEvent.getFuelLevelData(null);

                Log.d("CAR", "Fuel stuff");

                TextView t = resultTextView;
                if (t.getLineCount() > 25) {
                    t.setText("");
                }

                t.append("\nFuel data: " + fuelData.range + " level: " + ecoLevel);

            }
        };



        // get permissions
        if (ContextCompat.checkSelfPermission(this, Car.PERMISSION_FUEL)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("CAR", "Requesting permission to use fuel events.");
            ActivityCompat.requestPermissions(this,
                    new String[] {Car.PERMISSION_FUEL}, 38);
        } else {
            // permission already given
            Log.d("CAR", "Permission available to use fuel events.");
            useFuelData = true;
        }

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
                        Log.d("CAR", "HVAC manager received connected");*/

                    // hook up handlers
                    //carHvacManager.registerCallback(carHvacEventCallback);

                    sensorManager.registerListener(RPMStateChangeListener,
                            CarSensorManager.SENSOR_TYPE_RPM,
                            CarSensorManager.SENSOR_RATE_NORMAL);

                    if (useFuelData)
                        sensorManager.registerListener(FuelLevelStateChangeListener,
                            CarSensorManager.SENSOR_TYPE_FUEL_LEVEL,
                            CarSensorManager.SENSOR_RATE_NORMAL);

                    /*if (useSpeedData) {
                        sensorManager.registerListener(speedMonitor,
                                CarSensorManager.SENSOR_TYPE_CAR_SPEED,
                                CarSensorManager.SENSOR_RATE_NORMAL);
                    } else {
                        Log.d("CAR", "speedMonitor not registering...");
                    }*/

                    /*sensorManager.registerListener(testStateChanged, CarSensorManager.SENSOR_TYPE_RPM, CarSensorManager.SENSOR_RATE_NORMAL);*/

                } catch (CarNotConnectedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                Log.d("CAR", "Service disconnected");

            }
        };

        car = Car.createCar(this, serviceConnection, handler);
        Log.d("CAR", "Car created");

        car.connect();
        Log.d("CAR", "Car connected");

    }

    @Override
    public void initView() {

        statisticsButton = findViewById(R.id.statisticsButton);
        Clayout=findViewById(R.id.v);
        resultTextView = findViewById(R.id.startTextView);
        helloButton = findViewById(R.id.startButton);

        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                mPresenter.onClick(view);

            }
        });

        statisticsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
        });

    }

    public void adjustEcoLevel(int change){
        //clamp the adjusted eco level within the 0-100 range
        ecoLevel = Math.min(100, (Math.max(0, ecoLevel+=change)));

        updateEcoColor(ecoLevel);
    }

    private void updateEcoColor(int level){

        double red = ((100.0-level)/100.0)*RGBcolor1[1]+(level/100.0)*RGBcolor2[1];
        double green = ((100.0-level)/100.0)*RGBcolor1[2]+(level/100.0)*RGBcolor2[2];
        double blue = ((100.0-level)/100.0)*RGBcolor1[3]+(level/100.0)*RGBcolor2[3];

        statisticsButton.setBackgroundColor(Color.argb(255, (int)red, (int)green, (int)blue));
    }

    private void configureEcoColors(int[] RGBvalues1, int[] RGBvalues2){
        if(RGBvalues1.length == 4){
            for (int i : RGBvalues1){
                if(i < 0 || i > 255)
                    throw new InvalidParameterException("Incorrect RGB values");
            }

            RGBcolor1 = RGBvalues1;
        }
        if(RGBvalues2.length == 4){
            for (int i : RGBvalues1){
                if(i < 0 || i > 255)
                    throw new InvalidParameterException("Incorrect RGB values");
            }

            RGBcolor2 = RGBvalues2;
        }
    }

    @Override
    public void setViewData(String data) {

        resultTextView.setText(data);

    }

    public void setColor(int fargVal){

        final View cl = findViewById(R.id.v);

        String[] colArr = {"#66ff33","#6eff30","#75ff2e","#7dff2b","#85ff29","#8cff26","#94ff24","#9cff21","#a3ff1f","#abff1c","#b2ff1a","#baff17","#c2ff14","#c9ff12","#d1ff0f","#d9ff0d","#e0ff0a","#e8ff08","#f0ff05","#f7ff03","#ffff00","#ffff66","#fff261","#ffe65c","#ffd957","#ffcc52","#ffbf4c","#ffb247","#ffa642","#ff993d","#ff8c38","#ff8033","#ff732e","#ff6629","#ff5924","#ff4d1f","#ff401a","#ff3314","#ff260f","#ff190a","#ff0d05","#ff0000"};
        // EditText mEdit = findViewById(R.id.editText);        //presnterar hexvärdet i en given ruta
        // mEdit.setText(colArr[fargVal]);
        cl.setBackgroundColor(parseColor(colArr[fargVal]));


    }
}
