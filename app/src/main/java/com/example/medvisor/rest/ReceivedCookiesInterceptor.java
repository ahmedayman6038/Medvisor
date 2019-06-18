package com.example.medvisor.rest;

import com.example.medvisor.service.MyApplication;
import java.io.IOException;
import java.util.HashSet;
import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            try {
                CookiesMethods.setCookies(MyApplication.getAppContext(), cookies);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return originalResponse;
    }
}