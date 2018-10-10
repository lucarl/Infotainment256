package infotainment.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.graphics.ColorUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

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
        configureStatisticsButton();

        mPresenter = new MainActivityPresenter(this);

    }

    @Override
    public void initView() {

        resultTextView = findViewById(R.id.startTextView);
        helloButton = findViewById(R.id.startButton);

        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {
                TextView t = findViewById(R.id.startTextView);
                t.append("\nEco clicked");
                mPresenter.toggleEcoDriving();
            }
        });

    }

    public void updateBackgroundColor(int startColor, int endColor, float ratio) {
        ConstraintLayout cl = findViewById(R.id.main_cl);
        cl.setBackgroundColor(ColorUtils.blendARGB(startColor, endColor, ratio));
        Log.d("UpdateBG:" , "bg updated.");
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
