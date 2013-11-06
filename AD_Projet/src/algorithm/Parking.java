package algorithm;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Random;

import org.junit.Assert;

import reso.Reso;
import reso.ResoImpl;

public class Parking {

	public int nbPlace;
	public ArrayList<Porte> portes;
	
	public Parking(int nbPlace, int nbPorte) {
		this.nbPlace = nbPlace;
		portes = new ArrayList<Porte>();
		for(int i=0; i<nbPorte; i++) {
			try {
				this.portes.add(new PorteNaimiTrehel(nbPlace));
			} catch (Exception e) {
				System.out.println("[PARKING] Error, can not create door");
				e.printStackTrace();
			}
		}
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
		MyLogger.log("[SERVEUR] Nombre de place : "+p.portes.get(0).placeDisponible);
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
		MyLogger.log("[SERVEUR] Nombre de place restante sur la porte 0 : "+p.portes.get(0).placeDisponible);
		MyLogger.log("[SERVEUR] Nombre de place restante sur la porte 1 : "+p.portes.get(1).placeDisponible);
		MyLogger.log("[SERVEUR] Nombre de place restante sur la porte 2 : "+p.portes.get(2).placeDisponible);
		
	}
}
