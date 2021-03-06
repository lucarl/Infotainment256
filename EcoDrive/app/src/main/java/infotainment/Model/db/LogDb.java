package infotainment.Model.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.facebook.stetho.Stetho;
import java.io.NotActiveException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import infotainment.contract.LogDbContract;

/** This class handles the database querying logic from the Db Interface
 */
public class LogDb implements Db
{
    /* Stetho debugging tools & clean db each run MODE */
    private static final boolean DEBUG_MODE = true;

    private static SQLiteDatabase db;

    /** @param context Grabbed from app context
     */
    public LogDb(Context context)
    {
        LogDbHelper helper = new LogDbHelper(context);
        db = helper.getWritableDatabase();

        if (DEBUG_MODE)
        {
            /* facebook tool - chrome tools debugger */
            Stetho.initializeWithDefaults(context);

            /* Currently used to reset database tables each run */
            helper.onUpgrade(getDbInstance(), 1, 1);
        }
    }

    /** @param tableName The table name to grab with the query
     *  @param sortByColumn Sorts data by this columns descending
     *  @return A cursor with the result of the query
     */
    protected static Cursor query(String tableName, String sortByColumn)
    {
        /* How you want the results sorted in the resulting Cursor */
        String sortOrder = sortByColumn + " DESC limit " + GraphTweak.X_SIZE;

        /* Query of all items in column descending */
        return  getDbInstance().query(
                tableName,                  // The table to query
                null,               // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,           // The values for the WHERE clause
                null,               // don't group the rows
                null,                // don't filter by row groups
                sortOrder );                // The sort order
    }

    /** @param cursor The cursor with the data to list
     *  @return A list with RPM values from the cursor's query
     */
    protected static List<Pair<Integer, LocalDateTime>> rpmCursorList(Cursor cursor, LocalDateTime startDateTime)
    {
        ArrayList<Pair<Integer, LocalDateTime>> list = new ArrayList<>();

        while(cursor.moveToNext()) {

            Double itemId = cursor.getDouble(
                    cursor.getColumnIndexOrThrow(LogDbContract.rpmEntry.COLUMN_NAME_RPM));
            LocalDateTime itemldt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(
                                                        LogDbContract.rpmEntry.COLUMN_NAME_DATETIME)));
            //if (!itemldt.isBefore(startDateTime))
            list.add(new Pair<>(itemId.intValue(), itemldt));
        }

        cursor.close();
        return list;
    }

    /** @param cursor The cursor with the data to list
     *  @return A list with RPM values from the cursor's query
     */
    protected static List ecoCursorList(Cursor cursor, LocalDateTime startDateTime)
    {
        ArrayList<Pair<Integer, LocalDateTime>> list = new ArrayList<>();
        while(cursor.moveToNext()) {

            Integer itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(LogDbContract.ecoEntry.COLUMN_NAME_ECOSCORE));
            LocalDateTime itemldt = LocalDateTime.parse(cursor.getString(cursor.getColumnIndexOrThrow(
                    LogDbContract.ecoEntry.COLUMN_NAME_DATETIME)));

            //if (!itemldt.isBefore(startDateTime))
            list.add(new Pair<>(itemId, itemldt));
        }

        cursor.close();
        return list;
    }

    /** Ensure the database is connected
     *  @return Reference to the connected database
     */
    protected static SQLiteDatabase getDbInstance()
    {
        if (db == null) {
            try {
                throw new NotActiveException("Database not running");
            } catch (NotActiveException e) {
                e.printStackTrace();
            }
        }
        return db;
    }

    /** Close the database if it is connected
     */
    public static void exit()
    {
        if (db == null)
            return;
        db.close();
    }
}
