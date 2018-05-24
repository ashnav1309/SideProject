package com.scm.automation.parallel.ashnav.macros;

public final class ConstantLiterals {

	private ConstantLiterals(){};

	public enum Solr_fieldName {
		id, environment, increment_id, shipping_package_id, uw_item_id, product_type,
		product_barcode, product_warehouse_type, product_prescription_Llens, 
		product_prescription_Rlens, is_multiShipment, is_multiLine, is_vsmSynced,
		is_unicomSynced, is_picked, is_fitted, is_qcChecked, is_shipped, is_delivered,
		is_e2eFlowed, log_vsmSynced, log_unicomSynced, log_picked, log_fitted, log_qcChecked,
		log_shipped, log_delivered, log_e2eFlowed, created_at, updated_at
	}

	public static final String testOrder_SolrHostName						= "127.0.0.1";
	public static final int    testOrder_SolrPort							= 8983;
	public static final String testOrder_SolrCore							= "test_Orders";
	public static final String testOrder_UpdateSolrUrl						= "http://"+testOrder_SolrHostName+":"+testOrder_SolrPort+"/solr/"+testOrder_SolrCore+"/update/?commit=true";
	
	public static final String Localhost 			 						= "127.0.0.1";
	public static final int    RPort 				 						= 3306;
	public static final int    SSHPort 			 	 						= 22;
	public static final int    timeOut 				 						= 0;

	public static final String MajorDimension_Column 						= "COLUMNS";
	public static final String MajorDimension_Row 	 						= "ROWS";
	public static final String ResourcesPath 		 						= "src/main/resources/";
	public static final String APPLICATION_NAME 	 						= "Created_Order_WorkBook";
	public static final String GSHEETCREDENTIALSPATH 						= ".credentials/sheets.googleapis.com-orderState";
	
	public static final String RequestPayload_OrderId 						= "Request_Payload!@$";
	public static final String RequestPayload_sheetRange 					= "Request_Payload!A1:I";
	public static final String RequestPayload_OrderId_sheetRange 			= "Request_Payload!D1:I";
	public static final String Created_Order_WorkBook 						= "1ppbqQhNDplcH906K3oLFeLYd2d1jfQizfdr02DqpgeQ";

}
