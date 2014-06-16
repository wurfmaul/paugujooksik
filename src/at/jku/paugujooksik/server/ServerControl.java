package at.jku.paugujooksik.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

import at.jku.paugujooksik.action.BinaryAction;
import at.jku.paugujooksik.action.UnaryAction;
import at.jku.paugujooksik.logic.Configuration;

public interface ServerControl extends Remote {
	public boolean register(String name) throws RemoteException;

	public Configuration<?> getConfig() throws RemoteException;

	public boolean isRunning() throws RemoteException;

	public void performAction(String originId, BinaryAction action) throws RemoteException;

	public void performAction(String originId, UnaryAction action) throws RemoteException;

	public void unregister(String name) throws RemoteException;

}
