package com.github.dvriesman.bitsotrade.cloud.rest.util;

import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/***
 * An util class to create Retrofit Objects
 */
public class RetrofitClientBuilder {

    public static <S> S createService(Class<S> serviceClass, String baseUrl) {
        Retrofit retrofit  = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        return retrofit.create(serviceClass);
    }

}
