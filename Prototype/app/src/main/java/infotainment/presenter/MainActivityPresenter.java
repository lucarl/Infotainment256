package infotainment.presenter;

import java.util.Arrays;

import infotainment.Model.MainActivityModel;
import infotainment.contract.MainActivityContract;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private MainActivityContract.View mView;
    private MainActivityContract.Model mModel;

    public MainActivityPresenter(MainActivityContract.View view) {

        mView = view;
        initPresenter();
    }

    private void initPresenter() {

        mModel = new MainActivityModel();
        mView.initView();

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
