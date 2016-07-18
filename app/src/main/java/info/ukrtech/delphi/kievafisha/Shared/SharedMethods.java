package info.ukrtech.delphi.kievafisha.Shared;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Screen.FilmsListFragment;
import info.ukrtech.delphi.kievafisha.Screen.PlacesListFragment;

/**
 * Created by Delphi on 09.06.2016.
 */
public class SharedMethods {

    public static String screenName = "main";
    public static Context mContext;
    public static long mLastBackPress;
    public static ProgressDialog mProgressDialog;
    public static final long mBackPressThreshold = 2500;
    public static String ACTION_DOWNLOAD_FILMS_LIST = "ACTION_DOWNLOAD_FILMS_LIST";
    public static String ACTION_DOWNLOAD_PLACE_LIST = "ACTION_DOWNLOAD_PLACE_LIST";
    public static final String base_url = "http://test1.lisovolik.t.ukrtech.info";
    public static final String url = "/index.php/api/listfilms/";
    public static final String place_url = "/index.php/api/listplaces/";
    public static int rvFilmsListPosition = 0; //для открытия на нужной позиции
    public static int rvPlaceListPosition = 0; //для открытия на нужной позиции
    public static String openedPlace = "cinema";

    //Для загрузки нужных данных в фрагмент
    public static final int FLAG_OPEN_FILMS = 0;
    public static final int FLAG_OPEN_CONCERTS = 1;
    public static final int FLAG_OPEN_PERFOMANCE = 2;
    public static final int FLAG_OPEN_EXHIBITION = 3;
    public static final int FLAG_OPEN_PARTY = 4;
    public static final int FLAG_OPEN_SPORT = 5;

    //Меню "Места". Отдельные переменные для удобства
    public static final int MENU_PLACE_CINEMA = 0;
    public static final int MENU_PLACE_THATRE = 1;
    public static final int MENU_PLACE_HALL = 2;
    public static final int MENU_PLACE_CLUB = 3;
    public static final int MENU_PLACE_KARAOKE = 4;
    public static final int MENU_PLACE_MUSEUM = 5;
    public static final int MENU_PLACE_GALLERY = 6;
    public static final int MENU_PLACE_ARTCAFE = 7;
    public static final int MENU_PLACE_QUEST = 8;
    public static final int MENU_PLACE_CIRCUS = 9;
    public static final int MENU_PLACE_RESTAURANT = 10;
    public static final int MENU_PLACE_PUB = 11;
    public static final int MENU_PLACE_CAFE = 12;
    public static final int MENU_PLACE_SUSHI = 13;
    public static final int MENU_PLACE_COFFEE = 14;
    public static final int MENU_PLACE_PIZZA = 15;
    public static final int MENU_PLACE_ICE = 16;
    public static final int MENU_PLACE_SAUNA = 17;
    public static final int MENU_PLACE_BANI = 18;


    public static ArrayList<FilmsItem> allFilms = null;
    public static ArrayList<PlaceItem> allPlaces = null;
    public static List<FilmsItem> inetFilms = null;
    public static List<PlaceItem> inetPlaces = null;


    public static void logMessage(String message) {
        Log.d("AfishaKIEV", "--ОТЛАДКА-- " + message);
    }


