package de.lars.rgbwdesklamp.server;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import de.lars.rgbwdesklamp.Main;

public class Server {
	
	private final static int PORT = 20002;
	private static boolean running;
	private static ServerSocket serverSocket;
	private static Socket socket;
	private static ObjectInputStream ois;
	public static String remoteClientAddress = "";
	
	
	public static void start() {
		if(!running) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
					try {
						serverSocket = new ServerSocket(PORT);
						running = true;
						System.out.println("[Server] Wating for connection...");
						socket = serverSocket.accept();
						System.out.println("[Server] Client connected: " + socket.getRemoteSocketAddress());
						InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
						remoteClientAddress = isa.getAddress().getHostAddress();
						
						ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
						
						while(running) {
							Object o = ois.readObject();
							
							if(o instanceof HashMap<?, ?>) {
								// Pixel Hashmap (list of ws281x pixel and color
								@SuppressWarnings("unchecked")
								HashMap<Integer, Color> pixelHash = (HashMap<Integer, Color>) o;
								ServerInputHandler.handlePixelHash(pixelHash);
							} else {
								
								String[] msg = (String[]) o;
								if(msg[0].equals("DISCONNECT")) {
									System.out.println("[Server] Client disconnecting...");
									stop();
									System.out.println("[Server] Restarting...");
									start();
									Main.clientDisconnected();
								} else {
									System.out.println("[Server] Message received: " + msg[0]);
									ServerInputHandler.handle(msg);
								}	
							}
						}
						
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
				}
			}).start();
		}
	}
	
	public static void stop() {
		if(running) {
			running = false;
			try {
				if(ois != null)
					ois.close();
				if(socket != null)
					socket.close();
				if(serverSocket != null)
					serverSocket.close();
				System.out.println("[Server] Server stopped...");
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("[Server] Could not stop the Server!");
			}
		}
	}
	
	public static boolean isRunning() {
		return running;
	}

}
