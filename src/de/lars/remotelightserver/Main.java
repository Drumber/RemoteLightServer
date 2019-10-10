package de.lars.remotelightserver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tinylog.configuration.Configuration;
import org.tinylog.provider.ProviderRegistry;

import de.lars.remotelightserver.server.Server;
import de.lars.remotelightserver.utils.DirectoryUtil;

public class Main {
	
	public final static String VERSION = "pre-0.2.0.1";
	
	private Main instance;

	public static void main(String[] args) {
		System.out.println("<=== RemoteLightServer " + VERSION + " by Lars O. ===>");
		System.out.println(">> Type 'end' or 'CTRL + C' to exit...");
		try {
			String input = System.console().readLine().toUpperCase();
			if (input.equalsIgnoreCase("END")) {
				System.exit(0);
			}
		} catch(Exception e) {
			//do nothing
		}
		
	}
	
	public Main() {
		instance = this;
		configureLogger();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println(">> Closing...");
				close();
			}
		});
	}
	
	public Main getInstance() {
		return instance;
	}

	public void close() {
		//ledDriver.close();
		Server.stop();
		DataStorage.save();
		
		//copy log file and rename
		DirectoryUtil.copyAndRenameLog(new File(DirectoryUtil.getLogsPath() + "log.txt"), new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date().getTime()) + ".txt");
		
		try {
			ProviderRegistry.getLoggingProvider().shutdown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void configureLogger() {
		new File(DirectoryUtil.getLogsPath()).mkdir();
		Configuration.set("writerF.file", DirectoryUtil.getLogsPath() + "log.txt");
	}

}
