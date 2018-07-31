package com.sorc.content.elasticsearch.core.dao;

import org.springframework.stereotype.Component;

@Component
public class ElasticSearchClusterConfiguration {

	private String[] servers;
	private int port;
	private boolean sniff;
	
	public ElasticSearchClusterConfiguration() {
		super();
	}
	
	public ElasticSearchClusterConfiguration(String[] servers, int port) {
		super();
		this.servers = servers;
		this.port = port;
	}
	public String[] getServers() {
		return servers;
	}
	public void setServers(String[] servers) {
		this.servers = servers;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}

	public boolean isSniff() {
		return sniff;
	}

	public void setSniff(boolean sniff) {
		this.sniff = sniff;
	}

}
