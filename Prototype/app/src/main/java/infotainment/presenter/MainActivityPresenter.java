package infotainment.presenter;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

import infotainment.Model.MainActivityModel;
import infotainment.contract.MainActivityContract;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private MainActivityContract.View mView;
    private MainActivityContract.Model mModel;
    private boolean ecoDriving = false;

    public MainActivityPresenter(MainActivityContract.View view) {

        mView = view;
        initPresenter();

    }

    private void initPresenter() {

        mModel = new MainActivityModel();
        mView.initView();
    }

    public void toggleEcoDriving() {
        ecoDriving = !ecoDriving;
        Timer timer = new Timer();
        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                mView.updateBackgroundColor(mModel.getStartColor(),
                        mModel.getEndColor(),
                        mModel.getEcoRatio());
            }
        };
        if (ecoDriving) {
            timer.scheduleAtFixedRate(tt,0,500);
        } else {
            timer.cancel();
        }
        /* Handler handler = new Handler(); // TODO: create singleton / factory method
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mView.updateBackgroundColor(mModel.getStartColor(),
                        mModel.getEndColor(),
                        mModel.getEcoRatio());
            }
        }, 125);
        /* mView.updateBackgroundColor(mModel.getStartColor(),
                mModel.getEndColor(),
                mModel.getEcoRatio());
                */
    }

    @Override
    public void onClick(android.view.View view) {

        String data = mModel.getData();
        mView.setViewData(data);

    }
}
