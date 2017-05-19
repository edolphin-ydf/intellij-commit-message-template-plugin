package commitmessagetemplate.network;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by edolphin on 16-10-22.
 */
public class RpcUtils {
    private static final Logger logger = LoggerFactory.getLogger(RpcUtils.class);

    // GET
    public static String getResponseFromServerGET(String urlstr) {
        try {
            URL url = new URL(urlstr);
            URLConnection urlConnection = url.openConnection();

            StringBuilder builder = new StringBuilder();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = br.readLine() ) != null)
                builder.append(line);

            return builder.toString();
        } catch (Throwable e) {
            logger.error("get response from server fail", e);
            return null;
        }
    }

    // POST
    public static String getResponseFromServerPOST(String url, String param, String contentType) {
        PrintWriter out = null;
        BufferedReader br = null;
        try {
            URL realUrl = new URL(url);

            URLConnection conn = realUrl.openConnection();
            conn.setRequestProperty("Content-Type", contentType);

            conn.setDoOutput(true);
            conn.setDoInput(true);

            out = new PrintWriter(conn.getOutputStream());
            out.print(param);
            out.flush();

            br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder builder = new StringBuilder();
            String line;
            while ((line = br.readLine() ) != null)
                builder.append(line);

            return builder.toString();
        } catch (Exception e) {
            logger.error("get response from server fail", e);
            return null;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (br != null)
                    br.close();
            } catch (IOException e) {
                logger.error("get response from server, close stream fail.", e);
            }
        }
    }

    public static String getResponseFromServerPOST(String url, String param) {
        return getResponseFromServerPOST(url, param, "application/json;charset=UTF-8");
    }

    // GET ------------------------
    public static <T> T getResponseFromServerGET(String url, Class<T> clazz) {
        String responseBody = RpcUtils.getResponseFromServerGET(url);
        if (responseBody == null)
            return null;

        return JSON.parseObject(responseBody, clazz);
    }

    public static <T> T getResponseFromServerGET(String url, Class<T> clazz, Map<String, Object> params) {
        if (params.isEmpty())
            return getResponseFromServerGET(url, clazz);

        StringBuilder sb = new StringBuilder(url);
        sb.append("?")
                .append(params.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&")));
        return getResponseFromServerGET(sb.toString(), clazz);
    }

    public static <T> T getResponseFromServerGET(String url, Class<T> clazz, String ... params) {
        if (params.length %2 != 0)
            throw new IllegalArgumentException("params size invalid");

        if (params.length > 0)
            url += "?";
        for (int i = 0; i < params.length; i+=2) {
            url += params[i] + "=" + params[i+1];
        }
        String responseBody = RpcUtils.getResponseFromServerGET(url);
        if (responseBody == null)
            return null;

        return JSON.parseObject(responseBody, clazz);
    }

    public static <T> T getResponseFromServerGET(String url, Class<T> clazz, BaseQueryRequest request) {
        String responseBody = RpcUtils.getResponseFromServerGET(url + "?" + request.toQueryString());
        if (responseBody == null)
            return null;

        return JSON.parseObject(responseBody, clazz);
    }


    // POST -------------------------------
    public static <T> T getResponseFromServerPOST(String url, Class<T> clazz, BaseRequest param) {
        String responseBody = RpcUtils.getResponseFromServerPOST(url, JSON.toJSONString(param));
        if (responseBody == null)
            return null;

        return JSON.parseObject(responseBody, clazz);
    }

    public static <T> T getResponseFromServerPOST(String url, Class<T> clazz, String param) {
        String responseBody = RpcUtils.getResponseFromServerPOST(url, param);
        if (responseBody == null)
            return null;

        return JSON.parseObject(responseBody, clazz);
    }

    public static <T> T getResponseFromServerPOST(String url, Class<T> clazz, BaseQueryRequest request) {
        String responseBody = RpcUtils.getResponseFromServerPOST(url, request.toQueryString(), "text/html;charset=UTF-8");
        if (responseBody == null)
            return null;

        return JSON.parseObject(responseBody, clazz);
    }

    public static <T> T getResponseFromServerPOST(String url, Class<T> clazz, BaseJsonRequest request) {
        String responseBody = RpcUtils.getResponseFromServerPOST(url, request.toJsonString());
        if (responseBody == null)
            return null;

        return JSON.parseObject(responseBody, clazz);
    }

    public static String generateUrl(String host, String action, String key) {
        if (action.contains("?")) {
            if (action.endsWith("?")) {
                return host + action + "key=" + key;
            } else {
                return host + action + "&key=" + key;
            }
        }
        return host + action + "?key=" + key;
    }
}
