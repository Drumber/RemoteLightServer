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
