package sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Balázs on 2014.09.16..
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private Context ctx;
    private final static String TAG = "Database helper";
    public static final int IMPORT = 1;
    public static final int EXPORT = 2;
    public static final int IMPORT_DEFAULT = 3;

    public DatabaseHelper(Context context, String name) {
        super(context, name, null, DbConstants.DATABASE_VERSION);
        this.ctx= context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Creating all table if not exists");
        for (String sql : DbConstants.DATABASE_CREATE_ALL) {
            Log.d("DB_CREATE_ALL", sql);
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for(String sql:DbConstants.DATABASE_DROP_ALL){
            db.execSQL(sql);
            Log.d("DB_DROP_ALL", sql);
        }
        for(String sql:DbConstants.DATABASE_CREATE_ALL){
            db.execSQL(sql);
            Log.d("DB_CREATE_ALL", sql);
        }
    }
}
