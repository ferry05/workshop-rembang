package org.meruvian.restsecurity.task;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.restsecurity.R;
import org.meruvian.restsecurity.entity.News;
import org.meruvian.restsecurity.rest.RestVariable;
import org.meruvian.restsecurity.service.ConnectionUtil;
import org.meruvian.restsecurity.service.TaskService;
import org.meruvian.restsecurity.utils.AuthenticationUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by merv on 6/6/15.
 */
public class NewsGetTask extends AsyncTask<String, Void, JSONArray> {
    public Context context;
    public TaskService taskService;

    public NewsGetTask(Context context, TaskService taskService){
        this.taskService = taskService;
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        taskService.onExecute(RestVariable.NEWS_GET_TASK);
    }


    @Override
    protected JSONArray doInBackground(String... params) {
        JSONArray json = null;
        try{
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.GetHttpParams(15000, 15000));
            HttpGet httpGet = new HttpGet(RestVariable.SERVER_URL +"?title=" + params[0]);

            httpGet.setHeader("Content-Type","application/json");
//          Header Request Token
            httpGet.setHeader("Authorization", "Bearer "
                    + AuthenticationUtils.getCurrentAuthentication().getAccessToken());

            Log.d(getClass().getSimpleName(), "Host: " + httpGet.getURI().getHost()
                    + "\nURL Path: " + httpGet.getURI().getPath()
                    + "\nURL: " + httpGet.getURI().toString()
            );

            HttpResponse response = httpClient.execute(httpGet);
            json = new JSONArray(ConnectionUtil.convertEntityToString(response.getEntity()));
        }catch (IOException e){
            json = null;
            Log.e(getClass().getSimpleName(), "IOException: " + e.getMessage(), e);
            e.printStackTrace();
        }catch (JSONException e){
            json = null;
            Log.e(getClass().getSimpleName(), "JSONException: " + e.getMessage(), e);
            e.printStackTrace();
        }catch (Exception e){
            json = null;
            Log.e(getClass().getSimpleName(), "Exception: " + e.getMessage(), e);
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONArray jsonArray){
        try{
            if(jsonArray.length() > 0){
                List<News> newses = new ArrayList<News>();

                for (int index = 0;index < jsonArray.length(); index++){
                    JSONObject json = jsonArray.getJSONObject(index);

                    News news = new News();
                    news.setId(json.getInt("id"));
                    news.setTitle(json.getString("title"));
                    news.setContent(json.getString("content"));
                    news.setCreateDate(json.getLong("createDate"));

                    newses.add(news);
                }
                taskService.onSuccess(RestVariable.NEWS_GET_TASK, newses);
            }else{
                taskService.onError(RestVariable.NEWS_GET_TASK, context.getString(R.string.empty_news));
            }
        }catch (JSONException e){
            e.printStackTrace();
            taskService.onError(RestVariable.NEWS_GET_TASK, context.getString(R.string.failed_receive_news));
        }
    }
}
