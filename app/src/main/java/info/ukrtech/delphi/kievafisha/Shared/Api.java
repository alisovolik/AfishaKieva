package info.ukrtech.delphi.kievafisha.Shared;

/**
 * Created by Delphi on 15.01.2016.
 */

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface Api {
    @GET(SharedMethods.url)
    Call<List<FilmsItem>> filmsList(@Query("dat_pokaz") String dat_pokaz,
                                    @Query("tab_name") String tab_name);

    @GET(SharedMethods.place_url)
    Call<List<PlaceItem>> placesList(@Query("alias") String placeAlias);
}
