package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import reso.Client;
import reso.Reso;

public class Porte extends UnicastRemoteObject implements iPorte, Client {

	protected Reso reso;
	protected int id;
	
	public Porte() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		this.reso = (Reso)Naming.lookup(Reso.NAME);
		this.id = this.reso.declareClient(this);
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
