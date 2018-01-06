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


package com.aionemu.loginserver.service;

import java.util.Map;
import java.util.concurrent.Future;

import javolution.util.FastList;
import javolution.util.FastMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.GameServerInfo;
import com.aionemu.loginserver.GameServerTable;
import com.aionemu.loginserver.controller.AccountController;
import com.aionemu.loginserver.dao.AccountDAO;
import com.aionemu.loginserver.dao.PlayerTransferDAO;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.network.gameserver.serverpackets.SM_PTRANSFER_RESPONSE;
import com.aionemu.loginserver.service.ptransfer.PlayerTransferRequest;
import com.aionemu.loginserver.service.ptransfer.PlayerTransferResultStatus;
import com.aionemu.loginserver.service.ptransfer.PlayerTransferStatus;
import com.aionemu.loginserver.service.ptransfer.PlayerTransferTask;
import com.aionemu.loginserver.utils.ThreadPoolManager;

/**
 * @author KID
 */
public class PlayerTransferService {

    private static PlayerTransferService instance = new PlayerTransferService();
    private final Logger log = LoggerFactory.getLogger(PlayerTransferService.class);

    public static PlayerTransferService getInstance() {
        return instance;
    }
    private Map<Integer, PlayerTransferRequest> transfers = FastMap.newInstance();
    private Map<Integer, PlayerTransferTask> tasks = FastMap.newInstance();
    private Future<?> veryfyTask;
    private PlayerTransferDAO dao;

