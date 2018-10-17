package infotainment.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.semcon.oil.infotainment.R;

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
                // TODO Implement some way to toggle colorblind symbols on / off.
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
