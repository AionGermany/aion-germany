/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aionl.slf4j.conversion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.qos.logback.core.FileAppender;

/**
 * This class is appender that zips old file instead of appending it.<br>
 * File is recognized as old if it's lastModified() is < JVM startup time.<br>
 * So we can have per-run appending.
 * <p/>
 * <br>
 * 
 * @author SoulKeeper
 * @author KID 05-may-2011 log start time is written into file.
 */
public class TruncateToZipFileAppender extends FileAppender<Object> {

	private static final Logger log = LoggerFactory.getLogger(TruncateToZipFileAppender.class);
	private String backupDir = "log/backup";

	@Override
	public void openFile(String fname) throws IOException {
		File file = new File(fname);
		if (file.exists()) {
			truncate(file);
		}
		super.openFile(fname);
	}

	/**
	 * This method creates archive with file instead of deleting it.
	 * 
	 * @param file
	 *            file to truncate
	 */
	protected void truncate(File file) {
		File backupRoot = new File(backupDir);
		if ((!backupRoot.exists()) && (!backupRoot.mkdirs())) {
			log.warn("Can't create backup dir for backup storage");
			return;
		}

		String date = "";
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			date = reader.readLine().split("\f")[1];
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File zipFile = new File(backupRoot, file.getName() + "." + date + ".zip");
		ZipOutputStream zos = null;
		FileInputStream fis = null;
		try {
			zos = new ZipOutputStream(new FileOutputStream(zipFile));
			ZipEntry entry = new ZipEntry(file.getName());
			entry.setMethod(8);
			entry.setCrc(FileUtils.checksumCRC32(file));
			zos.putNextEntry(entry);
			fis = FileUtils.openInputStream(file);

			byte[] buffer = new byte[1024];
			int readed;
			while ((readed = fis.read(buffer)) != -1) {
				zos.write(buffer, 0, readed);
			}
		} catch (Exception e) {
			log.warn("Can't create zip file", e);
		} finally {
			if (zos != null) {
				try {
					zos.close();
				} catch (IOException e) {
					log.warn("Can't close zip file", e);
				}
			}

			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.warn("Can't close zipped file", e);
				}
			}
		}

		if (!file.delete()) {
			log.warn("Can't delete old log file " + file.getAbsolutePath());
		}
	}
}