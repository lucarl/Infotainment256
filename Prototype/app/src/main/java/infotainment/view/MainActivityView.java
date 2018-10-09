package infotainment.view;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semcon.oil.infotainment.R;

import java.util.ArrayList;
import java.util.List;

import infotainment.Model.db.logDbHelper;
import infotainment.contract.MainActivityContract;
import infotainment.contract.logDbContract;
import infotainment.presenter.MainActivityPresenter;


public class MainActivityView extends AppCompatActivity implements MainActivityContract.View{

    /*Car car;
    Handler handler;
    ServiceConnection serviceConnection;
    CarSensorManager sensorManager;
    CarHvacManager carHvacManager;*/

    private TextView resultTextView;
    private Button helloButton;
    private MainActivityContract.Presenter mPresenter;
    logDbHelper ldbh;



    /*CarSensorManager.OnSensorChangedListener ignitionStateChangedListener;
    /* CarSensorManager.OnSensorChangedListener testStateChanged; */
    //CarSensorManager.OnSensorChangedListener gearMonitor;
    //CarSensorManager.OnSensorChangedListener speedMonitor;

    //CarHvacManager.CarHvacEventCallback carHvacEventCallback;

    private static final int speedDataPermissionMagicNumber = 42;
    boolean useSpeedData = false;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ldbh = new logDbHelper(getApplicationContext());
        SQLiteDatabase db = ldbh.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(logDbContract.logEntry.COLUMN_NAME_TITLE, "Hej");
        values.put(logDbContract.logEntry.COLUMN_NAME_SUBTITLE, "OK");

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(logDbContract.logEntry.TABLE_NAME, null, values);


// Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                logDbContract.logEntry.COLUMN_NAME_TITLE,
                logDbContract.logEntry.COLUMN_NAME_SUBTITLE
        };

// Filter results WHERE "title" = 'My Title'
        String selection = logDbContract.logEntry.COLUMN_NAME_TITLE + " = ?";
        String[] selectionArgs = { "Hej" };

// How you want the results sorted in the resulting Cursor
        String sortOrder =
                logDbContract.logEntry.COLUMN_NAME_SUBTITLE + " DESC";

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


        mPresenter = new MainActivityPresenter(this);

    }

    @Override
    public void initView() {

        resultTextView = findViewById(R.id.resultTextView);
        helloButton = findViewById(R.id.helloButton);
        helloButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(android.view.View view) {

                mPresenter.onClick(view);

            }
        });

    }

    @Override
    public void setViewData(String data) {

        resultTextView.setText(data);

    }
}
