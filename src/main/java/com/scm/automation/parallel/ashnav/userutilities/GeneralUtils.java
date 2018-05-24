package com.scm.automation.parallel.ashnav.userutilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.scm.automation.parallel.ashnav.utilities.PostRequests;

public class GeneralUtils {

	public static String getAccessToken(String auth_url, String auth_api) throws ClientProtocolException, IOException, URISyntaxException, JSONException {
		String url = auth_url + auth_api;
		List<NameValuePair> headers = new ArrayList<NameValuePair>();
		headers.add(new BasicNameValuePair("Authorization", "Basic Y2xpZW50LWFkbWluOnNlY3JldA=="));
		HttpResponse response 		= PostRequests.post_Request(url, headers, null);
		JSONObject object 			= PostRequests.convert_HttpResponse_to_JsonObject(response);
		if(object.has("access_token")) return (String) object.get("access_token");
		return null;
	}
	public static int getRandomNo() {
		int randomno = (int) (Math.random() * 40000000 + 10000000);
		return randomno;
	}
}
