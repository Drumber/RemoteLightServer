package de.lars.rgbwdesklamp.server;

import java.awt.Color;
import java.util.HashMap;

import com.diozero.ws281xj.WS281x;

import de.lars.rgbwdesklamp.DataStorage;
import de.lars.rgbwdesklamp.Main;
import de.lars.rgbwdesklamp.animations.AnimationHandler;
import de.lars.rgbwdesklamp.utils.AutoShutdown;
import de.lars.rgbwdesklamp.ws281x.WS281xAnimationHandler;

public class ServerInputHandler {
	
	public static void handlePixelHash(HashMap<Integer, Color> pixelHash) { //integer = pixel number, Color = color of pixel
		WS281x strip = Main.getWS281xStrip();
		for(int i = 0; i < pixelHash.size(); i++) {
			Color c = Main.dimWS281x(pixelHash.get(i));
			
			strip.setPixelColourRGB(i, c.getRed(), c.getGreen(), c.getBlue());
		}
		strip.render();
	}

	public static void handle(String[] msg) {

		if (msg.length > 0) {
			try {
				switch (msg[0]) {
				// System
				case Identifier.SYS_SHUTDOWN_IFNOT_REACHABLE:
					AutoShutdown.shutdownIfNotReachable(Server.remoteClientAddress);
					break;
				case Identifier.SYS_SHUTDOWN_CANCEL:
					AutoShutdown.cancelAutoShutdown();
					break;
				case Identifier.SYS_SHUTDOWN_NOW:
					AutoShutdown.shutdownNow();
					break;
				
				// WS281x
				case Identifier.WS_COLOR_ALL:
					Main.setWS281xAll(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]));
					break;
				case Identifier.WS_COLOR_PIXEL:
					Main.setWS281xPixel(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]), Integer.parseInt(msg[4]));
					break;
				case Identifier.WS_COLOR_OFF:
					Main.setWS281xOff();
					break;
				case Identifier.WS_COLOR_DIM:
					Main.setDimWs281x(Integer.parseInt(msg[1]));
					break;
				case Identifier.WS_ANI_STOP:
					WS281xAnimationHandler.stop();
					break;
				case Identifier.WS_ANI_SPEED:
					WS281xAnimationHandler.setSpeed(Integer.parseInt(msg[1]));
					break;
				case Identifier.WS_ANI_RAINBOW:
					WS281xAnimationHandler.startRainbow();
					break;
				case Identifier.WS_ANI_RUNNING:
					WS281xAnimationHandler.startRunning();
					break;
				case Identifier.WS_ANI_WIPE:
					WS281xAnimationHandler.startWipe();
					break;
				case Identifier.WS_ANI_SCAN:
					WS281xAnimationHandler.startScan();
					break;
				case Identifier.WS_SHIFT_LEFT:
					Main.shiftWS281xLeft(Integer.parseInt(msg[1]));
					break;
				case Identifier.WS_SHIFT_RIGHT:
					Main.shiftWS281xRight(Integer.parseInt(msg[1]));
					break;
				
				// COLOR
				case Identifier.COLOR_RED:
					Main.setColorRed(Integer.parseInt(msg[1]));
					break;
				case Identifier.COLOR_GREEN:
					Main.setColorGreen(Integer.parseInt(msg[1]));
					break;
				case Identifier.COLOR_BLUE:
					Main.setColorBlue(Integer.parseInt(msg[1]));
					break;
				case Identifier.COLOR_COLOR:
					Main.setColorSeperateLeft(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]));
					Main.setColorSeperateRight(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]));
					break;
				case Identifier.COLOR_LEFT:
					Main.setColorSeperateLeft(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]));
					break;
				case Identifier.COLOR_RIGHT:
					Main.setColorSeperateRight(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]));
					break;

				// ANIMATION
				case Identifier.ANI_FLASH:
					AnimationHandler.startAnimation(AnimationHandler.FLASH, Integer.parseInt(msg[1]));
					break;
				case Identifier.ANI_JUMP:
					AnimationHandler.startAnimation(AnimationHandler.JUMP, Integer.parseInt(msg[1]));
					break;
				case Identifier.ANI_RAINBOW:
					AnimationHandler.startAnimation(AnimationHandler.RAINBOW, Integer.parseInt(msg[1]));
					break;
				case Identifier.ANI_PULSE:
					AnimationHandler.startAnimation(AnimationHandler.PULSE, Integer.parseInt(msg[1]));
					break;
				case Identifier.ANI_BLINK:
					AnimationHandler.startAnimation(AnimationHandler.BLINK, Integer.parseInt(msg[1]));
					break;
				case Identifier.ANI_SET_STOP:
					AnimationHandler.stop();
					break;
				case Identifier.ANI_SET_SPEED:
					AnimationHandler.setAnimationSpeed(Integer.parseInt(msg[1]));
					break;

				// SCREEN COLOR
				case Identifier.SC_START:
					AnimationHandler.stop();
					Main.setColorLeft(Color.BLACK);
					Main.setColorRight(Color.BLACK);
					break;
				case Identifier.SC_STOP:
					Main.setColorLeft(Color.BLACK);
					Main.setColorRight(Color.BLACK);
					break;
				case Identifier.SC_COLOR_LEFT:
					Main.setColorSeperateLeft(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]));
					break;
				case Identifier.SC_COLOR_RIGHT:
					Main.setColorSeperateRight(Integer.parseInt(msg[1]), Integer.parseInt(msg[2]),
							Integer.parseInt(msg[3]));
					break;
					
				// SCENES
				case Identifier.SCENE_STOP:
					Main.setColorLeft(Color.BLACK);
					Main.setColorRight(Color.BLACK);
					break;
					
				// SETTINGS
				case Identifier.STNG_MODE_RGB:
					Main.setRgbMode();
					DataStorage.store(DataStorage.SETTINGS_MODE, "RGB");
					break;
				case Identifier.STNG_MODE_WS28x:
					Main.setWS281xMode(Integer.parseInt(msg[1]));
					DataStorage.store(DataStorage.SETTINGS_MODE, "WS281x");
					break;
				case Identifier.STNG_BOOT_ANI:
					DataStorage.store(DataStorage.SETTINGS_BOOT_ANI, msg[1]);
					break;

				default:
					System.out.println("[ServerInputHandler] Identifier '" + msg[0] + "' not found!");
					break;
				}

			} catch (Exception e) {
				System.out.println("[ServerInputHandler] ERROR: " + e.toString());
				e.printStackTrace();
			}
		}
	}

}
