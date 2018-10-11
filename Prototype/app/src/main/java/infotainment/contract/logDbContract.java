package infotainment.contract;

import android.provider.BaseColumns;

public final class logDbContract
{
    private logDbContract() {}

    public static class logEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "log";
        public static final String COLUMN_NAME_RPM = "rpm";
        public static final String COLUMN_NAME_SPEED = "speed";
    }

    public static final String SQL_CREATE_ENTRIES =

            "CREATE TABLE " + logEntry.TABLE_NAME + " (" +
                    logEntry._ID + " INTEGER PRIMARY KEY," +
                    logEntry.COLUMN_NAME_RPM + " INTEGER," +
                    logEntry.COLUMN_NAME_SPEED + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =

            "DROP TABLE IF EXISTS " + logEntry.TABLE_NAME;
}