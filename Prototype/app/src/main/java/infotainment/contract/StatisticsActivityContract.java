package infotainment.contract;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

public interface StatisticsActivityContract {

    interface View {

        void initView();
        void setGraphData(BarGraphSeries<DataPoint> data);

    }

    interface Model {

        BarGraphSeries<DataPoint> getGraph();

    }

    interface Presenter {

        void onClick(android.view.View view);

    }

}