    public PlayerTransferService() {
        veryfyTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                verifyNewTasks();
            }
        }, 10000, 7 * 60000);
        this.dao = DAOManager.getDAO(PlayerTransferDAO.class);
        log.info("PlayerTransferService will be initialized in 10 sec.");
    }

    /**
     * first init. getting values from sql
     */
    protected void verifyNewTasks() {
        FastList<PlayerTransferTask> tasksNew = this.dao.getNew();
        log.info("PlayerTransfer perform task init. " + tasks.size() + " new tasks.");
        for (PlayerTransferTask task : tasksNew) {
            GameServerInfo server = GameServerTable.getGameServerInfo(task.sourceServerId);
            if (server == null || server.getConnection() == null) {
                log.error("cannot perform transfer task #" + task.id + " while source server is down #" + task.sourceServerId);
                continue;
            }

            GameServerInfo targetServer = GameServerTable.getGameServerInfo(task.targetServerId);
            if (targetServer == null || targetServer.getConnection() == null) {
                log.error("cannot perform transfer task #" + task.id + " while target server is down #" + task.targetServerId);
                continue;
            }

            if (server.isAccountOnGameServer(task.sourceAccountId)) {
                log.error("cannot perform transfer task #" + task.id + " while source account is online " + task.sourceAccountId);
                continue;
            }

            if (targetServer.isAccountOnGameServer(task.targetAccountId)) {
                log.error("cannot perform transfer task #" + task.id + " while target account is online " + task.targetAccountId);
                continue;
            }

            task.status = PlayerTransferTask.STATUS_ACTIVE;
            tasks.put(task.id, task);
            this.dao.update(task);
            server.getConnection().sendPacket(new SM_PTRANSFER_RESPONSE(PlayerTransferResultStatus.PERFORM_ACTION, task));
            log.info("performing player transfer #" + task.id);
        }
    }

    public void shutdown() {
        this.veryfyTask.cancel(true);
    }

    /**
     * sended from source server to login with character information
     */
    public void requestTransfer(int taskId, String name, byte[] db) {
        PlayerTransferTask task = this.tasks.get(taskId);
        GameServerInfo targetServer = GameServerTable.getGameServerInfo(task.targetServerId);
        if (targetServer == null || targetServer.getConnection() == null) {
            log.error("Player transfer requests offline server! #" + task.targetServerId);
            return;
        }

        GameServerInfo server = GameServerTable.getGameServerInfo(task.sourceServerId);
        if (server == null || server.getConnection() == null) {
            log.error("Player transfer requests offline server! #" + task.sourceServerId);
            return;
        }

        if (targetServer.isAccountOnGameServer(task.targetAccountId)) {
            log.error("Player transfer cant be performed while target account is online at server #" + task.targetServerId + ". " + task.targetAccountId);
            server.getConnection().sendPacket(new SM_PTRANSFER_RESPONSE(PlayerTransferResultStatus.ERROR, taskId, "transfer cant be performed while target account is online at server"));
            return;
        }

        if (transfers.containsKey(taskId)) {
            log.error("Player transfer cant be performed while it is already active #" + task.targetServerId + ". " + task.targetAccountId);
            server.getConnection().sendPacket(new SM_PTRANSFER_RESPONSE(PlayerTransferResultStatus.ERROR, taskId, "transfer cant be performed while it is already active"));
            return;
        }

        Account account = AccountController.loadAccount(task.targetAccountId);
        Account saccount = AccountController.loadAccount(task.sourceAccountId);

        PlayerTransferRequest request = new PlayerTransferRequest(PlayerTransferStatus.STEP1);
        request.serverId = task.sourceServerId;
        request.targetServerId = task.targetServerId;
        request.targetAccountId = task.targetAccountId;
        request.db = db;
        request.name = name;
        request.targetAccount = account;
        request.account = account;
        request.saccount = saccount;
        request.taskId = taskId;

        transfers.put(taskId, request);

        account.setActivated((byte) 0);
        saccount.setActivated((byte) 0);
        DAOManager.getDAO(AccountDAO.class).updateAccount(account);
        DAOManager.getDAO(AccountDAO.class).updateAccount(saccount);

        targetServer.getConnection().sendPacket(new SM_PTRANSFER_RESPONSE(PlayerTransferResultStatus.SEND_INFO, request));
        log.info("player transfer account " + task.targetServerId + " became active.");
    }

    /**
     * When source server refuse to do transfer with reason
     */
    public void onTaskStop(int taskId, String reason) {
        PlayerTransferTask task = this.tasks.remove(taskId);
        task.status = PlayerTransferTask.STATUS_ERROR;
        task.comment = reason;
        this.dao.update(task);
    }

    /**
     * response from target server after cloning character
     */
    public void onError(int taskId, String reason) {
        PlayerTransferRequest request = this.transfers.remove(taskId);
        PlayerTransferTask task = this.tasks.remove(taskId);
        task.status = PlayerTransferTask.STATUS_ERROR;
        task.comment = reason;
        this.dao.update(task);
        GameServerInfo targetServer = GameServerTable.getGameServerInfo(request.targetServerId);
        if (targetServer == null || targetServer.getConnection() == null) {
            log.error("Player transfer requests offline server! #" + request.targetServerId);
            return;
        }

        request.account.setActivated((byte) 1);
        request.saccount.setActivated((byte) 1);
        DAOManager.getDAO(AccountDAO.class).updateAccount(request.account);
        DAOManager.getDAO(AccountDAO.class).updateAccount(request.saccount);

        targetServer.getConnection().sendPacket(new SM_PTRANSFER_RESPONSE(PlayerTransferResultStatus.ERROR, taskId, reason));
    }

    /**
     * response from target server after cloning character
     */
    public void onOk(int taskId, int playerId) {
        PlayerTransferRequest request = this.transfers.remove(taskId);
        PlayerTransferTask task = this.tasks.remove(taskId);
        task.status = PlayerTransferTask.STATUS_DONE;
        task.comment = "task done";
        this.dao.update(task);
        GameServerInfo sourceServer = GameServerTable.getGameServerInfo(request.serverId);
        if (sourceServer == null || sourceServer.getConnection() == null) {
            log.error("Player transfer requests offline server! #" + request.serverId);
            return;
        }

        request.account.setActivated((byte) 1);
        request.saccount.setActivated((byte) 1);
        DAOManager.getDAO(AccountDAO.class).updateAccount(request.account);
        DAOManager.getDAO(AccountDAO.class).updateAccount(request.saccount);
        log.info("transfer #" + taskId + " went onOK!");
        sourceServer.getConnection().sendPacket(new SM_PTRANSFER_RESPONSE(PlayerTransferResultStatus.OK, request));
    }
}
