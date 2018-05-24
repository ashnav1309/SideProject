package com.scm.automation.parallel.ashnav.test_order;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import org.apache.solr.client.solrj.beans.Field;

import com.scm.automation.parallel.ashnav.macros.ConstantLiterals.Solr_fieldName;

public class TestOrderBean {

	private static final long serialVersionUID = 1L;

	@Field private String id;
	@Field private String environment;
	@Field private String increment_id;
	@Field private String shipping_package_id;
	@Field private String uw_item_id;
	@Field private String product_id;
	@Field private String product_type;
	@Field private String product_barcode;
	@Field private String product_warehouse_type;
	@Field private String product_prescription_Llens;
	@Field private String product_prescription_Rlens;
	@Field private Boolean is_multiShipment;
	@Field private Boolean is_multiLine;
	@Field private Boolean is_vsmSynced;
	@Field private Boolean is_unicomSynced;
	@Field private Boolean is_picked;
	@Field private Boolean is_fitted;
	@Field private Boolean is_qcChecked;
	@Field private Boolean is_shipped;
	@Field private Boolean is_delivered;
	@Field private Boolean is_e2eFlowed;
	@Field private String log_vsmSynced;
	@Field private String log_unicomSynced;
	@Field private String log_picked;
	@Field private String log_fitted;
	@Field private String log_qcChecked;
	@Field private String log_shipped;
	@Field private String log_delivered;
	@Field private String log_e2eFlowed;
	@Field private Date created_at;
	@Field private Date updated_at;

	public TestOrderBean () {
	}

	public TestOrderBean (String id) {
		this.id 								 = id;
		String [] incrementId_productId_parentUW = id.split("-");
		if(incrementId_productId_parentUW.length == 3) { 
			this.increment_id	 	= incrementId_productId_parentUW[0];
			this.product_id   		= incrementId_productId_parentUW[1];
			this.uw_item_id 		= incrementId_productId_parentUW[2];
		}
		setCreated_at();
	}

	public TestOrderBean (String increment_id, String product_id, String uw_item_id, String environment) {
		setId(increment_id, product_id, uw_item_id);
		this.environment = environment;
		this.increment_id = increment_id;
		this.uw_item_id = uw_item_id;
		this.product_id = product_id;
		setCreated_at();
	}

	public void setId(String increment_id, String product_id, String uw_item_id) {
		this.id =increment_id+"-"+product_id+"-"+uw_item_id;
	} 

	public String getId() {
		return id;
	}

	public String getIncrement_id() {
		return increment_id;
	}

	public String getUw_item_id() {
		return uw_item_id;
	}

	public String getProduct_id() {
		return product_id;
	}

	public String getShipping_package_id() {
		return shipping_package_id;
	}

	public void setShipping_package_id(String shipping_package_id) {
		this.shipping_package_id = shipping_package_id;
		setUpdated_at();
	}

	public String getProduct_type() {
		return product_type;
	}

	public void setProduct_type(String product_type) {
		this.product_type = product_type;
		setUpdated_at();
	}
	
	public String getEnvironment() {
		return environment;
	}

	public String getProduct_barcode() {
		return product_barcode;
	}

	public void setProduct_barcode(String product_barcode) {
		this.product_barcode = product_barcode;
		setUpdated_at();
	}

	public String getProduct_warehouse_type() {
		return product_warehouse_type;
	}

	public void setProduct_warehouse_type(String product_warehouse_type) {
		this.product_warehouse_type = product_warehouse_type;
		setUpdated_at();
	}

	public String getProduct_prescription_Llens() {
		return product_prescription_Llens;
	}

	public void setProduct_prescription_Llens(String product_prescription_Llens) {
		this.product_prescription_Llens = product_prescription_Llens;
		setUpdated_at();
	}

	public String getProduct_prescription_Rlens() {
		return product_prescription_Rlens;
	}

	public void setProduct_prescription_Rlens(String product_prescription_Rlens) {
		this.product_prescription_Rlens = product_prescription_Rlens;
		setUpdated_at();
	}

	public Boolean getIs_multiShipment() {
		return is_multiShipment;
	}

	public void setIs_multiShipment(Boolean is_multiShipment) {
		this.is_multiShipment = is_multiShipment;
		setUpdated_at();
	}

