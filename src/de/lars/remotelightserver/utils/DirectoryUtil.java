package de.lars.remotelightserver.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Date;

import org.tinylog.Logger;

public class DirectoryUtil {
	
	public final static String DATA_FILE_NAME = "data.dat";
	public final static String LOG_DIR_NAME = "logs";
	
	public static String getLogsPath() {
		return (LOG_DIR_NAME + File.separator);
	}
	
	/**
	 * 
	 * Deletes all log files older than defined days
	 */
	public static void deleteOldLogs(int days) {
		File dir = new File(DirectoryUtil.getLogsPath());
		dir.mkdir();
		for(File log : dir.listFiles()) {
			long diff = new Date().getTime() - log.lastModified();

			if (diff > days * 24 * 60 * 60 * 1000) {
			    log.delete();
			}
		}
	}
	
	public static void copyAndRenameLog(File logfile, String newName) {
		try {
			Files.copy(logfile.toPath(), new File(getLogsPath() + newName).toPath(), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			Logger.error(e, "Could not copy log file! (" + logfile.getAbsolutePath() + " -> " + getLogsPath() + newName + ")");
		}
	}

}