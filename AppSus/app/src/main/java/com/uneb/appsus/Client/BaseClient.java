package com.uneb.appsus.Client;

import android.content.Context;
import android.util.Log;


import com.uneb.appsus.Manager.TokenManager;

import java.io.IOException;
import java.io.InputStream;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class BaseClient {

    protected static final String BASE_URL = "http://172.20.10.4:8080/api";

    protected Context context;
    protected OkHttpClient client;


    public BaseClient(Context context) {
        this.context = context;
        this.client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        String token = TokenManager.getInstance(context).getBearerToken();
                        Log.i("BASECLIENT TOKEN", "intercept: " + token);
                        Request original = chain.request();
                        Request request = original.newBuilder()
                                .header("Authorization", "Bearer " + token)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    }
                })
                .build();
    }

    public String loadJSONFromAsset(String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public Request baseRequest(String url){
        return new Request.Builder()
                .url(BASE_URL + url)
                .build();
    }

    public Request postRequest(RequestBody body, String url){
        return new Request.Builder()
                .url(BASE_URL + url)
                .post(body)
                .build();
    }
}
