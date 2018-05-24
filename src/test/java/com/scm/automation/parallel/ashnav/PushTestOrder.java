package com.scm.automation.parallel.ashnav;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.scm.automation.parallel.ashnav.commonutils.SqlMethods;
import com.scm.automation.parallel.ashnav.macros.ConstantLiterals;
import com.scm.automation.parallel.ashnav.test_order.SolrUtils_test_Orders_Core;
import com.scm.automation.parallel.ashnav.test_order.TestOrderBean;
import com.scm.automation.parallel.ashnav.utilities.ConnectToGSheet;

import repositories.SQLRepository;

public class PushTestOrder {

	private static Logger logger = LogManager.getLogger(PushTestOrder.class.getName());

	private HashMap<String,String> dataFromGSheet = new HashMap<>();
	private ArrayList<String[]> dataToPushToSolr = new ArrayList<>();
	String increment_id, product_id, uw_item_id, environment="preprod";

	public HashMap<String, String> setup() {

		try {
			ConnectToGSheet.initGoogleWorkBook();
			ConnectToGSheet.initGoogleSheet(ConstantLiterals.Created_Order_WorkBook, ConstantLiterals.RequestPayload_OrderId_sheetRange, ConstantLiterals.MajorDimension_Row);
			int sizeRows = ConnectToGSheet.getGoogleSheetRowsSize();
			for(int x=2; x<=sizeRows; x++) {
				int sizeColumns = ConnectToGSheet.getGoogleSheetColumnsSize(x);
				int count = 1;
				while(count<=sizeColumns) {
					String headerName  	= (String)ConnectToGSheet.getCellValue(1, count);
					String increment_id = (String)ConnectToGSheet.getCellValue(x,count);
					count++;
					if(!increment_id.isEmpty() && increment_id != null) {
						if(dataFromGSheet.containsKey(headerName)) {
							String increment_idNew = dataFromGSheet.get(headerName)+", "+increment_id;
							dataFromGSheet.put(headerName, increment_idNew);
						}
						else {
							dataFromGSheet.put(headerName, increment_id);
						}
					}
				}
			}
		}		
		catch (IOException e) {
			e.printStackTrace();
		}
		logger.info("Fetched data from Google Sheet: >{}<",dataFromGSheet.toString());
		return dataFromGSheet;
	}

	public void pushTestOrderToSolr(String environment, String perLineIncrement_ids) {
		if(perLineIncrement_ids != null && !perLineIncrement_ids.isEmpty()) {
			String[] increment_ids = perLineIncrement_ids.split(", ");
			for(String increment_idStr : increment_ids) {
				List<Map<String, String>> values;
				values = SqlMethods.getResultAsListOfMap(SQLRepository.getQuery(SQLRepository.get_incrementId_productId_uw_item_id, increment_idStr));
				if(values != null && !values.isEmpty()) {
					for(Map<String, String> value : values) {
						String[] key = new String[4];
						key[0] = value.get("increment_id");
						key[1] = value.get("product_id");
						key[2] = value.get("uw_item_id");
						key[3] = environment;
						dataToPushToSolr.add(key);
					}
				}
			}
		}
	}

	public void tearDown() {
		if(!dataToPushToSolr.isEmpty() && dataToPushToSolr != null) {
			for(String[] testOrder : dataToPushToSolr) {
				TestOrderBean testOrderBean = new TestOrderBean(testOrder[0], testOrder[1], testOrder[2], testOrder[3]);
				testOrderBean.setIs_vsmSynced(true);
				testOrderBean.setLog_vsmSynced("Entry present in UW_Orders");
				SolrUtils_test_Orders_Core.sendToSolr_test_Orders_Core(testOrderBean);
			}
		}
	}

	public static void main(String[] args) {

		PushTestOrder pto;
		pto = new PushTestOrder();
		HashMap<String, String> perLineIncrement_idsMap = pto.setup();
		logger.info("START-finding data for test_Orders core");
		for(Entry<String, String> entryMap : perLineIncrement_idsMap.entrySet()) {
			pto.pushTestOrderToSolr(entryMap.getKey(), entryMap.getValue());
		}
		logger.info("END-finding data for test_Orders core");
		pto.tearDown();
	}
}
