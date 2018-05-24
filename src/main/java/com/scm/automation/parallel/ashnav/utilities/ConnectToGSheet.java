package com.scm.automation.parallel.ashnav.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.scm.automation.parallel.ashnav.macros.ConstantLiterals;

public final class ConnectToGSheet {

	private static Logger logger = LogManager.getLogger(ConnectToGSheet.class.getName());
	private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ConstantLiterals.GSHEETCREDENTIALSPATH);
	private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
	private static final List<String> SCOPES = Arrays.asList(SheetsScopes.SPREADSHEETS);
	private static FileDataStoreFactory DATA_STORE_FACTORY;
	private static HttpTransport HTTP_TRANSPORT;
	private static ValueRange response;
	private static Sheets service;


	static {
		try {
			HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
			DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
		} 
		catch (Throwable t) {
			System.exit(1);
		}
	}

	/**
	 * Creates an authorized Credential object.
	 * @return an authorized Credential object.
	 * @throws IOException
	 */
	private static Credential authorize() throws IOException {
		InputStream in = ConnectToGSheet.class.getResourceAsStream("/client_secret.json");
		GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
				.setDataStoreFactory(DATA_STORE_FACTORY)
				.setAccessType("offline")
				.build();

		Credential credential = new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
		return credential;
	}

	/**
	 * Build and return an authorized Sheets API client service.
	 * @return an authorized Sheets API client service
	 * @throws IOException
	 */
	private static Sheets getSheetsService() throws IOException {
		Credential credential;
		credential = authorize();
		return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName(ConstantLiterals.APPLICATION_NAME)
				.build();
	}

	private static List<List<Object>> setValue(Object value) {
		return Arrays.asList(Arrays.asList(value));
	}

	/**
	 * Build and return values of the cells defined in the range.
	 * @return a list of list of objects
	 * @throws IOException
	 */
	public static void initGoogleWorkBook() throws IOException {
		service = getSheetsService();
		if(service != null) {
			logger.info("Google workbook initialized: {}",service.getApplicationName());
		}
	}

	public static void initGoogleSheet(String spreadsheetId, String range, String majorDimensions) throws IOException {
		response = service.spreadsheets()
				.values()
				.get(spreadsheetId, range)
				.setMajorDimension(majorDimensions)
				.execute();
		logger.info("Google sheet initialized: {}",range);

	}

	public static Object getCellValue(int row, int column) {
		try {
			if(row == 0 || column == 0) {
				return "";
			}
			return response.getValues().get(row-1).get(column-1);
		}
		catch (IndexOutOfBoundsException e) {
			logger.error("IGNORE THIS EXCEPTION:IndexOutOfBoundsException exception in ConnectToGSheet.getCellValue");
			return "";
		}
	}

	public static List<String> getCellRangeFromValue(String value) {
		
		List<String> foundAt = new ArrayList<>();
		if(value != null && !value.isEmpty()) {
			value = value.toLowerCase();
			int rowSize = getGoogleSheetRowsSize();
			for(int x = 1 ; x <= rowSize; x++) {
				int columnSize = getGoogleSheetColumnsSize(x);
				System.out.printf("Row %d has %d columns\n",x,columnSize);
				int count = 1;
				while(count <= columnSize) {
					String columnValue = (String)getCellValue(x, count);
					columnValue = columnValue.toLowerCase();
					if(columnValue.contains(value)) {
						String cellRange;
						cellRange = Character.toString ((char) (count+64));
						cellRange = cellRange+x;
						foundAt.add(cellRange);
					}
					count++;
				}
			}
		}
		return foundAt;
	}

	public static int getGoogleSheetRowsSize() {
		try{
			return response.getValues().size();
		}
		catch (IndexOutOfBoundsException e) {
			logger.error("IGNORE THIS EXCEPTION:IndexOutOfBoundsException exception in ConnectToGSheet.getCellValue");
			return 0;
		}
		catch (NullPointerException e) {
			logger.error("IGNORE THIS EXCEPTION:NullPointerException exception in ConnectToGSheet.getCellValue");
			return 0;
		}
	}

	public static int getGoogleSheetColumnsSize(int row) {
		try {
			return response.getValues().get(row-1).size();
		}
		catch (IndexOutOfBoundsException e) {
			logger.error("IGNORE THIS EXCEPTION:IndexOutOfBoundsException exception in ConnectToGSheet.getCellValue");
			return 0;
		}
		catch (NullPointerException e) {
			logger.error("IGNORE THIS EXCEPTION:NullPointerException exception in ConnectToGSheet.getCellValue");
			return 0;
		}
	}

	public static void setCellValue(String spreadsheetId, String cell, String value) throws IOException {
		ValueRange body = new ValueRange().setValues(setValue(value));
		UpdateValuesResponse result = service.spreadsheets().values().update(spreadsheetId, cell, body).setIncludeValuesInResponse(Boolean.TRUE).setValueInputOption("RAW").execute();
		logger.info(result.getUpdatedRange()+" updated with "+result.getUpdatedData().getValues());
	}

	public static Object[] getHeaderIndexFromColumnName(String columnName) {
		int columnSize = getGoogleSheetColumnsSize(1);
		Object[] object = new Object[2];
		for(int count = 1 ; count <= columnSize; count++) {
			String headerName = (String)getCellValue(1, count);
			headerName = headerName.toLowerCase();
			if(headerName.equalsIgnoreCase(columnName)) {
				object[0] = count;
				object[1] = Character.toString ((char) (count+64));
				break;
			}	
		}
		return object;
	}
}

