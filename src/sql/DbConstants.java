package sql;

/**
 * Created by Balázs on 2014.09.16..
 */
public class DbConstants {

    public static final String DATABASE_NAME = "data.db";
    public static final int DATABASE_VERSION = 1;

    public static String[] DATABASE_CREATE_ALL = new String[]{
            Items.DATABASE_CREATE
    };
    public static String[] DATABASE_DROP_ALL = new String[]{
            Items.DATABASE_DROP
    };

    public class Items {

        public static final String DATABASE_TABLE = "items";
        public static final String KEY_ID = "id";
        public static final String KEY_BARCODE = "barcode";
        public static final String KEY_DESCRIPTION = "description";

        public static final String DATABASE_CREATE = " CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + "("
                                                        + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                                                        + KEY_BARCODE + " TEXT,"
                                                        + KEY_DESCRIPTION + " TEXT"
                                                        + "); ";

        public static final String DATABASE_DROP = "drop table if exists " + DATABASE_TABLE + "; ";
    }

}
