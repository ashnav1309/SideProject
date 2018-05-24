package com.scm.automation.parallel.ashnav;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrServerException;

import com.scm.automation.parallel.ashnav.macros.ConstantLiterals;
import com.scm.automation.parallel.ashnav.macros.ConstantLiterals.Solr_fieldName;
import com.scm.automation.parallel.ashnav.test_order.SolrUtils_test_Orders_Core;
import com.scm.automation.parallel.ashnav.utilities.ConnectToGSheet;

public class test {

	public static void main (String [] args) throws SolrServerException, IOException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException {

		//		List<Object> ids = SolrUtils_test_Orders_Core.getBeansFromSolr_test_Orders_Core(Solr_fieldName.created_at, "is_unicomSynced:false", "is_vsmSynced:true");
		//		for(Object id : ids) {
		//			System.out.println(id);
		//		}

		ConnectToGSheet.initGoogleWorkBook();
		ConnectToGSheet.initGoogleSheet(ConstantLiterals.Created_Order_WorkBook, ConstantLiterals.RequestPayload_sheetRange, ConstantLiterals.MajorDimension_Row);
		List<String> foundAts = ConnectToGSheet.getCellRangeFromValue("Yes");
		for(String foundAt : foundAts) {
			System.out.println(foundAt);
		}

		//		TestOrderBean testOrderBean1 = SolrUtils_test_Orders_Core.getBeansFromSolr_test_Orders_Core("increment_id:13").get(0);
		//		TestOrderBean testOrderBean2 = SolrUtils_test_Orders_Core.getBeansFromSolr_test_Orders_Core("increment_id:13").get(0);
		//		Map<Solr_fieldName, Object> testBeanParams = new HashMap<>();
		//		testBeanParams.put(Solr_fieldName.is_delivered, Boolean.TRUE);
		//		testBeanParams.put(Solr_fieldName.log_delivered, "15-Hello");
		//		testBeanParams.put(Solr_fieldName.is_e2eFlowed, Boolean.TRUE);
		//		try {
		//			TestOrderBean.setTestOrderBean(testBeanParams, testOrderBean1);
		//			System.out.println("\n\n\n\n\n\n\n\n");
		//			System.out.println("Bean 1:"+testOrderBean1.toString());
		//			System.out.println("\n\n\n");
		//			System.out.println("Bean 2:"+testOrderBean2.toString());
		//			System.out.println("\n\n\n");
		//			System.out.println(testOrderBean2.equals(testOrderBean1));
		//		} catch (IllegalArgumentException | InvocationTargetException e) {
		//			e.printStackTrace();
		//		}
	}
}


