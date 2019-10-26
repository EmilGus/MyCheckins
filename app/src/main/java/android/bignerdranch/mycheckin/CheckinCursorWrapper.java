package android.bignerdranch.mycheckin;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;
import java.util.UUID;

public class CheckinCursorWrapper extends CursorWrapper {
    public CheckinCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Checkin getCheckin(){
        String uuidString = getString(getColumnIndex(CheckinDBschema.CheckinTable.Cols.UUID));
        String title = getString(getColumnIndex(CheckinDBschema.CheckinTable.Cols.TITLE));
        String place = getString(getColumnIndex(CheckinDBschema.CheckinTable.Cols.PLACE));
        String details = getString(getColumnIndex(CheckinDBschema.CheckinTable.Cols.DETAILS));
        String date = getString(getColumnIndex(CheckinDBschema.CheckinTable.Cols.DATE));
        Double lat = getDouble(getColumnIndex(CheckinDBschema.CheckinTable.Cols.LAT));
        Double lng = getDouble(getColumnIndex(CheckinDBschema.CheckinTable.Cols.LNG));
        String photo = getString(getColumnIndex(CheckinDBschema.CheckinTable.Cols.PHOTO));

        Checkin checkin = new Checkin(UUID.fromString(uuidString));
        checkin.setTitle(title);
        checkin.setPlace(place);
        checkin.setDetails(details);
        checkin.setDate(new Date(date));

        checkin.setLat(lat);
        checkin.setLng(lng);
        checkin.setPhoto(photo);
        return checkin;
    }
}
