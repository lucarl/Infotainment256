package infotainment.contract;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public interface StatisticsActivityContract {

    interface View {

        void initView();
        void setGraphData(LineGraphSeries<DataPoint> data);
        void clearGraphData();

    }

    interface Model {

        LineGraphSeries<DataPoint> getGraph();
    }

    interface Presenter {

        void onClick(android.view.View view);

    }

}
