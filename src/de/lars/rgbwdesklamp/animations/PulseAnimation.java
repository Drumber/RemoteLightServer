package de.lars.rgbwdesklamp.animations;

import de.lars.rgbwdesklamp.Main;

public class PulseAnimation {
	private static boolean active;
	private static int speed;
	
	public static void start(int anispeed) {
		if(!active) {
			new Thread() {
				
				@Override
				public void run() {
					active = true;
					speed = anispeed;
					String side = "left"; //left/right/both
					String mode = "red+";
					int red = 0, blue = 0, green = 0;
					while(active) {
						if(side.equals("left") || side.equals("both")) {
							// RED
							if(mode.equals("red+")) {
								if(red < 100) red++;
								else mode = "red-";
							} else if(mode.equals("red-")) {
								if(red > 0) red--;
								else {
									mode = "green+";
									side = "right";
								}
							// GREEN
							} else if(mode.equals("green+")) {
								if(green < 100) green++;
								else mode = "green-";
							} else if(mode.equals("green-")) {
								if(green > 0) green--;
								else {
									mode = "blue+";
									side = "right";
								}
							// BLUE
							} else if(mode.equals("blue+")) {
								if(blue < 100) blue++;
								else mode = "blue-";
							} else if(mode.equals("blue-")) {
								if(blue > 0) blue--;
								else {
									mode = "red+";
									side = "right";
								}
							}
							Main.setColorSeperateLeft(red, green, blue);
						} else if(side.equals("right") || side.equals("both")) {
							// RED
							if(mode.equals("red+")) {
								if(red < 100) red++;
								else mode = "red-";
							} else if(mode.equals("red-")) {
								if(red > 0) red--;
								else {
									mode = "green+";
									side = "left";
								}
							// GREEN
							} else if(mode.equals("green+")) {
								if(green < 100) green++;
								else mode = "green-";
							} else if(mode.equals("green-")) {
								if(green > 0) green--;
								else {
									mode = "blue+";
									side = "left";
								}
							// BLUE
							} else if(mode.equals("blue+")) {
								if(blue < 100) blue++;
								else mode = "blue-";
							} else if(mode.equals("blue-")) {
								if(blue > 0) blue--;
								else {
									mode = "red+";
									side = "left";
								}
							}
							Main.setColorSeperateRight(red, green, blue);
						}
						try {
							Thread.sleep(speed);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}.start();
		}
	}
	
	public static void stop() {
		if(active) {
			active = false;
		}
	}
	
	public static boolean isRunning() {
		return active;
	}
	
	public static void setSpeed(int anispeed) {
		speed = anispeed;
	}
}
