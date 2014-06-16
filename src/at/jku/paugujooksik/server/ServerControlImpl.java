package at.jku.paugujooksik.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import at.jku.paugujooksik.action.BinaryAction;
import at.jku.paugujooksik.action.UnaryAction;
import at.jku.paugujooksik.gui.ServerGUI;
import at.jku.paugujooksik.logic.Configuration;

public class ServerControlImpl extends UnicastRemoteObject implements
		ServerControl {
	private static final long serialVersionUID = -53275326117633745L;
	private ServerGUI server;

	public ServerControlImpl(ServerGUI server) throws RemoteException {
		super();
		this.server = server;
	}

	@Override
	public boolean register(String name) throws RemoteException {
		return server.register(name);
	}

	@Override
	public Configuration<?> getConfig() throws RemoteException {
		return server.getConfig();
	}

	@Override
	public boolean isRunning() throws RemoteException {
		return server.isRunning();
	}

	@Override
	public void performAction(String originId, UnaryAction action)
			throws RemoteException {
		server.performAction(originId, action);
	}

	@Override
	public void performAction(String originId, BinaryAction action)
			throws RemoteException {
		server.performAction(originId, action);
	}

	@Override
	public void unregister(String name) throws RemoteException {
		server.unregister(name);
	}

}
