package info.ukrtech.delphi.kievafisha.Screen;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import info.ukrtech.delphi.kievafisha.BuildConfig;
import info.ukrtech.delphi.kievafisha.MainActivity;
import info.ukrtech.delphi.kievafisha.R;

import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;


public class MainScreenFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    ViewGroup mContainerScene;
    Scene mMainScreenScene;
    Scene mListScreenScene;


    public MainScreenFragment() {
        // Required empty public constructor
    }


    public static MainScreenFragment newInstance(Context context) {
        MainScreenFragment fragment = new MainScreenFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.actionBar.hide();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //View rootView = inflater.inflate(R.layout.fragment_main_screen, container, false);
        View rootView = inflater.inflate(R.layout.main_screen_layout, container, false);


        updateClickListener(rootView);

        return rootView;
    }






    private void updateClickListener(View rootView) {
        //Фильмы
        LinearLayout linFilm = (LinearLayout)rootView.findViewById(R.id.idFilmsLayout);
        linFilm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                Fragment fragment = FilmsListFragment.newInstance("yes", SharedMethods.rvFilmsListPosition,
                        SharedMethods.FLAG_OPEN_FILMS);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                MainActivity.actionBar.setTitle("Фильмы");

            }
        });

        //Концерты
        LinearLayout linConcert = (LinearLayout)rootView.findViewById(R.id.idConcertLayout);
        linConcert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                Fragment fragment = FilmsListFragment.newInstance("yes", SharedMethods.rvFilmsListPosition,
                        SharedMethods.FLAG_OPEN_CONCERTS);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                MainActivity.actionBar.setTitle("Концерты");
            }
        });


        //Спектакли
        LinearLayout linPerfomance = (LinearLayout)rootView.findViewById(R.id.idPerfomanceLayout);
        linPerfomance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                Fragment fragment = FilmsListFragment.newInstance("yes", SharedMethods.rvFilmsListPosition,
                        SharedMethods.FLAG_OPEN_PERFOMANCE);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                MainActivity.actionBar.setTitle("Спектакли");
            }
        });

        //Выставки
        LinearLayout linExhibition = (LinearLayout)rootView.findViewById(R.id.idExhibitionLayout);
        linExhibition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                Fragment fragment = FilmsListFragment.newInstance("yes", SharedMethods.rvFilmsListPosition,
                        SharedMethods.FLAG_OPEN_EXHIBITION);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                MainActivity.actionBar.setTitle("Выставки");
            }
        });

        //вечеринки
        LinearLayout linParty = (LinearLayout)rootView.findViewById(R.id.idPartyLayout);
        linParty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                Fragment fragment = FilmsListFragment.newInstance("yes", SharedMethods.rvFilmsListPosition,
                        SharedMethods.FLAG_OPEN_PARTY);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                MainActivity.actionBar.setTitle("Вечеринки");
            }
        });

        //Спорт
        LinearLayout linSport = (LinearLayout)rootView.findViewById(R.id.idSportLayout);
        linSport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                Fragment fragment = FilmsListFragment.newInstance("yes", SharedMethods.rvFilmsListPosition,
                        SharedMethods.FLAG_OPEN_SPORT);
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                MainActivity.actionBar.setTitle("Спорт");
            }
        });

        //Places
        LinearLayout linPlaces = (LinearLayout)rootView.findViewById(R.id.idPlacesLayout);
        linPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*if (BuildConfig.DEBUG){*/
                   FragmentManager fragmentManager = getActivity()
                        .getSupportFragmentManager();
                Fragment fragment = PlacesSreenFragment.newInstance(getActivity());
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment).commit();

                MainActivity.actionBar.setTitle("Места");
               /* }
                else {
                    SharedMethods.ShowMessage("В разработке");
                }*/


            }
        });

        //Exit
        LinearLayout linExit = (LinearLayout)rootView.findViewById(R.id.idExitLayout);
        linExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });


    }





    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onStart() {
        SharedMethods.screenName = "main";
        super.onStart();
    }

    @Override
    public void onResume() {
        SharedMethods.screenName = "main";
        super.onResume();
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
