package com.sorc.content.services.video.util;

import java.io.IOException;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {

	private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	public static Properties feedProperties = null;

	static {
		feedProperties = PropertiesUtil.loadProperties("videofeedconfig.properties");
	}
	
	private PropertiesUtil() {
	}

	public static Properties loadProperties(String propertiesFileName) {
		Resource resource = new ClassPathResource(propertiesFileName);

		try {
			return  PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.error("Error in loading " + propertiesFileName
					+ " - PropertiesUtil : " + e.getMessage());
		    return null;
		}
	}

	public static String getProperty(String key) {
	    return feedProperties.getProperty(key);
	}
}
