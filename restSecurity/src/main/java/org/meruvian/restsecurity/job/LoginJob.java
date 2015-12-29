package org.meruvian.restsecurity.job;

import android.util.Log;

import com.fasterxml.jackson.core.type.TypeReference;
import com.path.android.jobqueue.Job;
import com.path.android.jobqueue.Params;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.meruvian.restsecurity.core.commons.Role;
import org.meruvian.restsecurity.core.commons.User;
import org.meruvian.restsecurity.core.commons.UserRole;
import org.meruvian.restsecurity.entity.Authentication;
import org.meruvian.restsecurity.entity.PageEntity;
import org.meruvian.restsecurity.event.LoginEvent;
import org.meruvian.restsecurity.rest.RestVariable;
import org.meruvian.restsecurity.utils.AuthenticationUtils;
import org.meruvian.restsecurity.utils.JsonRequestUtils;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 29/07/15.
 */
public abstract class LoginJob extends Job {

    protected LoginJob(Params params) {
        super(params);
    }

    @Override
    public void onAdded() {
        EventBus.getDefault().post(new LoginEvent.DoLogin());
    }

    protected void registerAuthentication(JsonRequestUtils.HttpResponseWrapper<Authentication> responseWrapper) {
        HttpResponse response = responseWrapper.getHttpResponse();

        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            Authentication authentication = responseWrapper.getContent();
            AuthenticationUtils.registerAuthentication(authentication);
            User user = requestUser();

            user.setRoles(new ArrayList<Role>());
            for (UserRole userRole : requestRoles().getContent()) {
                user.getRoles().add(userRole.getRole());
            }
            authentication.setUser(user);

            AuthenticationUtils.registerAuthentication(authentication);
            Log.i(getClass().getSimpleName(), "ACCESS_TOKEN : " + authentication.getAccessToken());

            EventBus.getDefault().post(new LoginEvent.LoginSuccess(responseWrapper.getContent()));
        } else {
            EventBus.getDefault().post(new LoginEvent.LoginFailed(response.getStatusLine().getStatusCode()));
        }
    }

    protected User requestUser() {
        JsonRequestUtils requestUtils = new JsonRequestUtils(RestVariable.SERVER_URL_OAUTH + RestVariable.PGA_CURRENT_ME);
        return requestUtils.get(new TypeReference<User>() {}).getContent();
    }

    protected PageEntity<UserRole> requestRoles() {
        JsonRequestUtils requestUtils = new JsonRequestUtils(RestVariable.SERVER_URL_OAUTH + RestVariable.PGA_CURRENT_ROLE);
        return requestUtils.get(new TypeReference<PageEntity<UserRole>>() {}).getContent();
    }

    @Override
    protected void onCancel() {
        EventBus.getDefault().post(new LoginEvent.LoginFailed(0));
    }

    @Override
    protected boolean shouldReRunOnThrowable(Throwable throwable) {
        Log.e(LoginJob.class.getSimpleName(), throwable.getMessage(), throwable);

        return false;
    }
}
