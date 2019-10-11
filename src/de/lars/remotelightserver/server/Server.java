package de.lars.remotelightserver.server;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.tinylog.Logger;

import com.google.gson.Gson;

import de.lars.remotelightserver.Main;
import de.lars.remotelightserver.PixelController;

public class Server {
	
	private final static int PORT = 20002;
	private boolean running = false;
	private boolean autoRestart;
	private ServerSocket serverSocket;
	private Socket socket;
	private Scanner scanner;
	private Gson gson;
	private Color[] inputPixels;
	private List<ConnectionStateChangeListener> listenersState;
	
	/**
	 * 
	 * @param autoRestart Automatically restart server when client disconnects
	 */
	public Server(boolean autoRestart) {
		listenersState = new ArrayList<>();
		gson = new Gson();
		this.autoRestart = autoRestart;
	}
	
	/**
	 * start the server and wait for connection
	 */
	public void start() {
		if(!running) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						serverSocket = new ServerSocket(PORT);
						running = true;
						Logger.info("[Server] Wating for connection...");
						onStateChanged("Waiting for connection...");
						
						socket = serverSocket.accept();
						Logger.info("[Server] Client connected: " + socket.getRemoteSocketAddress());
						onStateChanged("Connected");
						
						scanner = new Scanner(new BufferedInputStream(socket.getInputStream()));
						
						while(running) {
							
							if(scanner.hasNextLine()) {
								String input = scanner.nextLine();
								inputPixels = gson.fromJson(input, Color[].class);
								
								PixelController controller = Main.getInstance().getPixelController();
								if(controller == null) {
									controller = new PixelController(60);
									Logger.debug("Started new PixelController for " + inputPixels.length + " leds.");
								}
								controller.show(inputPixels);
							}
						}
					} catch (Exception e) {
						Logger.error(e);
						
//						if(autoRestart) {
//							restart();
//						} else {
//							stop();
//						}
					}
				}
			}, "Server thread").start();
		}
	}
	
	/**
	 * stops and starts the server
	 */
	public void restart() {
		this.stop();
		Logger.info("[Server] Restarting...");
		this.start();
	}
	
	/**
	 * stops the server
	 */
	public void stop() {
		if(running) {
			running = false;
			onStateChanged("Disconnected");
			try {
				if(scanner != null)
					scanner.close();
				if(socket != null)
					socket.close();
				if(serverSocket != null)
					serverSocket.close();
				Logger.info("[Server] Server stopped...");
				
				PixelController controller = Main.getInstance().getPixelController();
				if(controller != null && controller.isDriverCreated()) {
					controller.close();
					controller = null;
				}
			} catch (IOException e) {
				Logger.error(e, "[Server] Could not stop the Server!");
			}
		}
	}
	
	public boolean isRunning() {
		return running;
	}
	
	/**
	 * @param autoRestart Automatically restart server when client disconnects
	 */
	public void setAutoRestart(boolean autoRestart) {
		this.autoRestart = autoRestart;
	}
	
	public Color[] getPixels() {
		return inputPixels;
	}
	
	public interface ConnectionStateChangeListener {
		public void onConnectionStateChanged(String status);
	}
	
	public synchronized void addStateChangeListener(ConnectionStateChangeListener l) {
		listenersState.add(l);
	}
	
	private void onStateChanged(String text) {
		for(ConnectionStateChangeListener l : listenersState) {
			l.onConnectionStateChanged(text);
		}
	}

}
