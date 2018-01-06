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


package com.aionemu.chatserver.utils.guice;

import com.aionemu.chatserver.network.aion.ClientPacketHandler;
import com.aionemu.chatserver.network.netty.NettyServer;
import com.aionemu.chatserver.network.netty.pipeline.LoginToClientPipeLineFactory;
import com.aionemu.chatserver.service.BroadcastService;
import com.aionemu.chatserver.service.ChatService;
import com.aionemu.chatserver.service.GameServerService;
import com.aionemu.chatserver.utils.IdFactory;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;

/**
 * @author ATracer
 */
public class ServiceInjectionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IdFactory.class).asEagerSingleton();

        bind(ClientPacketHandler.class).in(Scopes.SINGLETON);

        bind(LoginToClientPipeLineFactory.class).in(Scopes.SINGLETON);

        bind(NettyServer.class).asEagerSingleton();

        bind(GameServerService.class).in(Scopes.SINGLETON);
        bind(BroadcastService.class).in(Scopes.SINGLETON);
        bind(ChatService.class).in(Scopes.SINGLETON);
    }
}
