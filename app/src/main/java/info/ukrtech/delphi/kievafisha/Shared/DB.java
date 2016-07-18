package info.ukrtech.delphi.kievafisha.Shared;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import info.ukrtech.delphi.kievafisha.R;

/**
 * Created by Delphi on 12.07.2016.
 */
public class DB {

    private static final String DB_NAME = "afisha.db";
    private static final int DB_VERSION = 7;



    private final Context mCtx;


    private DBHelper mDBHelper;
    private SQLiteDatabase mDB;

    public DB(Context ctx) {
        mCtx = ctx;
    }

    // открыть подключение
    public void open() {
        mDBHelper = new DBHelper(mCtx, DB_NAME, null, DB_VERSION);
        mDB = mDBHelper.getWritableDatabase();
    }

    // закрыть подключение
    public void close() {
        if (mDBHelper!=null) mDBHelper.close();
    }

    // получить все данные из таблицы tableName
    public Cursor getAllData(String tableName) {
        return mDB.query(tableName + "_list", null, null, null, null, null, null);
    }

    // добавить запись в tableName
    public void addRec(FilmsItem item, String tableName, String dat_pokaz) {

        mDB.execSQL("DELETE FROM " + tableName + "_list WHERE dat_pokaz='" + dat_pokaz + "';");
        mDB.execSQL("DELETE FROM " + tableName + "_times WHERE dat_pokaz='" + dat_pokaz + "';");

        ContentValues cv = new ContentValues();
        cv.put("id", item.id);
        cv.put("title", item.title);
        cv.put("photo", item.photo);
        cv.put("trailer", item.trailer);
        cv.put("description", item.text);
        cv.put("full_text", item.full_text);
        cv.put("dat_pokaz", dat_pokaz);

        mDB.insert(tableName + "_list", null, cv);

        //Теперь для каждого фильма записываю расписание
        if (item.shedule != null) {
            for (int j = 0; j < item.shedule.size(); j++) {
                cv.clear();

                cv.put("id", item.shedule.get(j).id);
                cv.put("film_id", item.id);
                cv.put("kogda", item.shedule.get(j).kogda);
                cv.put("name", item.shedule.get(j).name);
                cv.put("price", item.shedule.get(j).price);
                cv.put("dat_pokaz", dat_pokaz);

                mDB.insert(tableName + "_times", null, cv);
            } //for
        }

    }



    // класс по созданию и управлению БД
    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                        int version) {
            super(context, name, factory, version);
        }

        // создаем и заполняем БД
        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL("create table if not exists films_list (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "title text, "
                        + "photo text, "
                        + "description text, "
                        + "dat_pokaz text, "
                        + "trailer text, "
                        + "full_text text, "
                        + "add_time text)");

                db.execSQL("create table if not exists films_times (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "film_id int,"
                        + "kogda text, "
                        + "name text, "
                        + "dat_pokaz text, "
                        + "price text)");

                db.execSQL("create table if not exists exhibition_list (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "title text, "
                        + "photo text, "
                        + "description text, "
                        + "dat_pokaz text, "
                        + "trailer text, "
                        + "full_text text, "
                        + "add_time text)");

                db.execSQL("create table if not exists exhibition_times (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "film_id int,"
                        + "kogda text, "
                        + "name text, "
                        + "dat_pokaz text, "
                        + "price text)");

                db.execSQL("create table if not exists koncert_list (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "title text, "
                        + "photo text, "
                        + "description text, "
                        + "dat_pokaz text, "
                        + "trailer text, "
                        + "full_text text, "
                        + "add_time text)");

                db.execSQL("create table if not exists koncert_times (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "film_id int,"
                        + "kogda text, "
                        + "name text, "
                        + "dat_pokaz text, "
                        + "price text)");

                db.execSQL("create table if not exists party_list (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "title text, "
                        + "photo text, "
                        + "description text, "
                        + "dat_pokaz text, "
                        + "trailer text, "
                        + "full_text text, "
                        + "add_time text)");

                db.execSQL("create table if not exists party_times (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "film_id int,"
                        + "kogda text, "
                        + "name text, "
                        + "dat_pokaz text, "
                        + "price text)");

                db.execSQL("create table if not exists perfomance_list (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "title text, "
                        + "photo text, "
                        + "description text, "
                        + "dat_pokaz text, "
                        + "trailer text, "
                        + "full_text text, "
                        + "add_time text)");

                db.execSQL("create table if not exists perfomance_times (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "film_id int,"
                        + "kogda text, "
                        + "name text, "
                        + "dat_pokaz text, "
                        + "price text)");

                db.execSQL("create table if not exists sport_list (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "title text, "
                        + "photo text, "
                        + "description text, "
                        + "dat_pokaz text, "
                        + "trailer text, "
                        + "full_text text, "
                        + "add_time text)");

                db.execSQL("create table if not exists sport_times (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "film_id int,"
                        + "kogda text, "
                        + "name text, "
                        + "dat_pokaz text, "
                        + "price text)");

                db.execSQL("create table if not exists favorites_films (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "title text, "
                        + "photo text, "
                        + "description text, "
                        + "dat_pokaz text, "
                        + "full_text text, "
                        + "add_time text)");

                db.execSQL("create table if not exists favorites_times (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "film_id int,"
                        + "kogda text, "
                        + "name text, "
                        + "dat_pokaz text, "
                        + "price text)");

                db.execSQL("create table if not exists places_list (_id integer primary key autoincrement," //id
                        + "id int,"
                        + "name text,"
                        + "address text, "
                        + "phone text, "
                        + "photo text, "
                        + "description text, "
                        + "alias text)");

            } catch (SQLException e) {
                SharedMethods.logMessage("onCreateDatabase " + e.toString());
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS films_list");
            db.execSQL("DROP TABLE IF EXISTS films_times");
            db.execSQL("DROP TABLE IF EXISTS party_list");
            db.execSQL("DROP TABLE IF EXISTS party_times");
            db.execSQL("DROP TABLE IF EXISTS perfomance_list");
            db.execSQL("DROP TABLE IF EXISTS perfomance_times");
            db.execSQL("DROP TABLE IF EXISTS exhibition_list");
            db.execSQL("DROP TABLE IF EXISTS exhibition_times");
            db.execSQL("DROP TABLE IF EXISTS koncert_list");
            db.execSQL("DROP TABLE IF EXISTS koncert_times");
            db.execSQL("DROP TABLE IF EXISTS sport_list");
            db.execSQL("DROP TABLE IF EXISTS sport_times");
            db.execSQL("DROP TABLE IF EXISTS favorites_films");
            db.execSQL("DROP TABLE IF EXISTS favorites_times");
            db.execSQL("DROP TABLE IF EXISTS places_list");
            onCreate(db);
        }
    }
}