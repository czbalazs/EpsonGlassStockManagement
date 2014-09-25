package sql;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import model.Model;

/**
 * Created by Balázs on 2014.09.16..
 */
public abstract class DbLoader {

    protected Context ctx;
    protected DatabaseHelper dbHelper;
    protected SQLiteDatabase mDb;

    public abstract int insert(Model model);
    public abstract void update(Model model);
    public abstract Cursor findById(int id);
    public abstract String getTableName();

    public static final String TAG = "DbLoader";

    public DbLoader(Context ctx){
            this.ctx = ctx;
            }


    public void open() throws SQLException {
        dbHelper = new DatabaseHelper (ctx, DbConstants.DATABASE_NAME);
        mDb = dbHelper.getWritableDatabase();
        dbHelper.onCreate(mDb);		//fontos, hogy lefusson
    }


    public void close(){
        dbHelper.close();
    }

}
