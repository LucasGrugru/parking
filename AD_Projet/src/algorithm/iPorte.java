package algorithm;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface iPorte extends Remote {

	public void demandeEntree() throws RemoteException;
	
	public void demandeSortie() throws RemoteException;
	
	public int getPlaceDisponible() throws RemoteException;
	
	public int getID() throws RemoteException;
	
}
