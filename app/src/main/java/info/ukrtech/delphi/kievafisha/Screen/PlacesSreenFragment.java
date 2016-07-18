package info.ukrtech.delphi.kievafisha.Screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.ukrtech.delphi.kievafisha.MainActivity;
import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;

public class PlacesSreenFragment extends Fragment {

    @BindView(R.id.idPlaceCinema) TextView idPlaceCinema;
    @BindView(R.id.idPlaceTheatre) TextView idPlaceTheatre;
    @BindView(R.id.idPlaceHall) TextView idPlaceHall;
    @BindView(R.id.idPlaceClub) TextView idPlaceClub;
    @BindView(R.id.idPlaceKaraoke) TextView idPlaceKaraoke;
    @BindView(R.id.idPlaceMuseum) TextView idPlaceMuseum;
    @BindView(R.id.idPlaceGallery) TextView idPlaceGallery;
    @BindView(R.id.idPlaceArtCafe) TextView idPlaceArtCafe;
    @BindView(R.id.idPlaceQuest) TextView idPlaceQuest;
    @BindView(R.id.idPlaceCircus) TextView idPlaceCircus;
    @BindView(R.id.idPlaceRestaurant) TextView idPlaceRestaurant;
    @BindView(R.id.idPlacePub) TextView idPlacePub;
    @BindView(R.id.idPlaceCafe) TextView idPlaceCafe;
    @BindView(R.id.idPlaceSushi) TextView idPlaceSushi;
    @BindView(R.id.idPlaceCoffe) TextView idPlaceCoffe;
    @BindView(R.id.idPlacePizza) TextView idPlacePizza;
    @BindView(R.id.idPlaceIce) TextView idPlaceIce;
    @BindView(R.id.idPlaceSauna) TextView idPlaceSauna;
    @BindView(R.id.idPlaceBani) TextView idPlaceBani;

    private OnFragmentInteractionListener mListener;

    public PlacesSreenFragment() {
        // Required empty public constructor
    }




    public static PlacesSreenFragment newInstance(Context context) {
        PlacesSreenFragment fragment = new PlacesSreenFragment();
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
        View rootView = inflater.inflate(R.layout.places_screen_layout, container, false);

        ButterKnife.bind(this, rootView);

        return rootView;

    }


    @OnClick({R.id.idPlaceCinema, R.id.idPlaceTheatre, R.id.idPlaceHall,
            R.id.idPlaceClub,R.id.idPlaceKaraoke,R.id.idPlaceMuseum,
            R.id.idPlaceGallery,R.id.idPlaceArtCafe,R.id.idPlaceQuest,
            R.id.idPlaceCircus,R.id.idPlaceRestaurant,R.id.idPlacePub,
            R.id.idPlaceCafe,R.id.idPlaceSushi,R.id.idPlaceCoffe,
            R.id.idPlacePizza,R.id.idPlaceIce,R.id.idPlaceSauna,R.id.idPlaceBani})
    public void cinemaClick(View view) {

        //Так как открываю из меню, то всегда будет поторная загрузка и
        //начальная позиция списка равна 0.
        String reload = "yes";
        int position = 0;
        String type = "cinema"; //по дефолту

        switch (view.getId()){

            case R.id.idPlaceCinema:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_CINEMA);
                break;

            case R.id.idPlaceTheatre:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_THATRE);
                break;

            case R.id.idPlaceHall:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_HALL);
                break;

            case R.id.idPlaceClub:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_CLUB);
                break;

            case R.id.idPlaceKaraoke:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_KARAOKE);
                break;

            case R.id.idPlaceMuseum:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_MUSEUM);
                break;

            case R.id.idPlaceGallery:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_GALLERY);
                break;

            case R.id.idPlaceArtCafe:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_ARTCAFE);
                break;

            case R.id.idPlaceQuest:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_QUEST);
                break;

            case R.id.idPlaceCircus:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_CIRCUS);
                break;

            case R.id.idPlaceRestaurant:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_RESTAURANT);
                break;

            case R.id.idPlacePub:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_PUB);
                break;

            case R.id.idPlaceCafe:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_CAFE);
                break;

            case R.id.idPlaceSushi:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_SUSHI);
                break;

            case R.id.idPlaceCoffe:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_COFFEE);
                break;

            case R.id.idPlacePizza:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_PIZZA);
                break;

            case R.id.idPlaceIce:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_ICE);
                break;

            case R.id.idPlaceBani:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_BANI);
                break;

            case R.id.idPlaceSauna:
                type = SharedMethods.getPlaceNameByCode(SharedMethods.MENU_PLACE_SAUNA);
                break;
        }

        SharedMethods.openedPlace = type;
        FragmentManager fragmentManager =
                ((FragmentActivity)SharedMethods.mContext).getSupportFragmentManager();
        Fragment fr = PlacesListFragment.newInstance(reload, position, type);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fr)
                .commit();
    }









    public void updateData(View rootView) {

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
        SharedMethods.screenName = "places";
        super.onStart();
    }

    @Override
    public void onResume() {
        SharedMethods.screenName = "places";
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
