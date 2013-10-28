package algorithm;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class Parking {

	public int nbPlace;
	public ArrayList<Porte> portes;
	
	public Parking(int nbPlace) {
		this.nbPlace = nbPlace;
		portes = new ArrayList<Porte>();
		for(int i=0; i<this.nbPlace; i++) {
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
}
