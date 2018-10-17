package infotainment.contract;

import android.util.Pair;
import android.widget.TextView;

import java.time.LocalDateTime;
import java.util.List;

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

    }

    interface Presenter {

        void onClick(android.view.View view);

    }


}
