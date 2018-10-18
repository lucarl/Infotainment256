package infotainment.contract;

import android.widget.TextView;

public interface MainActivityContract {

    interface View {

        void initView();
        void setViewData(String data);
        void setColor(int fargval);
        TextView getResultTextView();

    }

    interface Model {

        String getData();
        void setecoCal(double lambda, double tilt, double [] resArr, double ref);
        int getecoCal();
        void dataInput(Character c, int d);
            /* c is data tag and d is raw data */

    }

    interface Presenter {

        void onClick(android.view.View view);

    }

}
