package network;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Config;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Balázs on 2014.09.16..
 */

public class Request {

    public static final int METHOD_GET = 1;
    public static final int METHOD_POST = 2;
    public static final int METHOD_PUT = 3;

    public static String send(String url, Context ctx){
        return Request.send(url, Request.METHOD_GET, null, ctx);
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public static String send(String url, int method, List<NameValuePair> list, Context ctx ){

        AndroidHttpClient client = AndroidHttpClient.newInstance("Android", ctx);
        HttpResponse response = null;
        String result = "";
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(ctx);
        Log.d("send", "eleje �������e���");
        try{
            switch (method){
                case Request.METHOD_GET:
                    HttpGet httpGet = new HttpGet(url);
                    httpGet.addHeader("X-Auth-Session", sp.getString("user_session_token", "charset=utf-8"));
                    response = client.execute(httpGet);
                    Log.d("METHOD_GET", "benne vagyunk");
                    break;
                case Request.METHOD_POST:
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setEntity(new UrlEncodedFormEntity((List<? extends NameValuePair>) list));
                    httpPost.addHeader("X-Auth-Session", sp.getString("user_session_token", ""));
                    response = client.execute(httpPost);
                    Log.d("METHOD_POST", "benne vagyunk");
                    break;
                case Request.METHOD_PUT:
                    HttpPut httpPut = new HttpPut(url);
                    httpPut.setEntity(new UrlEncodedFormEntity((List<? extends NameValuePair>) list));
                    httpPut.addHeader("X-Auth-Session", sp.getString("user_session_token", ""));
                    response = client.execute(httpPut);
                    break;
                default:
                    return null;
            }

            if (!Config.DEBUG && response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
                return null;
            }

            InputStream is;
            HttpEntity entity = response.getEntity();
            if (entity != null){
                Log.d("request", "entity!=null");
                is = entity.getContent();
                StringBuilder sb = new StringBuilder();
                int chIn;
                while ((chIn = is.read()) != -1){
                    sb.append((char) chIn);
                }
                result = sb.toString();
                Log.d("valasz2", result);
            }
            client.close();

            if(!Config.DEBUG && response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
                return null;

            if (result == null)
                result = "OK";
            return result;

        } catch (Exception e){
            client.close();
            e.printStackTrace();
            return null;
        }
    }
}
