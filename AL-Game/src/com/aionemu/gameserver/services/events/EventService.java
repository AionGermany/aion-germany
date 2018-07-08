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
package com.aionemu.gameserver.services.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.dao.EventItemsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EventType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.event.EventTemplate;
import com.aionemu.gameserver.model.templates.quest.XMLStartCondition;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Rolandas
 */
public class EventService {

	Logger log = LoggerFactory.getLogger(EventService.class);
	private boolean isStarted = false;
	private Future<?> checkTask = null;
	private final List<EventTemplate> activeEvents;
	TIntObjectHashMap<List<EventTemplate>> eventsForStartQuest = new TIntObjectHashMap<List<EventTemplate>>();
	TIntObjectHashMap<List<EventTemplate>> eventsForMaintainQuest = new TIntObjectHashMap<List<EventTemplate>>();

	private static class SingletonHolder {

		protected static final EventService instance = new EventService();
	}

	public static final EventService getInstance() {
		return SingletonHolder.instance;
	}

	private EventService() {
		activeEvents = Collections.synchronizedList(DataManager.EVENT_DATA.getActiveEvents());
		updateQuestMap();
	}

	/**
	 * This method is called just after player logged in to the game.<br>
	 * <br>
	 * <b><font color='red'>NOTICE: </font>This method must not be called from anywhere else.</b>
	 */
	public void onPlayerLogin(Player player) {
		List<Integer> activeStartQuests = new ArrayList<Integer>();
		List<Integer> activeMaintainQuests = new ArrayList<Integer>();
		TIntObjectHashMap<List<EventTemplate>> map1 = null;
		TIntObjectHashMap<List<EventTemplate>> map2 = null;

		synchronized (activeEvents) {
			for (EventTemplate et : activeEvents) {
				if (et.isActive()) {
					activeStartQuests.addAll(et.getStartableQuests());
					activeMaintainQuests.addAll(et.getMaintainableQuests());
				}
			}
			map1 = new TIntObjectHashMap<>(eventsForStartQuest);
			map2 = new TIntObjectHashMap<>(eventsForMaintainQuest);
		}

		StartOrMaintainQuests(player, activeStartQuests.listIterator(), map1, true);
		StartOrMaintainQuests(player, activeMaintainQuests.listIterator(), map2, false);

		activeStartQuests.clear();
		activeMaintainQuests.clear();
		map1.clear();
		map2.clear();
	}

