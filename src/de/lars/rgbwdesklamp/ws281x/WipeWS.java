package de.lars.rgbwdesklamp.ws281x;

import java.awt.Color;
import com.diozero.ws281xj.WS281x;

import de.lars.rgbwdesklamp.Main;

public class WipeWS {
	
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
					Color[] colors = {Color.RED, Color.ORANGE, Color.PINK, Color.MAGENTA,
				    		Color.BLUE, Color.CYAN, Color.GREEN};
					Color c = Color.RED;
					WS281x strip = Main.getWS281xStrip();
					int pix = 0, count = 0;
					boolean wiping = false;
					while(active) {
						if(!wiping) {
							count++;
							if(count >= colors.length) count = 0;
							c = colors[count];
							strip.setPixelColourRGB(0, c.getRed(), c.getGreen(), c.getBlue());
							pix = 0;
							wiping = true;
						} else {
							pix++;
							if(pix < Main.getWS281xPixelNum()) {
								strip.setPixelColourRGB(pix, c.getRed(), c.getGreen(), c.getBlue());
							} else {
								wiping = false;
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
