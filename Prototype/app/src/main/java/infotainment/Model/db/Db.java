package infotainment.Model.db;

import android.content.ContentValues;
import android.util.Pair;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import infotainment.contract.LogDbContract;

/** Interface used to perform insert / get data operations on the database
 */
public interface Db
{
    /** Supported types and their char codes
     */
     interface EntryType {

        char RPM = 'r';
        char ECOSCORE = 'e';
    }

    /** @param entry Data entry to LogDb of type <EntryType , value>
     *  types supported : EntryType.RPM <integer> , EntryType.EcoScore <Double>
     *  DateTime added to every entry
     */
    static void insertData(Pair<Character, Integer> entry)
    {
        /* Values to insert contained within ContentValues */
        ContentValues values = new ContentValues();

        switch (entry.first){

            case EntryType.RPM:
                values.put(LogDbContract.rpmEntry.COLUMN_NAME_DATETIME,
                        String.valueOf(LocalDateTime.now()));

                values.put(LogDbContract.rpmEntry.COLUMN_NAME_RPM, entry.second);
                LogDb.getDbInstance().insert(LogDbContract.rpmEntry.TABLE_NAME,
                        null, values);
                break;

            case EntryType.ECOSCORE:
                values.put(LogDbContract.ecoEntry.COLUMN_NAME_DATETIME,
                        String.valueOf(LocalDateTime.now()));

                values.put(LogDbContract.ecoEntry.COLUMN_NAME_ECOSCORE, entry.second);
                LogDb.getDbInstance().insert(LogDbContract.ecoEntry.TABLE_NAME,
                        null, values);
                break;

            default:
                throw new IllegalArgumentException("insert of value " + entry.second +
                        "into '" + entry.first + "' failed"); }
    }

    /** @param startPeriod TODO To hold a way of matching time frame with datetime
     *  @return List with entry data for specified time frame, descending order
     */
    static List<Pair<Integer, LocalDateTime>> getData(char entryType, LocalDateTime dateTimeStart)
    {

        switch (entryType){

            case EntryType.RPM:
                return LogDb.rpmCursorList(LogDb.query(LogDbContract.rpmEntry.TABLE_NAME,
                        LogDbContract.rpmEntry.COLUMN_NAME_RPM), dateTimeStart);

            case EntryType.ECOSCORE:
                return LogDb.ecoCursorList(LogDb.query(LogDbContract.ecoEntry.TABLE_NAME,
                        LogDbContract.ecoEntry.COLUMN_NAME_ECOSCORE), dateTimeStart);

            default:
                throw new IllegalArgumentException("get of value type '" + entryType
                        + "' failed");
        }
    }

    /** @param minutes Time period from (now - minutes)
     *  @return LocalDateTime of time (now - minutes)
     *  Intended for graphs with timeframe : (now - minutes) - now
     */
    static LocalDateTime cTimeMinusMinutes(long minutes)
    {
        return LocalDateTime.now().minusMinutes(minutes);
    }
}
