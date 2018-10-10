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

package com.aionemu.commons.scripting.classlistener;

import java.lang.reflect.Modifier;
import java.util.Map;

import org.quartz.JobDetail;

import com.aionemu.commons.scripting.metadata.Scheduled;
import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.ClassUtils;

public class ScheduledTaskClassListener implements ClassListener {

	@Override
	@SuppressWarnings({ "unchecked" })
	public void postLoad(Class<?>[] classes) {
		for (Class<?> clazz : classes) {
			if (isValidClass(clazz)) {
				scheduleClass((Class<? extends Runnable>) clazz);
			}
		}
	}

	@Override
	@SuppressWarnings({ "unchecked" })
	public void preUnload(Class<?>[] classes) {
		for (Class<?> clazz : classes) {
			if (isValidClass(clazz)) {
				unScheduleClass((Class<? extends Runnable>) clazz);
			}
		}
	}

	public boolean isValidClass(Class<?> clazz) {

		if (!ClassUtils.isSubclass(clazz, Runnable.class)) {
			return false;
		}

		final int modifiers = clazz.getModifiers();

		if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers))
			return false;

		if (!Modifier.isPublic(modifiers))
			return false;

		if (!clazz.isAnnotationPresent(Scheduled.class)) {
			return false;
		}

		Scheduled scheduled = clazz.getAnnotation(Scheduled.class);
		if (scheduled.disabled()) {
			return false;
		}

		if (scheduled.value().length == 0) {
			return false;
		}

		return true;
	}

	protected void scheduleClass(Class<? extends Runnable> clazz) {
		Scheduled metadata = clazz.getAnnotation(Scheduled.class);

		try {
			if (metadata.instancePerCronExpression()) {
				for (String s : metadata.value()) {
					getCronService().schedule(clazz.newInstance(), s, metadata.longRunningTask());
				}
			} else {
				Runnable r = clazz.newInstance();
				for (String s : metadata.value()) {
					getCronService().schedule(r, s, metadata.longRunningTask());
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Failed to schedule runnable " + clazz.getName(), e);
		}
	}

	protected void unScheduleClass(Class<? extends Runnable> clazz) {
		Map<Runnable, JobDetail> map = getCronService().getRunnables();
		for (Map.Entry<Runnable, JobDetail> entry : map.entrySet()) {
			if (entry.getKey().getClass() == clazz) {
				getCronService().cancel(entry.getValue());
			}
		}
	}

	protected CronService getCronService() {
		if (CronService.getInstance() == null) {
			throw new RuntimeException("CronService is not initialized");
		}

		return CronService.getInstance();
	}
}
