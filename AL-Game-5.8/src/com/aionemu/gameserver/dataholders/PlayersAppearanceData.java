package com.aionemu.gameserver.dataholders;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.appearances.PlayerApp;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author CoolyT
 */
@XmlRootElement(name = "players")
public class PlayersAppearanceData {

	@XmlElement(name = "player")
	public static FastList<PlayerApp> players = new FastList<PlayerApp>();
	public static FastMap<String, PlayerApp> playerApps = new FastMap<String, PlayerApp>();

	public void addPlayer(PlayerApp player) {
		if (players.contains(player))
			players.add(player);
	}

	void afterUnmarshal(Unmarshaller u, Object parent) {
		for (PlayerApp ap : players) {
			playerApps.put(ap.name.toLowerCase(), ap);
		}
	}

	public static FastList<PlayerApp> getApp() {
		return players;
	}

	public PlayerApp getAppearanceByName(String name) {
		name = name.toLowerCase();
		PlayerApp app = new PlayerApp();
		if (playerApps.containsKey(name))
			return playerApps.get(name);
		return app;
	}

	public int size() {
		return players.size();
	}
}
