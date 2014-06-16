package at.jku.paugujooksik.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import at.jku.paugujooksik.logic.Configuration;
import at.jku.paugujooksik.sort.Action;

public interface ServerControl extends Remote {
	public boolean register(String name) throws RemoteException;

	public Configuration<?> getConfig() throws RemoteException;

	public void performAction(Action action) throws RemoteException;

	public boolean isRunning() throws RemoteException;

	public void unregister(String name) throws RemoteException;
}
