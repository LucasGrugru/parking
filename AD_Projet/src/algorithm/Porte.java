package algorithm;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import reso.Client;

public class Porte extends UnicastRemoteObject implements iPorte, Client {

	protected Porte() throws RemoteException {
		super();
	}

	@Override
	public void demandeEntree() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void demandeSortie() throws RemoteException {
		// TODO Auto-generated method stub

	}

	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		// TODO Auto-generated method stub
		
	}

}
