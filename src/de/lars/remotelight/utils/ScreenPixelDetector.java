package de.lars.remotelight.utils;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Robot;

public class ScreenPixelDetector {
	
	private static int widthL, heightL, widthR, heightR;
	
	
	public static void setPickPixel(int widthL, int heightL, int widthR, int heightR) {
		ScreenPixelDetector.widthL = widthL;
		ScreenPixelDetector.heightL = heightL;
		ScreenPixelDetector.widthR = widthR;
		ScreenPixelDetector.heightR = heightR;
	}
	
	public static Color getPixelColorLeft() {
		Robot r = null;
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return r.getPixelColor(widthL, heightL);
	}
	
	public static Color getPixelColorRight() {
		Robot r = null;
		try {
			r = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		return r.getPixelColor(widthR, heightR);
	}

}
