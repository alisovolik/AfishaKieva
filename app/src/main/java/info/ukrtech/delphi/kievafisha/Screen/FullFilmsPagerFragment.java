package info.ukrtech.delphi.kievafisha.Screen;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.ukrtech.delphi.kievafisha.Adapters.MyFragmentFilmsPagerAdapter;
import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;


/**
 * A simple {@link Fragment} subclass.
 */
public class FullFilmsPagerFragment extends Fragment {
    private static int position = 0;
    private static int dataType = 0; //Фильмы


    ViewPager pager;
    MyFragmentFilmsPagerAdapter pagerAdapter;

    public static FullFilmsPagerFragment newInstance(int _pos, int dataType) {
        FullFilmsPagerFragment fragment = new FullFilmsPagerFragment();
        position = _pos;
        dataType = dataType;
        return fragment;
    }


    public FullFilmsPagerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_full_films_item, container, false);


        if (pager == null)
        {
            try {
                pager = (ViewPager) view.findViewById(R.id.idFilmsPager);
                pagerAdapter = new MyFragmentFilmsPagerAdapter(this.getChildFragmentManager(), getContext(), dataType);
                pager.setAdapter(pagerAdapter);
                pager.setOffscreenPageLimit(1);
                pager.setCurrentItem(position);

                SharedMethods.rvFilmsListPosition = position;
            }catch(Exception e){
                SharedMethods.logMessage("FullFilmsPagerFragment #1 " + e.getMessage());
            }

        }

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                SharedMethods.rvFilmsListPosition = position;
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        return view;
    }



    @Override
    public void onStart() {
        SharedMethods.screenName = "film_detail";
        super.onStart();
    }

    @Override
    public void onResume() {
        SharedMethods.screenName = "film_detail";
        super.onResume();
    }

}
