package com.scm.automation.parallel.ashnav.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientException;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class ConnectToMongo {

	private static Logger logger = LogManager.getLogger(ConnectToMongo.class.getName());

	private MongoClient mongoClient 				= null;
	private MongoCollection<Document> collection 	= null;
	private String mongoHost 						= null;
	private String mongoDatabase        			= null;
	private String mongoCollection					= null;
	private String url								= null;
	private int mongoPort 							= 0;

	public ConnectToMongo(String mongoHost, int mongoPort, String mongoDatabase, String mongoCollection) {
		this.mongoHost 			= mongoHost;
		this.mongoPort 			= mongoPort;
		this.mongoDatabase		= mongoDatabase;
		this.mongoCollection	= mongoCollection;
	}

	public void createMongoConnection() throws MongoClientException{
		url			 			= "mongodb://"+mongoHost+":"+mongoPort;
		mongoClient 			= new MongoClient(new MongoClientURI(url));
		MongoDatabase database 	= mongoClient.getDatabase(mongoDatabase);
		collection 				= database.getCollection(mongoCollection);
		if(collection.count() < 1) {
			destroyMongoConnection();
			throw new MongoClientException("Mongo collection has no data");
		}
		logger.info("Mongo connection {} established", url);
	}

	public void destroyMongoConnection() {
		if(mongoClient != null) {
			mongoClient.close();
		}
		logger.info("Mongo connection {} destroyed", url);
	}

	public void getResultSet() {
		FindIterable<Document> documents = collection.find();
		for(Document document : documents) {
			logger.info(document);
		}
	}
}
