package info.ukrtech.delphi.kievafisha.Shared;

/**
 * Created by Delphi on 12.05.2015.
 */
public class SheduleFilmItem implements Comparable<SheduleFilmItem>{
    public int id;
    public int film_id;
    public String kogda;
    public String name;
    public String price;


    SheduleFilmItem(int id,int film_id, String kogda, String name, String price) {
        this.id = id;
        this.film_id = film_id;
        this.kogda = kogda;
        this.name = name;
        this.price = price;
    }

    public SheduleFilmItem() {
        this.id = -1;
        this.film_id = -1;
        this.kogda = "";
        this.name = "";
        this.price = "";
    }


    public int compareTo(SheduleFilmItem oneItem) {
        return compare(this.kogda, oneItem.kogda);
    }


    public static int compare(String x, String y) {
        if (x != null && y != null) {
            return x.compareToIgnoreCase(y);
        }
        else return 0;
    }

   /* @Override
    public String toString() {
        return "title = " + this.title + "\n";
    }*/
}
