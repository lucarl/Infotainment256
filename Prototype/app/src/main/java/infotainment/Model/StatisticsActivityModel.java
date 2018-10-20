package infotainment.Model;

import android.util.Pair;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.DataPointInterface;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import infotainment.Model.db.Db;
import infotainment.contract.StatisticsActivityContract;

public class StatisticsActivityModel implements StatisticsActivityContract.Model, ValueDependentColor
{
    /* Tweak this one for nice graph sizes */
    private static int X_SIZE = 12;

    /** @return Special list of x,y points.
     *  Queries database for entries the last minutes and returns it in the right format.
     */
    public BarGraphSeries<DataPoint> getGraph(char EntryType)
    {
        List<Pair<Integer, LocalDateTime>> inList = Db.getData(EntryType,
                                                            Db.cTimeMinusMinutes(1));
        DataPoint[] dpArray = new DataPoint[inList.size()];
        DataPoint dp;
        Collections.reverse(inList);

        int i = 0;
        for (Pair<Integer, LocalDateTime> p : inList)
        {
            dp = new DataPoint(i, p.first);
            dpArray[i++] = dp;
        }

        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>();
        barGraphSeries.resetData(dpArray);

        return barGraphSeries;
    }

    /** TODO change bar color
     *  @param data
     *  @return
     */
    @Override
    public int get(DataPointInterface data)
    { return 0; }

    /** @return x-size of graph
     */
    public int getGraphMaxX()
    { return X_SIZE; }
}
