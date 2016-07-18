package info.ukrtech.delphi.kievafisha.Shared;

import java.util.ArrayList;

/**
 * Created by Delphi on 12.05.2015.
 */
public class PlaceItem implements Comparable<PlaceItem>{
    public int id;
    public String name;
    public String address;
    public String description;
    public String phone;
    public String photo;
    public String alias;


    PlaceItem(int id, String name, String address, String phone,
              String photo, String description, String alias) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.photo = photo;
        this.description =description;
        this.alias = alias;


    }

    public PlaceItem() {
        this.id = -1;
        this.name = "";
        this.address = "Киев";
        this.phone = "";
        this.photo = "";
        this.alias = "";
        this.description = "";
    }


    public int compareTo(PlaceItem oneItem) {
        return compare(this.id, oneItem.id);
    }


    public static int compare(int x, int y) {
        return x < y ? -1
                : x > y ? 1
                : 0;
    }

}
