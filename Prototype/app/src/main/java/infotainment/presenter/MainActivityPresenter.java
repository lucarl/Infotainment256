package infotainment.presenter;

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

    }


    @Override
    public void onClick(android.view.View view) {

        String data = mModel.getData();
        mView.setViewData(data);

    }


}
