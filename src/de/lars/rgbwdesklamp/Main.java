package de.lars.rgbwdesklamp;

import java.awt.Color;
import java.lang.reflect.Field;

import com.diozero.ws281xj.PixelColour;
import com.diozero.ws281xj.WS281x;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.SoftPwm;

import de.lars.rgbwdesklamp.gui.MainGUI;
import de.lars.rgbwdesklamp.server.Server;

public class Main {
	public final static String VERSION = "0.1.0";
	
	private static MainGUI gui;
	private static boolean ws281xMode;
	private static int pixels = 60, dim = 1;
	private static WS281x ledDriver;
	//Left desk Lamp
	private static int redPinL = RaspiPin.GPIO_00.getAddress();
	private static int greenPinL = RaspiPin.GPIO_02.getAddress();
	private static int bluePinL = RaspiPin.GPIO_03.getAddress();
	//Right Desk Lamp
	private static int redPinR = RaspiPin.GPIO_04.getAddress();
	private static int greenPinR = RaspiPin.GPIO_05.getAddress();
	private static int bluePinR = RaspiPin.GPIO_06.getAddress();

	public static void main(String[] args) {
		System.out.println("Starting!");
		DataStorage.start();
		if(!DataStorage.isStored(DataStorage.WS281x_PIXEL_NUM))
			DataStorage.store(DataStorage.WS281x_PIXEL_NUM, 60);
		else
			pixels = (int) DataStorage.getData(DataStorage.WS281x_PIXEL_NUM);
		
		Gpio.wiringPiSetup();
		SoftPwm.softPwmCreate(redPinL, 0, 100);
		SoftPwm.softPwmCreate(greenPinL, 0, 100);
		SoftPwm.softPwmCreate(bluePinL, 0, 100);
		SoftPwm.softPwmCreate(redPinR, 0, 100);
		SoftPwm.softPwmCreate(greenPinR, 0, 100);
		SoftPwm.softPwmCreate(bluePinR, 0, 100);

		Server.start();
		
		if(DataStorage.isStored(DataStorage.SETTINGS_MODE) && DataStorage.getData(DataStorage.SETTINGS_MODE).equals("WS281x")) {
			ws281xMode = false;
			ledDriver = new WS281x(18, 255, pixels);
		}
		
		if(DataStorage.isStored(DataStorage.SETTINGS_BOOT_ANI) && DataStorage.getData(DataStorage.SETTINGS_BOOT_ANI).equals("true"))
			bootAnimation();
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println(">> Closing...");
				close();
			}
		});

		if (args.length != 0) {
			if (args[0].equalsIgnoreCase("GUI")) {
				gui = new MainGUI();
				gui.setVisible(true);
			} else if(args[0].equalsIgnoreCase("WS281x")) {
					System.out.println(">> WS281x Test for 60 Pixels");
					ledDriver = new WS281x(18, 255, pixels);
					ws281xTest();
			} else if (args[0].equalsIgnoreCase("COLOR")) {
				System.out.println("Enter color (like RED,BLUE,CYAN) and hit ENTER...");
				System.out.println("Type 'END' to exit...");
				while (true) {
					String input = System.console().readLine().toUpperCase();
					if (input.equalsIgnoreCase("END")) {
						close();
						System.exit(0);
					}
					Color color;
					try {
						Field field = Class.forName("java.awt.Color").getField(input);
						color = (Color) field.get(null);
					} catch (Exception e) {
						color = Color.BLACK;
					}
					setColorLeft(color);
					setColorRight(color);
				}
			} else if (args[0].equalsIgnoreCase("TEST")) {
				startTestProgram();
			}
		} else {
			System.out.println("<=== RGB DeskLamp v" + VERSION + " by Lars O. ===>");
			System.out.println(">> Type 'end' or 'CTRL + C' to exit...");
			try {
				String input = System.console().readLine().toUpperCase();
				if (input.equalsIgnoreCase("END")) {
					System.exit(0);
				}
			} catch(Exception e) {
				//do nothing
			}
		}
	}
	
	public static void setRgbMode() {
		ws281xMode = false;
	}
	
	public static void setWS281xMode(int pixelNum) {
		ws281xMode = true;
		pixels = pixelNum;
		if(ledDriver == null)
			ledDriver = new WS281x(18, 255, pixels);
		
		DataStorage.store(DataStorage.WS281x_PIXEL_NUM, pixelNum);
	}

	public static MainGUI getMainGUI() {
		return gui;
	}
	
	public static void clientDisconnected() {
		
	}
	
	/*
	 * 
	 * WS281x Section
	 * 
	 */

	public static WS281x getWS281xStrip() {
		return ledDriver;
	}
	
	public static void setWS281xAll(int red, int green, int blue) {
		Color c = dimWS281x(new Color(red, green, blue));
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
		int color = PixelColour.createColourRGB(red, green, blue);
		for(int i = 0; i < pixels; i++) {
			ledDriver.setPixelColour(i, color);
		}
	}
	
	public static void setWS281xPixel(int pixel, int red, int green, int blue) {
		Color c = dimWS281x(new Color(red, green, blue));
		red = c.getRed();
		green = c.getGreen();
		blue = c.getBlue();
		int color = PixelColour.createColourRGB(red, green, blue);
		ledDriver.setPixelColour(pixel, color);
		ledDriver.render();
	}
	
	public static void setWS281xOff() {
		ledDriver.allOff();
		ledDriver.render();
	}
	
	public static int getWS281xPixelColor(int pixel) {
		return ledDriver.getPixelColour(pixel);
	}
	
	public static int getWS281xPixelNum() {
		return ledDriver.getNumPixels();
	}
	
	public static void shiftWS281xLeft(int num) {
		for(int r = 0; r < num; r++) {
			for(int i = 0; i < ledDriver.getNumPixels(); i++) {
				if(i == ledDriver.getNumPixels() - 1) {
					ledDriver.setPixelColourRGB(ledDriver.getNumPixels() - 1, 0, 0, 0);
				} else {
					ledDriver.setPixelColour(i, Main.getWS281xPixelColor(i + 1));
				}
			}
		}
	}
	
	public static void shiftWS281xRight(int num) {
		for(int r = 0; r < num; r++) {
			for(int i = 1; i <= ledDriver.getNumPixels(); i++) {
				if(i == ledDriver.getNumPixels()) {
					ledDriver.setPixelColourRGB(0, 0, 0, 0);
				} else {
					ledDriver.setPixelColour(ledDriver.getNumPixels() - i, Main.getWS281xPixelColor(ledDriver.getNumPixels() - i - 1));
				}
			}
		}
	}
	
	public static void setDimWs281x(int dim) {
		Main.dim = dim;
	}
	
	public static Color dimWS281x(Color c) {
		try {
			int r = c.getRed() / dim, g = c.getGreen() / dim, b = c.getBlue() / dim;
			return new Color(r, g, b);
		} catch(Exception e) {
			
		}
		return c;
	}
	
	
	private static void ws281xTest() {
		try {
			for(int i = 0; i < pixels; i++) {
				if(i < (pixels / 2)) {
					ledDriver.setPixelColour(i, PixelColour.BLUE);
				} else {
					ledDriver.setPixelColour(i, PixelColour.RED);
				}
				ledDriver.render();
				Thread.sleep(100);
			}
			ledDriver.allOff();
			boolean s = false;
			for(int i = 0; i < pixels; i++) {
				if(s) {
					ledDriver.setPixelColour(i, PixelColour.GREEN);
					s = false;
				} else {
					ledDriver.setPixelColour(i, PixelColour.ORANGE);
					s = true;
				}
				ledDriver.render();
				Thread.sleep(150);
			}
			System.out.println("Off Effect");
			for(int i = 1; i <= pixels; i++) {
				ledDriver.setPixelColourRGB((pixels - i), 0, 0, 0);
				ledDriver.render();
				Thread.sleep(50);
			}
			
			Thread.sleep(1000);
			ledDriver.allOff();
			System.exit(0);
		} catch(Exception e) {
			System.out.println("WS281x ERROR:");
			e.printStackTrace();
		}
	}
	
	

	/*
	 * set individual Color (both)
	 */
	public static void setColorRed(int red) {
		if(ws281xMode) {
			red = dimWS281x(new Color(red, 0, 0)).getRed();
			for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
				ledDriver.setRedComponent(pixel, red);
			}
			ledDriver.render();
			return;
		}
		
		if (red > 100) {
			float newColor = (float) (red / 255. * 100f);
			SoftPwm.softPwmWrite(redPinL, (int) newColor);
			SoftPwm.softPwmWrite(redPinR, (int) newColor);
			return;
		}
		SoftPwm.softPwmWrite(redPinL, red);
		SoftPwm.softPwmWrite(redPinR, red);
	}

	public static void setColorGreen(int green) {
		if(ws281xMode) {
			green = dimWS281x(new Color(0, green, 0)).getGreen();
			for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
				ledDriver.setGreenComponent(pixel, green);
			}
			ledDriver.render();
			return;
		}
		
		if (green > 100) {
			float newColor = (float) (green / 255. * 100f);
			SoftPwm.softPwmWrite(greenPinL, (int) newColor);
			SoftPwm.softPwmWrite(greenPinR, (int) newColor);
			return;
		}
		SoftPwm.softPwmWrite(greenPinL, green);
		SoftPwm.softPwmWrite(greenPinR, green);
	}

	public static void setColorBlue(int blue) {
		if(ws281xMode) {
			blue = dimWS281x(new Color(0, 0, blue)).getBlue();
			for (int pixel = 0; pixel < ledDriver.getNumPixels(); pixel++) {
				ledDriver.setBlueComponent(pixel, blue);
			}
			ledDriver.render();
			return;
		}
		
		if (blue > 100) {
			float newColor = (float) (blue / 255. * 100f);
			SoftPwm.softPwmWrite(bluePinL, (int) newColor);
			SoftPwm.softPwmWrite(bluePinR, (int) newColor);
			return;
		}
		SoftPwm.softPwmWrite(bluePinL, blue);
		SoftPwm.softPwmWrite(bluePinR, blue);
	}

	/*
	 * set left Lamp color
	 */
	public static void setColorLeft(Color color) {
		if(ws281xMode) {
			color = dimWS281x(color);
			for (int pixel = 0; pixel < (ledDriver.getNumPixels() / 2); pixel++) {
				ledDriver.setPixelColour(pixel, PixelColour.createColourRGB(color.getRed(), color.getGreen(), color.getBlue()));
			}
			ledDriver.render();
			return;
		}
		
		float[] colors = color.getRGBColorComponents(null);
		System.out.println(
				"LINKS rot: " + (colors[0] * 100f) + " gr�n: " + (colors[1] * 100f) + " blau: " + (colors[2] * 100f));
		SoftPwm.softPwmWrite(redPinL, (int) (colors[0] * 100f));
		SoftPwm.softPwmWrite(greenPinL, (int) (colors[1] * 100f));
		SoftPwm.softPwmWrite(bluePinL, (int) (colors[2] * 100f));
	}

	public static void setColorSeperateLeft(int colorRed, int colorGreen, int colorBlue) {
		if(ws281xMode) {
			Color c = dimWS281x(new Color(colorRed, colorGreen, colorBlue));
			colorRed = c.getRed();
			colorGreen = c.getGreen();
			colorBlue = c.getBlue();
			for (int pixel = 0; pixel < (ledDriver.getNumPixels() / 2); pixel++) {
				ledDriver.setPixelColour(pixel, PixelColour.createColourRGB(colorRed, colorGreen, colorBlue));
			}
			ledDriver.render();
			return;
		}
		
		if (colorRed > 100 || colorGreen > 100 || colorBlue > 100) {
			float newRed = (float) (colorRed / 255. * 100f);
			float newGreen = (float) (colorGreen / 255. * 100f);
			float newBlue = (float) (colorBlue / 255. * 100f);
			System.out.println("LINKS rot: " + newRed + " gr�n: " + newGreen + " blau: " + newBlue);
			SoftPwm.softPwmWrite(redPinL, (int) newRed);
			SoftPwm.softPwmWrite(greenPinL, (int) newGreen);
			SoftPwm.softPwmWrite(bluePinL, (int) newBlue);
			return;
		}
		System.out.println("LINKS rot: " + colorRed + " gr�n: " + colorGreen + " blau: " + colorBlue);
		SoftPwm.softPwmWrite(redPinL, colorRed);
		SoftPwm.softPwmWrite(greenPinL, colorGreen);
		SoftPwm.softPwmWrite(bluePinL, colorBlue);
	}

	/*
	 * set right Lamp color
	 */
	public static void setColorRight(Color color) {
		if(ws281xMode) {
			color = dimWS281x(color);
			for (int pixel = ledDriver.getNumPixels() / 2; pixel < ledDriver.getNumPixels(); pixel++) {
				ledDriver.setPixelColour(pixel, PixelColour.createColourRGB(color.getRed(), color.getGreen(), color.getBlue()));
			}
			ledDriver.render();
			return;
		}
		
		float[] colors = color.getRGBColorComponents(null);
		System.out.println(
				"RECHTS rot: " + (colors[0] * 100f) + " gr�n: " + (colors[1] * 100f) + " blau: " + (colors[2] * 100f));
		SoftPwm.softPwmWrite(redPinR, (int) (colors[0] * 100f));
		SoftPwm.softPwmWrite(greenPinR, (int) (colors[1] * 100f));
		SoftPwm.softPwmWrite(bluePinR, (int) (colors[2] * 100f));
	}

	public static void setColorSeperateRight(int colorRed, int colorGreen, int colorBlue) {
		if(ws281xMode) {
			Color c = dimWS281x(new Color(colorRed, colorGreen, colorBlue));
			colorRed = c.getRed();
			colorGreen = c.getGreen();
			colorBlue = c.getBlue();
			for (int pixel = ledDriver.getNumPixels() / 2; pixel < ledDriver.getNumPixels(); pixel++) {
				ledDriver.setPixelColour(pixel, PixelColour.createColourRGB(colorRed, colorGreen, colorBlue));
			}
			ledDriver.render();
			return;
		}
		
		if (colorRed > 100 || colorGreen > 100 || colorBlue > 100) {
			float newRed = (float) (colorRed / 255. * 100f);
			float newGreen = (float) (colorGreen / 255. * 100f);
			float newBlue = (float) (colorBlue / 255. * 100f);
			System.out.println("LINKS rot: " + newRed + " gr�n: " + newGreen + " blau: " + newBlue);
			SoftPwm.softPwmWrite(redPinR, (int) newRed);
			SoftPwm.softPwmWrite(greenPinR, (int) newGreen);
			SoftPwm.softPwmWrite(bluePinR, (int) newBlue);
			return;
		}
		System.out.println("RECHTS rot: " + colorRed + " gr�n: " + colorGreen + " blau: " + colorBlue);
		SoftPwm.softPwmWrite(redPinR, colorRed);
		SoftPwm.softPwmWrite(greenPinR, colorGreen);
		SoftPwm.softPwmWrite(bluePinR, colorBlue);
	}

	public static void startTestProgram() {
		try {
			for (int l = 0; l < 3; l++) {
				int ledL = 0;
				int ledR = 0;
				switch (l) {
				case 0:
					ledL = redPinL;
					ledR = redPinR;
					break;
				case 1:
					ledL = greenPinL;
					ledR = greenPinR;
					break;
				case 2:
					ledL = bluePinL;
					ledR = bluePinR;
					break;
				}
				for (int i = 0; i < 100; i++) {
					SoftPwm.softPwmWrite(ledL, i);
					SoftPwm.softPwmWrite(ledR, i);
					Thread.sleep(20);
				}
				Thread.sleep(100);
				for (int i = 100; i >= 0; i--) {
					SoftPwm.softPwmWrite(ledL, i);
					SoftPwm.softPwmWrite(ledR, i);
					Thread.sleep(20);
				}
			}
			for (int r = 100; r != 0; r--) {
				setColorSeperateLeft(r, 100, 100);
				setColorSeperateRight(r, 100, 100);
				Thread.sleep(100);
			}
			for (int g = 100; g != 0; g--) {
				setColorSeperateLeft(100, g, 100);
				setColorSeperateRight(100, g, 100);
				Thread.sleep(100);
			}
			for (int b = 100; b != 0; b--) {
				setColorSeperateLeft(100, 100, b);
				setColorSeperateRight(100, 100, b);
				Thread.sleep(100);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Start / Close
	 */
	public static void bootAnimation() {
		if(DataStorage.getData(DataStorage.SETTINGS_MODE).equals("WS281x")) {
			//WS281x
			try {
				for(int i = 0; i < pixels / 2; i++) {
					ledDriver.setPixelColour(i, PixelColour.RED);
					ledDriver.setPixelColour(pixels - 1 - i, PixelColour.RED);
					ledDriver.render();
					Thread.sleep(30);
				}
				for(int i = 0; i < pixels / 2; i++) {
					ledDriver.setPixelColour(pixels / 2 - 1 - i, PixelColour.BLUE);
					ledDriver.setPixelColour(pixels / 2 + i, PixelColour.BLUE);
					ledDriver.render();
					Thread.sleep(30);
				}
				ledDriver.allOff();
				ledDriver.render();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		} else if(DataStorage.SETTINGS_MODE.equals("RGB")) {
			//RGB LED
			try {
				Main.setColorLeft(Color.RED);
				Main.setColorRight(Color.BLUE);
				Thread.sleep(100);
				Main.setColorLeft(Color.BLUE);
				Main.setColorRight(Color.RED);
				Thread.sleep(100);
				Main.setColorLeft(Color.RED);
				Main.setColorRight(Color.BLUE);
				Thread.sleep(100);
				Main.setColorLeft(Color.BLACK);
				Main.setColorRight(Color.BLACK);
			} catch(Exception e) {
				e.printStackTrace();
			}
		}	
	}

	public static void close() {
		Main.setColorLeft(Color.BLACK);
		Main.setColorRight(Color.BLACK);
		SoftPwm.softPwmWrite(redPinL, 0);
		SoftPwm.softPwmWrite(greenPinL, 0);
		SoftPwm.softPwmWrite(bluePinL, 0);
		SoftPwm.softPwmWrite(redPinR, 0);
		SoftPwm.softPwmWrite(greenPinR, 0);
		SoftPwm.softPwmWrite(bluePinR, 0);
		SoftPwm.softPwmStop(1);
		ledDriver.close();
		Server.stop();
		DataStorage.save();
	}

}
