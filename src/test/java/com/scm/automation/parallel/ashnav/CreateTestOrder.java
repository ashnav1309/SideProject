package com.scm.automation.parallel.ashnav;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

import com.scm.automation.parallel.ashnav.commonutils.SqlMethods;
import com.scm.automation.parallel.ashnav.macros.ConstantLiterals;
import com.scm.automation.parallel.ashnav.userutilities.GeneralUtils;
import com.scm.automation.parallel.ashnav.utilities.ConnectToGSheet;
import com.scm.automation.parallel.ashnav.utilities.ConnectToProperty;
import com.scm.automation.parallel.ashnav.utilities.PostRequests;

import repositories.SQLRepository;

public class CreateTestOrder {

	private ArrayList<Object[]> updateDataToGSheet = new ArrayList<>();
	private Map<Integer, String> requestPayloads = new HashMap<>();
	private String environment, api_url, auth_url, auth_api, create_order_api, orderId_series;

	public CreateTestOrder() throws IOException {
		ConnectToProperty connectToOrderFlowProp = new ConnectToProperty("orderFlowApi");
		ConnectToProperty connectToDatabaseProp = new ConnectToProperty("database");
		environment 						= connectToDatabaseProp.getPropertyValue("environment");
		api_url 							= connectToOrderFlowProp.getPropertyValue("NewOrderFlowApiURL");
		auth_url 							= connectToOrderFlowProp.getPropertyValue("NewOrderFlowAuthURL");
		auth_api            				= connectToOrderFlowProp.getPropertyValue("AuthPath");
		create_order_api 					= connectToOrderFlowProp.getPropertyValue("CreateOrderInterceptor");
		orderId_series	 					= connectToOrderFlowProp.getPropertyValue("orderId_series");
	}


	private void setCellValue(String value, int row) {
		environment 		= environment.toLowerCase();
		environment 		= Character.toUpperCase(environment.charAt(0))+ environment.substring(1);
		String columnName 	= environment+"_OrderID";
		String range 		= ConstantLiterals.RequestPayload_OrderId.replace("$", Integer.toString(row));
		Object[] object		= ConnectToGSheet.getHeaderIndexFromColumnName(columnName);
		range 				= range.replace("@", (String)object[1]);

		try {
			String cellValue = ConnectToGSheet.getCellValue(row,(Integer)object[0]).toString();
			cellValue = cellValue.trim();
			if(cellValue.isEmpty()) {
				cellValue = value;
			}
			else {
				cellValue = cellValue+", "+value;
			}
			ConnectToGSheet.setCellValue(ConstantLiterals.Created_Order_WorkBook, range, cellValue);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Exception caught while setting value in google sheet");
		}
	}

	public Map<Integer, String> setup() {
		try {
			ConnectToGSheet.initGoogleWorkBook();
			ConnectToGSheet.initGoogleSheet(ConstantLiterals.Created_Order_WorkBook, ConstantLiterals.RequestPayload_sheetRange, ConstantLiterals.MajorDimension_Row);
			int size = ConnectToGSheet.getGoogleSheetRowsSize();
			int count = 1;
			while(count <= size) {
				String toCreate = (String)ConnectToGSheet.getCellValue(count,3);
				if(toCreate.equalsIgnoreCase("yes")) {
					requestPayloads.put(count, (String)ConnectToGSheet.getCellValue(count,2));
				}
				count++;
			}
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		return requestPayloads;
	}

	public void createTestOrdersFromGSheet(int row, String requestPayload) {
		Boolean condition 	= false;
		String orderId 		= null;
		String incrementId 	= null;
		int increment 		= 1;

		try {
			do {
				String incrementIdStr = SqlMethods.getColumnValue(SQLRepository.getQuery(SQLRepository.max_incrementId,orderId_series,orderId_series), "increment_id", 0);
				if(incrementIdStr == null || incrementIdStr.isEmpty() || incrementIdStr.equals("null"))
				{
					Assert.fail("IncrementId is null or empty");
				}
				else {
					incrementId = Integer.toString(Integer.parseInt(incrementIdStr.trim()) + increment);
				}
				orderId = SqlMethods.getColumnValue(SQLRepository.getQuery(SQLRepository.get_orderId_by_incrementId,incrementId,incrementId), "order_id", 0);
				increment++;
			}while(orderId !=null && ! orderId.isEmpty());

			orderId = incrementId;

			JSONObject requestObject 	= new JSONObject(requestPayload);
			requestObject.put("order_id",orderId);

			HashMap<String,Integer> line_item_ids_map 	= new HashMap<>();
			JSONArray list_items 						= requestObject.getJSONArray("line_items");
			for(int count =  0;count < list_items.length();count++) {
				JSONObject item = (JSONObject)list_items.get(count);
				if(item.getString("item_type").equalsIgnoreCase("EYEFRAME")) {
					item.put("parent_line_item_id",0);
					String oldlineItemId 	= item.get("line_item_id").toString();
					int newlineItemId 		= GeneralUtils.getRandomNo();
					line_item_ids_map.put(oldlineItemId,newlineItemId);
					item.put("line_item_id", newlineItemId);
				}
				else {
					String parent_item_id 	= item.get("parent_line_item_id").toString();
					if(parent_item_id!=null) {
						item.put("parent_line_item_id",line_item_ids_map.get(parent_item_id));
					}
					item.put("line_item_id",GeneralUtils.getRandomNo());
				}
			}
			String RequestUrl= api_url + create_order_api; 
			RequestUrl=RequestUrl.replace("INCREMENT_ID",incrementId); //Creates interceptor's request URL for order creation
			String token = GeneralUtils.getAccessToken(auth_url, auth_api); // Gets resources access token from auth server
			List<NameValuePair> header = new ArrayList<NameValuePair>();
			header.add(new BasicNameValuePair("Content-Type", "application/json")); 
			header.add(new BasicNameValuePair("Authorization", "Bearer "+token));
			HttpResponse response = PostRequests.postJSON_Request(RequestUrl,header,requestObject); //Makes POST request to interceptor's createOrder api
			condition = response.getStatusLine().getStatusCode() == 202;
		} 
		catch (IOException | URISyntaxException | JSONException e) {
			e.printStackTrace();
			incrementId = e.getMessage()+" "+e.getClass();
		}

		Object[] lineData = new Object[3];
		lineData[0] = incrementId;
		lineData[1]	= row;
		if (condition) {
			System.out.println("\nIncrement_id: "+incrementId+"\n");
			updateDataToGSheet.add(lineData);
		}
	}

	public void tearDown() {
		if(!updateDataToGSheet.isEmpty() && updateDataToGSheet != null) {
			for(Object[] createdDatum : updateDataToGSheet) {
				setCellValue((String)createdDatum[0], (Integer)createdDatum[1]);
			}
		}
	}

	public static void main(String [] args) throws SQLException  {
		CreateTestOrder cto;
		try {
			cto = new CreateTestOrder();
			Map<Integer,String> requestPayloads = cto.setup();
			for(Entry<Integer, String> mapEntry : requestPayloads.entrySet()) {
				cto.createTestOrdersFromGSheet(mapEntry.getKey(), mapEntry.getValue()); 
			}
			cto.tearDown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