	void StartOrMaintainQuests(Player player, ListIterator<Integer> questList, TIntObjectHashMap<List<EventTemplate>> templateMap, boolean start) {
		while (questList.hasNext()) {
			int questId = questList.next();
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			QuestEnv cookie = new QuestEnv(null, player, questId, 0);
			QuestStatus status = qs == null ? QuestStatus.START : qs.getStatus();

			if (QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel())) {
				QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
				if (template.getRacePermitted() != null) {
					if (template.getRacePermitted().ordinal() != player.getCommonData().getRace().ordinal()) {
						continue;
					}
				}

				if (template.getClassPermitted().size() != 0) {
					if (!template.getClassPermitted().contains(player.getCommonData().getPlayerClass())) {
						continue;
					}
				}

				if (template.getGenderPermitted() != null) {
					if (template.getGenderPermitted().ordinal() != player.getGender().ordinal()) {
						continue;
					}
				}

				int amountOfStartConditions = template.getXMLStartConditions().size();
				int fulfilledStartConditions = 0;
				if (amountOfStartConditions != 0) {
					for (XMLStartCondition startCondition : template.getXMLStartConditions()) {
						if (startCondition.check(player, false)) {
							fulfilledStartConditions++;
						}
					}
					if (fulfilledStartConditions < 1) {
						continue;
					}
				}

				if (qs != null) {
					if (qs.getCompleteTime() != null || status == QuestStatus.COMPLETE) {
						DateTime completed = null;
						if (qs.getCompleteTime() == null) {
							completed = new DateTime(0);
						}
						else {
							completed = new DateTime(qs.getCompleteTime().getTime());
						}

						if (templateMap.containsKey(questId)) {
							for (EventTemplate et : templateMap.get(questId)) {
								// recurring event, reset it
								if (et.getStartDate().isAfter(completed)) {
									if (start) {
										status = QuestStatus.START;
										qs.setQuestVar(0);
										qs.setCompleteCount(0);
										qs.setStatus(status);
									}
									break;
								}
							}
						}
					}
					// re-register quests
					if (status == QuestStatus.COMPLETE) {
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, status, qs.getQuestVars().getQuestVars()));
					}
					else {
						QuestService.startEventQuest(cookie, status);
					}
				}
				else if (start) {
					QuestService.startEventQuest(cookie, status);
				}
			}
		}
	}

	public boolean isStarted() {
		return isStarted;
	}

	public void startCronCleanBase() {
		int size = 0;
		if (activeEvents != null) {
			for (EventTemplate et : activeEvents) {
				if (et.isActive() && et.getInventoryDrop() != null && et.getInventoryDrop().getCleanTime() != 0) {
					final int itemId = et.getInventoryDrop().getDropItem();
					CronService.getInstance().schedule(new Runnable() {

						@Override
						public void run() {
							DAOManager.getDAO(EventItemsDAO.class).deleteItems(itemId);
							for (Player p : World.getInstance().getAllPlayers()) {
								p.removeItemMaxThisCount(itemId);
							}
						}
					}, "0 0 " + et.getInventoryDrop().getCleanTime() + " ? * *");
					size++;
				}
			}
		}
		log.info("[EventService] EventCron: clean limits in db " + size + " size.");
	}

	public void start() {
		if (isStarted) {
			checkTask.cancel(false);
		}

		isStarted = true;

		int CHECK_TIME_PERIOD = 1000 * 60 * 5;
		checkTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				checkEvents();
			}
		}, 0, CHECK_TIME_PERIOD);
	}

	public void stop() {
		if (isStarted) {
			checkTask.cancel(false);
		}
		checkTask = null;
		isStarted = false;
	}

	private void checkEvents() {
		List<EventTemplate> newEvents = new ArrayList<EventTemplate>();
		List<EventTemplate> allEvents = DataManager.EVENT_DATA.getAllEvents();

		for (EventTemplate et : allEvents) {
			if (et.isActive()) {
				newEvents.add(et);
				et.Start();
			}
		}

		synchronized (activeEvents) {
			for (EventTemplate et : activeEvents) {
				if (et.isExpired() || !DataManager.EVENT_DATA.Contains(et.getName())) {
					et.Stop();
				}
			}

			activeEvents.clear();
			eventsForStartQuest.clear();
			eventsForMaintainQuest.clear();
			activeEvents.addAll(newEvents);
			updateQuestMap();
		}

		newEvents.clear();
		allEvents.clear();
	}

	private void updateQuestMap() {
		for (EventTemplate et : activeEvents) {
			for (int qId : et.getStartableQuests()) {
				if (!eventsForStartQuest.containsKey(qId)) {
					eventsForStartQuest.put(qId, new ArrayList<EventTemplate>());
				}
				eventsForStartQuest.get(qId).add(et);
			}
			for (int qId : et.getMaintainableQuests()) {
				if (!eventsForMaintainQuest.containsKey(qId)) {
					eventsForMaintainQuest.put(qId, new ArrayList<EventTemplate>());
				}
				eventsForMaintainQuest.get(qId).add(et);
			}
		}
	}

	public boolean checkQuestIsActive(int questId) {
		synchronized (activeEvents) {
			if (eventsForStartQuest.containsKey(questId) || eventsForMaintainQuest.containsKey(questId)) {
				return true;
			}
		}
		return false;
	}

	public EventType getEventType() {
		if (EventsConfig.ENABLE_EVENT_SERVICE) {
			for (EventTemplate et : activeEvents) {
				String theme = et.getTheme();
				if (theme != null) {
					EventType type = EventType.getEventType(theme);
					if (et.isActive() && !type.equals(EventType.NONE)) {
						return type;
					}
				}
			}
		}
		return EventType.NONE;
	}

	public List<EventTemplate> getActiveEvents() {
		return activeEvents;
	}
}
