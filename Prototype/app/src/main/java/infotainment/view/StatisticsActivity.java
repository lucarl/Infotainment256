package infotainment.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.Series;
import com.semcon.oil.infotainment.R;
import infotainment.contract.StatisticsActivityContract;
import infotainment.presenter.StatisticsPresenter;

public class StatisticsActivity extends AppCompatActivity implements StatisticsActivityContract.View {

    private StatisticsActivityContract.Presenter statisticsPresenter;
    private Button backButton;
    private GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        configureBackButton();
        statisticsPresenter = new StatisticsPresenter(this);
    }

    /** Init the view and set some settings for the layout of the graph
     */
    @Override
    public void initView()
    {
        graph = findViewById(R.id.graph);
        graph.getGridLabelRenderer().setGridStyle(GridLabelRenderer.GridStyle.NONE);
        graph.getGridLabelRenderer().setHorizontalLabelsVisible(false);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinX(-1);
        graph.getViewport().setMaxX(12);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(20);
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("EcoScore");
    }

    /** @param data Add the data series to the graph, but first a weird clear loop required to keep
     *              the graph going like it should and not clutter values.
     */
    @Override
    public void setGraphData(BarGraphSeries<DataPoint> data)
    {
        for (Series s : graph.getSeries())
            graph.removeSeries(s);

        graph.addSeries(data);
    }

    /** @param max sets the x-axis width of the graph
     */
    public void setGraphMaxX(int max)
    { graph.getViewport().setMaxX(statisticsPresenter.getGraphMaxX()); }


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

