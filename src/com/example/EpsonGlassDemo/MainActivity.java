package com.example.EpsonGlassDemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import model.Item;
import network.Request;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import sql.DbConstants;
import sql.ItemDbLoader;
import zxing.IntentIntegrator;
import zxing.IntentResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Balázs on 2014.09.16..
  Helloo
  Szia OctoCat

 */
public class MainActivity extends Activity {

    private ListView itemsList;
    private OptionsListAdapter adapter;
    private Cursor c;
    private ItemDbLoader dbLoader;
    private final String barcodes[] = { "9781855682979",
                                        "129002702047",
                                        "724352959020",
                                        "188334001402",
                                        "7235953525253"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collecting_items);
        Button scanBtn = (Button) findViewById(R.id.scanButton);

        if (dbLoader == null){
            dbLoader = new ItemDbLoader(getApplicationContext());
        }

        initList(true);

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.initiateScan();
            }
        });

        /* get items' list from server
        Thread t = new Thread(new Runnable(){
            @Override
            public void run() {
                getItemsList();
            }
        });
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        final String scanContent = scanningResult.getContents();
        String scanFormat = scanningResult.getFormatName();
        //Toast.makeText(getApplicationContext(), scanContent, Toast.LENGTH_LONG).show();
        if (scanningResult != null) {
            for(int i=0; i<barcodes.length; i++){
                if (barcodes[i].equals(scanContent)){
                    /*
                    Here we should ask if the user wants to put the item into his cart
                     */

                    /*POST
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            pickItemOut(scanContent);
                        }
                    });
                    */

                    //Put item out from client's List
                    dbLoader.open();
                    if (dbLoader.itemExists(scanContent)) {
                        dbLoader.delete(dbLoader.getArticleByCursor(dbLoader.findItemByBarcode(scanContent)));
                        dbLoader.close();
                        initList(false);
                        Toast.makeText(getApplicationContext(), scanContent + " azonosítva, kikönyvelve.", Toast.LENGTH_SHORT).show();
                    } else {
                        dbLoader.close();
                        Toast.makeText(getApplicationContext(), "Nem szükséges", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        } else {
            Toast toast = Toast.makeText(getApplicationContext(), "No scan", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void initList(boolean isFirst){
        itemsList = (ListView) findViewById(R.id.items_list);

        if (isFirst) {
            dbLoader.open();
            dbLoader.clear();
            dbLoader.close();

            dbLoader.insert(new Item(this.barcodes[0], "description1"));
            dbLoader.insert(new Item(this.barcodes[1], "description2"));
            dbLoader.insert(new Item(this.barcodes[2], "description3"));
            dbLoader.insert(new Item(this.barcodes[3], "description4"));
            dbLoader.insert(new Item(this.barcodes[4], "description5"));
        }

        dbLoader.open();
        int maxId = dbLoader.getMaxId();
        c = dbLoader.fetchItems();

        adapter = new OptionsListAdapter(getApplicationContext(), c, true);
        Log.d("Main/initList()", "before itemsList.setAdapter(adapter)");
        itemsList.setAdapter(adapter);
        itemsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();
            }
        });


        Log.d("initList()", "eddig eljutunk? @endOfInitList()");
        dbLoader.close();
    }

    public void getItemsList(){
        Request req = new Request();
        req.send("http://someurl.toget.com/", getApplicationContext());
    }

    public void pickItemOut(String item){
        Request req = new Request();
        final List<NameValuePair> data = new ArrayList<NameValuePair>();
        BasicNameValuePair pair = new BasicNameValuePair("data", item);
        data.add(pair);
        req.send("http://someurl.topost.com/", Request.METHOD_POST, data, getApplicationContext());
    }

    private class OptionsListAdapter extends CursorAdapter{

        public OptionsListAdapter(Context context, Cursor c, boolean autoRequery) {
            super(context, c, true);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View v = LayoutInflater.from(context).inflate(R.layout.row_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.id= (TextView) v.findViewById(R.id.item_id);
            holder.description = (TextView) v.findViewById(R.id.item_description);
            v.setTag(holder);
            return v;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final ViewHolder holder = (ViewHolder) view.getTag();
            holder.id.setText(c.getString(c.getColumnIndex(DbConstants.Items.KEY_BARCODE)));
            holder.description.setText(c.getString(c.getColumnIndex(DbConstants.Items.KEY_DESCRIPTION)));
        }
    }

    public static class ViewHolder {
        public TextView id;
        public TextView description;
    }
}