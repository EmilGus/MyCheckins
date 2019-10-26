package android.bignerdranch.mycheckin;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.bignerdranch.mycheckin.CheckinDBschema.*;

public class CheckinBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "CheckinDB.db";

    public CheckinBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + CheckinTable.NAME + "(" +
                " _id integer primary key autoincrement, " +
                CheckinTable.Cols.UUID + ", " +
                CheckinTable.Cols.TITLE + ", " +
                CheckinTable.Cols.PLACE + ", " +
                CheckinTable.Cols.DETAILS + ", " +
                CheckinTable.Cols.LAT + ", " +
                CheckinTable.Cols.LNG + ", " +
                CheckinTable.Cols.PHOTO + ", " +
                CheckinTable.Cols.DATE + ") ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
