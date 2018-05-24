package com.scm.automation.parallel.ashnav.utilities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommonMethods {

	private static Logger logger = LogManager.getLogger(CommonMethods.class.getName());

	public static List<Object> removeDuplicateRows(List<String> list) {
		Set<String> dataSet = new HashSet<>(list);
		logger.info("{} total record(s)\n", list.size());
		logger.info("{} unique record(s)\n", dataSet.size());
		return dataSet.stream().collect(Collectors.toList());
	}
}
