package de.lars.rgbwdesklamp.ws281x;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import com.diozero.ws281xj.WS281x;

import de.lars.rgbwdesklamp.Main;

public class RainbowWS {
	
	private static int speed;
	private static boolean active;
	
	public static void stop() {
		active = false;
	}
	
	public static void setSpeed(int speed) {
		RainbowWS.speed = speed;
	}
	
	
	public static void start(int delay) {
		List<Color> colors = new ArrayList<Color>();
	    for (int r=0; r<100; r++) colors.add(new Color(r*255/100,       255,         0));
	    for (int g=100; g>0; g--) colors.add(new Color(      255, g*255/100,         0));
	    for (int b=0; b<100; b++) colors.add(new Color(      255,         0, b*255/100));
	    for (int r=100; r>0; r--) colors.add(new Color(r*255/100,         0,       255));
	    for (int g=0; g<100; g++) colors.add(new Color(        0, g*255/100,       255));
	    for (int b=100; b>0; b--) colors.add(new Color(        0,       255, b*255/100));
	    colors.add(new Color(        0,       255,         0));
	    Color[] color = colors.toArray(new Color[colors.size()]);
	    
		speed = delay;
		if(!active) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					active = true;
					WS281x strip = Main.getWS281xStrip();
					int count = 0;
					for(int i = 0; i < Main.getWS281xPixelNum(); i++) {
						count += 8;
						if(count > color.length) count = 0;
						Color c = color[count];
						strip.setPixelColourRGB(i, c.getRed(), c.getGreen(), c.getBlue());
					}
					while(active) {
						for(int i = 1; i <= Main.getWS281xPixelNum(); i++) {
							if(i == Main.getWS281xPixelNum()) {
								count += 8;
								if(count > color.length) count = 0;
								Color c = color[count];
								strip.setPixelColourRGB(0, c.getRed(), c.getGreen(), c.getBlue());
							} else {
								strip.setPixelColour(Main.getWS281xPixelNum() - i, Main.getWS281xPixelColor(Main.getWS281xPixelNum() - i - 1));
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
