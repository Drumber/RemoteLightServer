/*******************************************************************************
 * ______                     _       _     _       _     _   
 * | ___ \                   | |     | |   (_)     | |   | |  
 * | |_/ /___ _ __ ___   ___ | |_ ___| |    _  __ _| |__ | |_ 
 * |    // _ \ '_ ` _ \ / _ \| __/ _ \ |   | |/ _` | '_ \| __|
 * | |\ \  __/ | | | | | (_) | ||  __/ |___| | (_| | | | | |_ 
 * \_| \_\___|_| |_| |_|\___/ \__\___\_____/_|\__, |_| |_|\__|
 *                                             __/ |          
 *                                            |___/           
 * 
 * Copyright (C) 2019 Lars O.
 * 
 * This file is part of RemoteLightServer.
 ******************************************************************************/
package de.lars.remotelightserver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;
import org.tinylog.provider.ProviderRegistry;

import com.diozero.ws281xj.LedDriverInterface;
import de.lars.remotelightserver.server.Server;
import de.lars.remotelightserver.utils.CommandHandler;
import de.lars.remotelightserver.utils.Config;
import de.lars.remotelightserver.utils.DirectoryUtil;
import de.lars.remotelightserver.utils.StripTypeUtil;

public class Main {

	public final static String VERSION = "pre0.2.0.4";

	private static Main instance;
	private Config config;
	private Server server;
	private PixelController controller;

	public static void main(String[] args) {
		System.out.println("<=== RemoteLightServer " + VERSION + " by Lars O. ===>");
		System.out.println(">> Type 'end' or 'CTRL + C' to exit...");
		
		new Main();
	}

	public Main() {
		instance = this;
		configureLogger();
		new CommandHandler();
		
		config = new Config();
		
		server = new Server(true);
		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println(">> Closing...");
				close();
			}
		});
	}
	

	public static Main getInstance() {
		return instance;
	}
	
	public Config getConfig() {
		return config;
	}

	public Server getServer() {
		return server;
	}

	public PixelController getPixelController() {
		return controller;
	}
	
	public void createPixelController(int ledNum) {
		if(controller != null && controller.isDriverCreated()) {
			controller.close();
		}
		
		if(config.isLoaded()) {
			Properties prop = config.getProperties();
			int leds = ledNum, pin = 18;
			String stripType = "";
			
			try {
				if(!prop.getProperty("led_number", "auto").equalsIgnoreCase("auto")) {
					leds = Integer.parseInt(prop.getProperty("led_number", "60"));
				}
				stripType = prop.getProperty("strip_type", "ws2812");
				pin = Integer.parseInt(prop.getProperty("gpio_pin", "18"));
			} catch (NumberFormatException e) {
				Logger.warn("Invalid config value (" + e.getMessage() + "), closing...");
				close();
				System.exit(1);
			}
			
			Logger.info("Creating new PixelController: " + stripType.toUpperCase() + ", " + leds + " pixels, Pin " + pin);
			LedDriverInterface driver = StripTypeUtil.getLedDriverInterface(stripType, pin, 255, leds);
			if(driver == null) {
				Logger.warn("Invalid strip type in config, closing...");
				close();
				System.exit(1);
			}
			controller = new PixelController(leds, driver);
			
		} else {
			Logger.info("Config not loaded. Use default options: WS2812, " + ledNum + " pixels, Pin 18");
			controller = new PixelController(ledNum);
		}
	}
	
	public void closePixelController() {
		if(controller != null && controller.isDriverCreated()) {
			controller.close();
		}
		controller = null;
	}

	/**
	 * shutdown routine
	 * (i) does not close the program!
	 */
	public void close() {
		if(server != null && server.isRunning()) {
			server.stop();
		}
		if(controller != null && controller.isDriverCreated()) {
			controller.close();
		}
		
		if(config.isLoaded() && Boolean.parseBoolean(config.getProperties().getProperty("save_logs", "true"))) {
			// copy log file and rename
			DirectoryUtil.copyAndRenameLog(new File(DirectoryUtil.getLogsPath() + "log.txt"),
					new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date().getTime()) + ".txt");
		}

		try {
			ProviderRegistry.getLoggingProvider().shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Executes OS shutdown command
	 */
	public void shutwdownSystem() {
		Logger.info("Shutting down...");
		Runtime rtime = Runtime.getRuntime();
		try {
			rtime.exec("shutdown -h now");
		} catch (IOException e) {
			System.out.println("Could not execute shutdown command! \n" + e);
		}
	}

	private void configureLogger() {
		new File(DirectoryUtil.getLogsPath()).mkdir();
		Configuration.set("writerF.file", DirectoryUtil.getLogsPath() + "log.txt");
	}

}
