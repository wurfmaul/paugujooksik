package at.jku.paugujooksik.network;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import at.jku.paugujooksik.gui.server.ServerGUI;
import at.jku.paugujooksik.model.Action;
import at.jku.paugujooksik.model.Configuration;

/**
 * One concrete implementation of the {@link ServerControl} interface to enable
 * communication between client and server.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public class ServerControlImpl extends UnicastRemoteObject implements ServerControl {
	private static final long serialVersionUID = -53275326117633745L;
	private ServerGUI server;

	public ServerControlImpl(ServerGUI server) throws RemoteException {
		super();
		this.server = server;
	}

	@Override
	public Configuration<?> getConfig() throws RemoteException {
		return server.getPresenter().config;
	}

	@Override
	public void incErrorCount(String clientId) throws RemoteException {
		server.getPresenter().incErrorCount(clientId);
	}

	@Override
	public boolean isJoinable() throws RemoteException {
		return server.isJoinable();
	}

	@Override
	public boolean isRunning() throws RemoteException {
		return server.isRunning();
	}

	@Override
	public void performAction(String clientId, Action action) throws RemoteException {
		server.getPresenter().queueAction(clientId, action);
	}

	@Override
	public boolean register(String name) throws RemoteException {
		return server.register(name);
	}

	@Override
	public void unregister(String name) throws RemoteException {
		server.unregister(name);
	}

}
