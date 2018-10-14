package infotainment.view;

<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
import android.car.Car;
import android.car.CarNotConnectedException;
import android.car.hardware.CarSensorEvent;
import android.car.hardware.CarSensorManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
=======
>>>>>>> Integrated event handling and updated simulations some more
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
import android.util.Log;
=======
>>>>>>> Integrated event handling and updated simulations some more
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import infotainment.contract.MainActivityContract;
import infotainment.presenter.MainActivityPresenter;

import static android.graphics.Color.parseColor;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    private TextView resultTextView;
    private Button helloButton;
    private Button statisticsButton;
    private ConstraintLayout Clayout;
    private MainActivityContract.Presenter mPresenter;

    /*
    //Värden för programmets bakgrundsfärg
    private int ecoLevel = 100;
    int[] RGBcolor1;
    int[] RGBcolor2;
    */

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainActivityPresenter(this);

        //sätter standard röd-grön skala
<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
        configureEcoColors(new int[]{255,255,10,10}, new int[]{255,10,255,10});
=======
        //configureEcoColors(new int[]{255,255,10,10}, new int[]{255,10,255,10});
        configureStatisticsButton();
>>>>>>> Integrated event handling and updated simulations some more

        mPresenter = new MainActivityPresenter(this, this);
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

<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
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
=======
    public TextView getResultTextView() {
        return resultTextView;
    }

    private void configureStatisticsButton() {

        statisticsButton = findViewById(R.id.statisticsButton);
>>>>>>> Integrated event handling and updated simulations some more

        /*statisticsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
        };

        car = Car.createCar(this, serviceConnection, handler);
        Log.d("CAR", "Car created");

        car.connect();
        Log.d("CAR", "Car connected");

=======
        });*/
>>>>>>> Integrated event handling and updated simulations some more
    }

    @Override
    public void setViewData(String data) {

<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
        statisticsButton = findViewById(R.id.statisticsButton);
        Clayout=findViewById(R.id.v);
        resultTextView = findViewById(R.id.startTextView);
        helloButton = findViewById(R.id.startButton);
=======
        resultTextView.setText(data);
>>>>>>> Integrated event handling and updated simulations some more

    }

    public void setColor(int fargVal){

        final View cl = findViewById(R.id.v);

<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
        statisticsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
            }
        });

=======
        String[] colArr = {"#66ff33","#6eff30","#75ff2e","#7dff2b","#85ff29","#8cff26","#94ff24","#9cff21","#a3ff1f","#abff1c","#b2ff1a","#baff17","#c2ff14","#c9ff12","#d1ff0f","#d9ff0d","#e0ff0a","#e8ff08","#f0ff05","#f7ff03","#ffff00","#ffff66","#fff261","#ffe65c","#ffd957","#ffcc52","#ffbf4c","#ffb247","#ffa642","#ff993d","#ff8c38","#ff8033","#ff732e","#ff6629","#ff5924","#ff4d1f","#ff401a","#ff3314","#ff260f","#ff190a","#ff0d05","#ff0000"};
        // EditText mEdit = findViewById(R.id.editText);        //presnterar hexvärdet i en given ruta
        // mEdit.setText(colArr[fargVal]);
        cl.setBackgroundColor(parseColor(colArr[fargVal]));
>>>>>>> Integrated event handling and updated simulations some more
    }

    /*

    --Funktioner för att uppdatera backgrundsfärg efter en gradient--

    public void adjustEcoLevel(int change){
        //clamp the adjusted eco level within the 0-100 range
        ecoLevel = Math.min(100, (Math.max(0, ecoLevel+=change)));

        updateEcoColor(ecoLevel);
    }

    private void updateEcoColor(int level){

        double red = ((100.0-level)/100.0)*RGBcolor1[1]+(level/100.0)*RGBcolor2[1];
        double green = ((100.0-level)/100.0)*RGBcolor1[2]+(level/100.0)*RGBcolor2[2];
        double blue = ((100.0-level)/100.0)*RGBcolor1[3]+(level/100.0)*RGBcolor2[3];

        final View cl = findViewById(R.id.v);

        cl.setBackgroundColor(Color.argb(255, (int)red, (int)green, (int)blue));
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
<<<<<<< 368f1d9ba8f4495ffa36387531274e003cef4244
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
=======
    }*/
>>>>>>> Integrated event handling and updated simulations some more
}
