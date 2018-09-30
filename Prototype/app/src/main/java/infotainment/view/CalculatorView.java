package infotainment.view;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

public class CalculatorView extends AppCompatActivity{

    Button addButton = (Button) findViewById(R.id.addButton);
    EditText firstNumEditText = findViewById(R.id.firstNumEditText);
    EditText secondNumEditText = findViewById(R.id.secondNumEditText);
    TextView resultTextView = findViewById(R.id.resultTextView);

    public int getFirstNumber() {
        return Integer.parseInt(firstNumEditText.getText().toString());
    }

    public int getSecondNumber() {
        return Integer.parseInt(secondNumEditText.getText().toString());
    }

    public int getCalcSolution() {
        return Integer.parseInt(resultTextView.getText().toString());
    }

    public void setCalcSolution(int solution) {
        resultTextView.setText(solution + "");
    }

    public void addCalculationListener(View.OnClickListener listenForCalcbutton) {
        addButton.setOnClickListener(listenForCalcbutton);

    }
}
