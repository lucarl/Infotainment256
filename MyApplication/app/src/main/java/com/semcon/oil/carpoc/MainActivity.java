package com.semcon.oil.carpoc;

import android.animation.ValueAnimator;
import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarPropertyValue;
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
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    Car car;
    Handler handler;
    ServiceConnection serviceConnection;
    CarSensorManager sensorManager;
    CarHvacManager carHvacManager;

    CarSensorManager.OnSensorChangedListener ignitionStateChangedListener;
    CarSensorManager.OnSensorChangedListener testStateChanged;
    CarSensorManager.OnSensorChangedListener gearMonitor;
    CarSensorManager.OnSensorChangedListener speedMonitor;
    CarSensorManager.OnSensorChangedListener myMonitor;

    CarHvacManager.CarHvacEventCallback carHvacEventCallback;

    private static final int speedDataPermissionMagicNumber = 42;
    boolean useSpeedData = false;

    final void launchStatsActivity() {
        Intent intent = new Intent(this, StatsActivity.class);
        startActivity(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button clearBtn = findViewById(R.id.swap);
        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchStatsActivity();
            }
        });

        myMonitor = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {
                TextView t = findViewById(R.id.mainText);
                CarSensorEvent.RpmData rpmData = carSensorEvent.getRpmData(null);
                t.append("/nCarRPM: " + rpmData.rpm + " " + rpmData.timestamp);
            }
        };

        Button myBtn = findViewById(R.id.testbutton);
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> hexColors = new ArrayList<>(16);
                Util.populateHex(hexColors);
                final ConstraintLayout cl = findViewById(R.id.cl);
                // Load initial and final colors.

                ValueAnimator anim = ValueAnimator.ofFloat(0, 1);
                final String color1 = Util.getRandomColor(hexColors);
                final String color2 = Util.getRandomColor(hexColors);

                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        // Use animation position to blend colors by the progress-ratio.
                        float position = animation.getAnimatedFraction();

                        // Apply blended color to the view.
                        cl.setBackgroundColor(ColorUtils.blendARGB(Color.parseColor(color1), Color.parseColor(color2), position));
                    }
                });
                anim.setDuration(5000).start();
            }
        });

        testStateChanged = new CarSensorManager.OnSensorChangedListener() {
            @Override
            public void onSensorChanged(CarSensorEvent carSensorEvent) {
                Log.d("CAR", "Speed changed event...");
            }
        };

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
VEHICLEPROPERTY_DOOR_LOCK = 0x16200b02
VEHICLEPROPERTY_DISPLAY_BRIGHTNESS = 0x11400a01
         */

        carHvacEventCallback = new CarHvacManager.CarHvacEventCallback() {
            @Override
            public void onChangeEvent(CarPropertyValue carPropertyValue) {
                Log.d("CAR", "HVAC property changed " + carPropertyValue.toString());
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

                    /* sensorManager.registerListener(myMonitor,
                            CarSensorManager.SENSOR_TYPE_RPM,
                            CarSensorManager.SENSOR_RATE_NORMAL);
                    */
                    sensorManager.registerListener(testStateChanged, CarSensorManager.SENSOR_TYPE_RPM, CarSensorManager.SENSOR_RATE_NORMAL);

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
