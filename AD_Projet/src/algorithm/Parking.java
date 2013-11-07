package algorithm;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Random;

import logger.MyLogger;
import reso.Reso;
import reso.ResoImpl;

public class Parking extends UnicastRemoteObject implements IParking{

	private static final long serialVersionUID = 1L;
	private int nbPlace;
	private ArrayList<iPorte> portes;
	private int nbPorte;
	
	public Parking(int nbPlace, int nbPorte) throws RemoteException{
		this.nbPlace = nbPlace;
		portes = new ArrayList<iPorte>();
		this.nbPorte = nbPorte;
	}
	
	public void demandeEntree(int idPorte) throws RemoteException {
		portes.get(idPorte).demandeEntree();
	}
	
	public void demandeSortie(int idPorte) throws RemoteException {
		portes.get(idPorte).demandeSortie();
	}
	
	public static void main(String[] args) throws RemoteException, NotBoundException, InterruptedException {
		Reso r = new ResoImpl();
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind(Reso.NAME, r);
		
		Parking p = new Parking(10, 3);
		MyLogger.log("[SERVEUR] Nombre de porte : "+p.portes.size());
		MyLogger.log("[SERVEUR] Nombre de place : "+p.portes.get(0).getPlaceDisponible());
		MyLogger.log("[SERVEUR] Demande d'entrée sur la porte 1");
		int nbEntree = 0;
		int nbSortie = 0;
		boolean entree;
		for(int i=0; i<10; i++) {
			Random random = new Random();
			int numPorte = random.nextInt(2);
			if(nbEntree == nbSortie) {
				entree = true;
			} else {
				entree = random.nextBoolean();
			}
			if(entree) {
				p.demandeEntree(numPorte);
				nbEntree++;
				MyLogger.log("[SERVEUR] Nombre de voiture entrée : "+nbEntree);
			} else {
				p.demandeSortie(numPorte);
				nbSortie++;
				MyLogger.log("[SERVEUR] Nombre de voiture sortie : "+nbSortie);
			}
		}
		Thread.sleep(5000);
		MyLogger.log("[SERVEUR] Nombre de place theoriquement restante "+(p.nbPlace - nbEntree + nbSortie));
		MyLogger.log("[SERVEUR] Nombre de place restante sur la porte 0 : "+p.portes.get(0).getPlaceDisponible());
		MyLogger.log("[SERVEUR] Nombre de place restante sur la porte 1 : "+p.portes.get(1).getPlaceDisponible());
		MyLogger.log("[SERVEUR] Nombre de place restante sur la porte 2 : "+p.portes.get(2).getPlaceDisponible());
		
	}

	@Override
	public CaracParking declarePorte(iPorte porte) throws RemoteException {
		System.out.println("P"+porte.getID()+" declare");
		boolean ajout = portes.add( porte );
		if( ajout ){
			if( portes.size() == nbPorte ){
				new Thread( new Runnable() {
					public void run() {
						Parking.this.run();
					}
				}).start();
			}
		}
		return new CaracParking(nbPlace, nbPorte);
	}

	private void run(){
		MyLogger.log("[SERVEUR] Nombre de porte : "+portes.size());
		try {
			MyLogger.log("[SERVEUR] Nombre de place : "+portes.get(0).getPlaceDisponible());
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MyLogger.log("[SERVEUR] Demande d'entrée sur la porte 1");
		int nbEntree = 0;
		int nbSortie = 0;
		boolean entree;
		for(int i=0; i<10; i++) {
			Random random = new Random();
			int numPorte = random.nextInt(2);
			if(nbEntree == nbSortie) {
				entree = true;
			} else {
				entree = random.nextBoolean();
			}
			if(entree) {
				try {
					demandeEntree(numPorte);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nbEntree++;
				MyLogger.log("[SERVEUR] Nombre de voiture entrée : "+nbEntree);
			} else {
				try {
					demandeSortie(numPorte);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				nbSortie++;
				MyLogger.log("[SERVEUR] Nombre de voiture sortie : "+nbSortie);
			}
		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		MyLogger.log("[SERVEUR] Nombre de place theoriquement restante "+(nbPlace - nbEntree + nbSortie));
		for( iPorte porte: portes ){
			try {
				MyLogger.log("[SERVEUR] Nombre de place restante sur la porte "+porte.getID()+" : "+porte.getPlaceDisponible());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public boolean undeclarePorte(iPorte porte) throws RemoteException {
		return portes.remove(porte);
	}

	public  CaracParking getCarac() throws RemoteException{
		return new CaracParking(nbPlace, nbPorte);
	}
}
