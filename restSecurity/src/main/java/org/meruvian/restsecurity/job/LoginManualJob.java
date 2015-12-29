package org.meruvian.restsecurity.job;

import android.util.Base64;
import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.restsecurity.entity.Authentication;
import org.meruvian.restsecurity.event.LoginEvent;
import org.meruvian.restsecurity.rest.RestVariable;
import org.meruvian.restsecurity.utils.JsonRequestUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 29/07/15.
 */
public class LoginManualJob extends LoginJob {
    private String username;
    private String password;

    public LoginManualJob(String username, String password) {
        super(new Params(Priority.HIGH).requireNetwork().persist());

        this.username = username;
        this.password = password;
    }


    @Override
    public void onRun() throws Throwable {
        JsonRequestUtils requestUtils = new JsonRequestUtils(RestVariable.SERVER_URL_OAUTH + RestVariable.PGA_REQUEST_TOKEN);
        requestUtils.addQueryParam("grant_type", "password");
        requestUtils.addQueryParam("client_id", RestVariable.PGA_APP_ID);
        requestUtils.addQueryParam("client_secret", RestVariable.PGA_API_SECRET);
//        requestUtils.addQueryParam("scope", "read write");
        requestUtils.addQueryParam("username", username);
        requestUtils.addQueryParam("password", password);

        String authorization = RestVariable.PGA_APP_ID + ":" + RestVariable.PGA_API_SECRET;
        authorization = Base64.encodeToString(authorization.getBytes(), Base64.DEFAULT);

        requestUtils.addHeader("Authorization", "Basic " + authorization);

        JsonRequestUtils.HttpResponseWrapper<Authentication> responseWrapper =
                requestUtils.post(null, new TypeReference<Authentication>() {});
        HttpResponse response = responseWrapper.getHttpResponse();

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            registerAuthentication(responseWrapper);
        } else {
            Log.e(LoginManualJob.class.getSimpleName(), "Access Code: " + response.getStatusLine().getStatusCode() + " " +response.getStatusLine().getReasonPhrase());
            EventBus.getDefault().post(new LoginEvent.LoginFailed(response.getStatusLine().getStatusCode()));
        }
    }
}
