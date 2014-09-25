package sql;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import model.Item;
import model.Model;

/**
 * Created by Balázs on 2014.09.16..
 */
public class ItemDbLoader extends DbLoader {

    @Override
    public int insert(Model model) {
        open();
        Item item = (Item) model;
        int nextId = getMaxId() + 1;

        String query = " INSERT INTO " + getTableName() + " (`" +
                DbConstants.Items.KEY_ID + "`, `" +
                DbConstants.Items.KEY_BARCODE + "`, `" +
                DbConstants.Items.KEY_DESCRIPTION + "`)" +
                " VALUES " + "(" +
                nextId + "," +
                "'" + item.barcode + "'" + "," +
                "'" + item.description + "'); ";

        Log.d("DB_QUERY", query);
        try {
            mDb.execSQL(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        close();
        return nextId;
    }

    @Override
    public void update(Model model) {

    }

    @Override
    public Cursor findById(int id) {
        return null;
    }

    @Override
    public String getTableName() {
        return DbConstants.Items.DATABASE_TABLE;
    }

    public ItemDbLoader(Context ctx) {
        super(ctx);
    }
/*
 *
 * ContentDbLoader-b�l bev�gni ide a dolgokat!
*/

    public void clear() {
        mDb.execSQL(DbConstants.Items.DATABASE_DROP);
        mDb.execSQL(DbConstants.Items.DATABASE_CREATE);
    }

    public Cursor fetchItems(int range) {
        String query = " SELECT " + getTableName() + ".*, " +
                getTableName() + "." + DbConstants.Items.KEY_ID + " AS _id" +
                " FROM " + getTableName() +
                " WHERE " + DbConstants.Items.KEY_ID +
                " BETWEEN " + getMinId() + " AND " + (getMinId() + range);
        Log.d("DB_QUERY", query);

        return mDb.rawQuery(query, null);
    }

    public Cursor fetchItems() {
        String query = " SELECT " + getTableName() + ".*, " +
                getTableName() + "." + DbConstants.Items.KEY_ID + " AS _id" +
                " FROM " + getTableName() +
                " WHERE " + DbConstants.Items.KEY_ID +
                " BETWEEN " + getMinId() + " AND " + (getMinId() + getMaxId());
        Log.d("DB_QUERY", query);
        return mDb.rawQuery(query, null);
    }

    public int getMaxId() throws NullPointerException {
        String query = " SELECT MAX(" + DbConstants.Items.KEY_ID + ") AS maxId " +
                " FROM " + getTableName() + ";";
        Log.d("getMaxId", query);
        Cursor cursor = mDb.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("maxId"));
        } else {
            return 0;
        }
    }

    public int getMinId() throws NullPointerException {
        String query = " SELECT MIN(" + DbConstants.Items.KEY_ID + ") AS minId " +
                " FROM " + getTableName() + ";";
        Log.d("getMinId", query);
        Cursor cursor = mDb.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            return cursor.getInt(cursor.getColumnIndex("minId"));
        } else {
            return 0;
        }
    }

    public Cursor findItemByBarcode(String barcode) {
        String query = "SELECT " + getTableName() + ".*, " +
                getTableName() + "." + DbConstants.Items.KEY_BARCODE + " AS _barcode " +
                " FROM " + getTableName() +
                " WHERE " + getTableName() + "." + DbConstants.Items.KEY_BARCODE + " = " + barcode +
                ";";
        return mDb.rawQuery(query, null);
    }

    public Item getArticleByCursor(Cursor c) {
        c.moveToFirst();
        return new Item(
                c.getString(c.getColumnIndex(DbConstants.Items.KEY_BARCODE)),
                c.getString(c.getColumnIndex(DbConstants.Items.KEY_DESCRIPTION))
        );
    }

    public void delete(Model model) {
        Item item = (Item) model;
        String query = " DELETE " +
                " FROM " + getTableName() +
                " WHERE " + getTableName() + "." + DbConstants.Items.KEY_BARCODE + " = " + item.barcode +
                ";";
        Log.d("delete Query", query);

        mDb.execSQL(query);
    }

    public boolean itemExists(String barcode) {
        String query = " SELECT " + DbConstants.Items.KEY_ID +
                " FROM " + getTableName() +
                " WHERE " + DbConstants.Items.KEY_BARCODE + " = " + barcode + ";";
        Cursor cursor = mDb.rawQuery(query, null);
        if (cursor.getCount() > 0){
            return true;
        } else {
            return false;
        }
    }
}
