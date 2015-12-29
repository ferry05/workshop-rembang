package org.meruvian.restsecurity;

import android.app.Application;
import android.util.Log;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.path.android.jobqueue.JobManager;
import com.path.android.jobqueue.config.Configuration;

/**
 * Created by meruvian on 29/12/15.
 */
public class RestAppication extends Application {
    private static RestAppication instance;
    private static ObjectMapper objectMapper;
    private ObjectMapper jsonMapper;
    private JobManager jobManager;

    public RestAppication() {
        instance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Configuration configuration = new Configuration.Builder(this)
                .minConsumerCount(1) //always keep at least one consumer alive
                .maxConsumerCount(3) //up to 3 consumers at a time
                .loadFactor(3) //3 jobs per consumer
                .consumerKeepAlive(120) //wait 2 minute
                .build();

        jobManager = new JobManager(this, configuration);
        jsonMapper = objectMapper;

        Log.d(getClass().getName(), "onCreate");
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static RestAppication getInstance() {
        return instance;
    }

    public JobManager getJobManager() {
        return jobManager;
    }

    public ObjectMapper getJsonMapper() {
        return jsonMapper;
    }
}
