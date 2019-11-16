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
package de.lars.remotelightserver.utils;

import org.tinylog.Logger;

import com.diozero.ws281xj.LedDriverInterface;
import com.diozero.ws281xj.StripType;
import com.diozero.ws281xj.apa102.Apa102LedDriver;
import com.diozero.ws281xj.rpiws281x.WS281x;

public class StripTypeUtil {
	
	// WS281x
	private static final int DEFAULT_FREQUENCY_WS281X = 800_000;
	private static final int DEFAULT_DMA_NUM = 5;
	//Apa102
	private static final int DEFAULT_CONTROLLER = 2;
	private static final int DEFAULT_CHIP_SELECT = 0;
	private static final int DEFAULT_FREQUENCY_APA102 = 2_000_000;
	
	public static LedDriverInterface getLedDriverInterface(String stripType, int gpioNum, int brightness, int numPixels) {
		StripType type = null;
		boolean apa102 = false;
		
		switch(stripType.toLowerCase()) {
		// SK6812
		case "sk6812_rgbw":
			type = StripType.SK6812_RGBW;
			break;
		case "sk6812_rbgw":
			type = StripType.SK6812_RBGW;
			break;
		case "sk6812_grbw":
			type = StripType.SK6812_GRBW;
			break;
		case "sk6812_gbrw":
			type = StripType.SK6812_GBRW;
			break;
		case "sk6812_brgw":
			type = StripType.SK6812_BRGW;
			break;
		case "sk6812_bgrw":
			type = StripType.SK6812_BGRW;
			break;
			
		case "sk6812":
			type = StripType.SK6812;
			break;
		case "sk6812w":
			type = StripType.SK6812W;
			break;
			
		// WS281x
		case "ws2811_rgb":
			type = StripType.WS2811_RGB;
			break;
		case "ws2811_rbg":
			type = StripType.WS2811_RBG;
			break;
		case "ws2811_grb":
			type = StripType.WS2811_GRB;
			break;
		case "ws2811_gbr":
			type = StripType.WS2811_GBR;
			break;
		case "ws2811_brg":
			type = StripType.WS2811_BRG;
			break;
		case "ws2811_bgr":
			type = StripType.WS2811_BGR;
			break;
			
		case "ws2811":
			type = StripType.WS2811_GRB;
			break;
		case "ws2812":
			type = StripType.WS2812;
			break;
		
		// Apa102
		case "apa102":
			apa102 = true;
			break;
		}
		
		if(type == null && !apa102) {
			Logger.error("LED strip type '" + stripType + "' not supported! Please change in " + Config.CONFIG_FILE_NAME + ".");
			return null;
		}
		if(!apa102) {
			// create new ws281x driver
			return new WS281x(DEFAULT_FREQUENCY_WS281X, DEFAULT_DMA_NUM, gpioNum, brightness, numPixels, type);
		} else {
			// create new apa102 driver
			return new Apa102LedDriver(DEFAULT_CONTROLLER, DEFAULT_CHIP_SELECT, DEFAULT_FREQUENCY_APA102, numPixels, brightness);
		}
	}

}
