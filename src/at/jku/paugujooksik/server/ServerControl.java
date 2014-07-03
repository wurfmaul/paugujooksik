package at.jku.paugujooksik.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import at.jku.paugujooksik.model.Action;
import at.jku.paugujooksik.model.Configuration;

public interface ServerControl extends Remote {
	public Configuration<?> getConfig() throws RemoteException;

	public void incErrorCount(String clientId) throws RemoteException;

	public boolean isJoinable() throws RemoteException;

	public boolean isRunning() throws RemoteException;

	public void performAction(String clientId, Action action) throws RemoteException;

	public boolean register(String name) throws RemoteException;

	public void unregister(String name) throws RemoteException;

}
