package infotainment.contract;

public interface MainActivityContract {

    interface View {

        void initView();
        void setViewData(String data);
        void setColor(int fargval);

    }

    interface Model {

        String getData();
        void setecoCal(double lambda, double tilt, double [] resArr, double ref);
        int getecoCal();

    }

    interface Presenter {

        void onClick(android.view.View view);

    }

}
