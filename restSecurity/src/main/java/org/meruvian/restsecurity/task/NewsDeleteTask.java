package org.meruvian.restsecurity.task;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.meruvian.restsecurity.R;
import org.meruvian.restsecurity.rest.RestVariable;
import org.meruvian.restsecurity.service.ConnectionUtil;
import org.meruvian.restsecurity.service.TaskService;
import org.meruvian.restsecurity.utils.AuthenticationUtils;

import java.io.IOException;

/**
 * Created by merv on 6/6/15.
 */
public class NewsDeleteTask extends AsyncTask<String, Void, Boolean>{
    private TaskService taskService;
    private Context context;

    public NewsDeleteTask(TaskService taskService, Context context){
        this.taskService = taskService;
        this.context = context;
    }

    @Override
    protected void onPreExecute(){
        taskService.onExecute(RestVariable.NEWS_DELETE_TASK);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        try {
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.GetHttpParams(15000, 15000));
            HttpDelete httpDelete = new HttpDelete(RestVariable.SERVER_URL +"/"+params[0]);

            httpDelete.setHeader("Authorization", "Bearer "
                    + AuthenticationUtils.getCurrentAuthentication().getAccessToken());

            HttpResponse response = httpClient.execute(httpDelete);
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_NO_CONTENT){
                return true;
            }else {
                return false;
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean check){
        if(check){
            taskService.onSuccess(RestVariable.NEWS_DELETE_TASK, context.getString(R.string.failed_delete_news));
        }
    }
}
