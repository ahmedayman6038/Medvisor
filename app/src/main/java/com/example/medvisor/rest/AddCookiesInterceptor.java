package com.example.medvisor.rest;
import android.util.Log;

import com.example.medvisor.service.MyApplication;
import java.io.IOException;
import java.util.HashSet;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = null;
        try {
            preferences = CookiesMethods.getCookies(MyApplication.getAppContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (String cookie : preferences) {
            builder.addHeader("Cookie", cookie);
            Log.v("OkHttp", "Adding Header: " + cookie);
        }
        return chain.proceed(builder.build());
    }
}