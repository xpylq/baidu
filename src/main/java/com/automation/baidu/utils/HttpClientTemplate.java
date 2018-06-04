/**
 * @(#)HttpClientTemplate.java, 2015年9月8日.
 * <p>
 * Copyright 2015 Netease, Inc. All rights reserved.
 * NETEASE PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.automation.baidu.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Http工具类
 * <p>
 * 本工具类主要针对常用的GET和POST请求进行了封装并提供了方便的HTTP调用接口
 * <p>
 * 对于常见的GET和POST请求，只需要指定url、参数和编码方式即可方便调用，参见
 * <li>{@code executeGet(url)}
 * <li>{@code executeGet(url, parameters)}
 * <li>{@code executeGet(url, parameters, charset)}
 * <li>{@code executePost(url)}
 * <li>{@code executePost(url, parameters)}
 * <li>{@code executePost(url, parameters, charset)}
 *
 * @author hzyurui
 */
public class HttpClientTemplate {

    private static final Logger logger = LoggerFactory
            .getLogger(HttpClientTemplate.class);

    private PoolingHttpClientConnectionManager connectionManager;

    private HttpClient httpClient;

    private int timeout = 50000;

    private String defaultCharset = "utf-8";

    public void init() {
        httpClient = HttpClients.custom()
                .setConnectionManager(connectionManager)
                .setConnectionManagerShared(true)
                .build();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }

    /**
     * 使用默认编码执行执行无参数的POST请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @return
     * @throws IOException
     */
    public String executePost(String url) throws IOException {
        return executePost(url, null);
    }

    /**
     * 使用默认编码执行POST请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @return
     * @throws IOException
     */
    public String executePost(String url, List<NameValuePair> parameters)
            throws IOException {
        return executePost(url, parameters, null);
    }

    /**
     * 使用json序列化对象传输,post请求
     * @author youzhihao
     */
    public String excuteJsonPost(String url, Object data, String charset) throws IOException {
        charset = charset != null ? charset : defaultCharset;
        HttpPost post = getHttpPost(url);
        StringEntity entity = new StringEntity(JSONObject.toJSONString(data), ContentType.APPLICATION_JSON);
        post.setEntity(entity);
        return requestAndParse(post, charset);
    }

    /**
     * 使用默认编码执行POST请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @param timeout
     *            超时时间
     * @return
     * @throws IOException
     */
    public String executePost(String url, List<NameValuePair> parameters,
                              int timeout) throws IOException {
        return executePost(url, parameters, null, timeout);
    }

