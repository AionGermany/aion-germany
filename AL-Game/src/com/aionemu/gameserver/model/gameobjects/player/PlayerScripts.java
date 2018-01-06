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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.dao.HouseScriptsDAO;
import com.aionemu.gameserver.model.house.PlayerScript;
import com.aionemu.gameserver.utils.xml.CompressUtil;

/**
 * @author Rolandas
 */
public class PlayerScripts {

	private static final Logger logger = LoggerFactory.getLogger(PlayerScripts.class);
	private final Map<Integer, PlayerScript> scripts;
	private final int houseObjId;

	public PlayerScripts(int houseObjectId) {
		this.scripts = new HashMap<Integer, PlayerScript>(8);
		for (int index = 0; index < 8; index++) {
			this.scripts.put(index, new PlayerScript());
		}
		this.houseObjId = houseObjectId;
	}

	public Map<Integer, PlayerScript> getScripts() {
		return Collections.unmodifiableMap(scripts);
	}

	public boolean addScript(int position, String scriptXML) {
		PlayerScript script = scripts.get(position);

		if (scriptXML == null) {
			script.setData(null, -1);
		}
		else if (StringUtils.EMPTY.equals(scriptXML)) {
			script.setData(new byte[0], 0);
		}
		try {
			byte[] bytes = CompressUtil.Compress(scriptXML);
			int oldLength = bytes.length;
			bytes = Arrays.copyOf(bytes, bytes.length + 8);
			for (int i = oldLength; i < bytes.length; i++) {
				bytes[i] = -51; // Add NC shit bytes, without which fails to load :)
			}
			script.setData(bytes, scriptXML.length() * 2);
		}
		catch (Exception ex) {
			logger.error("Script compression failed: " + ex);
			return false;
		}
		return script == null;
	}

	public String getUncompressedScript(int position) {
		if (!scripts.containsKey(position)) {
			return null;
		}

		PlayerScript script = scripts.get(position);
		byte[] bytes = null;

		script.readLock();
		bytes = script.getCompressedBytes();
		script.readUnlock();

		if (bytes == null) {
			return null;
		}

		if (bytes.length == 0) {
			return StringUtils.EMPTY;
		}

		try {
			return CompressUtil.Decompress(bytes);
		}
		catch (Exception ex) {
			logger.error("Script decompression failed: " + ex);
			return null;
		}
	}

	public boolean addScript(int position, byte[] compressedXML, int uncompressedSize) {
		String content = null;
		int size = -1;

		if (compressedXML == null) {
			// Nothing to do
		}
		else if (compressedXML.length == 0) {
			content = StringUtils.EMPTY;
			size = 0;
		}
		else {
			try {
				content = CompressUtil.Decompress(compressedXML);
				byte[] bytes = content.getBytes("UTF-16LE");
				if (bytes.length != uncompressedSize) {
					return false;
				}
				size = uncompressedSize;
			}
			catch (Exception ex) {
				return false;
			}
		}

		PlayerScript script = scripts.get(position);
		script.readLock();
		byte[] bytes = script.getCompressedBytes();
		script.readUnlock();
		script.setData(compressedXML, size);

		if (bytes == null) {
			DAOManager.getDAO(HouseScriptsDAO.class).addScript(houseObjId, position, content);
		}
		else {
			DAOManager.getDAO(HouseScriptsDAO.class).updateScript(houseObjId, position, content);
		}

		if (HousingConfig.HOUSE_SCRIPT_DEBUG) {
			logger.info(content);
		}

		return true;
	}

	public boolean removeScript(int position) {
		PlayerScript script = scripts.get(position);

		script.readLock();
		byte[] bytes = script.getCompressedBytes();
		script.readUnlock();

		if (bytes == null) {
			return false;
		}
		else {
			script.setData(null, -1);
			DAOManager.getDAO(HouseScriptsDAO.class).deleteScript(houseObjId, position);
		}
		return true;
	}

	public int getSize() {
		return 8;
	}
}
