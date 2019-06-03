package de.lars.rgbwdesklamp.ws281x;

import java.awt.Color;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.WS281x;
import de.lars.rgbwdesklamp.Main;

public class RunningLightWS {
	private static int speed;
	private static boolean active;
	private static boolean sym;
	
	public static void stop() {
		active = false;
	}
	
	public static void setSpeed(int delay) {
		speed = delay;
	}
	
	public static void setSymmetric(boolean symmetric) {
		sym = symmetric;
	}
	
	public static void start(int delay) {
		if(!active) {
			speed = delay;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					active = true;
				    Color[] color = {Color.RED, Color.ORANGE, Color.PINK, Color.MAGENTA,
				    		Color.BLUE, Color.CYAN, Color.GREEN};
				    
				    WS281x strip = Main.getWS281xStrip();
				    int pass = 0, counter = 0;
					while(active) {
						if(!sym) {
							if(pass < 5) {
								switch (pass) {
								case 0:
									if(counter >= color.length) counter = 0;
									int c = PixelColour.createColourRGB(color[counter].darker().darker().getRed(),
											color[counter].darker().darker().getGreen(), color[counter].darker().darker().getBlue());
									strip.setPixelColour(0, c);
									break;
								case 1:
									if(counter > color.length) counter = 0;
									c = PixelColour.createColourRGB(color[counter].darker().getRed(),
											color[counter].darker().getGreen(), color[counter].darker().getBlue());
									strip.setPixelColour(0, c);
									break;
								case 2:
									if(counter > color.length) counter = 0;
									c = PixelColour.createColourRGB(color[counter].getRed(),
											color[counter].getGreen(), color[counter].getBlue());
									strip.setPixelColour(0, c);
									break;
								case 3:
									if(counter > color.length) counter = 0;
									c = PixelColour.createColourRGB(color[counter].darker().getRed(),
											color[counter].darker().getGreen(), color[counter].darker().getBlue());
									strip.setPixelColour(0, c);
									break;
								case 4:
									c = PixelColour.createColourRGB(color[counter].darker().darker().getRed(),
											color[counter].darker().darker().getGreen(), color[counter].darker().darker().getBlue());
									strip.setPixelColour(0, c);
									counter++;
									if(counter >= color.length) counter = 0;
									break;
									
								default:
									break;
								}
								
								pass++;
							} else if(pass < 10) {
								pass++;
								strip.setPixelColour(0, 0);
							} else {
								pass = 0;
								strip.setPixelColour(0, 0);
							}
							
							//shift pixels to right
							for(int i = 1; i < Main.getWS281xPixelNum(); i++) {
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
