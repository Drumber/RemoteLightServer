package de.lars.remotelight.animations;

import java.awt.Color;

import de.lars.remotelight.Main;

public class AnimationHandler {
	
	private static boolean active;
	private static String activeAnimation = null;
	
	public static final String FLASH = "FLASH";
	public static final String JUMP = "JUMP";
	public static final String RAINBOW = "RAINBOW";
	public static final String PULSE = "PULSE";
	public static final String BLINK = "BLINK";
	
	
	public static void stop() {
		FlashAnimation.stop();
		JumpAnimation.stop();
		RainbowAnimation.stop();
		PulseAnimation.stop();
		BlinkAnimation.stop();
		Main.setColorLeft(Color.BLACK);
		Main.setColorRight(Color.BLACK);
	}
	
	public static void startAnimation(String animation, int speed) {
		activeAnimation = animation.toUpperCase();
		active = true;
		stop();
		switch (animation.toUpperCase()) {
		case "FLASH":
			FlashAnimation.start(speed);
			break;
		case "JUMP":
			JumpAnimation.start(speed);
			break;
		case "RAINBOW":
			RainbowAnimation.start(speed);
			break;
		case "PULSE":
			PulseAnimation.start(speed);
			break;
		case "BLINK":
			BlinkAnimation.start(speed);
			break;
		default:
			active = false;
			activeAnimation = null;
			break;
		}
	}
	
	public static void setAnimationSpeed(int speed) {
		if(active) {
			FlashAnimation.setSpeed(speed);
			JumpAnimation.setSpeed(speed);
			PulseAnimation.setSpeed(speed);
			RainbowAnimation.setSpeed(speed);
			BlinkAnimation.setSpeed(speed);
		}
	}
	
	public static String getActiveAnimation() {
		if(active) {
			return activeAnimation;
		} else
			return null;
	}
	
	public static boolean isActive() {
		return active;
	}

}
