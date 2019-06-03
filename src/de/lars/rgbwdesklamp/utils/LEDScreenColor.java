package de.lars.rgbwdesklamp.utils;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import de.lars.rgbwdesklamp.Main;

public class LEDScreenColor {
	
	private static boolean active;
	private static Timer timer;
	private static Color curentColorL = Color.BLACK;
	private static Color curentColorR = Color.BLACK;
	
	public static void start(int widthL, int heightL, int widthR, int heightR) {
		if(!active) {
			new Thread() {
				
				@Override
				public void run() {
					active = true;
					ScreenPixelDetector.setPickPixel(widthL, heightL, widthR, heightR);
					timer = new Timer();
					timer.scheduleAtFixedRate(new TimerTask() {
						
						@Override
						public void run() {
							if(active) {
								Main.setColorLeft(ScreenPixelDetector.getPixelColorLeft());
								Main.setColorRight(ScreenPixelDetector.getPixelColorRight());
								curentColorL = ScreenPixelDetector.getPixelColorLeft();
								curentColorR = ScreenPixelDetector.getPixelColorRight();
								Main.getMainGUI().setCurentColorPanel(curentColorL, curentColorR);
							} else
								timer.cancel();
							
						}
					}, 0, 1000);
				}
			}.start();
		}
	}
	
	public static void stop() {
		active = false;
		timer.cancel();
	}
	
	public static boolean isActive() {
		return active;
	}
	
	public static Color getColorLeft() {
		return curentColorL;
	}
	
	public static Color getColorRight() {
		return curentColorR;
	}

}
