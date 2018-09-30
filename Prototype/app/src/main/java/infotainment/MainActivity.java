package infotainment;
import android.car.Car;
import android.car.hardware.CarSensorManager;
import android.car.hardware.hvac.CarHvacManager;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;

import com.semcon.oil.infotainment.R;

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

    }
}
