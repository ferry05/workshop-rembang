package org.meruvian.workshop.form.task;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONException;
import org.json.JSONObject;
import org.meruvian.workshop.form.R;
import org.meruvian.workshop.form.entity.News;
import org.meruvian.workshop.form.rest.RestVariable;
import org.meruvian.workshop.form.service.ConnectionUtil;
import org.meruvian.workshop.form.service.TaskService;

/**
 * Created by merv on 6/6/15.
 */
public class NewsPutTask extends AsyncTask<News, Void, JSONObject>{
    private Context context;
    private TaskService taskService;

    public NewsPutTask(Context context, TaskService taskService){
        this.context = context;
        this.taskService = taskService;
    }

    @Override
    protected void onPreExecute(){
        taskService.onExecute(RestVariable.NEWS_PUT_TASK);
    }

    @Override
    protected JSONObject doInBackground(News... params) {
        JSONObject json = new JSONObject();
        try{
            json.put("id",params[0].getId());
            json.put("title", params[0].getTitle());
            json.put("content", params[0].getContent());
            json.put("createDate",0);

            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.GetHttpParams(15000,15000));
            HttpPut httpPut = new HttpPut(RestVariable.SERVER_URL +"/"+params[0].getId());
            httpPut.addHeader(new BasicHeader("Content-Type","application/json"));

//            Add Json
            httpPut.setEntity(new StringEntity(json.toString()));

            HttpResponse response = httpClient.execute(httpPut);
            json = new JSONObject(ConnectionUtil.convertEntityToString(response.getEntity()));
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
                news.setTitle(jsonObject.getString("title"));
                news.setContent(jsonObject.getString("content"));
                news.setCreateDate(jsonObject.getLong("createDate"));

                taskService.onSuccess(RestVariable.NEWS_PUT_TASK, news);
            }else {
                taskService.onError(RestVariable.NEWS_PUT_TASK, context.getString(R.string.failed_post_news));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            taskService.onError(RestVariable.NEWS_PUT_TASK, context.getString(R.string.failed_post_news));
        }
    }
}