	public Boolean getIs_multiLine() {
		return is_multiLine;
	}

	public void setIs_multiLine(Boolean is_multiLine) {
		this.is_multiLine = is_multiLine;
		setUpdated_at();
	}

	public Boolean getIs_vsmSynced() {
		return is_vsmSynced;
	}

	public void setIs_vsmSynced(Boolean is_vsmSynced) {
		this.is_vsmSynced = is_vsmSynced;
		setUpdated_at();
	}

	public Boolean getIs_unicomSynced() {
		return is_unicomSynced;
	}

	public void setIs_unicomSynced(Boolean is_unicomSynced) {
		this.is_unicomSynced = is_unicomSynced;
		setUpdated_at();
	}

	public Boolean getIs_picked() {
		return is_picked;
	}

	public void setIs_picked(Boolean is_picked) {
		this.is_picked = is_picked;
		setUpdated_at();
	}

	public Boolean getIs_fitted() {
		return is_fitted;
	}

	public void setIs_fitted(Boolean is_fitted) {
		this.is_fitted = is_fitted;
		setUpdated_at();
	}

	public Boolean getIs_qcChecked() {
		return is_qcChecked;
	}

	public void setIs_qcChecked(Boolean is_qcChecked) {
		this.is_qcChecked = is_qcChecked;
		setUpdated_at();
	}

	public Boolean getIs_shipped() {
		return is_shipped;
	}

	public void setIs_shipped(Boolean is_shipped) {
		this.is_shipped = is_shipped;
		setUpdated_at();
	}

	public Boolean getIs_delivered() {
		return is_delivered;
	}

	public void setIs_delivered(Boolean is_delivered) {
		this.is_delivered = is_delivered;
		setUpdated_at();
	}

	public Boolean getIs_e2eFlowed() {
		return is_e2eFlowed;
	}

	public void setIs_e2eFlowed(Boolean is_e2eFlowed) {
		this.is_e2eFlowed = is_e2eFlowed;
		setUpdated_at();
	}

	public String getLog_vsmSynced() {
		return log_vsmSynced;
	}

	public void setLog_vsmSynced(String log_vsmSynced) {
		this.log_vsmSynced = log_vsmSynced;
		setUpdated_at();
	}

	public String getLog_unicomSynced() {
		return log_unicomSynced;
	}

	public void setLog_unicomSynced(String log_unicomSynced) {
		this.log_unicomSynced = log_unicomSynced;
		setUpdated_at();
	}

	public String getLog_picked() {
		return log_picked;
	}

	public void setLog_picked(String log_picked) {
		this.log_picked = log_picked;
		setUpdated_at();
	}

	public String getLog_fitted() {
		return log_fitted;
	}

	public void setLog_fitted(String log_fitted) {
		this.log_fitted = log_fitted;
		setUpdated_at();
	}

	public String getLog_qcChecked() {
		return log_qcChecked;
	}

	public void setLog_qcChecked(String log_qcChecked) {
		this.log_qcChecked = log_qcChecked;
		setUpdated_at();
	}

	public String getLog_shipped() {
		return log_shipped;
	}

	public void setLog_shipped(String log_shipped) {
		this.log_shipped = log_shipped;
		setUpdated_at();
	}

	public String getLog_delivered() {
		return log_delivered;
	}

	public void setLog_delivered(String log_delivered) {
		this.log_delivered = log_delivered;
		setUpdated_at();
	}

	public String getLog_e2eFlowed() {
		return log_e2eFlowed;
	}

	public void setLog_e2eFlowed(String log_e2eFlowed) {
		this.log_e2eFlowed = log_e2eFlowed;
		setUpdated_at();
	}

	public Date getCreated_at() {
		return created_at;
	}

	public void setCreated_at() {
		this.created_at = new Date();
	}

	public Date getUpdated_at() {
		return updated_at;
	}

