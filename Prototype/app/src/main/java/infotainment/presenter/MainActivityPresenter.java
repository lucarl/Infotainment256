package infotainment.presenter;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.Pair;

import infotainment.Model.DataFilter;
import infotainment.Model.MainActivityModel;
import infotainment.Model.db.Db;
import infotainment.Model.db.LogDb;
import infotainment.contract.MainActivityContract;
import infotainment.view.MainActivity;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private MainActivityContract.View mView;
    private MainActivityContract.Model mModel;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {

        new LogDb(context);
        new RpmGenerator();
        //this.dataFilter = new DataFilter();
        mView = view;
        initPresenter();
    }

    private class GenTimerTask extends TimerTask{

        @Override
        public void run() {

            int randRPM = RpmGenerator.rndInt();
            Db.insertData(new Pair<>(Db.EntryType.RPM, randRPM));

            double resArr[] = new double[10];   //storleken bestämmer hur stor snittet är i ecoCal
            double ecoPointsRef=36;             //Ansatt referensvärde
            Arrays.fill(resArr,ecoPointsRef);
            double lambda= randRPM;                   //  l/100km
            double tilt = 0;
            //grader lutning
            mModel.setecoCal(lambda, tilt, resArr, ecoPointsRef);
            mView.setColor(mModel.getecoCal());
        }
    }

    private void initPresenter() {

        mModel = new MainActivityModel();
        mView.initView();

        new Timer().scheduleAtFixedRate(new GenTimerTask(), 0, 400);

        double resArr[] = new double[10];   //storleken bestämmer hur stor snittet är i ecoCal
        double ecoPointsRef=36;             //Ansatt referensvärde
        Arrays.fill(resArr,ecoPointsRef);
        double lambda=10;                   //  l/100km
        double tilt = 0;                    //grader lutning

        mModel.setecoCal(lambda, tilt, resArr, ecoPointsRef);
    }

    @Override
    public void onClick(android.view.View view) {

        String data = mModel.getData();
        mView.setViewData(data);

        mView.setColor(mModel.getecoCal());
    }


}
