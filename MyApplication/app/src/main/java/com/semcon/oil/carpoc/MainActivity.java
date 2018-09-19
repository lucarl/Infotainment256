package com.semcon.oil.carpoc;
import android.Manifest;
import android.car.hardware.CarPropertyValue;
import android.car.hardware.CarSensorEvent;
import android.car.hardware.CarSensorManager;
import android.car.hardware.hvac.CarHvacManager;
import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.car.*;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Car car;
    Handler handler;
    ServiceConnection serviceConnection;
    CarSensorManager sensorManager;
    CarHvacManager carHvacManager;

    CarSensorManager.OnSensorChangedListener ignitionStateChangedListener;
    /* CarSensorManager.OnSensorChangedListener testStateChanged; */
    CarSensorManager.OnSensorChangedListener gearMonitor;
    CarSensorManager.OnSensorChangedListener speedMonitor;

    CarHvacManager.CarHvacEventCallback carHvacEventCallback;

    private static final int speedDataPermissionMagicNumber = 42;
    boolean useSpeedData = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        testStateChanged = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {
                Log.d("CAR", "Speed changed event...");
            }
        };
        */

        gearMonitor = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {
                Log.d("CAR", "Gear data event...");

                CarSensorEvent.GearData gearData = carSensorEvent.getGearData(null);

                TextView t = findViewById(R.id.mainText);
                t.append("\nGear data: " + gearData.gear + " at: " + gearData.timestamp);
            }
        };

        speedMonitor = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {
                Log.d("CAR", "Speed event...");

                CarSensorEvent.CarSpeedData speedData = carSensorEvent.getCarSpeedData(null);

                TextView t = findViewById(R.id.mainText);
                t.append("\nNew speed: " + speedData.carSpeed + " at: " + speedData.timestamp);
            }
        };

        ignitionStateChangedListener = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {
                Log.d("CAR", "Ignition changed event...");

                for (int i=0; i < carSensorEvent.intValues.length; i++) {
                    Log.d("CAR", "Ignition state values= " + carSensorEvent.intValues[i]);
                    TextView t = findViewById(R.id.mainText);
                    t.append("\nIgnition state: " + carSensorEvent.intValues[i]);
                }
            }
        };

        /*
VEHICLEPROPERTY_DRIVING_STATUS = 0x11400404
VEHICLEPROPERTY_NIGHT_MODE = 0x11200407
x VEHICLEPROPERTY_IGNITION_STATE = 0x11400409

x VEHICLEPROPERTY_HVAC_FAN_SPEED = 0x12400500
x VEHICLEPROPERTY_HVAC_FAN_DIRECTION = 0x12400501
x VEHICLEPROPERTY_HVAC_TEMPERATURE_SET = 0x12600503
x VEHICLEPROPERTY_HVAC_DEFROSTER = 0x13200504
VEHICLEPROPERTY_HVAC_AC_ON = 0x12200505
VEHICLEPROPERTY_HVAC_RECIRC_ON = 0x12200508
VEHICLEPROPERTY_HVAC_AUTO_ON = 0x1220050a
x VEHICLEPROPERTY_HVAC_POWER_ON = 0x12200510

VEHICLEPROPERTY_ENV_OUTSIDE_TEMPERATURE = 0x11600703
VEHICLEPROPERTY_DISPLAY_BRIGHTNESS = 0x11400a01
VEHICLEPROPERTY_DOOR_LOCK = 0x16200b02
         */

        carHvacEventCallback = new CarHvacManager.CarHvacEventCallback() {
            @Override
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
               // Log.d("CAR", "HVAC property changed " + carPropertyValue.toString());
                TextView t = findViewById(R.id.mainText);

                switch (carPropertyValue.getPropertyId() ) {
                    case CarHvacManager.ID_ZONED_FAN_SPEED_SETPOINT:
                        t.append("\nFan speed setpoint: " + carPropertyValue.getValue());
                    break;
                    case CarHvacManager.ID_ZONED_FAN_POSITION:
                        t.append("\nFan direction or positgearion: " + carPropertyValue.getValue());
                        break;
                    case CarHvacManager.ID_ZONED_TEMP_SETPOINT:
                        t.append("\nTemperature setpoint: " + carPropertyValue.getValue());
                    break;
                    case CarHvacManager.ID_ZONED_HVAC_POWER_ON:
                        t.append("\nPower on: " + carPropertyValue.getValue());
                    break;
                    case 0x5001: // Not included in the HVAC properties0x11600207
                        t.append("\nDefroster on: " + carPropertyValue.getValue());
                    break;
                }

            }

            @Override
            public void onErrorEvent(int i, int i1) {

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
                    carHvacManager = (CarHvacManager) car.getCarManager(Car.HVAC_SERVICE);

                    // report connections
                    if (sensorManager != null)
                        Log.d("CAR","Sensor manager received connected");
                    if (carHvacManager != null)
                        Log.d("CAR", "HVAC manager received connected");

                    // hook up handlers
                    carHvacManager.registerCallback(carHvacEventCallback);

                    sensorManager.registerListener(ignitionStateChangedListener,
                            CarSensorManager.SENSOR_TYPE_IGNITION_STATE,
                            CarSensorManager.SENSOR_RATE_NORMAL);

                    sensorManager.registerListener(gearMonitor,
                            CarSensorManager.SENSOR_TYPE_GEAR,
                            CarSensorManager.SENSOR_RATE_NORMAL);

                    if (useSpeedData) {
                        sensorManager.registerListener(speedMonitor,
                                CarSensorManager.SENSOR_TYPE_CAR_SPEED,
                                CarSensorManager.SENSOR_RATE_NORMAL);
                    } else {
                        Log.d("CAR", "speedMonitor not registering...");
                    }

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
    public void onRequestPermissionsResult(int requestCode,
                                          String permissions[],
                                          int[] grantResults) {
        if (requestCode == speedDataPermissionMagicNumber
                && ContextCompat.checkSelfPermission(this, Car.PERMISSION_SPEED)
                == PackageManager.PERMISSION_GRANTED) {
            Log.d("CAR", "Permission granted to use speed events (CAR_SPEED).");
            useSpeedData = true;
        } else {
            Log.d("CAR", "Permission NOT granted to use speed events.");
        }

    }
}
