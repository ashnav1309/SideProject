package com.scm.automation.parallel.ashnav.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.params.MapSolrParams;

public class ConnectToSolr {

	private static Logger logger = LogManager.getLogger(ConnectToSolr.class.getName());

	private SolrClient solrClient;
	private StringBuilder uri;
	private String solrHost;
	private String coreName;
	private int solrPort;

	public ConnectToSolr(String solrHost, int solrPort, String coreName) {
		this.solrHost = solrHost;
		this.solrPort = solrPort;
		this.coreName = coreName;
	}

	public void createSolrConnection() {	
		uri = new StringBuilder("http://");
		uri.append(solrHost).append(":").append(solrPort).append("/solr/");
		logger.info("Connecting solr at: {}",uri.toString());
		solrClient = new HttpSolrClient.Builder(uri.toString()).build();
	}
	
	public void closeSolrConnection() throws IOException {	
		logger.info("Closing solr at: {}",uri.toString());
		if(solrClient != null) 
		solrClient.close();
	}

	public void setFieldValueFromBean(Object object) throws SolrServerException, IOException {
		logger.info("Setting bean: "+object.toString());
		final UpdateResponse response = solrClient.addBean(coreName, object);
		logger.info("setFieldValue method's response: "+response.getStatus());
		solrClient.commit(coreName);
	}

	public <T> List<T> getFieldValueToBean(Map<String, String> queryParamMap, Class<T> className) throws SolrServerException, IOException {
		List<T> values = new ArrayList<>();
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);
		QueryResponse response;
		response = solrClient.query(coreName, queryParams);
		values = response.getBeans(className);
		logger.info("Getting bean(s): "+values.toString());
		return values;
	}

	public List<String> getFieldValue(Map<String, String> queryParamMap) throws SolrServerException, IOException {
		List<String> values = new ArrayList<>();
		MapSolrParams queryParams = new MapSolrParams(queryParamMap);
		final String fieldName = queryParamMap.get("fl");
		QueryResponse response;
		response = solrClient.query(coreName, queryParams);
		List<SolrDocument> documents = response.getResults();
		logger.info("Getting field values: "+documents.toString());
		for(SolrDocument document : documents) {
			values.add((String) document.getFirstValue(fieldName));	
		}
		return values;
	}
}