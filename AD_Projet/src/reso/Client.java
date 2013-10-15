package reso;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Client extends Remote {
	
	void receiveMessage(int from, int to, Serializable msg) throws RemoteException;
	
}
