package infotainment.presenter;

import android.util.Pair;
import android.view.View;

import java.util.Random;
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

    private void initPresenter() {

        statisticsModel = new StatisticsActivityModel();
        statisticsView.initView();
        statisticsView.setGraphData(statisticsModel.getGraph());
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Db.insertData(new Pair<>(Db.EntryType.RPM, new Random().nextInt(10)));
                statisticsView.setGraphData(statisticsModel.getGraph());
            }
        }, 500, 1000);
    }

    @Override
    public void onClick(View view) {
        // TODO
    }
}
