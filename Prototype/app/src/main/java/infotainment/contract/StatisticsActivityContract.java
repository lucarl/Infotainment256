package infotainment.contract;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

public interface StatisticsActivityContract {

    interface View {

        void initView();
        void setGraphData(BarGraphSeries<DataPoint> data);
        void setGraphMaxX(int max);
    }

    interface Model {

        BarGraphSeries<DataPoint> getGraph(char EntryType);
        int getGraphMaxX();
    }

    interface Presenter {

        void onUpdate();
        int getGraphMaxX();
    }

}
