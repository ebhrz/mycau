package com.mycau.mycau;

import android.os.Build;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class myutil {

    private static OkHttpClient client = null;

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            if (Build.VERSION.SDK_INT < 29) {
                builder.sslSocketFactory(sslSocketFactory);
            }
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            builder.cookieJar(new CookieJar() {
                private final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();
                @Override
                public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                    cookieStore.put(httpUrl.host(), list);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                    List<Cookie> cookies = cookieStore.get(httpUrl.host());
                    return cookies != null ? cookies : new ArrayList<Cookie>();
                }
            });
            OkHttpClient okHttpClient = builder.build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String url, Map<String,String> headers) {

        if(client == null)
        {
            client = getUnsafeOkHttpClient();
        }
        Request.Builder builder = new Request.Builder()
                .url(url);
        headers.forEach((key,value)->{
            builder.header(key,value);
        });
        Request request = builder.build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String post(String url, Map<String,String> postdata,Map<String,String> headers) {
        if(client == null)
        {
            client = getUnsafeOkHttpClient();
        }
        FormBody.Builder bodybuilder=new FormBody.Builder();
        postdata.forEach((key,value)->{
            bodybuilder.add(key,value);
        });

        RequestBody formBody = bodybuilder.build();
        Request.Builder builder = new Request.Builder()
                .url(url);
        headers.forEach((key,value)->{
            builder.header(key,value);
        });
        Request request = builder.post(formBody).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject j2m(String json){
//        Gson gson = new Gson();
//        Type stringStringMap = new TypeToken<Map<String, String>>(){}.getType();
//        Map<String,String> map = gson.fromJson(json, stringStringMap);
//        return map;
        return JSONObject.parseObject(json);
    }

    public static String m2j(Map<String,String> map){
        return JSON.toJSONString(map);
    }

}
