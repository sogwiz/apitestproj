package com.sogwiz.api;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import okhttp3.*;
import okio.Buffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by sogwiz on 2/2/17.
 */
public class SogwizAPIService {

    private SogwizAPIInterface api;
    Logger logger = LoggerFactory.getLogger(SogwizAPIService.class);

    public String getApiEndpoint(){
        return "https://jsonplaceholder.typicode.com";
    }

    public SogwizAPIInterface getClient(){
        if(api==null){
            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                    .create();

            OkHttpClient httpClient = new OkHttpClient.Builder()
                    .addInterceptor(new SogwizRequestInterceptor())
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

    /**
     * intercept http requests
     * add default headers
     * add logging for each request / response
     */
    class SogwizRequestInterceptor implements Interceptor {
        private final Charset UTF8 = Charset.forName("UTF-8");

        public Response intercept(Chain chain) throws IOException {

            Request sourceRequest = chain.request();
            HttpUrl url = sourceRequest.url();

            //add default query parameters based on config settings
            //eg if config set, then:
            //url = url.newBuilder().addQueryParameter("parameter", "value").build();

            Request modifiedRequest = sourceRequest.newBuilder()
                    .addHeader("Time-Zone", TimeZone.getDefault().getID())
                    .addHeader("User-Agent", "QA_sogwizapiservice")
                    .addHeader("Content-Type", "application/json")
                    .build();

            logger.info(String.format("Sending request %s: %s on %s%n%s",
                    modifiedRequest.method(), modifiedRequest.url(), chain.connection(), modifiedRequest.headers()));

            RequestBody requestBody = modifiedRequest.body();

            if (requestBody != null) {
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);

                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    contentType.charset(UTF8);
                }

                String bodyString = "Request Body :\n" + buffer.readString(charset) + " (" + requestBody.contentLength() + "-byte body)";
                logger.debug(bodyString);
            }

            long time1 = System.nanoTime();
            Response response = chain.proceed(modifiedRequest);
            long time2 = System.nanoTime();

            logger.info(String.format("Received response code %d for %s in %.1fms%n%s",
                    response.code(), response.request().url(), (time2 - time1) / 1e6d, response.headers()));

            //TODO: log the response body

            return response;
        }
    }
}
