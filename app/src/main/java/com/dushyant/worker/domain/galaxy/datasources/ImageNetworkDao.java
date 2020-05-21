package com.dushyant.worker.domain.galaxy.datasources;


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageNetworkDao {

    @GET("65535/{image}")
    Call<ResponseBody> getImage(@Path("image") String image);

}
