package com.dushyant.worker.framework.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.dushyant.worker.BuildConfig;
import com.dushyant.worker.domain.image.datasources.ImageNetworkDao;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/*
 * Module responsible for network operations in entire app.
 * It just provides retrofitClient of particular DAO to perform request. */
@Module
public class NetworkModule {

    private Context applicationContext;

    private NetworkConfiguration networkConfiguration;

    public NetworkModule(Context applicationContext) {

        this.applicationContext = applicationContext;
        this.networkConfiguration = new NetworkConfiguration(ApiUrls.getBaseUrl());
    }

    private ImageNetworkDao getImageNetworkDao(Retrofit retrofit) {
        return retrofit.create(ImageNetworkDao.class);
    }

    /*
     * Dao provided by Dagger to repositories to make use of to perform dao requests on network.*/
    @Provides
    public ImageNetworkDao getImageNetworkDao() {
        return getImageNetworkDao(networkConfiguration.getRetrofitClient());
    }

    /*
     * Actually configures Network Mechanism such has some edits in HTTP clients or event in Retrofit.*/
    private class NetworkConfiguration {

        private final HttpUrl baseUrl;

        NetworkConfiguration(HttpUrl baseUrl) {
            this.baseUrl = baseUrl;
        }

        private OkHttpClient getHttpClient() {

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.readTimeout(20, TimeUnit.SECONDS);
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request request = original.newBuilder()
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            });

            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor mLogging = new HttpLoggingInterceptor();
                mLogging.setLevel(HttpLoggingInterceptor.Level.BODY);
                //httpClient.addInterceptor(mLogging);
            }
            return httpClient.build();
        }

        private boolean isNetworkConnected(Context mContext) {
            ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }

        Retrofit getRetrofitClient() {
            return new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(getHttpClient())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
    }
}
