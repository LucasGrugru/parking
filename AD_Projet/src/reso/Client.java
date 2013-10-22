package reso;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This class represents a client in a distributed network. This is the class
 * to implement to be able to send and receive messages through the Reso
 * class.
 * 
 */
public interface Client extends Remote {

	/**
	 * Receive a message from another client.
	 * 
	 * @param from the id of the sender of the message.
	 * @param to the id of the receiver of the message (the client itself normally).
	 * @param msg the received message.
	 * @throws RemoteException
	 */
	void receiveMessage(int from, int to, Serializable msg) throws RemoteException;

}