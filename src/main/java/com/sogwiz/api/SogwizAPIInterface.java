package com.sogwiz.api;

import com.google.gson.internal.LinkedTreeMap;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by sogwiz on 2/2/17.
 */
public interface SogwizAPIInterface {

    @GET("/posts/{post}")
    public Call<LinkedTreeMap> getPost(
            @Path("post") Integer postId
    );


}
