package com.scm.automation.parallel.ashnav;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;

public class Hup {

	public static void main	(String [] args) throws ParseException {
		
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY'-'MM'-'dd'T'HH:mm:ss");
		System.out.println(new Date().toInstant());
	}
}