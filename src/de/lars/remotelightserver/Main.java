package de.lars.remotelightserver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;
import org.tinylog.provider.ProviderRegistry;

import de.lars.remotelightserver.server.Server;
import de.lars.remotelightserver.utils.CommandHandler;
import de.lars.remotelightserver.utils.DirectoryUtil;

public class Main {

	public final static String VERSION = "pre-0.2.0.1";

	private static Main instance;
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

	public Server getServer() {
		return server;
	}

	public PixelController getPixelController() {
		return controller;
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
		
		// copy log file and rename
		DirectoryUtil.copyAndRenameLog(new File(DirectoryUtil.getLogsPath() + "log.txt"),
				new SimpleDateFormat("yyyy-MM-dd_HH-mm").format(new Date().getTime()) + ".txt");

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
