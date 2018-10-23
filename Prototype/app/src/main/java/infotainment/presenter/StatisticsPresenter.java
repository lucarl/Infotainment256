package infotainment.presenter;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import java.util.Timer;
import java.util.TimerTask;
import infotainment.Model.StatisticsActivityModel;
import infotainment.Model.db.Db;
import infotainment.contract.StatisticsActivityContract;

public class StatisticsPresenter implements StatisticsActivityContract.Presenter{

    private StatisticsActivityContract.View statisticsView;
    private StatisticsActivityContract.Model statisticsModel;

    public StatisticsPresenter(StatisticsActivityContract.View view) {
        statisticsView = view;
        initPresenter();
    }

    /** Inits the presenter, model and view. Creates a timer to update the graph.
     */
    private void initPresenter()
    {
        statisticsModel = new StatisticsActivityModel();
        statisticsView.initView();

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                onUpdate();
            }
        }, 0, 800);
    }

    /** Update logic for graph
     *  Plots any EntryType feeded.
     */
    public void onUpdate()
    {
        BarGraphSeries<DataPoint> graph = statisticsModel.getGraph(Db.EntryType.ECOSCORE);
        graph.setSpacing(5);
        statisticsView.setGraphData(graph);
        statisticsView.setGraphMaxX(statisticsModel.getGraphMaxX());
    }

    /** @return x-size of graph from model
     */
    @Override
    public int getGraphMaxX()
    { return statisticsModel.getGraphMaxX(); }
}
