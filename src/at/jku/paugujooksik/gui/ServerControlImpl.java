package at.jku.paugujooksik.gui;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import at.jku.paugujooksik.sort.Action;

public class ServerControlImpl extends UnicastRemoteObject implements ServerControl {
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
	public void getConfig() throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("config");
	}

	@Override
	public void performAction(Action action) throws RemoteException {
		// TODO Auto-generated method stub
		System.out.println("action: " + action);
	}

	@Override
	public boolean isRunning() throws RemoteException {
		return server.isRunning();
	}

	@Override
	public void unregister(String name) throws RemoteException {
		server.unregister(name);
	}

}
