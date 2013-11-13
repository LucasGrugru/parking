package algorithm;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import reso.Client;
import reso.Reso;

public class Porte extends UnicastRemoteObject implements iPorte, Client {
	private static final long serialVersionUID = 1L;
	
	protected Reso reso;
	protected int id;
	public int placeDisponible;
	protected int placeTotal;
	
	public Porte( int nbPlace, String hostname ) throws RemoteException, MalformedURLException, NotBoundException {
		super();

		this.placeTotal = nbPlace;
		this.placeDisponible = this.placeTotal;
		this.reso = (Reso)Naming.lookup("rmi://"+hostname+"/"+Reso.NAME);
		this.id = this.reso.declareClient(this);
		((IParking)Naming.lookup("rmi://"+hostname+"/"+IParking.NAME)).declarePorte( this );
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

	public static void launchPorte( ParkingAlgo algorithm, int nbPorte, String hostname){
		if( nbPorte < 0 ){
			nbPorte = 0 - nbPorte;
		}
		try {
			for( int i = 0; i < nbPorte; i++ ){
				CaracParking cara = ((IParking) Naming.lookup("//"+hostname+":1099/"+IParking.NAME)).getCarac();
	
				switch( algorithm ){
				case ABCAST:
					new PorteABCAST(cara.getNbPlace(), hostname);
					break;
				case NAIMI_TREHEL:
					new PorteNaimiTrehel(cara.getNbPlace(), hostname);
					break;
				case RICART_AGRAWALA:
					new PorteRicartAgrawala(cara.getNbPlace(), cara.getNbPorte(), hostname);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String hostname = "192.168.1.1";
		int nbPorte = 1;
		ParkingAlgo algo = ParkingAlgo.RICART_AGRAWALA;
		if( args.length > 2 ){
			nbPorte = Integer.valueOf(args[0]);
			hostname = args[2];
			try{
				algo = ParkingAlgo.valueOf(args[1]);
			}catch( Exception e ){
				System.err.println("L'algo n'est pas reconnu, l'ago par defaut est Ricart agrawala");
			}
		}
		launchPorte(algo, nbPorte, hostname);
	}
}
