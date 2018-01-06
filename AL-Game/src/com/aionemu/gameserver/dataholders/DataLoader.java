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
package com.aionemu.gameserver.dataholders;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.HiddenFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is responsible for loading data from static .txt files.<br>
 * It's used as base class of {@link NpcData} and {@link SpawnData}.<br>
 * <br>
 * <font color="red">NOTICE: </font> This class is used temporarily and later will be removed and npc and spawn data will be loaded with xml loader.<br>
 * <br>
 * <font color="red"><b>Do not use this class for anything else than <tt>NpcData</tt> or <tt>SpawnData</tt></b></font>
 *
 * @author Luno
 */
abstract class DataLoader {

	/**
	 * The logger used for <tt>DataLoader</tt> and its subclasses
	 */
	protected Logger log = LoggerFactory.getLogger(getClass().getName());
	/**
	 * Relative path to directory containing .txt files with static data
	 */
	private static final String PATH = "./data/static_data/";
	/**
	 * File containing data to load ( may be file or directory )
	 */
	private File dataFile;

	/**
	 * Constructor that is supposed to be called from subclass.
	 *
	 * @param file
	 *            file or directory in the static data directory, containing data that will be loaded
	 */
	DataLoader(String file) {
		this.dataFile = new File(PATH + file);
	}

	/**
	 * This method is supposed to be called from subclass to initialize data loading process.<br>
	 * <br>
	 * This method is using file given in the constructor to load the data and there are two possibilities:
	 * <ul>
	 * <li>Given file is file is in deed the <b>file</b> then it's forwarded to {@link #loadFile(File)} method</li>
	 * <li>Given file is a <b>directory</b>, then this method is obtaining list of all visible .txt files in this directory and subdirectiores ( except hidden ones and those named "new" ) and call
	 * {@link #loadFile(File)} for each of these files.
	 * </ul>
	 */
	@SuppressWarnings("deprecation")
	protected void loadData() {
		if (dataFile.isDirectory()) {
			Collection<?> files = FileUtils.listFiles(dataFile, FileFilterUtils.andFileFilter(FileFilterUtils.andFileFilter(FileFilterUtils.notFileFilter(FileFilterUtils.nameFileFilter("new")), FileFilterUtils.suffixFileFilter(".txt")), HiddenFileFilter.VISIBLE), HiddenFileFilter.VISIBLE);

			for (Object file1 : files) {
				File f = (File) file1;
				loadFile(f);
			}
		}
		else {
			loadFile(dataFile);
		}
	}

	/**
	 * This method is loading data from particular .txt file.
	 *
	 * @param file
	 *            a file which the data is loaded from.<br>
	 *            The method is loading the file row by row, omitting those started with "#" sign.<br>
	 *            Every read row is then forwarded to {@link #parse(String)} method, which should be overriden in subclcass.
	 */
	private void loadFile(File file) {
		LineIterator it = null;
		try {
			it = FileUtils.lineIterator(file);
			while (it.hasNext()) {
				String line = it.nextLine();
				if (line.isEmpty() || line.startsWith("#")) {
					continue;
				}
				parse(line);
			}
		}
		catch (IOException e) {
			log.error("Error while loading " + getClass().getSimpleName() + ", file: " + file.getPath(), e);
		}
		finally {
			LineIterator.closeQuietly(it);
		}
	}

	/**
	 * This method must be overriden in every subclass and is responsible for parsing given <tt>dataEntry</tt> String which represents one row from data file.
	 *
	 * @param dataEntry
	 *            A String containing data about a data entry, that is to be parsed by this method.
	 */
	protected abstract void parse(String dataEntry);

	/**
	 * Saves data to the file. Used only by {@link SpawnData}.
	 *
	 * @return true if the data was successfully saved, false - if some error occurred.
	 */
	public boolean saveData() {
		String desc = PATH + getSaveFile();

		log.info("Saving " + desc);

		FileWriter fr = null;
		try {
			fr = new FileWriter(desc);

			saveEntries(fr);

			fr.flush();

			return true;
		}
		catch (Exception e) {
			log.error("Error while saving " + desc, e);
			return false;
		}
		finally {
			if (fr != null) {
				try {
					fr.close();
				}
				catch (Exception e) {
					log.error("Error while closing save data file", e);
				}
			}
		}
	}

	/**
	 * Name of the file which is used to store data in.<br>
	 * This method must be overriden in sublass if we want to be able to store its data. It's used only in {@link SpawnData} and should not be used anywhere else.
	 *
	 * @return name of the file
	 */
	protected abstract String getSaveFile();

	/**
	 * This method must be overriden in subclass which we want to be able to save data. It's responsibility is basicly to put data into given FileWriter instance.
	 *
	 * @param fileWriter
	 * @throws Exception
	 */
	protected void saveEntries(FileWriter fileWriter) throws Exception {
		// TODO Auto-generated method stub
	}
}
