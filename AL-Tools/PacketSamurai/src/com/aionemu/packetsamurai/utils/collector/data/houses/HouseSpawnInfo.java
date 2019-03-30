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
package com.aionemu.packetsamurai.utils.collector.data.houses;

import com.aionemu.packetsamurai.parser.valuereader.ClientStringReader;

public class HouseSpawnInfo {

    public int nameId;
    public int creatorId;
    public float X;
    public float Y;
    public float Z;
    private int h;
  
    public HouseSpawnInfo(int creatorId, int nameId) {
        this.creatorId = creatorId;
        this.nameId = nameId;
    }
  
    public SpawnType getSpawnType() {
        String npcName = ClientStringReader.getStringById(this.nameId).toLowerCase();
        if (npcName.indexOf("butler") != -1) {
            return SpawnType.MANAGER;
        }
        if (npcName.indexOf("crystal") != -1) {
            return SpawnType.TELEPORT;
        }
        return SpawnType.SIGN;
    }
  
    public void setCoords(float x, float y, float z, int h) {
        this.X = x;
        this.Y = y;
        this.Z = z;
        this.h = (h & 0xFF);
        if (h > 120) {
            this.h -= 120;
        }
    }
  
    public int getH() {
        return this.h;
    }
}
