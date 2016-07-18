package info.ukrtech.delphi.kievafisha.Screen;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.FilmsItem;
import info.ukrtech.delphi.kievafisha.Shared.PatchedTextView;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;
import info.ukrtech.delphi.kievafisha.Shared.UrlImageParser;


public class FullFilmsFragment extends Fragment{
    private static int item = 0;
    private int dataType = 0;
    private String[] cinemaList;
    private String selectedCinema = "";
    private LayoutInflater mLayoutInflater;
    private View mView;

    private Handler handler;
    private YouTubePlayer YPlayer;

    @BindView(R.id.idShowTrailer) TextView idShowTrailer;
    @BindView(R.id.idLayoutTrailer) LinearLayout idLayoutTrailer;
    @BindView(R.id.youtube_fragment)
    FrameLayout youtubeLayout;


    public static FullFilmsFragment newInstance(int _item) {

        FullFilmsFragment fragment = new FullFilmsFragment();
        FullFilmsFragment.item = _item;
        return fragment;
    }

    public FullFilmsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public void setViewText(TextView v, String text) {
        String ss = Html.fromHtml(text).toString();
        Spanned s = Html.fromHtml(ss);
        v.setText(s);
    }


    @OnClick(R.id.idLayoutTrailer)
    public void idLayoutTrailer(View view) {

        if (youtubeLayout.getVisibility() == View.VISIBLE){
            youtubeLayout.setVisibility(View.GONE);
            if (YPlayer != null) {
                YPlayer.pause();
            }

        }
        else {
            if (SharedMethods.allFilms.get(item).trailer != null &&
                    !SharedMethods.allFilms.get(item).trailer.equals("")) {
                youtubeLayout.setVisibility(View.VISIBLE);
                YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
                FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                transaction.replace(R.id.youtube_fragment, youTubePlayerFragment).commit();

                youTubePlayerFragment.initialize(getResources().getString(R.string.debug_key_google), new YouTubePlayer.OnInitializedListener() {

                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                        if (!b) {
                            YPlayer = youTubePlayer;
                            YPlayer.setFullscreen(false);
                            Uri uri = Uri.parse(SharedMethods.allFilms.get(item).trailer);
                            //SharedMethods.logMessage("ЮТУБ = " + SharedMethods.allFilms.get(item).trailer);
                            //SharedMethods.logMessage("ЮТУБ = " + uri.getLastPathSegment());
                            YPlayer.loadVideo(uri.getLastPathSegment());
                            YPlayer.play();
                        }
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                        SharedMethods.logMessage("onInitializationFailure " + arg1.toString());

                    }
                });
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.films_detail_item, container, false);
        mLayoutInflater = inflater;
        mView = view;

        ButterKnife.bind(this, view);

        Bundle args = getArguments();

        if (args != null) {
            item = args.getInt("position", 0);
            dataType = args.getInt("dataType", 0); //По умолчанию - фильмы
        }

        //Сразу проверяю, чтобы трейлер был
        if (SharedMethods.allFilms.get(item).trailer == null || dataType != 0) {
            idLayoutTrailer.setVisibility(View.GONE);
        }

        //Список кинотеатров для фильтра
        final List<String> allCinema = new ArrayList<>();
        for (int k = 0; k < SharedMethods.allFilms.get(item).shedule.size(); k++) {
            if (SharedMethods.allFilms.get(item).shedule.get(k).name != null) {
                allCinema.add(SharedMethods.allFilms.get(item).shedule.get(k).name);
            }
        }
        final Set<String> uniqueCinema = new HashSet<String>(allCinema);
        allCinema.clear();
        allCinema.addAll(uniqueCinema);
        allCinema.add("---- Все кинотеатры ----");
        cinemaList  = new String[allCinema.size()];
        cinemaList = allCinema.toArray(cinemaList);
        try {
            Arrays.sort(cinemaList);
        } catch (Exception e) {
            SharedMethods.logMessage(cinemaList.toString());
        }


        //Получаю данные
        TextView tvTitle = (TextView)view.findViewById(R.id.idFullTitle);
        final PatchedTextView tvText = (PatchedTextView)view.findViewById(R.id.idFullText);
        ImageView imgPhoto = (ImageView)view.findViewById(R.id.idFullLogo);


        final FilmsItem film;

        film = SharedMethods.allFilms.get(item);

        if (film != null) {
            tvTitle.setText(film.title);

            String text = film.full_text;
            if (text == null) text = "";

            UrlImageParser urlImageParser = new UrlImageParser(tvText, getActivity());
            Spanned spannedValue = Html.fromHtml(text, urlImageParser,null);
            tvText.setText(spannedValue);

            //Расписание
            reloadShedule(inflater);

            //картинка
            final String pic = film.photo;
            if (pic != null && !pic.isEmpty()) {
                Picasso.with(getActivity())
                        .load(pic)
                        .placeholder(R.drawable.ic_no_image)
                        .into(imgPhoto);
            }
        }


        return view;
    }




