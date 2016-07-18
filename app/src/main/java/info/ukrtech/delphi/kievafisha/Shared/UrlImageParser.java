package info.ukrtech.delphi.kievafisha.Shared;

/**
 * Created by Delphi on 10.03.2016.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UrlImageParser implements ImageGetter {
    Context c;
    View container;

    /***
     * Construct the URLImageParser which will execute AsyncTask and refresh the container
     * @param t
     * @param c
     */
    public UrlImageParser(View t, Context c) {
        this.c = c;
        this.container = t;
    }

    public Drawable getDrawable(String source) {
        UrlDrawable urlDrawable = new UrlDrawable();

        // get the actual source
        ImageGetterAsyncTask asyncTask =
                new ImageGetterAsyncTask( urlDrawable);

        asyncTask.execute(source);

        // return reference to URLDrawable where I will change with actual image from
        // the src tag
        return urlDrawable;
    }

    public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable>  {
        UrlDrawable urlDrawable;

        public ImageGetterAsyncTask(UrlDrawable d) {
            this.urlDrawable = d;
        }

        @Override
        protected Drawable doInBackground(String... params) {
            String source = params[0];
            return fetchDrawable(source);
        }

        @Override
        protected void onPostExecute(Drawable result) {
            // set the correct bound according to the result from HTTP call
            /*urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0
                    + result.getIntrinsicHeight());

            // change the reference of the current drawable to the result
            // from the HTTP call
            urlDrawable.drawable = result;

            // redraw the image by invalidating the container
            UrlImageParser.this.container.invalidate();*/

            if (result != null) {
                int width = result.getIntrinsicWidth();
                int height = result.getIntrinsicHeight();
                int newWidth = width;
                int newHeight = height;

                if (width > container.getWidth()) {
                    newWidth = container.getWidth();
                    newHeight = (newWidth * height) / width;
                }

                UrlImageParser.this.container.getLayoutParams().width = container.getWidth() + 1;
                UrlImageParser.this.container.getLayoutParams().height = container.getHeight() + newHeight+10;

                result.setBounds(0, 0, newWidth, newHeight);
                urlDrawable.setBounds(0, 0, newWidth, newHeight);
                urlDrawable.drawable = result;
                UrlImageParser.this.container.requestLayout();
                UrlImageParser.this.container.invalidate();
            }
        }

        /***
         * Get the Drawable from URL
         * @param urlString
         * @return
         */
        public Drawable fetchDrawable(String urlString) {
            try {
                InputStream is = fetch(urlString);
                Drawable drawable = Drawable.createFromStream(is, "src");
                drawable.setBounds(0, 0, 0 + drawable.getIntrinsicWidth(), 0
                        + drawable.getIntrinsicHeight());
                return drawable;
            } catch (Exception e) {
                return null;
            }
        }

        private InputStream fetch(String urlString) throws MalformedURLException, IOException {
            OkHttpClient  httpClient = new OkHttpClient();
            Request request = new Request.Builder().url(urlString)
                    .build();
            Response response = httpClient.newCall(request).execute();
            return response.body().byteStream();
        }
    }

    @SuppressWarnings("deprecation")
    public class UrlDrawable extends BitmapDrawable {
        // the drawable that you need to set, you could set the initial drawing
        // with the loading image if you need to
        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {
            // override the draw to facilitate refresh function later
            if(drawable != null) {
                drawable.draw(canvas);
            }
        }
    }
}
