package infotainment.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.semcon.oil.infotainment.R;

import infotainment.contract.StatisticsActivityContract;

public class StatisticsActivity extends AppCompatActivity implements StatisticsActivityContract.View {

    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        configureBackButton();


    }

    @Override
    public void initView() {


    }

    private void configureBackButton() {

        backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void setViewData(String data) {

    }
}