    public static void hideProgressDialog(){
        if (mProgressDialog != null) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
        }
    }


    public static void showProgressDialog(){
        mProgressDialog = new ProgressDialog(mContext, R.style.MyDialogTheme);
        mProgressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        if (SharedMethods.mProgressDialog != null) {
            SharedMethods.mProgressDialog.setMessage(mContext.getResources().getString(R.string.download_films_list_text));
            SharedMethods.mProgressDialog.show();
        }
    }

    public static void ShowMessage(String msg) {
        Toast toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        toast.getView().setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorToast));
        toast.show();
    }



    //Какие данные загружать по коду
    public static String getPlaceNameByCode(int code) {
        String place_name = "cinema";

        //Определяю, из какой таблицы загружать
        switch (code){
            case MENU_PLACE_CINEMA :
            {
                place_name = "cinema";
                break;
            }
            case MENU_PLACE_THATRE :
            {
                place_name = "theatre";
                break;
            }
            case MENU_PLACE_HALL :
            {
                place_name = "hall";
                break;
            }
            case MENU_PLACE_CLUB :
            {
                place_name = "night_club";
                break;
            }
            case MENU_PLACE_KARAOKE:
            {
                place_name = "karaoke";
                break;
            }
            case MENU_PLACE_MUSEUM :
            {
                place_name = "museum";
                break;
            }
            case MENU_PLACE_GALLERY :
            {
                place_name = "gallery";
                break;
            }
            case MENU_PLACE_ARTCAFE :
            {
                place_name = "artcafe";
                break;
            }
            case MENU_PLACE_QUEST :
            {
                place_name = "quest";
                break;
            }
            case MENU_PLACE_CIRCUS :
            {
                place_name = "kids";
                break;
            }
            case MENU_PLACE_RESTAURANT :
            {
                place_name = "restoraunt";
                break;
            }
            case MENU_PLACE_PUB :
            {
                place_name = "pubs";
                break;
            }
            case MENU_PLACE_CAFE :
            {
                place_name = "cafe";
                break;
            }
            case MENU_PLACE_SUSHI :
            {
                place_name = "sushi";
                break;
            }
            case MENU_PLACE_COFFEE :
            {
                place_name = "kofejni";
                break;
            }
            case MENU_PLACE_PIZZA :
            {
                place_name = "pizza";
                break;
            }
            case MENU_PLACE_ICE :
            {
                place_name = "katki";
                break;
            }
            case MENU_PLACE_SAUNA :
            {
                place_name = "sauni";
                break;
            }
            case MENU_PLACE_BANI :
            {
                place_name = "bani";
                break;
            }

        }

        return place_name;
    }

    //Случайное число
    public static int maxRangeValue(){
        Random r = new Random();
        return r.nextInt(100 - 10 + 1) + 10;
    }


    public static String mixString(String in){
        ArrayList<Character> arrayList = new ArrayList<>();
        for (int i = 0; i < in.length(); i++) {
            arrayList.add(in.charAt(i));
        }

        Collections.shuffle(arrayList);
        String result = "";
        for (int i = 0; i < arrayList.size(); i++) {
            result = result + arrayList.get(i);
        }

        return result;
    }



    public static void shareEvent(Context context, FilmsItem item) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("plane/txt");
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String shareBody = item.text;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, item.title);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, item.text);
        context.startActivity(Intent.createChooser(sharingIntent, "Поделиться"));
    }


    //Есть ли интернет
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }


    //Префикс таблицы по коду
    public static String getTabNameByCode(int code) {
        String tab_name = "films";

        //Определяю, из какой таблицы загружать
        switch (code){
            case FLAG_OPEN_FILMS :
            {
                tab_name = "films";
                break;
            }
            case FLAG_OPEN_CONCERTS :
            {
                tab_name = "koncert";
                break;
            }
            case FLAG_OPEN_PERFOMANCE :
            {
                tab_name = "perfomance";
                break;
            }
            case FLAG_OPEN_EXHIBITION :
            {
                tab_name = "exhibition";
                break;
            }
            case FLAG_OPEN_PARTY :
            {
                tab_name = "party";
                break;
            }
            case FLAG_OPEN_SPORT :
            {
                tab_name = "sport";
                break;
            }
        }

        return tab_name;
    }



    public static void loadPlacesListFromDB(Context context,  String place) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query;
        PlaceItem item;
        int id;

        try
        {
            if (allPlaces == null) allPlaces = new ArrayList<>();

            //Сразу очищаю
            allPlaces.clear();

            Cursor cur;
            query = "select *  from places_list where alias='" + place + "'  order by id asc";

            cur = db.rawQuery(query, null);
            if (cur.moveToFirst()) {
                do {

                    id = cur.getInt(cur.getColumnIndex("id"));

                    item = new PlaceItem(
                            id,
                            cur.getString(cur.getColumnIndex("name")),
                            cur.getString(cur.getColumnIndex("address")),
                            cur.getString(cur.getColumnIndex("phone")),
                            cur.getString(cur.getColumnIndex("photo")),
                            cur.getString(cur.getColumnIndex("description")),
                            cur.getString(cur.getColumnIndex("alias")));

                    allPlaces.add(item);

                } while (cur.moveToNext());
            }
            cur.close();

            //обновляю на экране
            PlacesListFragment frag = (PlacesListFragment)
                    ((info.ukrtech.delphi.kievafisha.MainActivity) context).getSupportFragmentManager().findFragmentById(R.id.container);
            if (frag != null) {
                frag.mAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            dbHelper.close();
            logMessage("ERROR LoadPlaceFromDB " + e.toString());
        }
        dbHelper.close();

    }



    public static void loadFilmsListFromDB(Context context, String dat_pokaz, int tab_code) {
        DBHelper dbHelper = DBHelper.getInstance(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query;
        FilmsItem item;
        int id;

        try
        {
            //Сразу очищаю
            allFilms.clear();
            String tab_name = getTabNameByCode(tab_code);

            Cursor cur, cur_shedule;
            query = "select *  from " + tab_name + "_list where dat_pokaz='" + dat_pokaz + "'  order by id asc";

            cur = db.rawQuery(query, null);
            if (cur.moveToFirst()) {
                do {

                    id = cur.getInt(cur.getColumnIndex("id"));

                    //Теперь для каждого фильма подгружаю расписание
                    String query_sedule = "select *  from " + tab_name + "_times where film_id=" +
                            id + " and dat_pokaz='" + dat_pokaz + "' order by id desc";

                    cur_shedule = db.rawQuery(query_sedule, null);

                    ArrayList<SheduleFilmItem> shedule = new ArrayList<>();
                    if (cur_shedule.moveToFirst()) {
                        do {
                            SheduleFilmItem sheduleFilmItem = new SheduleFilmItem(
                                    cur_shedule.getInt(cur_shedule.getColumnIndex("id")),
                                    cur_shedule.getInt(cur_shedule.getColumnIndex("film_id")),
                                    cur_shedule.getString(cur_shedule.getColumnIndex("kogda")),
                                    cur_shedule.getString(cur_shedule.getColumnIndex("name")),
                                    cur_shedule.getString(cur_shedule.getColumnIndex("price")));

                            shedule.add(sheduleFilmItem);

                        } while (cur_shedule.moveToNext());
                    }

                    cur_shedule.close();


                    item = new FilmsItem(
                            id,
                            cur.getString(cur.getColumnIndex("title")),
                            cur.getString(cur.getColumnIndex("description")),
                            cur.getString(cur.getColumnIndex("photo")),
                            cur.getString(cur.getColumnIndex("trailer")),
                            cur.getString(cur.getColumnIndex("full_text")),
                            shedule);

                    allFilms.add(item);




                } while (cur.moveToNext());
            }
            cur.close();

            //обновляю на экране
            FilmsListFragment frag = (FilmsListFragment)
                    ((info.ukrtech.delphi.kievafisha.MainActivity) context).getSupportFragmentManager().findFragmentById(R.id.container);
            if (frag != null) {
                //frag.mAdapter.setLoaded();
                frag.mAdapter.notifyDataSetChanged();
            }
        }
        catch (Exception e)
        {
            dbHelper.close();
            logMessage("ERROR LoadFromDB " + e.toString());
        }
        dbHelper.close();

    }




    //Сохраняю загруженный список фильмов в БД
    public static void saveToDB(Context context, String dat_pokaz, String tab_name) {
        ContentValues cv = new ContentValues();
        DBHelper dbHelper = DBHelper.getInstance(context.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            /*Перезаписываю каждый раз. Возможно изменилось описание или исправили заголовок и тд*/
            db.execSQL("DELETE FROM " + tab_name + "_list WHERE dat_pokaz='" + dat_pokaz + "';");
            db.execSQL("DELETE FROM " + tab_name + "_times WHERE dat_pokaz='" + dat_pokaz + "';");
            allFilms.clear();


            for (int i = 0; i < inetFilms.size(); i++) {
                cv.clear();

                cv.put("id", inetFilms.get(i).id);
                cv.put("title", inetFilms.get(i).title);
                cv.put("photo", inetFilms.get(i).photo);
                cv.put("trailer", inetFilms.get(i).trailer);
                cv.put("description", inetFilms.get(i).text);
                cv.put("full_text", inetFilms.get(i).full_text);
                cv.put("dat_pokaz", dat_pokaz);

                db.insert(tab_name + "_list", null, cv);

                //Теперь для каждого фильма записываю расписание
                if (inetFilms.get(i).shedule != null) {
                    for (int j = 0; j < inetFilms.get(i).shedule.size(); j++) {
                        cv.clear();

                        cv.put("id", inetFilms.get(i).shedule.get(j).id);
                        cv.put("film_id", inetFilms.get(i).id);
                        cv.put("kogda", inetFilms.get(i).shedule.get(j).kogda);
                        cv.put("name", inetFilms.get(i).shedule.get(j).name);
                        cv.put("price", inetFilms.get(i).shedule.get(j).price);
                        cv.put("dat_pokaz", dat_pokaz);

                        db.insert(tab_name + "_times", null, cv);
                    } //for
                }

            } //for


        } catch (Exception e) {
            logMessage("SharedMethods #1 " + e.getMessage());
            db.close();

        }
        inetFilms.clear();
        db.close();

    }


    //Сохраняю загруженный список мест в БД
    public static void savePlacesToDB(Context context, String placeAlias) {
        ContentValues cv = new ContentValues();
        DBHelper dbHelper = DBHelper.getInstance(context.getApplicationContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        try {
            /*Перезаписываю каждый раз. Возможно изменилось описание или исправили заголовок и тд*/
            db.execSQL("DELETE FROM places_list WHERE alias='" + placeAlias + "';");

            allPlaces.clear();


            for (int i = 0; i < inetPlaces.size(); i++) {
                cv.clear();

                cv.put("id", inetPlaces.get(i).id);
                cv.put("name", inetPlaces.get(i).name);
                cv.put("address", inetPlaces.get(i).address);
                cv.put("phone", inetPlaces.get(i).phone);
                cv.put("photo", inetPlaces.get(i).photo);
                cv.put("description", inetPlaces.get(i).description);
                cv.put("alias", placeAlias);

                db.insert("places_list", null, cv);

            } //for

        } catch (Exception e) {
            logMessage("SharedMethods #1 " + e.getMessage());
            db.close();

        }
        inetPlaces.clear();
        db.close();

    }


    public static class DBHelper extends SQLiteOpenHelper {
        private static DBHelper instance = null;

        public DBHelper(Context context) {

            super(context, "afisha.db", null, 7);
        }

        public synchronized static DBHelper getInstance(Context ctx) {

            if (instance == null) {
                instance = new DBHelper(ctx.getApplicationContext());
            }
            return instance;
        }


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
                logMessage("onCreateDatabase " + e.toString());
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
