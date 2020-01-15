package app.airtouchmedia.candidate.com.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;

class RestHandler {

    private static final int TIMEOUT = 30;  //Seconds
    private static final int CACHE_SIZE = 10 * 1024 * 1024; // 10 MB
    private static final String HEADER_PRAGMA = "Pragma";
    private static final String HEADER_CACHE_CONTROL = "Cache-Control";


    OkHttpClient getHTTP(Context context) {
        return new OkHttpClient.Builder()
                .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .cache(provideCache(context))
                .addNetworkInterceptor(provideCacheInterceptor(context))
                .addInterceptor(provideOfflineCacheInterceptor(context))
                .build();
    }

    private Cache provideCache(Context context) {
        Cache cache = null;
        try {
            cache = new Cache(new File(context.getCacheDir(), "http-cache"), CACHE_SIZE);
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
        return cache;
    }

    /**
     * Network interceptor  with network state
     * @param context : Context
     */
    private Interceptor provideCacheInterceptor(Context context) {
        return chain -> {
            okhttp3.Response response = chain.proceed(chain.request());
            CacheControl cacheControl;
            if (isConnectingToInternet(context)) {
                cacheControl = getCacheControlWithMaxAge();
            } else {
                cacheControl = getCacheControlWithMaxStale();
            }
            return response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();
        };
    }


    /**
     * Cache interceptor for offline state
     * @param context : Context
     */
    private Interceptor provideOfflineCacheInterceptor(Context context) {
        return chain -> {
            Request request = chain.request();
            if (!isConnectingToInternet(context)) {
                CacheControl cacheControl = getCacheControlWithMaxStale();

                request = request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    private CacheControl getCacheControlWithMaxAge() {
        return new CacheControl.Builder()
                .maxAge(0, TimeUnit.SECONDS)
                .build();
    }
    private CacheControl getCacheControlWithMaxStale() {
        return new CacheControl.Builder()
                .maxStale(7, TimeUnit.DAYS)
                .build();
    }

    private boolean isConnectingToInternet(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

}
