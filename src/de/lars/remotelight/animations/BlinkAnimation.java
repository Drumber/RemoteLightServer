package de.lars.remotelight.animations;

import java.awt.Color;

import de.lars.remotelight.Main;

public class BlinkAnimation {
	private static boolean active;
	private static int speed;
	private final static Color[] colors = {Color.RED, Color.BLUE, Color.GREEN, Color.CYAN, Color.YELLOW, Color.MAGENTA, Color.WHITE, Color.PINK};
	
	public static void start(int anispeed) {
		if(!active) {
			new Thread() {
				
				@Override
				public void run() {
					active = true;
					speed = anispeed;
					int lastColor = 0;
					int loop = 0;
					while(active) {
						Main.setColorLeft(colors[loop]);
						Main.setColorRight(colors[lastColor]);
						lastColor = loop;
						if(loop < (colors.length - 1)) {
							loop++;
						} else
							loop = 0;
						try {
							Thread.sleep(speed * 10);
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
