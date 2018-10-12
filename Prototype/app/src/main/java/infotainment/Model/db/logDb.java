package infotainment.Model.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

import infotainment.Model.DataFilter;
import infotainment.contract.logDbContract;

public class logDb extends Thread
{
    private logDbHelper helper;
    private SQLiteDatabase db;

    public logDb(logDbHelper helper)
    {
        this.helper = helper;
        this.db = this.helper.getWritableDatabase();
        //helper.onUpgrade(this.db, 1, 2);
        this.start();
    }

    @Override
    public void run()
    {
        ContentValues values = new ContentValues();
        this.insert(new Pair<Character, Integer>('r', 33));


/* Define a projection that specifies which columns from the database
 you will actually use after this query. _ID from logDbContract*/
        String[] projection = {
                BaseColumns._ID,
                logDbContract.logEntry.COLUMN_NAME_RPM,
                logDbContract.logEntry.COLUMN_NAME_SPEED
        };

// Filter results WHERE "title" = 'Hej'
        String selection = logDbContract.logEntry.COLUMN_NAME_RPM + " = ?";
        String[] selectionArgs = { "33" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                logDbContract.logEntry.COLUMN_NAME_SPEED + " DESC";

        Cursor cursor = db.query(
                logDbContract.logEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );


        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(logDbContract.logEntry._ID));
            itemIds.add(itemId);
        }
        cursor.close();

        for (Object item : itemIds)
        {
            System.out.println(item);
        }
    }

    /* could return newRowID & -1 instead of IllegalArg? */

    public void insert(Pair<Character, Integer> entry)
    {
        ContentValues values = new ContentValues();

        switch (entry.first){
            case 'r':
                values.put(logDbContract.logEntry.COLUMN_NAME_RPM, " " + entry.second);
                break;
            case 's':
                values.put(logDbContract.logEntry.COLUMN_NAME_SPEED, " " + entry.second);
                break;
            default:
                throw new IllegalArgumentException("insert of value " + entry.second +
                                                    " failed to logDb");
        }

        long newRowId = this.db.insert(logDbContract.logEntry.TABLE_NAME,
                            null, values);
    }


}
