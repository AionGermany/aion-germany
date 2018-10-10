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

package com.aionemu.commons.utils.xml;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;

public class StringSchemaOutputResolver extends SchemaOutputResolver {

	private StringWriter sw = null;

	@Override
	public Result createOutput(String namespaceUri, String suggestedFileName) throws IOException {
		sw = new StringWriter();
		StreamResult sr = new StreamResult();

		// If it's not set - schemagen throws AssertionError
		sr.setSystemId(String.valueOf(System.currentTimeMillis()));

		sr.setWriter(sw);
		return sr;
	}

	public String getSchemma() {
		return sw != null ? sw.toString() : null;
	}
}