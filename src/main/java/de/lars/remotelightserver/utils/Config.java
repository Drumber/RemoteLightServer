/*-
 * >===license-start
 * RemoteLightServer
 * ===
 * Copyright (C) 2019 - 2021 Lars O.
 * ===
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <===license-end
 */

package de.lars.remotelightserver.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Properties;

import org.tinylog.Logger;

public class Config {
	
	public final static String CONFIG_FILE_NAME = "config.properties";
	
	private Properties prop;
	
	public Config() {
		// copy config file from classpath if not exists
		if(!DirectoryUtil.isCreated(CONFIG_FILE_NAME)) {
			try {
				InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE_NAME);
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
