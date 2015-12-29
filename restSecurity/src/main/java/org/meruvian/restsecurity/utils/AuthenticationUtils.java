package org.meruvian.restsecurity.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.meruvian.restsecurity.RestAppication;
import org.meruvian.restsecurity.entity.Authentication;

import java.io.IOException;

/**
 * Created by meruvian on 29/07/15.
 */
public class AuthenticationUtils {
    private static final String AUTHENTICATION = "AUTHENTICATION";

    public static void registerAuthentication(Authentication authentication) {
        ObjectMapper mapper = RestAppication.getInstance().getJsonMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RestAppication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        try {
            editor.putString(AUTHENTICATION, mapper.writeValueAsString(authentication));
        } catch (JsonProcessingException e) {
            Log.e(AuthenticationUtils.class.getSimpleName(), e.getMessage(), e);
        }
        editor.apply();
    }

    public static Authentication getCurrentAuthentication() {
        RestAppication instance = RestAppication.getInstance();
        ObjectMapper mapper = instance.getJsonMapper();
//        ObjectMapper mapper = QrscanApplication.getInstance().getJsonMapper();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RestAppication.getInstance());
        String jsonAuth = preferences.getString(AUTHENTICATION, "");

        if (!jsonAuth.equals("")) {
            try {
                return mapper.readValue(jsonAuth, Authentication.class);
            } catch (IOException e) {
                Log.e(AuthenticationUtils.class.getSimpleName(), e.getMessage(), e);
            }
        }

        return null;
    }

    public static void logout() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RestAppication.getInstance());
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(AUTHENTICATION);
        editor.apply();
    }
}
