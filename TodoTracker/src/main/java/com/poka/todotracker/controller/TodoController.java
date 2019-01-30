package com.poka.todotracker.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.services.sheets.v4.model.ValueRange;
import com.poka.todotracker.sheets.GoogleSheetsAPI;

@RestController
public class TodoController {

	GoogleSheetsAPI gs = new GoogleSheetsAPI();

	@GetMapping("/daily")
	public String getDailyData() {
		final String range = "Sheet1!A1:E14";

		List<List<Object>> values;
		try {
			values = gs.getValues(range);
			if (values == null || values.isEmpty()) {
				System.out.println("No data found.");
			} else {
				System.out.println("Name, Major");
				for (List row : values) {
					// Print columns A and E, which correspond to indices 0 and 4.
					System.out.printf("%s, %s, %s, %s, \n", row.get(0), row.get(1), row.get(2), row.get(3));
				}
			}
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	@GetMapping("/data")
	public String putData() {
		String range = "Sheet1!A1:E1";
		ValueRange body = new ValueRange()
			      .setValues(Arrays.asList(
			        Arrays.asList("Expenses January","Dhyanesh","Dhyanesh","Dhyanesh","Dhyanesh","=IF(A1 =\"\", \" \", now())")
			       ));
		
		try {
			gs.putValues(range,body);
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

}
