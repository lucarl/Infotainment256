package infotainment.view;

import android.car.CarNotConnectedException;
import android.car.hardware.CarSensorEvent;
import android.car.hardware.CarSensorManager;
import android.car.hardware.hvac.CarHvacManager;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.car.*;

import com.semcon.oil.infotainment.R;

import java.security.InvalidParameterException;

import infotainment.contract.MainActivityContract;
import infotainment.presenter.MainActivityPresenter;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    /*Car car;
    Handler handler;
    ServiceConnection serviceConnection;
    CarSensorManager sensorManager;
    CarHvacManager carHvacManager;*/

    private TextView resultTextView;
    private Button helloButton;
    private Button statisticsButton;
    private MainActivityContract.Presenter mPresenter;

    private int ecoLevel = 50;
    int[] ecoColor;
    int[] RGBcolor1;
    int[] RGBcolor2;


    Car car;
    Handler handler;
    ServiceConnection serviceConnection;

    CarSensorManager.OnSensorChangedListener RPMStateChangeListener;
    CarSensorManager sensorManager;



    /*CarSensorManager.OnSensorChangedListener ignitionStateChangedListener;
    /* CarSensorManager.OnSensorChangedListener testStateChanged; */
    //CarSensorManager.OnSensorChangedListener gearMonitor;
    //CarSensorManager.OnSensorChangedListener speedMonitor;

    //CarHvacManager.CarHvacEventCallback carHvacEventCallback;

    private static final int speedDataPermissionMagicNumber = 42;
    boolean useSpeedData = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sätter standard röd-grön skala
        configureEcoColors(new int[]{255,255,10,10}, new int[]{255,10,255,10});




        configureStatisticsButton();

        RPMStateChangeListener = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {

                CarSensorEvent.RpmData rpmData = carSensorEvent.getRpmData(null);

                if(rpmData.rpm!=0.0) {

                    Log.d("CAR", "RPM stuff");

                    /*if (rpmData.rpm > 3500.0 && ecoLevel >= 0) {
                        ecoLevel -= 2;
                    } else if (rpmData.rpm > 2500.0 && ecoLevel >= 0) {
                        ecoLevel -= 1;
                    } else if (rpmData.rpm < 2000.0 && ecoLevel <= 100) {
                        ecoLevel += 1;
                    } else if (rpmData.rpm < 1500.0 && ecoLevel <= 100) {
                        ecoLevel += 2;
                    }*/

                    updateEcoColor();


                    TextView t = resultTextView;
                    if (t.getLineCount() > 25) {
                        t.setText("");
                    }
                    t.append("\nRPM data: " + rpmData.rpm + " level: " + ecoLevel);
                }
            }
        };

        // get permissions
        if (ContextCompat.checkSelfPermission(this, Car.PERMISSION_SPEED)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("CAR", "Requesting permission to use speed events.");
            ActivityCompat.requestPermissions(this,
                    new String[] {Car.PERMISSION_SPEED}, speedDataPermissionMagicNumber);
        } else {
            // permission already given
            Log.d("CAR", "Permission available to use speed events.");
            useSpeedData = true;
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

        mPresenter = new MainActivityPresenter(this);

    }

    @Override
    public void initView() {

        resultTextView = findViewById(R.id.startTextView);
        helloButton = findViewById(R.id.startButton);

        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                mPresenter.onClick(view);

            }
        });

    }

    private void updateEcoColor(){

        int level = ecoLevel;



        double red = (((100.0-level)/100.0)*RGBcolor1[1])+((level/100.0)*RGBcolor2[1]);
        double green = (((100.0-level)/100.0)*RGBcolor1[2])+((level/100.0)*RGBcolor2[2]);
        double blue = (((100.0-level)/100.0)*RGBcolor1[3])+((level/100.0)*RGBcolor2[3]);

        Log.d("CAR", "RGB: " + red + " " + green + " " + blue);


        /*statisticsButton.setBackgroundColor(Color.argb(
                255,
                (((100-level)/100)*(int)RGBcolor1[1])+((level/100)*(int)RGBcolor2[1]),
                (((100-level)/100)*(int)RGBcolor1[2])+((level/100)*(int)RGBcolor2[2]),
                (((100-level)/100)*(int)RGBcolor1[3])+((level/100)*(int)RGBcolor2[3])));*/

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

    private void configureStatisticsButton() {

        statisticsButton = findViewById(R.id.statisticsButton);

        statisticsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
        });

    }

    @Override
    public void setViewData(String data) {

        resultTextView.setText(data);

    }
}
