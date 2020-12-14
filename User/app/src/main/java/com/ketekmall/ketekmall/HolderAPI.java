package com.ketekmall.ketekmall;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface HolderAPI {
    @Headers({"X-User-Key: S2cwNDRCbkl5OEt4OXF6WlFpQ0dHd1NSN1R2eVV5WDk="})
    @GET("posts")
    Call<List<Post>> getPosts();
}
