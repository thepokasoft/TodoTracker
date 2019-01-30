package com.poka.todotracker.sheets;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.common.util.concurrent.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoogleSheetsAPI {

    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws IOException {
        // Load client secrets.
        InputStream in = GoogleSheetsAPI.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

	
    
    private static Sheets getService() throws GeneralSecurityException, IOException {
    	final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
		return service;
    }
    
    final String spreadsheetId = "1_vsCdrTGP7bkCwtSCeNXNoUwgSPd-WpUstP9L4OucIM";
    public List<List<Object>> getValues(String range) throws IOException, GeneralSecurityException{
        //final String spreadsheetId = "1_vsCdrTGP7bkCwtSCeNXNoUwgSPd-WpUstP9L4OucIM";
        //final String range = "Sheet1!A2:E";
    	 ValueRange response = getService().spreadsheets().values()
                 .get(spreadsheetId, range)
                 .execute();
		return response.getValues();
    }
    
    public boolean putValue(Map<String, String> sheetValuesMap) throws IOException, GeneralSecurityException {
    	
		Iterable<String> cells = sheetValuesMap.keySet();
		List<UpdateValuesResponse> updateValueResponses = new ArrayList<>();
		UpdateValuesResponse writeResponse;
		String valueInputOption = "RAW";

		for (String cell : cells) {
			String range1 = cell;
			String value = sheetValuesMap.get(cell);
			List<Object> valueList = new ArrayList<>();
			valueList.add(value);

			List<List<Object>> values = new ArrayList<>();
			values.add(valueList);

			ValueRange body = new ValueRange().setValues(values);

			AppendValuesResponse appendValuesResponse  = getService().spreadsheets().values().append(spreadsheetId, range1, body)
					.setValueInputOption(valueInputOption)
					.setInsertDataOption("INSERT_ROWS")
					 .execute();
			System.out.println(appendValuesResponse);
			//updateValueResponses.add(appendValuesResponse);
		}
    	return false;
    }
    
public boolean putValues(String range, ValueRange body) throws IOException, GeneralSecurityException {
    	
		String valueInputOption = "USER_ENTERED";	

			AppendValuesResponse appendValuesResponse  = getService().spreadsheets().values().append(spreadsheetId, range, body)
					.setValueInputOption(valueInputOption)
					.setInsertDataOption("INSERT_ROWS")
					 .execute();
			System.out.println(appendValuesResponse);
    	return false;
    }
		

	}

