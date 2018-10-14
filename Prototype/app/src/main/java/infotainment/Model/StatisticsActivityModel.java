package infotainment.Model;

import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;

import infotainment.Model.db.Db;
import infotainment.contract.StatisticsActivityContract;

public class StatisticsActivityModel implements StatisticsActivityContract.Model {


    @Override
    public LineGraphSeries<DataPoint> getGraph() {

        List data = Db.getData(Db.EntryType.RPM, 0);
        DataPoint[] dp = new DataPoint[data.size()];

        int i = data.size() - 1;
        for (Object entry : data)
            dp[i] = new DataPoint(i--, ((Double) entry).intValue());

        return new LineGraphSeries<>(dp);
    }
}
