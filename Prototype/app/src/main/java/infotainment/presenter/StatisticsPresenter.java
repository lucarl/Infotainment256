package infotainment.presenter;

import android.view.View;

import infotainment.Model.StatisticsActivityModel;
import infotainment.contract.StatisticsActivityContract;

public class StatisticsPresenter implements StatisticsActivityContract.Presenter{

    private StatisticsActivityContract.View statisticsView;
    private StatisticsActivityContract.Model statisticsModel;

    public StatisticsPresenter(StatisticsActivityContract.View view) {
        statisticsView = view;
        initPresenter();
    }

    private void initPresenter() {

        statisticsModel = new StatisticsActivityModel();
        statisticsView.initView();
        statisticsView.setGraphData(statisticsModel.getGraph());
    }

    @Override
    public void onClick(View view) {
        // TODO
    }
}
