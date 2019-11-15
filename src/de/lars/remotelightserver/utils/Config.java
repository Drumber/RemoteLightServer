package de.lars.remotelightserver.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import org.tinylog.Logger;

public class Config {
	
	public final static String CONFIG_FILE_NAME = "config.propeties";
	public final static String CONFIG_CLASSPATH = "resourcen/config.propeties";
	
	private Properties prop;
	
	public Config() {
		// copy config file from classpath if not exists
		if(!DirectoryUtil.isCreated(CONFIG_FILE_NAME)) {
			try {
				InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_CLASSPATH);
				Files.copy(input, new File(CONFIG_FILE_NAME).toPath());
			} catch (IOException e) {
				Logger.error(e, "Could not copy config from classpath.");
			}
		}
		// load properties
		if(DirectoryUtil.isCreated(CONFIG_FILE_NAME)) {
			try (InputStream input = new FileInputStream(CONFIG_FILE_NAME)) {
				
				prop = new Properties();
				prop.load(input);
				
			} catch (IOException e) {
				Logger.error(e, "Could not load properties.");
			}
		}
	}
	
	public boolean isLoaded() {
		return prop != null;
	}
	
	public Properties getProperties() {
		return prop;
	}

}
