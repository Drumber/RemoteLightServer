package de.lars.remotelight.ws281x;

import java.awt.Color;
import java.util.Random;

import com.diozero.ws281xj.WS281x;

import de.lars.remotelight.Main;

public class ScanWS {
	private static int speed;
	private static boolean active;
	
	public static void stop() {
		active = false;
	}
	
	public static void setSpeed(int delay) {
		speed = delay;
	}
	
	public static void start(int delay) {
		if(!active) {
			speed = delay;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					active = true;
					Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.PINK, Color.MAGENTA,
				    		Color.BLUE, Color.CYAN, Color.GREEN};
					Color c = Color.RED;
					WS281x strip = Main.getWS281xStrip();
					int pix = 0;
					boolean scanning = false, reverse = false;
					while(active) {
						if(!scanning) {
							int r = new Random().nextInt(colors.length);
							c = colors[r];
							strip.setPixelColourRGB(0, c.getRed(), c.getGreen(), c.getBlue());
							pix = 0;
							scanning = true;
						} else {
							strip.setPixelColour(pix, 0);
							if(!reverse) {
								pix++;
								if(pix < Main.getWS281xPixelNum()) {
									strip.setPixelColourRGB(pix, c.getRed(), c.getGreen(), c.getBlue());
								} else {
									reverse = true;
									pix--;
									strip.setPixelColourRGB(pix, c.getRed(), c.getGreen(), c.getBlue());
								}
							} else {
								pix--;
								if(pix > 0) {
									strip.setPixelColourRGB(pix, c.getRed(), c.getGreen(), c.getBlue());
								} else {
									reverse = false;
									scanning = false;
									strip.setPixelColourRGB(pix, c.getRed(), c.getGreen(), c.getBlue());
								}
							}
						}
						
						strip.render();
						
						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}).start();
		}
	}
}
