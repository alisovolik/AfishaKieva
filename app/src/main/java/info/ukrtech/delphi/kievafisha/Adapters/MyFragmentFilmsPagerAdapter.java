package info.ukrtech.delphi.kievafisha.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import info.ukrtech.delphi.kievafisha.Screen.FullFilmsFragment;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;

/**
 * Created by Delphi on 15.05.2015.
 */

public class MyFragmentFilmsPagerAdapter extends FragmentStatePagerAdapter {

    private static int NUM_ITEMS = SharedMethods.allFilms.size();
    private Context mCont;
    private int dataType; //Тип отображаемой информации (по умолчанию фильмы)


    public MyFragmentFilmsPagerAdapter(FragmentManager fragmentManager, Context context, int dataType) {
        super(fragmentManager);
        this.mCont = context;
        this.dataType = dataType;
     }

    @Override
    public int getCount() {
        int c = SharedMethods.allFilms.size();
        return c;
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {
        Fragment f = new FullFilmsFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        args.putInt("dataType", dataType);
        f.setArguments(args);
        return f;
    }




    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }



    public void addToFavorite(int position) {
        //SharedMethods.saveOneNewsToFavorite(mCont, SharedMethods.allFilms.get(position));
        //Shared.allNews.remove(position);
        //notifyDataSetChanged();
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

}
