package reso;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

public interface Reso extends Remote {
	
	public static final String NAME = "Reso";

	int declareClient(Client client) throws RemoteException;
	
	Set<Integer> getClients() throws RemoteException;
	
	void sendMessage(int from, int to, Serializable msg) throws RemoteException;

}
