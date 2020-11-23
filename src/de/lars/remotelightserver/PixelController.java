/*******************************************************************************
 * ______                     _       _     _       _     _   
 * | ___ \                   | |     | |   (_)     | |   | |  
 * | |_/ /___ _ __ ___   ___ | |_ ___| |    _  __ _| |__ | |_ 
 * |    // _ \ '_ ` _ \ / _ \| __/ _ \ |   | |/ _` | '_ \| __|
 * | |\ \  __/ | | | | | (_) | ||  __/ |___| | (_| | | | | |_ 
 * \_| \_\___|_| |_| |_|\___/ \__\___\_____/_|\__, |_| |_|\__|
 *                                             __/ |          
 *                                            |___/           
 * 
 * Copyright (C) 2019 Lars O.
 * 
 * This file is part of RemoteLightServer.
 ******************************************************************************/
package de.lars.remotelightserver;

import java.awt.Color;

import org.pmw.tinylog.Logger;

import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;

public class PixelController {
	
	private int pixels;
	private LedDriverInterface driver;
	// used to show warnings only once
	private boolean warnWrongPixNum;
	private boolean warnTooManyPix;
	
	public PixelController(int pixels) {
		this.pixels = pixels;
		driver = new WS281x(18, 255, pixels);
	}
	
	public PixelController(int pixels, LedDriverInterface driver) {
		this.pixels = pixels;
		this.driver = driver;
	}
	
	public boolean isDriverCreated() {
		return driver != null;
	}
	
	public void close() {
		if(isDriverCreated()) {
			driver.allOff();
			driver.render();
			//driver.close(); //produces a fatal error
			driver = null;
		}
	}
	
	public void shutdown() {
		if(isDriverCreated()) {
			driver.allOff();
			driver.render();
			// do not close driver due to fatal error
			driver = null;
		}
	}
	
	public int getPixelNumber() {
		return pixels;
	}
	
	public void show(Color[] pixels) {
		if(!checkValidPixelNumber(pixels.length))
			return;
		
		for(int i = 0; i < getPixelNumber(); i++) {
			driver.setPixelColour(i, convertWS281xColor(pixels[i]));
		}
		driver.render();
	}
	
	private boolean checkValidPixelNumber(int receivedPixNum) {
		if(getPixelNumber() < receivedPixNum && !warnTooManyPix) {
			Logger.warn(String.format("Received too many pixels! Expected %d, got %d pixels. Program will continue with the lower pixel number.",
					getPixelNumber(), receivedPixNum));
			warnTooManyPix = true;
			return true; // can still show pixels
			
		} else if(getPixelNumber() != receivedPixNum && !warnWrongPixNum) {
			Logger.warn(String.format("Received wrong pixel number! Expected %d, got %d pixels. Can not show pixels!",
					getPixelNumber(), receivedPixNum));
			warnWrongPixNum = true;
			return false;
		}
		if(warnWrongPixNum && getPixelNumber() == receivedPixNum) {
			// reset
			warnWrongPixNum = false;
		} else {
			return false;
		}
		return true;
	}
	
	private int convertWS281xColor(Color c) {
		return PixelColour.createColourRGB(c.getRed(), c.getGreen(), c.getBlue());
	}

}
