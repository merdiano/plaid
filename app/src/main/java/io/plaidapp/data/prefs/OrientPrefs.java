package io.plaidapp.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.plaidapp.BuildConfig;
import io.plaidapp.data.api.AuthInterceptor;
import io.plaidapp.data.api.DenvelopingConverter;
import io.plaidapp.data.api.dribbble.model.User;
import io.plaidapp.data.api.orientnews.OrientNewsService;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by merdan on 6/9/18.
 * Storing Orient user prefs
 */

public class OrientPrefs {
    private static final String ORIENT_PREF = "ORIENT_PREF";
    private static final String KEY_ACCESS_TOKEN = "KEY_ACCESS_TOKEN";
    private static final String KEY_USER_ID = "KEY_USER_ID";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
//    private static final String KEY_USER_USERNAME = "KEY_USER_USERNAME";
//    private static final String KEY_USER_AVATAR = "KEY_USER_AVATAR";
    private static volatile OrientPrefs singleton;
    private final SharedPreferences prefs;
    private String accessToken;
    private boolean isLoggedIn = false;
    private long userId;
    private String userName;
    private OrientNewsService api;
    private List<OrientPrefs.OrientLoginStatusListener> loginStatusListeners;
    public interface OrientLoginStatusListener{
        void onOrientLogin();
        void onOrientLogout();
    }

    public static OrientPrefs get(Context context) {
        if (singleton == null) {
            synchronized (DribbblePrefs.class) {
                singleton = new OrientPrefs(context);
            }
        }
        return singleton;
    }

    private OrientPrefs(Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(ORIENT_PREF, Context
                .MODE_PRIVATE);
        accessToken = prefs.getString(KEY_ACCESS_TOKEN, null);
        isLoggedIn = !TextUtils.isEmpty(accessToken);
        if (isLoggedIn) {
            userId = prefs.getLong(KEY_USER_ID, 0l);
            userName = prefs.getString(KEY_USER_NAME, null);
//            userUsername = prefs.getString(KEY_USER_USERNAME, null);
//            userAvatar = prefs.getString(KEY_USER_AVATAR, null);
//            userType = prefs.getString(KEY_USER_TYPE, null);
        }
    }

    public boolean isLoggedIn() {
        return isLoggedIn;
    }
    public void setAccessToken(String accessToken) {
        if (!TextUtils.isEmpty(accessToken)) {
            this.accessToken = accessToken;
            isLoggedIn = true;
            prefs.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
            createApi();
            dispatchLoginEvent();
        }
    }
    private void createApi() {
        final OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(getAccessToken()))
                .build();
        final Gson gson = new GsonBuilder()
                .setDateFormat(OrientNewsService.DATE_FORMAT)
                .create();
        api = new Retrofit.Builder()
                .baseUrl(OrientNewsService.ENDPOINT)
                .client(client)
//                .addConverterFactory(new DenvelopingConverter(gson))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create((OrientNewsService.class));
    }

    private String getAccessToken() {
        return !TextUtils.isEmpty(accessToken) ? accessToken
                : BuildConfig.DRIBBBLE_CLIENT_ACCESS_TOKEN;
    }

    public void setLoggedInUser(User user) {
        if (user != null) {
            userName = user.name;
//            userUsername = user.username;
            userId = user.id;
//            userAvatar = user.avatar_url;
//            userType = user.type;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putLong(KEY_USER_ID, userId);
            editor.putString(KEY_USER_NAME, userName);
//            editor.putString(KEY_USER_USERNAME, userUsername);
//            editor.putString(KEY_USER_AVATAR, userAvatar);
//            editor.putString(KEY_USER_TYPE, userType);
            editor.apply();
        }
    }

    public User getUser() {
        return new User.Builder()
                .setId(userId)
                .setName(userName)
//                .setUsername(userUsername)
//                .setAvatarUrl(userAvatar)
//                .setType(userType)
                .build();
    }
    public long getUserId() {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public OrientNewsService getApi() {
        if (api == null) createApi();
        return api;
    }

    public void logout() {
        isLoggedIn = false;
        accessToken = null;
        userId = 0l;
        userName = null;
//        userUsername = null;
//        userAvatar = null;
//        userType = null;
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_ACCESS_TOKEN, null);
        editor.putLong(KEY_USER_ID, 0l);
        editor.putString(KEY_USER_NAME, null);
//        editor.putString(KEY_USER_AVATAR, null);
//        editor.putString(KEY_USER_TYPE, null);
        editor.apply();
        createApi();
        dispatchLogoutEvent();
    }

    public void addLoginStatusListener(OrientLoginStatusListener listener) {
        if (loginStatusListeners == null) {
            loginStatusListeners = new ArrayList<>();
        }
        loginStatusListeners.add(listener);
    }

    public void removeLoginStatusListener(OrientLoginStatusListener listener) {
        if (loginStatusListeners != null) {
            loginStatusListeners.remove(listener);
        }
    }
    private void dispatchLoginEvent() {
        if (loginStatusListeners != null && !loginStatusListeners.isEmpty()) {
            for (OrientLoginStatusListener listener: loginStatusListeners) {
                listener.onOrientLogin();
            }
        }
    }

    private void dispatchLogoutEvent() {
        if (loginStatusListeners != null && !loginStatusListeners.isEmpty()) {
            for (OrientLoginStatusListener listener : loginStatusListeners) {
                listener.onOrientLogout();
            }
        }
    }
}
