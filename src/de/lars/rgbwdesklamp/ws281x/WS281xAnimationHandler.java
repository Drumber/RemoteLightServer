package de.lars.rgbwdesklamp.ws281x;

import de.lars.rgbwdesklamp.Main;

public class WS281xAnimationHandler {
	
	private static boolean active;
	private static int speed = 50;
	
	public static void stop() {
		active = false;
		RainbowWS.stop();
		RunningLightWS.stop();
		WipeWS.stop();
		ScanWS.stop();
		SnakesWS.stop();
		
		Main.setWS281xOff();
	}
	
	public static boolean isActive() {
		return active;
	}
	
	public static void setSpeed(int speed) {
		WS281xAnimationHandler.speed = speed;
		RainbowWS.setSpeed(speed);
		RunningLightWS.setSpeed(speed);
		WipeWS.setSpeed(speed);
		ScanWS.setSpeed(speed);
		SnakesWS.setSpeed(speed);
	}
	
	public static void startRainbow() {
		stop();
		active = true;
		RainbowWS.start(speed);
	}
	
	public static void startRunning() {
		stop();
		active = true;
		RunningLightWS.start(speed);
	}
	
	public static void startWipe() {
		stop();
		active = true;
		WipeWS.start(speed);
	}
	
	public static void startScan() {
		stop();
		active = true;
		ScanWS.start(speed);
	}
	
	public static void startSnakes() {
		stop();
		active = true;
		SnakesWS.start(speed);
	}

}
