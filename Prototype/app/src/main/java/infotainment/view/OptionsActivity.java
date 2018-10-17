package infotainment.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.semcon.oil.infotainment.R;

import infotainment.presenter.MainActivityPresenter;

public class OptionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);
        configureBackButton();
        configureColorBlindButton();
    }

    private void configureColorBlindButton() {
        Button cbButton = findViewById(R.id.OptionsActivity_btn_colorblindmode);
        cbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.toggleColorBlindMode();
                Log.d("OptionsMen", "toggled colorblind mode");
            }
        });
    }

    private void configureBackButton() {

        Button backButton = findViewById(R.id.OptionsActivity_btn_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
