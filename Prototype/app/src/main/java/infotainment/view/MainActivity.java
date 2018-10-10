package infotainment.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import infotainment.contract.MainActivityContract;
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

        Clayout=findViewById(R.id.v);
        resultTextView = findViewById(R.id.startTextView);
        helloButton = findViewById(R.id.startButton);

        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                mPresenter.onClick(view);

            }
        });

    }

    private void configureStatisticsButton() {

        statisticsButton = findViewById(R.id.statisticsButton);

        /*statisticsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
        });*/

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
