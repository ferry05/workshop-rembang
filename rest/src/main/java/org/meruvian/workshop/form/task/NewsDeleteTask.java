package org.meruvian.workshop.form.task;

import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.meruvian.workshop.form.R;
import org.meruvian.workshop.form.rest.RestVariable;
import org.meruvian.workshop.form.service.ConnectionUtil;
import org.meruvian.workshop.form.service.TaskService;

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
            HttpClient httpClient = new DefaultHttpClient(ConnectionUtil.GetHttpParams(15000,15000));
            HttpDelete httpDelete = new HttpDelete(RestVariable.SERVER_URL +"/"+params[0]);

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
