package com.scm.automation.parallel.ashnav.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class ConnectToServer {

	private static Logger logger = LogManager.getLogger(ConnectToServer.class.getName());

	private Session session 	= null;
	private int LPort 			= 0;
	private int RPort 			= 0;
	private int SSHPort 		= 0;
	private String RHost 		= null;
	private String SSHUser, SSHHost, SSHPassword, environment;

	public ConnectToServer(String environment, String SSHUser, String SSHHost, String SSHPassword, String SSHPort, String LPort, String RHost, String RPort) {
		this.environment = environment;
		this.SSHUser = SSHUser;
		this.SSHHost = SSHHost;
		this.SSHPassword = SSHPassword;
		this.SSHPort = Integer.parseInt(SSHPort);
		this.RHost = RHost;
		this.LPort = Integer.parseInt(LPort);
		this.RPort = Integer.parseInt(RPort);
	}

	public void createSSHSession() throws JSchException {
		JSch jsch = new JSch();
		session	= jsch.getSession(SSHUser, SSHHost, SSHPort);
		session.setConfig("StrictHostKeyChecking", "No");
		session.setPassword(SSHPassword);
		session.connect(10000);
		logger.info("ssh {}@{}:{}",SSHUser,SSHHost, SSHPort);
		lPortRhostRPort();
	}

	public int getLPort() {	
		return LPort;	
	}
	public int getRPort() {	
		return RPort;	
	}
	public String getRHost() {	
		return RHost;	
	}
	public void startEnvironment() {
		logger.trace("====================START-{}====================",environment.toUpperCase());
	}
	public void endEnvironment() {
		logger.trace("====================END-{}====================",environment.toUpperCase());
	}

	public Boolean isSessionConnected() {
		Boolean isSessionConnected = false;
		if(session != null) {
			isSessionConnected = session.isConnected();
			logger.info("ssh established? "+isSessionConnected);
		}
		return isSessionConnected;
	}

	private void lPortRhostRPort() throws JSchException {
		if(session !=null) {
			session.setPortForwardingL(LPort, RHost, RPort);
			for(String value : session.getPortForwardingL()) {
				logger.info("Local port forwarding as {}",value);
			}
		}
	}

	public void destroySSHSession() {
		if(session != null) {
			session.disconnect();
			logger.info("{}@{}:{} logged out",SSHUser,SSHHost, SSHPort);
		}
	}

	public void destroyLPort() throws JSchException {
		if(session != null) 
			session.delPortForwardingL(LPort);
		logger.info("{} local port forwarding destroyed",LPort);
	}
}
