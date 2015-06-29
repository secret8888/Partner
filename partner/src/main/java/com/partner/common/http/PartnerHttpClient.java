package com.partner.common.http;

import com.partner.common.constant.Consts;
import com.partner.common.util.Logcat;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by yuym on 6/8/15.
 */
public class PartnerHttpClient {
    private static final OkHttpClient client = new OkHttpClient();

    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    public static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

    static {
        client.setConnectTimeout(Consts.TIMEOUT, TimeUnit.SECONDS);
        client.setWriteTimeout(Consts.TIMEOUT, TimeUnit.SECONDS);
        client.setReadTimeout(Consts.TIMEOUT, TimeUnit.SECONDS);
    }

    /**
     * 同步执行请求
     *
     * @param request
     * @return
     */
    public static Response execute(Request request) throws IOException {
        return client.newCall(request).execute();
    }

    /**
     * 异步发送请求
     *
     * @param request
     * @param callback
     */
    public static void enqueue(Request request, Callback callback) {
        client.newCall(request).enqueue(callback);
    }

    /**
     * 异步发送请求，无回调
     *
     * @param request
     */
    public static void enqueue(Request request) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) throws IOException {

            }
        });
    }

    /**
     * 同步执行get请求
     *
     * @param url
     */
    public static String syncGet(String url) throws IOException {
        Logcat.d("syncGet request url : " + url);
        Request request = new Request.Builder().url(url).build();
        return getResponseBody(request);
    }

    /**
     * 异步执行get请求
     *
     * @param url
     * @param callBack
     */
    public static void asyncGet(String url, final AsyncHttpCallback callBack) {
        Logcat.d("asyncGet request url : " + url);
        Request request = new Request.Builder().url(url).build();
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.sendFailureMessage(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callBack.sendResponseMessage(response);
            }
        });
    }

    /**
     * 同步执行post请求，参数键值对形式
     *
     * @param url
     * @param values
     * @return
     * @throws IOException
     */
    public static String syncPost(String url, Map<String, String> values) throws IOException {
        return getResponseBody(getRequest(url, values));
    }

    /**
     * 异步执行post请求，参数键值对形式
     *
     * @param url
     * @param values
     * @param callBack
     */
    public static void asyncPost(String url, Map<String, String> values, final AsyncHttpCallback callBack) {
        Request request = getRequest(url, values);
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callBack.sendFailureMessage(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callBack.sendResponseMessage(response);
            }
        });
    }

    /**
     * 同步执行post请求，参数json形式
     *
     * @param url
     * @param json
     * @return
     * @throws IOException
     */
    public static String syncPost(String url, String json) throws IOException {
        RequestBody requestBody = RequestBody.create(JSON, json);
        return getResponseBody(getRequest(url, requestBody));
    }

    /**
     * 异步执行post请求，参数json形式
     *
     * @param url
     * @param json
     * @param callback
     */
    public static void asyncPost(String url, String json, final AsyncHttpCallback callback) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        Request request = getRequest(url, requestBody);
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.sendFailureMessage(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.sendResponseMessage(response);
            }
        });
    }

    /**
     * 异步上传文件
     *
     * @param url
     * @param file
     * @param callback
     */
    public static void asyncPostFile(String url, File file, final AsyncHttpCallback callback) {
        RequestBody requestBody = RequestBody.create(MEDIA_TYPE_MARKDOWN, file);
        Request request = getRequest(url, requestBody);
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.sendFailureMessage(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.sendResponseMessage(response);
            }
        });
    }

    /**
     * 异步上传文件和其他
     *
     * @param url
     * @param json
     * @param file
     * @param callback
     */
    public static void asyncPostFile(String url, String json, File file, final AsyncHttpCallback callback) {
        RequestBody requestBody = new MultipartBuilder()
                .type(MultipartBuilder.FORM)
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"title\""),
                        RequestBody.create(JSON, json))
                .addPart(
                        Headers.of("Content-Disposition", "form-data; name=\"image\""),
                        RequestBody.create(MEDIA_TYPE_PNG, new File("website/static/logo-square.png")))
                .build();
        Request request = getRequest(url, requestBody);
        enqueue(request, new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.sendFailureMessage(request, e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                callback.sendResponseMessage(response);
            }
        });
    }

    /**
     * get request body
     *
     * @param request
     * @return
     * @throws IOException
     */
    private static String getResponseBody(Request request) throws IOException {
        Response response = execute(request);
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code : " + response);
        }
    }

    /**
     * get okhtto request by key-value params
     *
     * @param url
     * @param values
     * @return
     */
    private static Request getRequest(String url, Map<String, String> values) {
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Map.Entry<String, String> entry : values.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }

        RequestBody requestBody = builder.build();
        return getRequest(url, requestBody);
    }

    /**
     * get request by RequestBody
     *
     * @param url
     * @param requestBody
     * @return
     */
    private static Request getRequest(String url, RequestBody requestBody) {
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return request;
    }
}
