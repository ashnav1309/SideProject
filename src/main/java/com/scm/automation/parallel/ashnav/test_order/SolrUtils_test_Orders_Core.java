package com.scm.automation.parallel.ashnav.test_order;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrServerException;

import com.scm.automation.parallel.ashnav.macros.ConstantLiterals;
import com.scm.automation.parallel.ashnav.macros.ConstantLiterals.Solr_fieldName;
import com.scm.automation.parallel.ashnav.utilities.ConnectToSolr;

public class SolrUtils_test_Orders_Core {

	private static Logger logger = LogManager.getLogger(SolrUtils_test_Orders_Core.class);

	public static void updateToSolr_test_Orders_Core(Map<Solr_fieldName, Object> testBeanParams, TestOrderBean testOrderBean) {
		logger.trace("Inside updateToSolr_test_Orders_Core()");
		try {
			TestOrderBean testOrderBeanObject = TestOrderBean.setTestOrderBean(testBeanParams, testOrderBean);
			sendToSolr_test_Orders_Core(testOrderBeanObject);
		} 
		catch (NoSuchMethodException | SecurityException | IllegalArgumentException | InvocationTargetException | IllegalAccessException e) {
			e.printStackTrace();
		}
		logger.trace("Outside updateToSolr_test_Orders_Core()");
	}

	public static void sendToSolr_test_Orders_Core(TestOrderBean testOrderBean) {
		logger.trace("Inside setToSolr_test_Orders_Core()");
		ConnectToSolr connectToSolr = new ConnectToSolr(ConstantLiterals.testOrder_SolrHostName, ConstantLiterals.testOrder_SolrPort, ConstantLiterals.testOrder_SolrCore);
		connectToSolr.createSolrConnection();
		try {
			connectToSolr.setFieldValueFromBean(testOrderBean);
		} 
		catch (IOException | SolrServerException e) {
			e.printStackTrace();
		}
		finally {
			try {
				connectToSolr.closeSolrConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("Updating to Solr: {}", testOrderBean.toString());
		logger.trace("Outside setToSolr_test_Orders_Core()");
	}

	public static List<TestOrderBean> getBeansFromSolr_test_Orders_Core(String... searchQuery) {
		logger.trace("Inside getBeanFromSolr_test_Orders_Core()");
		ConnectToSolr connectToSolr = new ConnectToSolr(ConstantLiterals.testOrder_SolrHostName, ConstantLiterals.testOrder_SolrPort, ConstantLiterals.testOrder_SolrCore);
		connectToSolr.createSolrConnection();

		List<TestOrderBean> values = new ArrayList<>();
		try {
			Map<String, String> queryParamMap = new HashMap<>();
			queryParamMap.put("q", "*:*");
			if(searchQuery != null) {
				for (String searchqry : searchQuery) {
					queryParamMap.put("fq", searchqry);
				}
			}
			values = connectToSolr.getFieldValueToBean(queryParamMap, TestOrderBean.class);
		} 
		catch (SolrServerException | IOException | SecurityException | IllegalArgumentException  e) {
			e.printStackTrace();
		} 
		finally {
			try {
				connectToSolr.closeSolrConnection();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("Getting from Solr: {}", values.toString());
		logger.trace("Outside getBeanFromSolr_test_Orders_Core()");
		return values;
	}

	public static List<Object> getBeansFromSolr_test_Orders_Core(Solr_fieldName solrFieldName, String... searchQuery) {
		logger.trace("Inside getFromSolr_test_Orders_Core()");
		ConnectToSolr connectToSolr = new ConnectToSolr(ConstantLiterals.testOrder_SolrHostName, ConstantLiterals.testOrder_SolrPort, ConstantLiterals.testOrder_SolrCore);
		connectToSolr.createSolrConnection();

		List<TestOrderBean> values = new ArrayList<>();
		List<Object> fieldValue = new ArrayList<>();
		try {
			Map<String, String> queryParamMap = new HashMap<>();
			queryParamMap.put("q", "*:*");
			if(searchQuery != null) {
				for (String searchqry : searchQuery) {
					queryParamMap.put("fq", searchqry);
				}
			}
			values = connectToSolr.getFieldValueToBean(queryParamMap, TestOrderBean.class);
			for(TestOrderBean value : values) {
				String fieldName = solrFieldName.name();
				String _fieldName_ = Character.toUpperCase(fieldName.charAt(0))+ fieldName.substring(1);
				String methodName = "get"+_fieldName_;
				Method method = TestOrderBean.class.getDeclaredMethod(methodName);
				Object object = method.invoke(value);
				fieldValue.add(object);
			}
		} 
		catch (SolrServerException | IOException | SecurityException | IllegalArgumentException | NoSuchMethodException | IllegalAccessException | InvocationTargetException  e) {
			e.printStackTrace();
		} 
		finally {
			try {
				connectToSolr.closeSolrConnection();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		logger.info("Getting from Solr: {}", values.toString());
		logger.trace("Outside getFromSolr_test_Orders_Core()");
		return fieldValue;
	}
}
