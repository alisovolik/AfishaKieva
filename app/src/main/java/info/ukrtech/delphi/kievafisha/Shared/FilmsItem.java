package info.ukrtech.delphi.kievafisha.Shared;

import android.database.Cursor;

import java.util.ArrayList;

/**
 * Created by Delphi on 12.05.2015.
 */
public class FilmsItem implements Comparable<FilmsItem>{
    public int id;
    public String title;
    public String text;
    public String photo;
    public String trailer;
    public String full_text;
    public ArrayList<SheduleFilmItem> shedule;

    FilmsItem(int id, String title, String text, String photo, String trailer,
              String full_text, ArrayList<SheduleFilmItem> shedule) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.photo = photo;
        this.trailer = trailer;
        this.full_text = full_text;
        this.shedule = new ArrayList<>();
        this.shedule.addAll(shedule);

    }

    public FilmsItem() {
        this.id = -1;
        this.title = "";
        this.text = "";
        this.photo = "";
        this.trailer = "";
        this.full_text = "";
        this.shedule = new ArrayList<>();
    }


    public int compareTo(FilmsItem oneItem) {
        return compare(this.id, oneItem.id);
    }



    public static int compare(int x, int y) {
        return x < y ? -1
                : x > y ? 1
                : 0;
    }

   /* @Override
    public String toString() {
        return "title = " + this.title + "\n";
    }*/
}
