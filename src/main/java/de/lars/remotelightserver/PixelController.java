/*-
 * >===license-start
 * RemoteLightServer
 * ===
 * Copyright (C) 2019 - 2021 Lars O.
 * ===
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * <===license-end
 */

package de.lars.remotelightserver;

import java.awt.Color;

import org.tinylog.Logger;

import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.rpiws281x.WS281x;

public class PixelController {
	
	private int pixels;
	private LedDriverInterface driver;
	
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
	
	/**
	 * Check if the received amount of pixels is equal to the pixel number of this controller.
	 * If not, create a new pixel controller with the received pixel number.
	 * @param receivedPixNum		amount of received pixels
	 * @return						true if the pixel number is equal, false otherwise
	 */
	private boolean checkValidPixelNumber(int receivedPixNum) {
		if(getPixelNumber() != receivedPixNum) {
			Logger.warn(String.format("Received wrong pixel number! Expected %d, got %d pixels. Creating new pixel controller...",
					getPixelNumber(), receivedPixNum));
			// create new pixel controller
			Main.getInstance().createPixelController(receivedPixNum);
			return false;
		}
		return true;
	}
	
	private int convertWS281xColor(Color c) {
		return PixelColour.createColourRGB(c.getRed(), c.getGreen(), c.getBlue());
	}

}