	private void setUpdated_at() {
		this.updated_at = new Date();
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static TestOrderBean setTestOrderBean(Map<Solr_fieldName, Object> testBeanParams, TestOrderBean testOrderBean) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		Class<?>[] paramString = new Class[1];	
		paramString[0] = String.class;
		Class<?>[] paramBoolean = new Class[1];	
		paramBoolean[0] = Boolean.class;

		for (Map.Entry<Solr_fieldName, Object> entry : testBeanParams.entrySet()) {
			Solr_fieldName fieldName = entry.getKey(); 
			Object fieldValue = entry.getValue();
			String _fieldName_ = fieldName.name();
			_fieldName_ = Character.toUpperCase((_fieldName_).charAt(0))+ _fieldName_.substring(1);
			String methodName = "set"+_fieldName_;
			if(fieldValue instanceof String) {
				Method method = TestOrderBean.class.getDeclaredMethod(methodName, paramString);
				method.invoke(testOrderBean, (String)fieldValue);
			}
			else if (fieldValue instanceof Boolean) {
				Method method = TestOrderBean.class.getDeclaredMethod(methodName, paramBoolean);
				method.invoke(testOrderBean, (Boolean)fieldValue);
			}
			else {
				throw new IllegalArgumentException();
			}
		}
		return testOrderBean;
	}

	@Override
	public String toString() {
		return "[{" 
				+ (id != null ? "id:\"" + id + "\",  " : "")
				+ (environment != null ? "environment:\"" + environment + "\",  " : "")
				+ (increment_id != null ? "increment_id:\"" + increment_id + "\",  " : "")
				+ (shipping_package_id != null ? "shipping_package_id:\"" + shipping_package_id + "\",  " : "")
				+ (uw_item_id != null ? "uw_item_id:\"" + uw_item_id + "\",  " : "")
				+ (product_id != null ? "product_id:\"" + product_id + "\",  " : "")
				+ (product_type != null ? "product_type:\"" + product_type + "\",  " : "")
				+ (product_barcode != null ? "product_barcode:\"" + product_barcode + "\",  " : "")
				+ (product_warehouse_type != null ? "product_warehouse_type:\"" + product_warehouse_type + "\",  " : "")
				+ (product_prescription_Llens != null ? "product_prescription_Llens:\"" + product_prescription_Llens + "\",  " : "")
				+ (product_prescription_Rlens != null ? "product_prescription_Rlens:\"" + product_prescription_Rlens + "\",  " : "")
				+ (is_multiShipment != null ? "is_multiShipment:\"" + is_multiShipment + "\",  " : "")
				+ (is_multiLine != null ? "is_multiLine:\"" + is_multiLine + "\",  " : "")
				+ (is_vsmSynced != null ? "is_vsmSynced:\"" + is_vsmSynced + "\",  " : "")
				+ (is_unicomSynced != null ? "is_unicomSynced:\"" + is_unicomSynced + "\",  " : "")
				+ (is_picked != null ? "is_picked:\"" + is_picked + "\",  " : "")
				+ (is_fitted != null ? "is_fitted:\"" + is_fitted + "\",  " : "")
				+ (is_qcChecked != null ? "is_qcChecked:\"" + is_qcChecked + "\",  " : "")
				+ (is_shipped != null ? "is_shipped:\"" + is_shipped + "\",  " : "")
				+ (is_delivered != null ? "is_delivered:\"" + is_delivered + "\",  " : "")
				+ (is_e2eFlowed != null ? "is_e2eFlowed:\"" + is_e2eFlowed + "\",  " : "")
				+ (log_vsmSynced != null ? "log_vsmSynced:\"" + log_vsmSynced + "\",  " : "")
				+ (log_unicomSynced != null ? "log_unicomSynced:\"" + log_unicomSynced + "\",  " : "")
				+ (log_picked != null ? "log_picked:\"" + log_picked + "\",  " : "")
				+ (log_fitted != null ? "log_fitted:\"" + log_fitted + "\",  " : "")
				+ (log_qcChecked != null ? "log_qcChecked:\"" + log_qcChecked + "\",  " : "")
				+ (log_shipped != null ? "log_shipped:\"" + log_shipped + "\",  " : "")
				+ (log_delivered != null ? "log_delivered:\"" + log_delivered + "\",  " : "")
				+ (log_e2eFlowed != null ? "log_e2eFlowed:\"" + log_e2eFlowed + "\",  " : "")
				+ (created_at != null ? "created_at:\"" + created_at.toInstant().toString() + "\",  " : "")
				+ (updated_at != null ? "updated_at:\"" + updated_at.toInstant().toString()+ "\"" : "") + "}]";
	}
}