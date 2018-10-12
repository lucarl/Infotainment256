package infotainment.presenter;

import android.content.Context;

import infotainment.Model.DataFilter;
import infotainment.Model.MainActivityModel;
import infotainment.Model.db.logDb;
import infotainment.Model.db.logDbHelper;
import infotainment.contract.MainActivityContract;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private MainActivityContract.View mView;
    private MainActivityContract.Model mModel;
    logDb db;
    private DataFilter dataFilter;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {

        this.db = new logDb(new logDbHelper(context));
        this.dataFilter = new DataFilter(db);
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
