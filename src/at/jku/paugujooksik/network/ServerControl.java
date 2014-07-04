package at.jku.paugujooksik.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

import at.jku.paugujooksik.model.Action;
import at.jku.paugujooksik.model.Configuration;

/**
 * Provides an interface between client and server.
 * 
 * @author Wolfgang Kuellinger (0955711), 2014
 */
public interface ServerControl extends Remote {
	/**
	 * @return the configuration.
	 * @throws RemoteException
	 */
	public Configuration<?> getConfig() throws RemoteException;

	/**
	 * Increments the error counter of a specified player.
	 * 
	 * @param clientId
	 *            The name of the player.
	 * @throws RemoteException
	 */
	public void incErrorCount(String clientId) throws RemoteException;

	/**
	 * @return true if the server is welcoming players.
	 * @throws RemoteException
	 */
	public boolean isJoinable() throws RemoteException;

	/**
	 * @return true if a game is currently running.
	 * @throws RemoteException
	 */
	public boolean isRunning() throws RemoteException;

	/**
	 * Perform a specified action.
	 * 
	 * @param clientId
	 *            The name of the player that performs the action.
	 * @param action
	 *            The performed action.
	 * @throws RemoteException
	 */
	public void performAction(String clientId, Action action) throws RemoteException;

	/**
	 * Register a player at the server
	 * 
	 * @param name
	 *            The player's name.
	 * @return false if the name has been already registered.
	 * @throws RemoteException
	 */
	public boolean register(String name) throws RemoteException;

	/**
	 * Unregister a player from the server.
	 * 
	 * @param name
	 *            The player's name.
	 * @throws RemoteException
	 */
	public void unregister(String name) throws RemoteException;

}
