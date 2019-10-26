package android.bignerdranch.mycheckin;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import static android.bignerdranch.mycheckin.CheckinDBschema.*;
import static android.bignerdranch.mycheckin.CheckinDBschema.CheckinTable.Cols.DETAILS;

public class CheckinLab {
    private static CheckinLab sCheckinLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;

    public static CheckinLab get(Context context){
        if(sCheckinLab == null){
            sCheckinLab = new CheckinLab(context);
        }
        return sCheckinLab;
    }

    public File getPhotoFile(Checkin checkin){
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, checkin.getPhotoFilename());
    }

    private CheckinLab(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new CheckinBaseHelper(mContext).getWritableDatabase();
    }

    public List<Checkin> getCheckins(){
        List<Checkin> checkins = new ArrayList<>();

        CheckinCursorWrapper cursor = queryCrimes(null, null);

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                checkins.add(cursor.getCheckin());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }

        return checkins;
    }

    public void addCheckin(Checkin checkin){
        ContentValues values = getContentValues(checkin);
        mDatabase.insert(CheckinTable.NAME, null, values);

    }

    public Checkin getCheckin(UUID id){
        CheckinCursorWrapper cursor = queryCrimes(
                CheckinTable.Cols.UUID + " = ?",
                new String[]{id.toString()}
        );

        try{
            if(cursor.getCount() == 0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getCheckin();
        }finally {
            cursor.close();
        }
    }

    public void updateCheckin(Checkin checkin){
        String uuidString = checkin.getID().toString();
        ContentValues values = getContentValues(checkin);

        mDatabase.update(CheckinTable.NAME, values, CheckinTable.Cols.UUID + " = ?", new String[]{uuidString});
    }

    public void deleteCheckin(UUID id){
        mDatabase.delete(CheckinDBschema.CheckinTable.NAME,CheckinTable.Cols.UUID + " = ?", new String[]{id.toString()});
    }

    private CheckinCursorWrapper queryCrimes(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(
                CheckinTable.NAME,
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );

        return new CheckinCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Checkin checkin){
        ContentValues values = new ContentValues();
        values.put(CheckinTable.Cols.UUID, checkin.getID().toString());
        values.put(CheckinTable.Cols.TITLE, checkin.getTitle());
        values.put(CheckinTable.Cols.PLACE, checkin.getPlace());
        values.put(CheckinTable.Cols.DETAILS, checkin.getDetails());
        values.put(CheckinTable.Cols.DATE, checkin.getDate().toString());
        values.put(CheckinTable.Cols.LAT, checkin.getLat());
        values.put(CheckinTable.Cols.LNG, checkin.getLng());
        values.put(CheckinTable.Cols.PHOTO, checkin.getPhoto());

        return values;
    }
}
