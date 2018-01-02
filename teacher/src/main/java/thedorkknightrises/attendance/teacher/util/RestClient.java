package thedorkknightrises.attendance.teacher.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Created by Samriddha Basu on 5/20/2017.
 */

public class RestClient {
    private static final String BASE_URL = "http://192.168.31.119:8000/api/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        try {
            client.get(getAbsoluteUrl(url), params, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        try {
            client.post(getAbsoluteUrl(url), params, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void delete(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        try {
            client.delete(getAbsoluteUrl(url), params, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager != null ? manager.getActiveNetworkInfo() : null;
        return (networkInfo != null && networkInfo.isConnected());
    }


    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

}
