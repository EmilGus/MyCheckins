package android.bignerdranch.mycheckin;

public class CheckinDBschema {

    public static final class CheckinTable {
        public static final String NAME = "checkins";

        public static final class Cols {
            public static final String ID = "id";
            public static final String TITLE = "title";
            public static final String PLACE = "place";
            public static final String DETAILS = "details";
            public static final String DATE = "date";
            public static final String LAT = "lat";
            public static final String LNG = "lng";
            //public static final String PHOTO = "photo";
        }
    }
}
