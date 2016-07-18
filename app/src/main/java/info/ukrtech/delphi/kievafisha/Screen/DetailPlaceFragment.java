package info.ukrtech.delphi.kievafisha.Screen;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import java.util.List;
import java.util.Locale;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import info.ukrtech.delphi.kievafisha.MainActivity;
import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.ResizableImageView;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DetailPlaceFragment extends Fragment{
    private int item = 0;
    @BindView(R.id.idPlaceName)
    TextView placeName;

    @BindView(R.id.idPlaceAddress)
    TextView placeAddress;

    @BindView(R.id.idPlaceLogo)
    ResizableImageView placeLogo;

    @BindView(R.id.idShowInMap)
    LinearLayout showInMap;

    @BindView(R.id.idMapLayout)
    LinearLayout mapLayout;

    @BindView(R.id.idPlaceDetailParent)
    ScrollView mapScrollView;

    private GoogleMap mMap;
    private static View view;

    private String name = "";

    private double latitude = 50.4501;
    private double longitude = 30.5234;
    private String address = "";
    private List<Address> result;
    private Handler h;


    public DetailPlaceFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivity.actionBar.show();
        if (getArguments() != null) {
            item = getArguments().getInt("position");
        } else
        {
            item = 0;
        }
    }


    public void setViewText(TextView v, String text) {
        String ss = Html.fromHtml(text).toString();
        Spanned s = Html.fromHtml(ss);
        v.setText(s);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
                parent.removeView(view);
        }
        try {
            view = inflater.inflate(R.layout.place_detail_layout, container, false);
        } catch (InflateException e) {
            SharedMethods.logMessage("inlfalte Place " + e.getMessage());
        }

        ButterKnife.bind(this, view);
        installPlayServices(); //Чтобы были установлены PlayServices

        name = SharedMethods.allPlaces.get(item).description;
        placeName.setText(name);

        address = SharedMethods.allPlaces.get(item).address;
        if (address == null) address = "";
        placeAddress.setText(address);

        mapLayout.setVisibility(View.GONE);
        if (mMap != null) {
            mMap.clear();
            mapLayout.setVisibility(View.GONE);
        }


        //Картинка
        String pic = SharedMethods.allPlaces.get(item).photo;
        if (pic != null && !pic.isEmpty()) {
            Picasso.with(getActivity())
                    .load(pic)
                    .placeholder(R.drawable.ic_no_image)
                    .into(placeLogo);
        }


        return view;
    }


    @OnClick(R.id.idShowInMap)
    public void showMapClick(View view) {
        if (mapLayout.getVisibility() == View.VISIBLE){
            mapLayout.setVisibility(View.GONE);
            if (mMap != null) {
                mMap.clear();
            }
        }
        else {
            mapLayout.setVisibility(View.VISIBLE);
            setUpMapIfNeeded();


        }
    }


    public void setUpMapIfNeeded() {

        if (mMap == null) {

            ((MapFragment) getActivity().getFragmentManager()
                    .findFragmentById(R.id.idPlaceMap)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;

                    if (mMap != null) setUpMap();
                }
            });
        }
        else
        {
            setUpMap();
        }


    }



    private void installPlayServices() {
        boolean services = false;

        try
        {
            ApplicationInfo info = getActivity().getPackageManager().getApplicationInfo("com.google.android.gms", 0);
            services = true;
        }
        catch(PackageManager.NameNotFoundException e)
        {
            services = false;
        }

        if (services)
        {
            return;
        }
        else
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

            alertDialogBuilder
                    .setTitle("Google Play Services")
                    .setMessage("Для отображения карт должны быть установлены Google Play Services.")
                    .setCancelable(true)
                    .setPositiveButton("Установить", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.dismiss();
                            // Try the new HTTP method (I assume that is the official way now given that google uses it).
                            try
                            {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms"));
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                intent.setPackage("com.android.vending");
                                startActivity(intent);
                            }
                            catch (ActivityNotFoundException e)
                            {

                                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms"));
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                                    startActivity(intent);
                                //}
                            }
                        }
                    })
                    .setNegativeButton("Нет",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    })
                    .create()
                    .show();
        }
    }


    @Override
    public void onStart() {
        SharedMethods.screenName = "place_detail";
        super.onStart();
    }

    @Override
    public void onResume() {
        SharedMethods.screenName = "place_detail";

        super.onResume();
    }


    public void getLatLongFromAddress() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String uri = "http://maps.google.com/maps/api/geocode/json?address="
                        + address + "&sensor=false";
                try {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                            .url(uri)
                            .build();

                Response response = client.newCall(request).execute();
                JSONObject jsonObject = new JSONObject(response.body().string());

                double lng = ((JSONArray) jsonObject.get("results"))
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").getDouble("lng");

                double lat = ((JSONArray) jsonObject.get("results"))
                        .getJSONObject(0).getJSONObject("geometry")
                        .getJSONObject("location").getDouble("lat");

                LatLng position = new LatLng(lat, lng);

                Message msg = h.obtainMessage(0,0,0,position);
                h.sendMessage(msg);

                } catch (IOException e) {
                    SharedMethods.logMessage("getLatLongFromAddress " + e.getMessage());
                }
                catch (JSONException e1) {
                    SharedMethods.logMessage("getLatLongFromAddress " + e1.getMessage());
                }
            }
        }).start();
    }




    private void setUpMap() {
        if (mMap != null) {
            mapScrollView.scrollTo(0, (int) mapLayout.getY());
            if (ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getActivity(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {


                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    //Не все адреса содержат город.
                    //Иногда выдает неверно.
                    if (!address.contains("Киев") && !address.contains("Київ")) {
                        address = "Киев, " + address;
                        //SharedMethods.logMessage("Новый адрес = " + address);
                    }
                    result = geocoder.getFromLocationName(address, 1);

                    if (result.size() > 0) {
                        latitude = result.get(0).getLatitude();
                        longitude = result.get(0).getLongitude();
                    } else //Адрес не найден либо вернулось пустое значение
                    {
                        getLatLongFromAddress();
                        h = new Handler() {
                            public void handleMessage(android.os.Message msg) {
                                //Пытаюсь загрузить адрес через google api
                                latitude = ((LatLng)msg.obj).latitude;
                                longitude = ((LatLng)msg.obj).longitude;

                                mMap.clear();
                                mMap.addMarker(new MarkerOptions()

                                        .position(new LatLng(latitude, longitude))
                                        .title(name)
                                        .snippet(placeAddress.getText().toString()));

                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                                        longitude), 12.0f));
                            };
                        };

                    }

                } catch (IOException e) {
                    SharedMethods.logMessage("geocoder " + e.getMessage());
                }
                mMap.setMyLocationEnabled(true);
                mMap.clear();

                mMap.addMarker(new MarkerOptions()

                        .position(new LatLng(latitude, longitude))
                        .title(name)
                        .snippet(placeAddress.getText().toString()));

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,
                        longitude), 12.0f));
            }
        }
    }


    @Override
    public void onDestroy(){
        if (mapLayout.getChildCount() > 0 && mapLayout.getChildAt(0) instanceof MapView)
            mapLayout.removeViewAt(0);

        super.onDestroy();
    }


}
