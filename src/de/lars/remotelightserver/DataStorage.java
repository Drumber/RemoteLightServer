package de.lars.remotelightserver;

import java.io.File;
import java.io.IOException;

import com.blogspot.debukkitsblog.util.FileStorage;


public class DataStorage {
	
	private static FileStorage storage;
	
	public final static String SETTINGS_BOOT_ANI = "settings_boot_ani";
	public final static String SETTINGS_MODE = "settings_mode";
	public final static String WS281x_PIXEL_NUM = "ws281x_pixel_num";
	

	public static void start() {
		try {
			storage = new FileStorage(new File("data.dat"));
		} catch (IllegalArgumentException | IOException e) {
			e.printStackTrace();
		}
		if(!isCreated()) System.out.println("ERROR: Could not create data file!");;
	}
	
	public static void store(String key, Object data) {
		try {
			if(isCreated()) {
				if(storage.hasKey(key)) {
					storage.remove(key);
				}
				storage.store(key, data);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("ERROR: Could not save data!");
		}
	}
	
	public static void remove(String key) {
		try {
			storage.remove(key);
		} catch (IOException e) {
			//do nothing
		}
	}
	
	public static void save() {
		if(isCreated()) {
			try {
				storage.save();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Object getData(String key) {
		try {
			if(isCreated())
				return storage.get(key);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR: Could not read data file!");
		}
		return null;
	}
	
	public static FileStorage getstorage() {
		return storage;
	}
	
	public static boolean isStored(String key) {
		try {
			Object data = DataStorage.getData(key);
			if(data != null)
				return true;
		} catch (Exception e) {
			//do nothing
		}
		return false;
	}
	
	public static boolean isCreated() {
		if(new File("data.dat").isFile())
			return true;
		else
			return false;
	}

	
}
