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
	
	public Porte( int nbPlace ) throws RemoteException, MalformedURLException, NotBoundException {
		super();

		this.placeTotal = nbPlace;
		this.placeDisponible = this.placeTotal;
		try {
			System.out.println(InetAddress.getLocalHost().getHostAddress());
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.reso = (Reso)Naming.lookup("rmi://192.168.1.9/"+Reso.NAME);
		this.id = this.reso.declareClient(this);
		((IParking)Naming.lookup("rmi://192.168.1.9/"+IParking.NAME)).declarePorte( this );
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
				CaracParking cara = ((IParking) Naming.lookup("//192.168.1.9:1099/"+IParking.NAME)).getCarac();
	
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
	
	public static void main(String[] args) {
		String[] strs;
		try {
			strs = Naming.list("//192.168.1.9/");
			System.out.println("liste");
			for( String str: strs ){
				System.out.println(str);
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		int nbPorte = 1;
		ParkingAlgo algo = ParkingAlgo.RICART_AGRAWALA;
		if( args.length > 1 ){
			nbPorte = Integer.valueOf(args[0]);
			try{
				algo = ParkingAlgo.valueOf(args[1]);
			}catch( Exception e ){
				System.err.println("L'algo n'est pas reconnu, l'ago par defaut est Ricart agrawala");
			}
		}
		launchPorte(algo, nbPorte);
	}
}
