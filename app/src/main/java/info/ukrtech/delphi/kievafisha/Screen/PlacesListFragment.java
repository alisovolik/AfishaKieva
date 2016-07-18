package info.ukrtech.delphi.kievafisha.Screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import info.ukrtech.delphi.kievafisha.Adapters.PlacesListAdapter;
import info.ukrtech.delphi.kievafisha.MainActivity;
import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.FilmsDownloadService;
import info.ukrtech.delphi.kievafisha.Shared.PlacesDownloadService;
import info.ukrtech.delphi.kievafisha.Shared.RecyclerViewClickListener;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;


public class PlacesListFragment extends Fragment implements RecyclerViewClickListener {

    private OnFragmentInteractionListener mListener;
    public PlacesListAdapter mAdapter;
    private BroadcastReceiver br;
    private static final String ARG_RELOAD = "reload";
    private static final String ARG_POSITION = "position";
    private static final String ARG_TYPE = "type";
    private String mReload = "yes";
    private static String placeAlias = "cinema"; //По умолчанию кинотеатры

    public PlacesListFragment() {
        // Required empty public constructor
    }


    public static PlacesListFragment newInstance(String mReload, int position, String type) {
        PlacesListFragment fragment = new PlacesListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RELOAD, mReload);
        args.putInt(ARG_POSITION, position);
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);

        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.actionBar.show();
        if (getArguments() != null) {
            mReload = getArguments().getString(ARG_RELOAD);
            placeAlias = getArguments().getString(ARG_TYPE, "cinema");
            SharedMethods.rvPlaceListPosition = getArguments().getInt(ARG_POSITION);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_films_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.idListFilms);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PlacesListAdapter(SharedMethods.allPlaces, this, getActivity());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (mReload.equals("yes")) {

            loadPlaceListFromInet();

        }

        recyclerView.scrollToPosition(SharedMethods.rvPlaceListPosition);

        return view;
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
    public void onDestroy() {
        super.onDetach();
        mListener = null;
        try {
            getActivity().unregisterReceiver(br);
        }catch (Exception e){
            //
        }

    }


    @Override
    public void recyclerViewListClicked(View v, int position) {
        SharedMethods.rvPlaceListPosition = position;
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Fragment fr = new DetailPlaceFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fr.setArguments(args);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fr)
                .commit();
    }


    private void loadPlaceListFromInet() {
        br = new BroadcastReceiver() {
            // действия при получении сообщений
            public void onReceive(Context context, Intent intent) {
                int status = intent.getIntExtra("status", -1);
                if (status == 5) {
                    //Началась загрузка - отобразить прогресс
                    //Показываю прогресс
                    SharedMethods.showProgressDialog();
                }
                if (status == 10) {
                    SharedMethods.loadPlacesListFromDB(getActivity(), placeAlias);
                    //Загрузка окончена - спрятать прогресс
                    SharedMethods.hideProgressDialog();

                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(SharedMethods.ACTION_DOWNLOAD_PLACE_LIST);
        // регистрируем (включаем) BroadcastReceiver
        getActivity().registerReceiver(br, intFilt);
        //Запускаю сервис
        Intent intent;
        intent = new Intent(getActivity(), PlacesDownloadService.class);
        intent.putExtra("placeAlias", placeAlias);
        // стартуем сервис
        getActivity().startService(intent);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onStart() {
        SharedMethods.screenName = "place_list";
        super.onStart();
    }

    @Override
    public void onResume() {
        SharedMethods.screenName = "place_list";
        super.onResume();
    }
}
