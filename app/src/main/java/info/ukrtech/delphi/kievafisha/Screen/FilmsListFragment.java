package info.ukrtech.delphi.kievafisha.Screen;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import info.ukrtech.delphi.kievafisha.Adapters.FilmsListAdapter;
import info.ukrtech.delphi.kievafisha.MainActivity;
import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.DB;
import info.ukrtech.delphi.kievafisha.Shared.FilmsDownloadService;
import info.ukrtech.delphi.kievafisha.Shared.RecyclerViewClickListener;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;


public class FilmsListFragment extends Fragment implements RecyclerViewClickListener {

    private OnFragmentInteractionListener mListener;
    public FilmsListAdapter mAdapter;
    private BroadcastReceiver br;
    private static final String ARG_RELOAD = "reload";
    private static final String ARG_POSITION = "position";
    private static final String ARG_TYPE = "type";
    private String mReload = "yes";
    private int dataType = 0; //По умолчанию загружаю фильмы

    private String selectedDate = "Расписание на сегодня";
    private int mYear, mMonth, mDay;

    public FilmsListFragment() {
        // Required empty public constructor
    }


    public static FilmsListFragment newInstance(String mReload, int position, int type) {
        FilmsListFragment fragment = new FilmsListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RELOAD, mReload);
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_TYPE, type);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        MainActivity.actionBar.show();


        Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = df.parse(mYear + "-" + (mMonth + 1) + "-" + mDay);
        } catch (ParseException e) {
            SharedMethods.logMessage("FilmListFragment #2 " + e.getMessage());
        }


        selectedDate = df.format(date);

        if (getArguments() != null) {
            mReload = getArguments().getString(ARG_RELOAD);
            SharedMethods.rvFilmsListPosition = getArguments().getInt(ARG_POSITION);
            dataType = getArguments().getInt("type", 0);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_films_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.idListFilms);
        recyclerView.setLayoutManager(new LinearLayoutManager(SharedMethods.mContext));
        mAdapter = new FilmsListAdapter(SharedMethods.allFilms, this, SharedMethods.mContext);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        if (mReload.equals("yes")) {

            loadFilmListFromInet(SharedMethods.mContext, 0, selectedDate, dataType);

        }

        recyclerView.scrollToPosition(SharedMethods.rvFilmsListPosition);

        return view;
    }



    private void changeDate(@Nullable final TextView tvName){
        DatePickerDialog dpd = new DatePickerDialog(SharedMethods.mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                        Date date = new Date();
                        try {
                            date = df.parse(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                        } catch (ParseException e) {
                            SharedMethods.logMessage("FilmListFragment #1 " + e.getMessage());
                        }

                        selectedDate = df.format(date);
                        //SharedMethods.logMessage("Дата = " + selectedDate);

                        //df = new SimpleDateFormat("dd MMMM yyyy");
                        //if (tvName != null)tvName.setText("Расписание на " + df.format(date));

                        loadFilmListFromInet(SharedMethods.mContext, 0, selectedDate, dataType);

                    }
                }, mYear, mMonth, mDay);

        dpd.show();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.list_change_date_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.mnu_btn_change_date:
            {
                changeDate(null);

                return true;
            }


            default:
                return super.onOptionsItemSelected(menuItem);
        }
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
            SharedMethods.mContext.unregisterReceiver(br);
        }catch (Exception e){
            //
        }

    }


    @Override
    public void recyclerViewListClicked(View v, int position) {
        SharedMethods.rvFilmsListPosition = position;
        FragmentManager fragmentManager = ((FragmentActivity)SharedMethods.mContext).getSupportFragmentManager();
        //Fragment fr = FullFilmsFragment.newInstance(position);
        Fragment fr = FullFilmsPagerFragment.newInstance(position, dataType);
        //Fragment fr = TestFragment.newInstance(position);
        fragmentManager.beginTransaction()
                .replace(R.id.container, fr)
                .commit();
    }


    private void loadFilmListFromInet(final Context cntxt, int from, final String selectedDate,
                                      final int type) {
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
                    SharedMethods.loadFilmsListFromDB(SharedMethods.mContext, selectedDate, type);
                    //Загрузка окончена - спрятать прогресс
                    SharedMethods.hideProgressDialog();

                    mAdapter.notifyDataSetChanged();
                }
            }
        };
        // создаем фильтр для BroadcastReceiver
        IntentFilter intFilt = new IntentFilter(SharedMethods.ACTION_DOWNLOAD_FILMS_LIST);
        // регистрируем (включаем) BroadcastReceiver
        cntxt.registerReceiver(br, intFilt);
        //Запускаю сервис
        Intent intent;
        intent = new Intent(cntxt, FilmsDownloadService.class);
        intent.putExtra("from", from);
        intent.putExtra("dat_pokaz", selectedDate);
        intent.putExtra("tab_name", SharedMethods.getTabNameByCode(type));
        // стартуем сервис
        cntxt.startService(intent);
    }



    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }


    @Override
    public void onStart() {
        SharedMethods.screenName = "films_list";
        super.onStart();
    }

    @Override
    public void onResume() {
        SharedMethods.screenName = "films_list";
        super.onResume();
    }


}
