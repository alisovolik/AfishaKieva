package info.ukrtech.delphi.kievafisha.Screen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerSupportFragment;

import info.ukrtech.delphi.kievafisha.R;
import info.ukrtech.delphi.kievafisha.Shared.SharedMethods;


public class TestFragment extends Fragment {

    private YouTubePlayer YPlayer;
    private static int item = 0;


    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance(int _item) {

        TestFragment fragment = new TestFragment();
        TestFragment.item = _item;
        return fragment;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.films_detail_item, container, false);

        YouTubePlayerSupportFragment youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance();
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.youtube_fragment, youTubePlayerFragment).commit();

        youTubePlayerFragment.initialize(getResources().getString(R.string.debug_key_google), new YouTubePlayer.OnInitializedListener() {

            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider arg0, YouTubePlayer youTubePlayer, boolean b) {
                if (!b) {
                    YPlayer = youTubePlayer;
                    YPlayer.setFullscreen(false);
                    YPlayer.loadVideo("nlNU3qMrmlQ");
                    YPlayer.play();
                }
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider arg0, YouTubeInitializationResult arg1) {
                SharedMethods.logMessage("onInitializationFailure " + arg1.toString());

            }
        });

        return rootView;
    }




    @Override
    public void onDetach() {
        super.onDetach();

    }


}
