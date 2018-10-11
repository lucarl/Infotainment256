package infotainment.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import java.util.ArrayList;
import java.util.List;

import infotainment.Model.db.logDb;
import infotainment.Model.db.logDbHelper;
import infotainment.contract.MainActivityContract;
import infotainment.contract.logDbContract;
import infotainment.presenter.MainActivityPresenter;


public class MainActivityView extends AppCompatActivity implements MainActivityContract.View{

    /*Car car;
    Handler handler;
    ServiceConnection serviceConnection;
    CarSensorManager sensorManager;
    CarHvacManager carHvacManager;*/

    private TextView resultTextView;
    private Button helloButton;
    private MainActivityContract.Presenter mPresenter;
    logDb db;



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


        db = new logDb(new logDbHelper(getApplicationContext()));
        mPresenter = new MainActivityPresenter(this);

    }

    @Override
    public void initView() {

        resultTextView = findViewById(R.id.resultTextView);
        helloButton = findViewById(R.id.helloButton);
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
