package com.sogwiz.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

/**
 * Created by sogwiz on 2/2/17.
 */
public class SogwizAPIService {

    private SogwizAPIInterface api;

    public String getApiEndpoint(){
        return "https://jsonplaceholder.typicode.com";
    }

    public SogwizAPIInterface getClient(){
        if(api==null){
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    //.addInterceptor(new SogwizRequestRequestInterceptor())
                    //https://www.codefactor.io/repository/github/square/okhttp/files/source/master/okhttp-urlconnection/src/main/java/okhttp3/JavaNetCookieJar.java
                    //.cookieJar(new JavaNetCookieJar(cookieManager))
                    .retryOnConnectionFailure(true)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .build();

            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(getApiEndpoint())
                            .client(httpClient)
                            .addConverterFactory(GsonConverterFactory.create(gson));

            Retrofit retrofit = builder.build();
            api = retrofit.create(SogwizAPIInterface.class);
        }
        return api;
    }
}
