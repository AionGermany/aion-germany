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
package com.aionemu.gameserver.model.account;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.transform_book.TransformBookTemplate;

public class AccountTransfo {

	private TransformBookTemplate template;
	private int cardId;
	private int count;

	public AccountTransfo(int cardId, int count) {
		this.template = DataManager.TRANSFORM_BOOK_DATA.getTransformBookById(cardId);
		this.cardId = cardId;
		this.count = count;
	}

	public TransformBookTemplate getTemplate() {
		return template;
	}

	public int getCardId() {
		return cardId;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
