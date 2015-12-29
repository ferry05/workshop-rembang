package org.meruvian.restsecurity.service;

import android.content.Context;
import android.net.ConnectivityManager;

import org.apache.http.HttpEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by merv on 6/6/15.
 */
public class ConnectionUtil {
    public static boolean isInternetAvaible(Context contect){
        ConnectivityManager cm = (ConnectivityManager) contect.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm.getActiveNetworkInfo() != null){
            return cm.getActiveNetworkInfo().isConnectedOrConnecting();
        }else{
            return false;
        }
    }

    public static HttpParams GetHttpParams(int connectionTimeout, int socketTimeout){
        HttpParams params = new BasicHttpParams();

        HttpConnectionParams.setConnectionTimeout(params, connectionTimeout);
        HttpConnectionParams.setSoTimeout(params, socketTimeout);

        return params;
    }

    public static String convertEntityToString(HttpEntity entity){
        InputStream inputStream;
        StringBuilder total = null;
        try {
            inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            total = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                total.append(line);
            }
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
        return total.toString();
    }
}
