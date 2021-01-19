package com.aionemu.gameserver.model.gameobjects;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
public enum MinionAction {

	ADOPT(0),
	DISMISS(1),
	RENAME(2),
	LOCK(3),
	SUMMON(4),
	UNSUMMON(5),
	GROWTH(6),
	EVOLVE(7),
	COMBINE(8),
	FUNCTION_SETTING(9),
	FUNCTION(10),
	ENERGY_RECHARGE(11),
	AUTO_FUNCTION(12),
	UNK(13),
	BUFFING(14),
	UNKNOWN(255);

	private static TIntObjectHashMap<MinionAction> minionActions;

	static {
		minionActions = new TIntObjectHashMap<MinionAction>();
		for (MinionAction action : values()) {
			minionActions.put(action.getActionId(), action);
		}
	}

	private int actionId;

	private MinionAction(int actionId) {
		this.actionId = actionId;
	}

	public int getActionId() {
		return actionId;
	}

	public static MinionAction getActionById(int actionId) {
		MinionAction action = minionActions.get(actionId);
		return action != null ? action : UNKNOWN;
	}
}
