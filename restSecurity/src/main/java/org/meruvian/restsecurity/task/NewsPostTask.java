package org.meruvian.restsecurity.task;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.restsecurity.entity.News;
import org.meruvian.restsecurity.rest.RestVariable;
import org.meruvian.restsecurity.service.ConnectionUtil;
import org.meruvian.restsecurity.service.TaskService;
import org.meruvian.restsecurity.R;
import org.meruvian.restsecurity.utils.AuthenticationUtils;

import java.io.IOException;

/**
 * Created by merv on 6/6/15.
 */
public class NewsPostTask extends AsyncTask<News, Void, JSONObject>{

    private TaskService taskService;
    private Context context;

    public  NewsPostTask(TaskService taskService, Context context){
        this.taskService = taskService;
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        taskService.onExecute(RestVariable.NEWS_POST_TASK);
    }

    @Override
    protected JSONObject doInBackground(News... params) {
        JSONObject json = new JSONObject();

        try {
            json.put("id",0);
            json.put("title",params[0].getTitle());
            json.put("content",params[0].getContent());
            json.put("createDate",0);

            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.GetHttpParams(15000, 15000));
            HttpPost httpPost = new HttpPost(RestVariable.SERVER_URL);

            httpPost.addHeader(new BasicHeader("Content-Type","application/json"));
            httpPost.setHeader("Authorization", "Bearer "
                    + AuthenticationUtils.getCurrentAuthentication().getAccessToken());

            httpPost.setEntity(new StringEntity(json.toString()));

            HttpResponse response = httpClient.execute(httpPost);
            json = new JSONObject(ConnectionUtil.convertEntityToString(response.getEntity()));
        } catch (IOException e) {
            json = null;
            e.printStackTrace();
        } catch (JSONException e) {
            json = null;
            e.printStackTrace();
        } catch (Exception e) {
            json = null;
            e.printStackTrace();
        }

        return json;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject){
        try {
            if(jsonObject != null){
                News news = new News();
                news.setId(jsonObject.getInt("id"));
                news.setContent(jsonObject.getString("content"));
                news.setCreateDate(jsonObject.getLong("createDate"));

                taskService.onSuccess(RestVariable.NEWS_POST_TASK, news);
            }else{
                taskService.onError(RestVariable.NEWS_POST_TASK, context.getString(R.string.failed_post_news));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            taskService.onError(RestVariable.NEWS_POST_TASK, context.getString(R.string.failed_post_news));
        }
    }
}
