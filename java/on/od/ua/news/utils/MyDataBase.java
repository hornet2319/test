package on.od.ua.news.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lubomyr Shershun on 25.07.2015.
 * l.sherhsun@gmail.com
 */
public class MyDataBase extends SQLiteAssetHelper {
    private static final String TAG = "MyDataBase.class";
    private static final String DATABASE_NAME = "news.sqlite";
    private static final int DATABASE_VERSION = 1;

    private static final String ITEM_TITLE = "title";
    private static final String ITEM_DESCRIPTION = "description";
    private static final String ITEM_CATEGORY = "category";
    private static final String ITEM_PUBDATE = "pubDate";
    private static final String ITEM_CREATOR = "creator";
    private static final String ITEM_URL = "url";
    private static final String ITEM_IMAGE_URI = "image_uri";
    private static final String ITEM_IMAGE_PATH = "image_path";
    private static final String TABLE = "Item";
    public MyDataBase(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Item getItem(String title){
        Item item=null;
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String selectQuery = "SELECT  * FROM " + TABLE + " WHERE " + ITEM_TITLE + " = '" +title+"'" ;
        String sqlTables = TABLE;
        qb.setTables(sqlTables);
        Cursor c =db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {

                item = new Item();
                item.setTitle(c.getString(c.getColumnIndex(ITEM_TITLE)));
                item.setCategory(c.getString(c.getColumnIndex(ITEM_CATEGORY)));
                item.setDescription(c.getString(c.getColumnIndex(ITEM_DESCRIPTION)));
                item.setPubDate(c.getString(c.getColumnIndex(ITEM_PUBDATE)));
                item.setCreator(c.getString(c.getColumnIndex(ITEM_CREATOR)));
                item.setURL(c.getString(c.getColumnIndex(ITEM_URL)));
                item.setImage_uri(c.getString(c.getColumnIndex(ITEM_IMAGE_URI)));
                item.setImage_path(c.getString(c.getColumnIndex(ITEM_IMAGE_PATH)));

            } while (c.moveToNext());
        }
        Log.i(TAG, "getItem() completed");
        c.close();
        db.close();
        return item;
    }
    public ArrayList<Item> getItems(){
        ArrayList<Item> items=new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

        String [] sqlSelect = {"id", ITEM_TITLE, ITEM_DESCRIPTION, ITEM_CATEGORY, ITEM_PUBDATE,
                ITEM_CREATOR, ITEM_URL, ITEM_IMAGE_URI, ITEM_IMAGE_PATH  };
        String sqlTables = TABLE;
        qb.setTables(sqlTables);
        Cursor c = qb.query(db, sqlSelect, null, null,
                null, null, null);

        if (c.moveToFirst()) {
            do {

                Item item = new Item();
                item.setTitle(c.getString(c.getColumnIndex(ITEM_TITLE)));
                item.setCategory(c.getString(c.getColumnIndex(ITEM_CATEGORY)));
                item.setDescription(c.getString(c.getColumnIndex(ITEM_DESCRIPTION)));
                item.setPubDate(c.getString(c.getColumnIndex(ITEM_PUBDATE)));
                item.setCreator(c.getString(c.getColumnIndex(ITEM_CREATOR)));
                item.setURL(c.getString(c.getColumnIndex(ITEM_URL)));
                item.setImage_uri(c.getString(c.getColumnIndex(ITEM_IMAGE_URI)));
                item.setImage_path(c.getString(c.getColumnIndex(ITEM_IMAGE_PATH)));
                items.add(item);
            } while (c.moveToNext());
        }
        Log.i(TAG, "getItems() completed");
        c.close();
        db.close();
        return items;
    }
    public void setItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ITEM_TITLE, item.getTitle());
        values.put(ITEM_DESCRIPTION, item.getDescription());
        values.put(ITEM_CATEGORY, item.getCategory());
        values.put(ITEM_PUBDATE, item.getPubDate());
        values.put(ITEM_CREATOR, item.getCreator());
        values.put(ITEM_URL, item.getURL());
        values.put(ITEM_IMAGE_URI, item.getImage_uri());
        values.put(ITEM_IMAGE_PATH, item.getImage_path());


        // insert row
        db.insert(TABLE, null, values);
        Log.i(TAG, "Item added");
        db.close();
    }
    public void clear(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE, null, null);
        Log.i(TAG, "All Items deleted");
        db.close();
    }
    public boolean isEmpty(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor dbCur = db.rawQuery("SELECT * FROM " + TABLE, null);
        Log.e("a", "" + dbCur.getCount());
        db.close();
        if (dbCur.getCount()>0){
            return false;

        }
        else return true;
    }
    public int numRows(){
        SQLiteDatabase db = getReadableDatabase();
        Log.i(TAG, "numRows=" + DatabaseUtils.queryNumEntries(db, TABLE));
        int n=(int)DatabaseUtils.queryNumEntries(db,TABLE);
        db.close();
        return n;
    }
    public List<Item> getItems(int from ,int to) {
        List<Item> items = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();


        String selectQuery = "Select * from " + TABLE + " limit " + from + ", " + to;
        String sqlTables = TABLE;
        qb.setTables(sqlTables);
        Cursor c = db.rawQuery(selectQuery, null);


        if (c.moveToFirst()) {
            do {

                Item item = new Item();
                item.setTitle(c.getString(c.getColumnIndex(ITEM_TITLE)));
                item.setCategory(c.getString(c.getColumnIndex(ITEM_CATEGORY)));
                item.setDescription(c.getString(c.getColumnIndex(ITEM_DESCRIPTION)));
                item.setPubDate(c.getString(c.getColumnIndex(ITEM_PUBDATE)));
                item.setCreator(c.getString(c.getColumnIndex(ITEM_CREATOR)));
                item.setURL(c.getString(c.getColumnIndex(ITEM_URL)));
                item.setImage_uri(c.getString(c.getColumnIndex(ITEM_IMAGE_URI)));
                item.setImage_path(c.getString(c.getColumnIndex(ITEM_IMAGE_PATH)));
                items.add(item);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return items;

    }
    public void deleteItem(Item item){
        SQLiteDatabase db = getWritableDatabase();
        String Query = "DELETE FROM "+TABLE+" WHERE "+ITEM_TITLE+"='"+item.getTitle()+"'";
        db.execSQL(Query);
        db.close();

    }
}