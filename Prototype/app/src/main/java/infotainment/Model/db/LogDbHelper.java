package infotainment.Model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import infotainment.contract.LogDbContract;

/** Class containing help method for LogDb operations and maintenance
 * TODO Clean database entries older than <time>
 */
  public class LogDbHelper extends SQLiteOpenHelper
{
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "logv1.db";

    /** @param context Included in SQLiteOpenHelper constructor
     */
    LogDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /** @param db Database to execute on
     *  Creates each table separately
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LogDbContract.rpmEntry.SQL_CREATE_RPM);
        db.execSQL(LogDbContract.ecoEntry.SQL_CREATE_ECOSCORE);
    }

    /** @param db
     *  @param oldVersion
     *  @param newVersion
     *  SQLiteOpenHelper
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        dropTables(db);
        onCreate(db);
    }

    /** @param db Database to execute on
     * Deletes each table separately
     */
    protected void dropTables(SQLiteDatabase db)
    {
        db.execSQL(LogDbContract.rpmEntry.SQL_DELETE_RPM);
        db.execSQL(LogDbContract.ecoEntry.SQL_DELETE_ECOSCORE);
    }
}