    /**
     * 执行POST请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @param charset
     *            编码，若为null则使用默认编码
     * @return
     * @throws IOException
     */
    public String executePost(String url, List<NameValuePair> parameters,
                              String charset) throws IOException {
        charset = charset != null ? charset : defaultCharset;
        HttpPost postRequest = makePostRequest(url, parameters, charset);
        String result = requestAndParse(postRequest, charset);
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "[op:HttpClientTemplate] response from url={}, param={}, data={}",
                    url, JSON.toJSON(parameters), result);
        }
        return result;
    }

    /**
     * 执行POST请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @param charset
     *            编码，若为null则使用默认编码
     * @param timeout
     *            超时时间
     * @return
     * @throws IOException
     */
    public String executePost(String url, List<NameValuePair> parameters,
                              String charset, int timeout) throws IOException {
        charset = charset != null ? charset : defaultCharset;
        HttpPost postRequest = makePostRequest(url, parameters, charset,
                timeout);
        String result = requestAndParse(postRequest, charset);
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "[op:HttpClientTemplate] response from url={}, param={}, timeout={}, data={}",
                    url, JSON.toJSON(parameters), timeout, result);
        }
        return result;
    }

    /**
     * 执行POST请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @param stringEntity
     *            请求内容体，默认为json格式
     * @param charset
     *            编码，若为null则使用默认编码
     * @return
     * @throws IOException
     */
    public String executePost(String url, String stringEntity, String charset)
            throws IOException {
        charset = charset != null ? charset : defaultCharset;
        HttpPost postRequest = makePostRequest(url, stringEntity, charset);
        String result = requestAndParse(postRequest, charset);
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "[op:HttpClientTemplate] response from url={}, entity={}, data={}",
                    url, stringEntity, result);
        }
        return result;
    }

    /**
     * 使用默认的编码执行无参数的GET请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @return
     * @throws IOException
     */
    public String executeGet(String url) throws IOException {
        return executeGet(url, null);
    }

    /**
     * 使用默认的编码执行GET请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @return
     * @throws IOException
     */
    public String executeGet(String url, List<NameValuePair> parameters)
            throws IOException {
        return executeGet(url, parameters, null);
    }

    /**
     * 执行GET请求并将响应实体以字符串返回
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @param charset
     *            编码，若为null则使用默认编码
     * @return
     * @throws IOException
     */
    public String executeGet(String url, List<NameValuePair> parameters,
                             String charset) throws IOException {
        charset = charset != null ? charset : defaultCharset;
        HttpGet getRequest = makeGetRequest(url, parameters, charset);
        if (logger.isDebugEnabled()) {
            logger.debug("[op:executeGet] getRequest={}",
                    JSON.toJSON(getRequest));
        }
        String result = requestAndParse(getRequest, charset);
        if (logger.isDebugEnabled()) {
            logger.debug(
                    "[op:executeGet] response from url={}, param={}, data={}",
                    url, JSON.toJSON(parameters), result);
        }
        return result;
    }

    /**
     * 根据给定的url、参数和编码方式构建一个GET请求
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @param charset
     *            编码，若为null则使用默认编码
     * @return
     */
    private HttpGet makeGetRequest(String url, List<NameValuePair> parameters,
                                   String charset) {
        String queryString = null;
        if (parameters != null && parameters.size() > 0) {
            charset = charset != null ? charset : defaultCharset;
            queryString = URLEncodedUtils.format(parameters, charset);
        }
        String requestUrl = url;
        if (queryString != null) {
            if (!url.contains("?"))
                requestUrl += "?" + queryString;
            else
                requestUrl += "&" + queryString;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("[op:makeGetRequest] making get request url={}",
                    requestUrl);
        }
        return getHttpGet(requestUrl);
    }

    /**
     * 根据给定的url、参数和编码方式构建一个POST请求
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @param charset
     *            编码，若为null则使用默认编码
     * @return
     */
    private HttpPost makePostRequest(String url,
                                     List<NameValuePair> parameters, String charset) throws IOException {
        HttpPost post = getHttpPost(url);
        if (parameters != null && parameters.size() > 0) {
            charset = charset != null ? charset : defaultCharset;
            UrlEncodedFormEntity urfe = new UrlEncodedFormEntity(parameters,
                    charset);
            post.setEntity(urfe);
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "[op:makePostRequest] making post request url={}, param={}",
                        url, JSON.toJSON(parameters));
            }
        }
        return post;
    }

    /**
     * 根据给定的url、参数和编码方式构建一个POST请求
     *
     * @param url
     *            url
     * @param parameters
     *            参数
     * @param charset
     *            编码，若为null则使用默认编码
     * @param timeout
     *            超时时间
     * @return
     */
    private HttpPost makePostRequest(String url, List<NameValuePair> parameters,
                                     String charset, int timeout) throws IOException {
        HttpPost post = getHttpPost(url, timeout);
        if (parameters != null && parameters.size() > 0) {
            charset = charset != null ? charset : defaultCharset;
            UrlEncodedFormEntity urfe = new UrlEncodedFormEntity(parameters,
                    charset);
            post.setEntity(urfe);
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "[op:makePostRequest] making post request url={}, param={}, timeout={}",
                        url, JSON.toJSON(parameters), timeout);
            }
        }
        return post;
    }

    /**
     * 根据给定的url、参数和编码方式构建一个POST请求
     *
     * @param url
     *            url
     * @param stringEntity
     *            请求内容体，默认为json格式
     * @param charset
     *            编码，若为null则使用默认编码
     * @return
     */
    private HttpPost makePostRequest(String url, String stringEntity,
                                     String charset) throws IOException {
        HttpPost post = getHttpPost(url);
        if (stringEntity != null) {
            charset = charset != null ? charset : defaultCharset;
            StringEntity se = new StringEntity(stringEntity,
                    ContentType.APPLICATION_JSON);
            post.setEntity(se);
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "[op:makePostRequest] making post request url={}, entity={}",
                        url, stringEntity);
            }
        }
        return post;
    }

    /**
     * 生成POST请求，使用配置的参数
     *
     * @param url
     * @return
     */
    private HttpPost getHttpPost(String url) {
        HttpPost postMethod = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout)
                .setRedirectsEnabled(false).build();
        postMethod.setConfig(requestConfig);
        return postMethod;
    }

    /**
     * 生成POST请求，使用配置的参数
     *
     * @param url
     * @param timeout
     * @return
     */
    private HttpPost getHttpPost(String url, int timeout) {
        if (timeout <= 0) {
            return getHttpPost(url);
        }

        HttpPost postMethod = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout).setConnectionRequestTimeout(timeout)
                .setSocketTimeout(timeout).setRedirectsEnabled(false).build();
        postMethod.setConfig(requestConfig);
        return postMethod;
    }

    /**
     * 生成GET请求，使用配置的参数
     *
     * @param url
     * @return
     */
    private HttpGet getHttpGet(String url) {
        HttpGet getMethod = new HttpGet(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(timeout)
                .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout)
                .setRedirectsEnabled(false).build();
        getMethod.setConfig(requestConfig);
        return getMethod;
    }

    /**
     * 执行请求并获取响应
     *
     * @param httpRequest
     *            HTTP请求
     * @param charset
     *            响应体编码
     * @return
     * @throws ClientProtocolException
     * @throws IOException
     */
    private String requestAndParse(HttpUriRequest httpRequest, String charset)
            throws IOException {
        HttpResponse httpResponse = httpClient.execute(httpRequest);
        return getResponseContentStr(httpResponse, charset);
    }

    /**
     * 使用指定编码将响应实体转为字符串
     *
     * @param httpResponse
     *            响应
     * @param charset
     *            编码
     * @return
     * @throws IOException
     */
    private String getResponseContentStr(HttpResponse httpResponse,
                                         String charset) throws IOException {
        HttpEntity entity = getResponseContentEntity(httpResponse);
        if (null == entity) {
            return null;
        }
        return EntityUtils.toString(entity, defaultCharset);
    }

    /**
     * 获取响应实体
     *
     * @param httpResponse
     * @return
     * @throws IOException
     */
    private HttpEntity getResponseContentEntity(HttpResponse httpResponse)
            throws IOException {
        StatusLine statusLine = httpResponse.getStatusLine();
        if (null == statusLine) {
            throw new IOException("status not specified");
        }
        int statusCode = statusLine.getStatusCode();
        if (statusCode < 200 || statusCode > 299) {
            EntityUtils.consumeQuietly(httpResponse.getEntity());
            throw new IOException("status code: " + statusCode);
        }
        return httpResponse.getEntity();
    }

    /**
     * 参数处理:无参数时，return一个空的map
     *
     * @param map
     * @return
     */
    public List<NameValuePair> parseToNameValuePairs(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return new ArrayList<>();
        }

        List<NameValuePair> formParams = new ArrayList<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            formParams
                    .add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        return formParams;
    }

    public void setConnectionManager(
            PoolingHttpClientConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void setDefaultCharset(String defaultCharset) {
        this.defaultCharset = defaultCharset;
    }
}
