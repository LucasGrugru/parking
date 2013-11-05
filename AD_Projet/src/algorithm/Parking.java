package algorithm;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;

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
		Logger l = new Logger();
		Registry registry = LocateRegistry.getRegistry();
		registry.rebind(Reso.NAME, r);
		
		Parking p = new Parking(10, 3);
		l.log("Nombre de porte : "+p.portes.size());
		l.log("Nombre de place : "+p.portes.get(0).placeDisponible);
		l.log("Demande d'entrée sur la porte 1");
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(1);
		p.demandeEntree(0);
		p.demandeEntree(2);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(0);
		p.demandeEntree(1);
		p.demandeEntree(1);
		p.demandeEntree(0);
		p.demandeEntree(2);
		p.demandeEntree(2);
		//p.demandeSortie(1);
		//Thread.sleep(5000);
		l.log("Nombre de place restante sur la porte 0 : "+p.portes.get(0).placeDisponible);
		l.log("Nombre de place restante sur la porte 1 : "+p.portes.get(1).placeDisponible);
		l.log("Nombre de place restante sur la porte 2 : "+p.portes.get(2).placeDisponible);
		registry.unbind(Reso.NAME);
	}
}
