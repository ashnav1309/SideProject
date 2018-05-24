package com.scm.automation.parallel.ashnav.utilities;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class PostRequests {

	private static CloseableHttpClient httpClient;
	private final static int TIMEOUT = 30;

	private static void reset_connection() throws IOException {
		if (httpClient != null) {
			httpClient.close();
		}
		RequestConfig config = RequestConfig.custom().setSocketTimeout(TIMEOUT * 1000).setConnectTimeout(TIMEOUT * 1000).setConnectionRequestTimeout(TIMEOUT * 1000).build();
		httpClient = HttpClientBuilder.create().setDefaultRequestConfig(config).build();
	}

	public static Boolean postJSONRequestToSolr(String solrUrl, String request) {

		Boolean status = false;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		CloseableHttpResponse response;
		StringEntity entity;
		try {
			entity = new StringEntity(request);
			HttpPost httpPost = new HttpPost(solrUrl);
			httpPost.setEntity(entity);
			httpPost.setHeader("Content-type", "application/json");
			response = httpclient.execute(httpPost);
			int responseStatus = response.getStatusLine().getStatusCode();
			String responseStatusLine = response.getStatusLine().toString();
			response.close();
			httpclient.close();
			if(responseStatus == 200) {
				status = true;
			}
			System.out.println(responseStatusLine);
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			status = false;
		} 
		catch (ClientProtocolException e) {
			e.printStackTrace();
			status = false;
		} 
		catch (IOException e) {
			e.printStackTrace();
			status = false;
		}
		return status;
	}

	public static HttpResponse post_Request(String restURL, List<NameValuePair> headers, List<NameValuePair> queryParams) throws ClientProtocolException, IOException, URISyntaxException {

		reset_connection();

		URIBuilder builder = new URIBuilder(restURL);
		if (queryParams != null) {
			for (NameValuePair param : queryParams) {
				builder.setParameter(param.getName(), param.getValue());
			}
		}
		HttpPost httpPost = new HttpPost(builder.build());
		if (headers != null) {
			for (NameValuePair headerPair : headers) {
				httpPost.setHeader(headerPair.getName(), headerPair.getValue());
			}
		}
		HttpResponse httpResponse = httpClient.execute(httpPost);
		return httpResponse;
	}
	
	public static HttpResponse postJSON_Request(String restURL, List<NameValuePair> headers, JSONObject jsonObj) throws ClientProtocolException, IOException {

		reset_connection();

		HttpPost httpPost = new HttpPost(restURL);
		if (headers != null) {
			for (NameValuePair headerPair : headers) {

				httpPost.setHeader(headerPair.getName(), headerPair.getValue());
			}
		}
		StringEntity JsonEntityObj = new StringEntity(jsonObj.toString());
		httpPost.setEntity(JsonEntityObj);
		HttpResponse httpResponse = httpClient.execute(httpPost);
		return httpResponse;
	}

	public static JSONObject convert_HttpResponse_to_JsonObject(HttpResponse httpResponse) throws ParseException, IOException, JSONException {

		HttpEntity responseEntity = httpResponse.getEntity();
		String responseString = EntityUtils.toString(responseEntity, "UTF-8");
		return new JSONObject(responseString);
	}
}
