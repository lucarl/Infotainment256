package infotainment.Model;

import android.util.Pair;

import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import infotainment.Model.db.Db;
import infotainment.contract.LogDbContract;
import infotainment.contract.StatisticsActivityContract;

public class StatisticsActivityModel implements StatisticsActivityContract.Model {



    public BarGraphSeries<DataPoint> getGraph1() {

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {
                new DataPoint(0, 1),
                new DataPoint(1, 5),
                new DataPoint(2, 3),
                new DataPoint(3, 2),
                new DataPoint(4, 6)
        });
        return series;

    }

    public BarGraphSeries<DataPoint> getGraph()
    {
        List<Pair<Integer, LocalDateTime>> inList = Db.getData(Db.EntryType.RPM, Db.cTimeMinusMinutes(1));
        DataPoint[] dp = new DataPoint[inList.size()];

        int i = 0;
        for (Pair<Integer, LocalDateTime> p : inList)
            dp[i++] = new DataPoint(i, p.first);

        return new BarGraphSeries<>(dp);
    }

}
