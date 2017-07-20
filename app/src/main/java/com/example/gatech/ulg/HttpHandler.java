package com.example.gatech.ulg;
import android.os.Debug;
import android.text.TextUtils;
import android.util.DebugUtils;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * Created by rainbowu on 19/06/2017.
 */

public class HttpHandler {

    private static final String TAG = HttpHandler.class.getSimpleName();
    private static final String COOKIES_HEADER = "Set-Cookie";
    private static java.net.CookieManager msCookieManager = new java.net.CookieManager();
    public static boolean isLogged = false;


    private DataOutputStream printout;


    public HttpHandler() {
    }

    public String makeGETServiceCall(String reqUrl) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Authorization","token b679973efbf6197ba69346d3986a63ea0a6caef8");
//            conn.setRequestProperty("Cookie","csrftoken=G2W9GzHY8P4DAdqMKVSJ30LtIfdCc0kOudKLE97zUqjjeQH6U4MRr7RrtlYodXip; sessionid=4epjnr5jq209vjees4bad3reeeh6r6rz;");


            if (msCookieManager.getCookieStore().getCookies().size() > 0 && isLogged) {
                conn.setRequestProperty("Cookie",
                        TextUtils.join("; ",  msCookieManager.getCookieStore().getCookies()));
            }

            // read the response
            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);
        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }

    public String makePOSTServiceCall(String reqUrl, JSONObject jsonObject) {
        String response = null;
        try {
            URL url = new URL(reqUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
//            conn.setRequestProperty("Cookie","csrftoken=xYvIEAb5TVosSB0UmlkD4CB2URJe9vMECioHIOGSo85OTJRJ3q8j5Jbye24E7xp8;");
//            cookie:csrftoken=xYvIEAb5TVosSB0UmlkD4CB2URJe9vMECioHIOGSo85OTJRJ3q8j5Jbye24E7xp8

            // When isLogged, we need cookie to access the APIs.
            if (msCookieManager.getCookieStore().getCookies().size() > 0 && isLogged) {
                conn.setRequestProperty("Cookie",
                        TextUtils.join("; ",  msCookieManager.getCookieStore().getCookies()));
            }



            Log.d(TAG, url.toString());
            Log.d(TAG, jsonObject.toString());

            printout = new DataOutputStream(conn.getOutputStream ());
            printout.writeBytes(jsonObject.toString());
            printout.flush ();
            printout.close ();

            Map<String, List<String>> headerFields = conn.getHeaderFields();
            List<String> cookiesHeader = headerFields.get(COOKIES_HEADER);

            // Not logging yet, Store the cookie(csrf token and the session id)
            if (cookiesHeader != null && !isLogged) {
                for (String cookie : cookiesHeader) {
                    msCookieManager.getCookieStore().add(null, HttpCookie.parse(cookie).get(0));
                }
            }

            InputStream in = new BufferedInputStream(conn.getInputStream());
            response = convertStreamToString(in);

            Log.d(TAG, response);


        } catch (MalformedURLException e) {
            Log.e(TAG, "MalformedURLException: " + e.getMessage());
        } catch (ProtocolException e) {
            Log.e(TAG, "ProtocolException: " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "IOException: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
        return response;
    }



    private String convertStreamToString(InputStream is) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.e(TAG, "Response from api: " + sb.toString());

        return sb.toString();
    }

}
