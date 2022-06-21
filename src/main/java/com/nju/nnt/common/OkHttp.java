package com.nju.nnt.common;

import okhttp3.*;
import okhttp3.Response;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class OkHttp {

    public static String get(String url){
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = okHttpClient.newCall(request);
        try {
            Response response = call.execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
    /**
     * 发送post json请求
     * */
    public static String post(String url, String body,String cookie) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody postBody = RequestBody.create(JSON, body);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .addHeader("cookie",cookie)
                .post(postBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }
    /**
     * 发送post form表单请求
     * @param url
     * @param parametersBody
     * @param headermap
     * @return
     * @throws RuntimeException
     * @throws IOException
     */
    public  String post(String url, Map<String, String> parametersBody,HashMap<String, String> headermap) throws RuntimeException, IOException {
        CloseableHttpResponse httpResponse;
        //将传入的map集合转换为list
        List<NameValuePair> params=new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : parametersBody.entrySet()) {
            params.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        //调用上面方法，传入list集合参数
        httpResponse =new OkHttp().post(url,params, headermap);
        String responseString = EntityUtils.toString(httpResponse.getEntity());
        System.out.println("请求返回 ： " + responseString);
        return responseString;

    }
    public static String post(String url, String body) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody postBody = RequestBody.create(JSON, body);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .post(postBody)
                .build();
        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public CloseableHttpResponse post(String url, List<NameValuePair> params, HashMap<String, String> headermap) throws ClientProtocolException, IOException {
        //创建一个可关闭的HttpClient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        //创建一个HttpPost的请求对象
        HttpPost httppost = new HttpPost(url);

//        List<NameValuePair> params=new ArrayList<NameValuePair>();
//        params.add(new BasicNameValuePair("q",entityString));
        //添加参数
        httppost.setEntity(new UrlEncodedFormEntity(params));


        //设置payload
//        httppost.setEntity(new StringEntity(entityString));

        //加载请求头到httppost对象
        for (Map.Entry<String, String> entry : headermap.entrySet()) {
            httppost.addHeader(entry.getKey(), entry.getValue());
        }

        System.out.println("开始发送post请求");
//        ReporterLog.info("请求URL ： " + url);
//        ReporterLog.info("请求参数 ： " + params.toString());
        System.out.println("请求URL ： " + url);
        System.out.println("请求参数 ： " + params.toString());
        //发送post请求
        CloseableHttpResponse httpResponse = httpclient.execute(httppost);

        return httpResponse;
    }


}

