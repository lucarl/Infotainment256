package infotainment.presenter;

import android.content.Context;

import infotainment.Model.DataFilter;
import infotainment.Model.MainActivityModel;
import infotainment.Model.db.Db;
import infotainment.Model.db.LogDb;
import infotainment.contract.MainActivityContract;

public class MainActivityPresenter implements MainActivityContract.Presenter{

    private MainActivityContract.View mView;
    private MainActivityContract.Model mModel;
    private DataFilter dataFilter;

    public MainActivityPresenter(MainActivityContract.View view, Context context) {

        new LogDb(context);
        //this.dataFilter = new DataFilter();
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

        for (Object d : Db.getData(Db.EntryType.RPM, 0)){
            System.out.println(d);
        }

        mView.setViewData(data);

    }


}
