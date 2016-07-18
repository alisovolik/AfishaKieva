package info.ukrtech.delphi.kievafisha;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import info.ukrtech.delphi.kievafisha.Screen.FilmsListFragment;
import info.ukrtech.delphi.kievafisha.Screen.MainScreenFragment;
import info.ukrtech.delphi.kievafisha.Screen.PlacesListFragment;
import info.ukrtech.delphi.kievafisha.Screen.PlacesSreenFragment;
import info.ukrtech.delphi.kievafisha.Shared.FilmsItem;
import info.ukrtech.delphi.kievafisha.Shared.PlaceItem;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;


public class MainActivity extends AppCompatActivity
        implements MainScreenFragment.OnFragmentInteractionListener,
        FilmsListFragment.OnFragmentInteractionListener,
        PlacesSreenFragment.OnFragmentInteractionListener,
        PlacesListFragment.OnFragmentInteractionListener{

    private Toast pressBackToast;
    public static ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedMethods.mContext = this;
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        pressBackToast = Toast.makeText(getApplicationContext(), "Нажмите еще раз для выхода",
                Toast.LENGTH_SHORT);
        pressBackToast.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorFontAndIcon));
        TextView v = (TextView) pressBackToast.getView().findViewById(android.R.id.message);
        v.setTextColor(ContextCompat.getColor(this, R.color.colorBackground));

        SharedMethods.allFilms = new ArrayList<FilmsItem>();
        SharedMethods.allPlaces = new ArrayList<PlaceItem>();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MainScreenFragment()).commit();
        }

    }





    public void displayView(int position) {
        // ***************************************************///
        Fragment fragment = new Fragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (position) {
            case 0:
                fragment = new MainScreenFragment();
                break;

        }
        fragmentManager.beginTransaction().replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {

        // Главный экран
        if (SharedMethods.screenName.equals("main")) {
            long currentTime = System.currentTimeMillis();
            if (Math.abs(currentTime - SharedMethods.mLastBackPress) > SharedMethods.mBackPressThreshold) {
                pressBackToast.show();
                SharedMethods.mLastBackPress = currentTime;
            } else {
                pressBackToast.cancel();
                super.onBackPressed();
            }
        }else
            // Просмотр детальной информации по фильму - возвращаюсь к списку фильмов
            if (SharedMethods.screenName.equals("film_detail")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = FilmsListFragment.newInstance("no", SharedMethods.rvFilmsListPosition,
                        SharedMethods.FLAG_OPEN_FILMS); //Отобразить старые даннные
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }
        else
            // Просмотр списка групп - к главному меню
            if (SharedMethods.screenName.equals("films_list")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = new MainScreenFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }

        else
            // Просмотр мест - созврат в главное меню
            if (SharedMethods.screenName.equals("places")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = new MainScreenFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }
        else
            // Детальный просмотр места - нужно вернуться в список
            if (SharedMethods.screenName.equals("place_detail")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = PlacesListFragment.newInstance("no", SharedMethods.rvPlaceListPosition,
                        SharedMethods.openedPlace); //Отобразить старые даннные
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }

        else
            // Просмотр списка мест - к меню
            if (SharedMethods.screenName.equals("place_list")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = new PlacesSreenFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
            }

        else
            // Просмотр списка групп - к главному меню
            if (SharedMethods.screenName.equals("afisha_group")) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment = new MainScreenFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();
            } else
                super.onBackPressed();

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    /*@SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return true;
    }*/

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }






    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedMethods.allFilms.clear();

        if (SharedMethods.mProgressDialog != null) {
            SharedMethods.mProgressDialog.dismiss();
        }
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

}
