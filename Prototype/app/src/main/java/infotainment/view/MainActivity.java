package infotainment.view;

import android.car.Car;
import android.car.hardware.CarSensorManager;
import android.car.hardware.hvac.CarHvacManager;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import infotainment.contract.MainActivityContract;
import infotainment.presenter.MainActivityPresenter;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    Car car;
    Handler handler;
    ServiceConnection serviceConnection;
    CarSensorManager sensorManager;
    CarHvacManager carHvacManager;

    private TextView resultTextView;
    private Button helloButton;
    private MainActivityContract.Presenter mPresenter;



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

        mPresenter = new MainActivityPresenter(this);

    }

    @Override
    public void initView() {

        resultTextView = (TextView) findViewById(R.id.resultTextView);
        helloButton = (Button) findViewById(R.id.helloButton);
        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                mPresenter.onClick(view);

            }
        });

    }

    @Override
    public void setViewData(String data) {

        resultTextView.setText(data);

    }
}
