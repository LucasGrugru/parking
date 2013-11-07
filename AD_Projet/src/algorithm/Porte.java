package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import logger.MyLogger;
import reso.Client;
import reso.Reso;

public class Porte extends UnicastRemoteObject implements iPorte, Client {
	private static final long serialVersionUID = 1L;
	
	protected Reso reso;
	protected int id;
	public int placeDisponible;
	protected int placeTotal;
	
	public Porte( int nbPlace ) throws RemoteException, MalformedURLException, NotBoundException {
		super();

		this.placeTotal = nbPlace;
		this.placeDisponible = this.placeTotal;
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

	@Override
	public int getPlaceDisponible() throws RemoteException {
		return placeDisponible;
	}

	@Override
	public int getID() throws RemoteException {
		return id;
	}

	public static void launchPorte( ParkingAlgo algorithm, int nbPorte){
		if( nbPorte < 0 ){
			nbPorte = 0 - nbPorte;
		}
		try {
			for( int i = 0; i < nbPorte; i++ ){
				CaracParking cara = ((IParking) Naming.lookup(IParking.NAME)).getCarac();
	
				switch( algorithm ){
				case ABCAST:
					new PorteABCAST(cara.getNbPlace());
					break;
				case NAIMI_TREHEL:
					new PorteNaimiTrehel(cara.getNbPlace());
					break;
				case RICART_AGRAWALA:
					new PorteRicartAgrawala(cara.getNbPlace(), cara.getNbPorte());
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
