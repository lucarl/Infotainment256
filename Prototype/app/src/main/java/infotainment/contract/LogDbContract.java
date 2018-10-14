package infotainment.contract;

import android.provider.BaseColumns;

/** Schema contract for LogDb
 */
public final class LogDbContract
{
    private LogDbContract() {}

    /** Table 'rpm' for RPM data entry
     */
    public static class rpmEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "rpmEntry";
        public static final String COLUMN_NAME_RPM = "rpm";
        public static final String COLUMN_NAME_DATETIME = "datetime";

        public static final String SQL_CREATE_RPM =

                        "CREATE TABLE " + rpmEntry.TABLE_NAME + " (" +
                        rpmEntry._ID + " INTEGER PRIMARY KEY, " +
                        rpmEntry.COLUMN_NAME_RPM + " INTEGER, " +
                        rpmEntry.COLUMN_NAME_DATETIME + "  DEFAULT CURRENT_DATETIME);";

        public static final String SQL_DELETE_RPM =

                        "DROP TABLE IF EXISTS " + rpmEntry.TABLE_NAME;
    }

    /** Table 'ecoScore' for EcoScore data entry
     */
    public static class ecoEntry implements BaseColumns
    {
        public static final String TABLE_NAME = "ecoEntry";
        public static final String COLUMN_NAME_ECOSCORE = "ecoScore";
        public static final String COLUMN_NAME_DATETIME = "datetime";

        public static final String SQL_CREATE_ECOSCORE =

                        "CREATE TABLE " + ecoEntry.TABLE_NAME + " (" +
                        ecoEntry._ID + " INTEGER PRIMARY KEY, " +
                        ecoEntry.COLUMN_NAME_ECOSCORE + " REAL, " +
                        ecoEntry.COLUMN_NAME_DATETIME + "  DEFAULT CURRENT_DATETIME);";

        public static final String SQL_DELETE_ECOSCORE =

                        "DROP TABLE IF EXISTS " + ecoEntry.TABLE_NAME;
    }
}