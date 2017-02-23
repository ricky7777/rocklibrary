package com.rockduck.rocklibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * call http時的utils
 *
 * @author Ricky
 */
public class HttpUtils {

    private final static String PREFIX_JSONOBJECT = "{";
    private final static String PREFIX_JSONARRAY = "[";

    //    public final static String               HEADER_KEY_CONTENT_TYPE = "Content-Type";
    private final static StringBuilder sb = new StringBuilder();
    private final static HttpClient httpClient = new DefaultHttpClient();

    private final static List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

    public final static String HEADER_CONTENT_TYPE = "Content-Type";
    public final static String HEADER_CONTENT_TYPE_JSON = "application/json";

    /**
     * 發httpclient,取得資料
     *
     * @param url
     * @return
     */
    public static String getData(String url) {
        HttpGet httpGet = new HttpGet(url);
        try {
            httpGet.setHeader("Cache-Control", "no-cache");

            // 加入gzip
            httpGet.setHeader("Accept-Encoding", "gzip");
            synchronized (httpClient) {
                // httpClient.getParams().setParameter("Accept-Encoding", "gzip");

                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    sb.setLength(0);
                    InputStream instream = httpEntity.getContent();

                    Header contentEncoding = httpResponse.getFirstHeader("Content-Encoding");
                    // Header[] allHeader = httpResponse.getAllHeaders();
                    // for (Header header : allHeader) {
                    // LogUtils.d("log", "name:%1$s,value:%2$s", header.getName(), header.getValue());
                    // }

                    if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
                        instream = new GZIPInputStream(instream);
                    }

                    // BufferedReader reader = new BufferedReader(new InputStreamReader(instream, "big5"));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }

                    instream.close();

                    return sb.toString();
                }
            }
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            LogUtils.d(HttpUtils.class.getSimpleName(), "http errror:%1$s", writer.toString());
        }
        return null;
    }

    /**
     * 從網路取得檔案
     *
     * @param fileURL
     * @param fileDstPath
     * @return
     */
    public static void getFile(String fileURL, String fileDstPath) {
        // HttpGet httpGet = new HttpGet(url);
        try {
            int count;
            URL url = new URL(fileURL);
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.connect();
            int lengthOfFile = connection.getContentLength();
            // long total = 0;
            InputStream input = new BufferedInputStream(connection.getInputStream());
            // InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(fileDstPath);
            byte data[] = new byte[1024];
            while ((count = input.read(data)) != -1) {
                // total += count;
                output.write(data, 0, count);
            }
            output.flush();
            output.close();
            input.close();
            Log.d("log",fileDstPath+" download success");
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            LogUtils.d(HttpUtils.class.getSimpleName(), "http errror:%1$s", writer.toString());
            Log.i("log", "http errror:" + writer.toString());
        }
    }

    public static String getDataBySSL(String url) {
        HttpClient httpClient = new DefaultHttpClient();
        httpClient = wrapClient(httpClient);
        HttpGet httpGet = new HttpGet(url);

        try {
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                StringBuilder sb = new StringBuilder();
                InputStream instream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                instream.close();
                return sb.toString();
            }
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            LogUtils.d(HttpUtils.class.getSimpleName(), "http errror:%1$s", writer.toString());
        }
        return null;
    }

    public static HttpResponse postURL(String url, HashMap<String, String> headers, StringEntity entity) {
        // StringBuilder sb = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        // 加入header
        if (headers != null && !headers.isEmpty()) {
            Set<String> headerKeys = headers.keySet();
            for (String headerKey : headerKeys) {
                String headerValue = headers.get(headerKey);
                // LogUtils.d("log", "add header %1$s:%2$s", headerKey, headerValue);
                // httpPost.removeHeaders(headerKey);
                httpPost.addHeader(headerKey, headerValue);
            }
        }

        try {
            httpPost.setEntity(entity);

            Header[] allHeader = httpPost.getAllHeaders();
            if (allHeader != null) {
                for (Header header : allHeader) {
                    LogUtils.d("log", "header:%1$s:%2$s", header.getName(), header.getValue());
                }
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);
            // HttpEntity httpEntity = httpResponse.getEntity();
            // if (httpEntity != null) {
            // StringBuilder sb = new StringBuilder();
            // InputStream instream = httpEntity.getContent();
            // BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            // String line = null;
            // while ((line = reader.readLine()) != null) {
            // sb.append(line);
            // }
            // return sb.toString();
            // }
            return httpResponse;
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Log.i("HttpUtils", writer.toString());
            LogUtils.d(HttpUtils.class.getSimpleName(), "httppost errror:%1$s", writer.toString());
        }
        return null;
    }

    /**
     * post url,會順便帶指定header進去
     *
     * @param url
     * @param headers
     * @return
     */
    public static String postURL(String url, HashMap<String, String> headers, HashMap<String, String> params) {
        // StringBuilder sb = new StringBuilder();
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);

        // 加入header
        if (headers != null && !headers.isEmpty()) {
            Set<String> headerKeys = headers.keySet();
            for (String headerKey : headerKeys) {
                String headerValue = headers.get(headerKey);
                // LogUtils.d("log", "add header %1$s:%2$s", headerKey, headerValue);
                // httpPost.removeHeaders(headerKey);
                httpPost.addHeader(headerKey, headerValue);
            }
        }

        // 加入params
        nameValuePairs.clear();
        sb.setLength(0);
        //to json
        sb.append("{");
        if (params != null && !params.isEmpty()) {
            Set<String> paramKeys = params.keySet();
            for (String paramKey : paramKeys) {
                String paramValue = params.get(paramKey);
                LogUtils.d("log", "add param %1$s=%2$s", paramKey, paramValue);
//                nameValuePairs.add(new BasicNameValuePair(paramKey, paramValue));
                String rawData = String.format("\"%1$s\":\"%2$s\",",paramKey,paramValue);
                sb.append(rawData);
            }
        }

        sb.setLength(sb.length()-1);
        sb.append("}");

        try {
            if (sb.length()!=0) {
//                StringEntity entity = new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8);
                LogUtils.d("log","sb:%1$s",sb.toString());
                StringEntity entity = new StringEntity(sb.toString());
                httpPost.setEntity(entity);
            }

            Header[] allHeader = httpPost.getAllHeaders();
            if (allHeader != null) {
                for (Header header : allHeader) {
                    LogUtils.d("log", "header:%1$s:%2$s", header.getName(), header.getValue());
                }
            }

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            if (httpEntity != null) {
                StringBuilder sb = new StringBuilder();
                InputStream instream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                return sb.toString();
            }
//            return httpResponse;
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            LogUtils.d(HttpUtils.class.getSimpleName(), "httppost errror:%1$s", writer.toString());
        }
        return null;
    }

    /**
     * 取得url的response,並且做成bitmap
     *
     * @param myurl
     * @return
     */
    public static Bitmap getURLBitmap(String myurl) {
        Bitmap bitmap = null;
        try {
            InputStream in = null;
            URL url = new URL(myurl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            in = new BufferedInputStream(con.getInputStream());

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = false;
            options.inSampleSize = 2; // width，hight設為原來的十分一

            bitmap = BitmapFactory.decodeStream(in, null, null);
            in.close();
        } catch (Exception e1) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            e1.printStackTrace(printWriter);
            LogUtils.d("log", "get bitmap error:" + writer.toString());
        }
        return bitmap;
    }

    /**
     * 取得JSONObject
     *
     * @param data
     * @return
     */
    public static JSONObject getJSONObject(String data) {
        try {
            if (data.startsWith(PREFIX_JSONOBJECT)) {
                return new JSONObject(data);
            }
        } catch (Exception ex) {
            LogUtils.d(HttpUtils.class.getSimpleName(), "jsonobject errror:%1$s", ex);
        }

        return null;
    }

    /**
     * 取出JSONObj內指定的value
     *
     * @param data
     * @param key
     * @return
     */
    public static JSONObject getJSONObject(JSONObject data, String key) {
        try {
            return data.getJSONObject(key);
        } catch (Exception ex) {
            LogUtils.d(HttpUtils.class.getSimpleName(), "get special key errror:%1$s", ex);
        }

        return null;
    }

    /**
     * 取得JSONArray
     *
     * @param data
     * @return
     */
    public static JSONArray getJSONArray(String data) {
        try {
            if (data.startsWith(PREFIX_JSONARRAY)) {
                return new JSONArray(data);
            }
        } catch (Exception ex) {
            LogUtils.d(HttpUtils.class.getSimpleName(), "jsonarray errror:%1$s", ex);
        }

        return null;
    }

    /**
     * 取出JSONArray內指定的value
     *
     * @param data
     * @param key
     * @return
     */
    public static JSONArray getJSONArray(JSONObject data, String key) {
        try {
            return data.getJSONArray(key);
        } catch (Exception ex) {
            LogUtils.d(HttpUtils.class.getSimpleName(), "get special key errror:%1$s", ex);
        }

        return null;
    }

    /**
     * 將接回來的Json字串資料,parse成map型式
     *
     * @param data
     */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> parseData(String data) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        if (data.startsWith(PREFIX_JSONOBJECT)) {
            parseJSONObject(list, data);
            return list;
        } else if (data.startsWith(PREFIX_JSONARRAY)) {
            parseJSONArray(list, data);
            return list;
        } else {
            return null;
        }
    }

    private static void parseJSONObject(List<Map<String, Object>> list, String data) {
        try {
            JSONObject dataObj = new JSONObject(data);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(dataObj.toString(), Map.class);
            list.add(map);

            // Iterator<String> keys = dataObj.keys();
            // while (keys.hasNext()) {
            // String key = keys.next();
            // String value = dataObj.getString(key);
            // ObjectMapper mapper = new ObjectMapper();
            // Map<String, String> map = mapper.readValue(dataObj.toString(), Map.class);
            // list.add(map);
            // }
        } catch (Exception ex) {
            LogUtils.d(HttpUtils.class.getSimpleName(), "jsonobject errror:%1$s", ex);
        }
    }

    private static void parseJSONArray(List<Map<String, Object>> list, String data) {
        try {
            JSONArray dataAry = new JSONArray(data);
            ObjectMapper mapper = new ObjectMapper();
            for (int i = 0; i < dataAry.length(); i++) {
                Map<String, Object> map = mapper.readValue(dataAry.getString(i), Map.class);
                list.add(map);
                // LogUtils.d(this, "遊戲列表:%1$s", map.toString());
            }
        } catch (Exception ex) {
            LogUtils.d(HttpUtils.class.getSimpleName(), "jsonarray errror:%1$s", ex);
        }
    }

    public static HttpResponse postBySSL(String url, HashMap<String, String> headers, HashMap<String, String> params) {
        // 加入params
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if (params != null && !params.isEmpty()) {
            Set<String> paramKeys = params.keySet();
            for (String paramKey : paramKeys) {
                String paramValue = params.get(paramKey);
                // log.debug("add param %1$s=%2$s", paramKey, paramValue);
                nameValuePairs.add(new BasicNameValuePair(paramKey, paramValue));
            }
        }

        try {
            HttpEntity entity = new UrlEncodedFormEntity(nameValuePairs);
            return postBySSL(url, headers, entity);
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            LogUtils.d("HttpException:%1$s", writer.toString());
            return null;
        }
    }

    public static HttpResponse postBySSL(String url, HashMap<String, String> headers, HttpEntity entity) {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient = wrapClient(httpclient);

        HttpPost post = new HttpPost(url);

        // 加入header
        if (headers != null && !headers.isEmpty()) {
            Set<String> headerKeys = headers.keySet();
            for (String headerKey : headerKeys) {
                String headerValue = headers.get(headerKey);
                // LogUtils.d("log", "add header %1$s:%2$s", headerKey, headerValue);
                // httpPost.removeHeaders(headerKey);
                post.addHeader(headerKey, headerValue);
            }
        }

        try {
            post.setEntity(entity);
            // httpclient = wrapClient(httpclient);

            HttpResponse response2 = httpclient.execute(post);
            HttpEntity httpEntity = response2.getEntity();
            // if (httpEntity != null) {
            // StringBuilder sb = new StringBuilder();
            // InputStream instream = httpEntity.getContent();
            // BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
            // String line = null;
            // while ((line = reader.readLine()) != null) {
            // sb.append(line);
            // }
            // // log.debug("發ssl httppost結果:%1$s", sb);
            // }
            return response2;
        } catch (Exception ex) {
            Writer writer = new StringWriter();
            PrintWriter printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            LogUtils.d("log", "HttpException:%1$s", writer.toString());
        }
        return null;
    }

    /**
     * 將client轉為https使用的 httpclient
     *
     * @param base
     * @return
     */
    private static HttpClient wrapClient(org.apache.http.client.HttpClient base) {
        try {

            SchemeRegistry schemeRegistry = new SchemeRegistry();

            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            SSLSocketFactory socketFactory = new MySSLSocketFactory(trustStore);
            schemeRegistry.register(new Scheme("https", socketFactory, 443));

            HttpParams params = new BasicHttpParams();

            ClientConnectionManager mgr = new ThreadSafeClientConnManager(params, schemeRegistry);

            return new DefaultHttpClient(mgr, params);

            // SSLContext ctx = SSLContext.getInstance("TLS");
            //
            // X509TrustManager tm = new X509TrustManager() {
            // public X509Certificate[] getAcceptedIssuers() {
            // return null;
            // }
            //
            // public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // }
            //
            // public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // }
            // };
            // ctx.init(null, new TrustManager[] { tm }, null);
            // SSLSocketFactory ssf = new SSLSocketFactory(ctx, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            // SchemeRegistry registry = new SchemeRegistry();
            // int port = 443;
            // if (specialPort != 0) {
            // port = 8443;
            // }
            // LogUtils.d("log", "post by ssl port:%1$s", port);
            // registry.register(new Scheme("https", port, ssf));
            // ThreadSafeClientConnManager mgr = new ThreadSafeClientConnManager(registry);
            // return new DefaultHttpClient(mgr, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * 取得document型式的物件
     *
     * @param url
     * @return
     */
    public static Document getDocument(String url) {
        Document doc = null;
        try {
            // 用gzip取回來
            String data = getData(url);
            // LogUtils.d("log", "gzip data:%1$s", data);
            doc = Jsoup.parse(data);
            // LogUtils.d("document", "doc:%1$s", doc.toString());

        } catch (Exception e) {
            // Writer writer = new StringWriter();
            // PrintWriter printWriter = new PrintWriter(writer);
            // e.printStackTrace(printWriter);
            // LogUtils.d("log", "error getDocument:" + writer.toString());
            LogUtils.d("log", "error getDocument:" + e.getMessage());
        }

        return doc;
    }

    private static class MySSLSocketFactory extends SSLSocketFactory {

        SSLContext sslContext = SSLContext.getInstance("TLS");

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws java.security.cert.CertificateException {
                }
            };
            sslContext.init(null, new TrustManager[]{tm}, null);
        }

        @Override
        public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
            return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
        }

        @Override
        public Socket createSocket() throws IOException {
            return sslContext.getSocketFactory().createSocket();
        }
    }
}
