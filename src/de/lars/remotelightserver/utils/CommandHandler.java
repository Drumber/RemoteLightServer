package de.lars.remotelightserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.tinylog.Logger;

import de.lars.remotelightserver.Main;

public class CommandHandler {
	
	private boolean active;
	private BufferedReader bf;
	
	public CommandHandler() {
		bf = new BufferedReader(new InputStreamReader(System.in));
		this.start();
	}
	
	private void start() {
		if(!active) {
			active = true;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					while(active) {
						try {
							String input = bf.readLine();
							handleInput(input);
						} catch (IOException e) {
							Logger.error(e);
						}
					}
				}
			}, "CommandHandler").start();
		}
	}
	
	public void close() {
		active = false;
		try {
			bf.close();
		} catch (IOException e) {}
	}
	
	private void handleInput(String in) {
		switch (in.toLowerCase()) {
		
		case "end":
			System.exit(0);
			break;
			
		case "info":
		case "help":
			System.out.println("RemoteLightServer " + Main.VERSION + " by Lars O.");
			System.out.println("Type 'end' or 'CTRL + C' to exit...");
			break;

		default:
			System.out.println("Command '" + in + "' not found!");
			break;
		}
	}

}
