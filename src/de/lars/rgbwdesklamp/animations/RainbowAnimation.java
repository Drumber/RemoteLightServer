package de.lars.rgbwdesklamp.animations;

import de.lars.rgbwdesklamp.Main;

public class RainbowAnimation {
	private static boolean active;
	private static int speed;
	
	public static void start(int anispeed) {
		if(!active) {
			new Thread() {
				
				@Override
				public void run() {
					int red = 0;
					int green = 95;
					int blue = 0;
					active = true;
					speed = anispeed;
					while(active) {
						Main.setColorSeperateRight(red, green, blue);
						if(green == 95 && red < 95 && blue == 0) {
							red++;
						}
						if(red == 95 && green > 0 && blue == 0) {
							green--;
						}
						if(red == 95 && blue < 95 && green == 0) {
							blue++;
						}
						if(blue == 95 && red > 0 && green == 0) {
							red--;
						}
						if(blue == 95 && green < 95 && red == 0) {
							green++;
						}
						if(green == 95 && blue > 0 && red == 0) {
							blue--;
						}
						Main.setColorSeperateLeft(red, green, blue);
						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}
	
	public static void stop() {
		if(active) {
			active = false;
		}
	}
	
	public static boolean isRunning() {
		return active;
	}
	
	public static void setSpeed(int anispeed) {
		speed = anispeed;
	}
}
