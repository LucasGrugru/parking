package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

public class PorteNaimiTrehel  extends Porte {
	
	/**
	 * Identifiant supposé du processus possédant le jeton
	 * -1 pour nil
	 */
	public int owner;
	
	/**
	 * Identifiant du processus suivant à qui envoyer le jeton
	 * -1 pour nil
	 */
	public int next;
	
	/**
	 * Booleen indiquant si le processus possédant le jeton
	 */
	public boolean jeton;
	
	/**
	 * Boolean indiquant si le processus a fait une demande de section critique
	 */
	public boolean sc;
	
	public PorteNaimiTrehel(int place) throws RemoteException, MalformedURLException, NotBoundException {
		super(place);
		this.owner = 0;
		this.sc = false;
		this.jeton = false;
		this.next = -1;
		
		if(super.id == 0) {
			this.owner = -1;
			this.jeton = true;
		}
	}

	private void demandeSectionCritique() throws RemoteException, InterruptedException {
		this.sc = true;
		if (this.owner != -1) {
			super.reso.sendMessage(super.id, this.owner, new Message("REQ", super.id));
			this.owner = -1;
			// TODO c'est moche !
			while(this.jeton == false) {
				Thread.sleep(1000);
			}
		}
	}
	
	private void accepteREQ(int from, int needer) throws RemoteException {
		if(this.owner == -1) {
			if(this.sc == true) {
				this.next = needer;
			} else {
				this.jeton = false;
				super.reso.sendMessage(super.id, needer, new Message("JETON"));
			}
		} else {
			super.reso.sendMessage(super.id, this.owner, new Message("REQ", needer));
		}
		this.owner = needer;
	}
	
	private void accepteJETON() {
		this.jeton = true;
		notifyAll();
	}
	
	private void sortieSectionCritique() throws RemoteException {
		this.sc = false;
		if(this.next != -1) {
			super.reso.sendMessage(super.id, this.next, new Message("JETON"));
			this.jeton = false;
			this.next = -1;
		}
	}
	
	@Override
	public void demandeEntree() {
		try {
			demandeSectionCritique();
			//ENTREE SECTION CRITIQUE
			super.placeDisponible--;
			
			Set<Integer> allClients = super.reso.getClients();

			for (Integer i : allClients) {
				super.reso.sendMessage(super.id, i, new Message("ENTREE_DE_VOITURE"));
			}
			//FIN
			sortieSectionCritique();
		} catch (RemoteException e) {
			System.out.println("[PORTE] error, RemoteException dans DemandeEntree()");
			e.printStackTrace();
		} catch (InterruptedException e) {
			System.out.println("[PORTE] error, InterruptedException dans DemandeEntree()");
			e.printStackTrace();
		}
	}
	
	@Override
	public void demandeSortie() {
		super.placeDisponible++;
		try {
			Set<Integer> allClients = super.reso.getClients();

			for (Integer i : allClients) {
				super.reso.sendMessage(super.id, i, new Message("SORTIE_DE_VOITURE"));
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		if(((Message)msg).message == "JETON") {
			accepteJETON();
		} else if(((Message)msg).message == "REQ") {
			accepteREQ(from, ((Message)msg).demandeur);
		} else if(((Message)msg).message == "ENTREE_DE_VOITURE") {
			super.placeDisponible--;
		} else if(((Message)msg).message == "SORTIE_DE_VOITURE") {
			super.placeDisponible++;
		} else {
			System.out.println("[PORTE] Error, unknown received message. \n"+msg.toString());
		}
	}
}
