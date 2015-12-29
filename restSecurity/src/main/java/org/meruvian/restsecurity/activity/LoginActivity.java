package org.meruvian.restsecurity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.path.android.jobqueue.JobManager;

import org.meruvian.restsecurity.R;
import org.meruvian.restsecurity.RestAppication;
import org.meruvian.restsecurity.event.LoginEvent;
import org.meruvian.restsecurity.job.LoginManualJob;
import org.meruvian.restsecurity.utils.AuthenticationUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by meruvian on 29/12/15.
 */
public class LoginActivity extends ActionBarActivity {
    private JobManager jobManager;
    private Button submit;
    private TextView username;
    private TextView password;
    private View loginProgress;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        EventBus.getDefault().register(this);
        jobManager = RestAppication.getInstance().getJobManager();

        submit = (Button) findViewById(R.id.button_login);
        username = (TextView) findViewById(R.id.edit_username);
        password = (TextView) findViewById(R.id.edit_password);
        loginProgress = findViewById(R.id.login_progress);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManualJob loginJob = new LoginManualJob(username.getText().toString(), password.getText().toString());
                jobManager.addJobInBackground(loginJob);
            }
        });
    }

    public void onEventMainThread(LoginEvent.LoginSuccess loginSuccess) {
        goToMainActivity();
    }

    public void onEventMainThread(LoginEvent.LoginFailed loginFailed) {
        loginProgress.setVisibility(View.GONE);
        submit.setVisibility(View.VISIBLE);
        username.setEnabled(true);
        password.setEnabled(true);
    }

    private void goToMainActivity() {
        startActivity(new Intent(this, NewsActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AuthenticationUtils.getCurrentAuthentication() != null) {
            goToMainActivity();
        }
    }

}
