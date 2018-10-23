package infotainment.Model;

import android.graphics.Color;
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

public class StatisticsActivityModel implements StatisticsActivityContract.Model
{
    /* Tweak this one for nice graph sizes */
    private static int X_SIZE = 12;

    /** @return Special list of x,y points.
     *  Queries database for entries the last minutes and returns it in the right format.
     */
    public BarGraphSeries<DataPoint> getGraph(char EntryType)
    {
        List inList = Db.getData(EntryType,
                                                            Db.cTimeMinusMinutes(1));
        DataPoint[] dpArray = new DataPoint[inList.size()];
        DataPoint dp;
        Collections.reverse(inList);

        int i = 0;
        for (Object p : inList)
        {
            if (p instanceof Pair)
            {
                dp = new DataPoint(i, (Integer) ((Pair) p).first);
                dpArray[i++] = dp;
            }
        }

        BarGraphSeries<DataPoint> barGraphSeries = new BarGraphSeries<>();
        barGraphSeries.resetData(dpArray);

        barGraphSeries.setValueDependentColor(data -> {
            if (data.getY() > 8)
                return Color.rgb(50, 255, 50);

            else if (data.getY() > 6)
                return Color.rgb(100, 200, 60);

            else if (data.getY() > 4)
                return Color.rgb(225, 100, 50);

            else
                return Color.rgb(255, 50, 50);
        });

        return barGraphSeries;
    }

    /** @return x-size of graph
     */
    public int getGraphMaxX()
    { return X_SIZE; }
}
