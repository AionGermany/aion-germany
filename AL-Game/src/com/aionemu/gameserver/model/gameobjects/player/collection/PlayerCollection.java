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
package com.aionemu.gameserver.model.gameobjects.player.collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.gameserver.model.templates.collection.CollectionType;

public class PlayerCollection {

    private Map<CollectionType, PlayerCollectionInfos> collectionInfos;
    private List<PlayerCollectionEntry> completeCollection;
    private Map<Integer, PlayerCollectionEntry> playerCollectionEntry;
    
    public PlayerCollection() {
		collectionInfos = new HashMap<CollectionType, PlayerCollectionInfos>();
		completeCollection = new ArrayList<PlayerCollectionEntry>();
		playerCollectionEntry = new HashMap<Integer, PlayerCollectionEntry>();
	}
    
    public List<PlayerCollectionEntry> getCompleteCollection() {
        return completeCollection;
    }
    
    public void setCompleteCollection(List<PlayerCollectionEntry> completeCollection) {
        this.completeCollection = completeCollection;
    }
    
    public Map<Integer, PlayerCollectionEntry> getPlayerCollectionEntry() {
        return playerCollectionEntry;
    }
    
    public void setPlayerCollectionEntry(Map<Integer, PlayerCollectionEntry> playerCollectionEntry) {
        this.playerCollectionEntry = playerCollectionEntry;
    }
    
    public Map<CollectionType, PlayerCollectionInfos> getCollectionInfos() {
        return collectionInfos;
    }
    
    public void setCollectionInfos(Map<CollectionType, PlayerCollectionInfos> collectionInfos) {
        this.collectionInfos = collectionInfos;
    }
}
