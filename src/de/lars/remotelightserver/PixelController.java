package de.lars.remotelightserver;

import java.awt.Color;

import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;

public class PixelController {
	
	private int pixels;
	private WS281x driver;
	
	public PixelController(int pixels) {
		this.pixels = pixels;
		driver = new WS281x(18, 255, pixels);
	}
	
	public boolean isDriverCreated() {
		return driver != null;
	}
	
	public void close() {
		if(isDriverCreated()) {
			driver.allOff();
			driver.render();
			driver.close();
			driver = null;
		}
	}
	
	public int getPixelNumber() {
		return pixels;
	}
	
	public void show(Color[] pixels) {
		for(int i = 0; i < getPixelNumber(); i++) {
			driver.setPixelColour(i, convertWS281xColor(pixels[i]));
		}
		driver.render();
	}
	
	private int convertWS281xColor(Color c) {
		return PixelColour.createColourRGB(c.getRed(), c.getGreen(), c.getBlue());
	}

}
