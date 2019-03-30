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


package com.aionemu.packetsamurai.utils.collector;

import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

/**
 * 
 * @author ViAl
 *
 */
public class DataLoader {

	private Object data;
	
	public DataLoader(String fileName, Object object) throws JAXBException {
		this.data = object;
		File file = new File(fileName);
        JAXBContext jaxbContext = JAXBContext.newInstance(data.getClass());
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        this.data = jaxbUnmarshaller.unmarshal(file);
	}
	
	public Object getData() {
		return this.data;
	}
}
