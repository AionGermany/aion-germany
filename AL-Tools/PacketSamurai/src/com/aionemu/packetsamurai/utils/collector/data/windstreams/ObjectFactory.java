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

package com.aionemu.packetsamurai.utils.collector.data.windstreams;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the com.packetsamurai.data.aion.windstreams package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: com.packetsamurai.data.aion.windstreams
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Windstreams }
     * 
     */
    public Windstreams createWindstreams() {
        return new Windstreams();
    }

    /**
     * Create an instance of {@link WindFlight }
     * 
     */
    public WindFlight createWindFlight() {
        return new WindFlight();
    }

    /**
     * Create an instance of {@link StreamLocations }
     * 
     */
    public StreamLocations createStreamLocations() {
        return new StreamLocations();
    }

    /**
     * Create an instance of {@link Location2D }
     * 
     */
    public Location2D createLocation2D() {
        return new Location2D();
    }

}
