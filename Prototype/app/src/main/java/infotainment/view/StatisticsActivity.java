package infotainment.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.semcon.oil.infotainment.R;

import infotainment.contract.StatisticsActivityContract;
import infotainment.presenter.StatisticsPresenter;

public class StatisticsActivity extends AppCompatActivity implements StatisticsActivityContract.View {

    private StatisticsActivityContract.Presenter statisticsPresenter;
    private Button backButton;
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        configureBackButton();
        statisticsPresenter = new StatisticsPresenter(this);
    }

    @Override
    public void initView() {
        graph =  findViewById(R.id.graph);
    }

    @Override
    public void setGraphData(BarGraphSeries<DataPoint> data) {

        graph.removeAllSeries();
        graph.clearSecondScale();
        graph.addSeries(data);
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



}

