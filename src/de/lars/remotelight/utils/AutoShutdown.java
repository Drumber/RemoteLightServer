package de.lars.remotelight.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;

import de.lars.remotelight.Main;
import de.lars.remotelight.server.Server;

public class AutoShutdown {
	private static boolean active = false;
	private static Timer timer;
	
	public static void shutdownNow() {
		System.out.println("[AutoShutdown] Shutting down...");
		Server.stop();
		Main.clientDisconnected();
		Main.close();
	    Runtime runtime = Runtime.getRuntime();
	    try {
			runtime.exec("shutdown -h now");
		} catch (IOException e) {
			System.out.println("[AutoShutdown] Could not execute command!");
			e.printStackTrace();
		}
	    System.exit(0);
	}
	
	public static void shutdownIfNotReachable(String ipAddress) {
		if(active)
			cancelAutoShutdown();
		active = true;
		new Thread(new Runnable() {
			@Override
			public void run() {
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask() { //check every 5 seconds, if computer is reachable. If not --> shutdown
					
					@Override
					public void run() {
						try {
							InetAddress address = InetAddress.getByName(ipAddress);
							if(!address.isReachable(5000)) {
								System.out.println("[AutoShutdown] Host not reachable! --> Shutdown");
								shutdownNow();
							}
							
						} catch (UnknownHostException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					
				}, 0, 5000);
			}
		}).start();
	}
	
	public static void cancelAutoShutdown() {
		if(active) {
			active = false;
			timer.cancel();
		}
	}

}