    //Загрузка списка сеансов для выбранного фильма
    public void reloadShedule(final LayoutInflater inflater) {
        final TableLayout tableLayout = (TableLayout)mView.findViewById(R.id.idTableSheduleFilms);
        tableLayout.removeAllViews();
        Collections.sort(SharedMethods.allFilms.get(item).shedule);

        //new Thread(new Runnable() {
            String name;
            String time;
            String price;

           // public void run() {

                for(int i = 0 ; i < SharedMethods.allFilms.get(item).shedule.size() ; i++){

                    name = SharedMethods.allFilms.get(item).shedule.get(i).name;
                    if (name != null) {
                        if (selectedCinema.equals("") || selectedCinema.equals(name)) {

                            TableRow tableRow = (TableRow) inflater.inflate(R.layout.shedule_row_template, null);
                            //Название кинотеатра
                            TextView tvName = (TextView) tableRow.findViewById(R.id.idKinoteatrName);

                            tvName.setText(name);
                            tvName.setId(1000 + i);

                            //Время показа
                            TextView tvTime = (TextView) tableRow.findViewById(R.id.idFilmTime);
                            time = SharedMethods.allFilms.get(item).shedule.get(i).kogda;
                            tvTime.setText(time);
                            tvTime.setId(2000 + i);

                            //Стоимость билетов
                            TextView tvPrice = (TextView) tableRow.findViewById(R.id.idFilmPrice);
                            price = SharedMethods.allFilms.get(item).shedule.get(i).price;
                            tvPrice.setText(price);
                            tvPrice.setId(2000 + i);

                            tableLayout.addView(tableRow);
                        }
                    }
                }

        LinearLayout linearLayout = (LinearLayout) mView.findViewById(R.id.idFilterKinoteatr);
        final TextView tvFilterName = (TextView)mView.findViewById(R.id.idFiltrKinoteatrTitle);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCinema(tvFilterName);
            }
        });
    }


    public void selectCinema(final TextView tvName){
        AlertDialog cinemaChoice;
        ListAdapter itemlist = new ArrayAdapter(SharedMethods.mContext, R.layout.simple_list_item_1, cinemaList);
        final AlertDialog.Builder builder = new AlertDialog.Builder(SharedMethods.mContext);

        builder.setAdapter(itemlist, new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int item)
            {
                selectedCinema = cinemaList[item];
                tvName.setText(selectedCinema);
                if (item == 0) {
                    selectedCinema = "";
                }
                dialog.cancel();
                reloadShedule(mLayoutInflater);
            }
        });
        cinemaChoice = builder.create();
        cinemaChoice.getListView().setBackgroundColor(getResources().getColor(R.color.colorBackground));
        cinemaChoice.show();
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.change_date_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        FilmsItem oneItem = SharedMethods.allFilms.get(item);

        switch (menuItem.getItemId()) {

            case R.id.mnu_btn_change_date:
            {
                final TextView tvFilterName = (TextView)mView.findViewById(R.id.idFiltrKinoteatrTitle);
                selectCinema(tvFilterName);

                return true;
            }


            case R.id.mnu_btn_share:
            {
                //Поделиться
                SharedMethods.shareEvent(getActivity(),oneItem);
                return true;
            }

            default:
                return super.onOptionsItemSelected(menuItem);
        }
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
