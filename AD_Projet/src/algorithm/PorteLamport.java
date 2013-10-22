package algorithm;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Porte utilisant les horloges logiques pour l'acces en section critique
 * @author lucas & matin
 */
public class PorteLamport extends Porte {
	
	protected PorteLamport() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void demandeEntree() {
		// TODO auto-generated method stub
	}
	
	@Override
	public void demandeSortie() {
		// TODO auto-generated method stub
	}
}
