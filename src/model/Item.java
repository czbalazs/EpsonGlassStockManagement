package model;

/**
 * Created by Balázs on 2014.09.16..
 */
public class Item extends SyncModel {

    public static String barcode;
    public static String description;

    public Item(String id, String description){
        this.barcode = id;
        this.description = description;
    }

}
