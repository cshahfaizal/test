package app.airtouchmedia.candidate.com.network;

import android.content.Context;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static app.airtouchmedia.candidate.com.util.Constants.BASE_URL;

/**
 * Retrofit Client Instance
 */
public class RetrofitClientInstance {

    /**
     * Retrofit
     */
    private static Retrofit retrofit;

    /**
     * Function to initialize retrofit instance
     * @return retrofit
     */
    public static Retrofit getRetrofitInstance(Context context) {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new RestHandler().getHTTP(context))
                    .build();
        }
        return retrofit;
    }
}