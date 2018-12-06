package com.bacancy.eprodigy.API;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by vishal on 10/2/17.
 */

public class ApiClient {

//Local
//    private static final String BASE_URL = "http://192.168.1.99:8084/api/";
//Stage
    private static final String BASE_URL = "http://158.69.205.234/eprodigy/api/";
//Live
//    private static final String BASE_URL = "http://18.235.127.113/";


    private static Retrofit retrofit = null;


    public static ApiInterface getClient() {


        if (retrofit == null) {
            /**
             * HttpLogginInterceptor :An OkHttp interceptor which logs request and response information
             */
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS).addInterceptor(interceptor)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(ApiInterface.class);
    }
}
