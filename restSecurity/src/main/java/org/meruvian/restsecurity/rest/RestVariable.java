package org.meruvian.restsecurity.rest;

/**
 * Created by merv on 6/6/15.
 */
public class RestVariable {

//    localhost
//    public static final String SERVER_URL = "http://10.0.2.2:8080/api/news";
//    public static final String SERVER_URL_OAUTH = "http://10.0.2.2:8080";


    public static final String SERVER_URL = "http://192.168.4.83:8080/api/news";
    public static final String SERVER_URL_OAUTH = "http://192.168.4.83:8080";

    public static final String PGA_APP_ID = "419c6697-14b7-4853-880e-b68e3731e316";
    public static final String PGA_API_SECRET = "s3cr3t";

    public static final int NEWS_GET_TASK = 1;
    public static final int NEWS_POST_TASK = 2;
    public static final int NEWS_DELETE_TASK = 3;
    public static final int NEWS_PUT_TASK = 4;

    public static final String PGA_REQUEST_TOKEN = "/oauth/token";
    public static final String PGA_CURRENT_ME = "/api/users/me";
    public static final String PGA_CURRENT_SITE = "/api/sites/me";
    public static final String PGA_CURRENT_ROLE = "/api/users/me/roles";



}
