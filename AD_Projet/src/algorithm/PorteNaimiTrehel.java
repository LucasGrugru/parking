package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

import logger.MyLogger;

public class PorteNaimiTrehel  extends Porte {
	
	private static final long serialVersionUID = 1L;

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
	
	public int compteur;
	
	public PorteNaimiTrehel( int nbPlace, String hostname ) throws RemoteException, MalformedURLException, NotBoundException {
		super(nbPlace, hostname);
		this.owner = 0;
		this.sc = false;
		this.jeton = false;
		this.next = -1;
		
		if(super.id == 0) {
			this.owner = -1;
			this.jeton = true;
		}
	}

	private synchronized void demandeSectionCritique() throws RemoteException, InterruptedException {
		this.sc = true;
		if (this.owner != -1) {
			super.reso.sendMessage(super.id, this.owner, new Message("REQ", super.id));
			this.owner = -1;
			// TODO c'est moche ! c'est toi le moche
			while(this.jeton == false) {
				wait();
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
				synchronized(this) {
					notify();
				}
			}
		} else {
			super.reso.sendMessage(super.id, this.owner, new Message("REQ", needer));
		}
		this.owner = needer;
	}
	
	private synchronized void accepteJETON() {
		this.jeton = true;
		notify();
	}
	
	private void sortieSectionCritique() throws RemoteException {
		this.sc = false;
		if(this.next != -1) {
			super.reso.sendMessage(super.id, this.next, new Message("JETON"));
			notify();
			this.jeton = false;
			this.next = -1;
		}
	}
	
	@Override
	public synchronized void demandeEntree() {
		try {
			MyLogger.log("[PORTE] Demande SC sur la porte "+super.id);
			demandeSectionCritique();
			//ENTREE SECTION CRITIQUE
			
			while(super.placeDisponible <= 0) {
				MyLogger.log("[PORTE] Attente de place libre sur la porte "+super.id);
				wait();
			}
			MyLogger.log("[PORTE] Entrée de voiture sur la porte "+super.id);
			super.placeDisponible--;
			
			Set<Integer> allClients = super.reso.getClients();

			compteur = 0;
			
			for (Integer i : allClients) {
				if(i != super.id)
					super.reso.sendMessage(super.id, i, new Message("ENTREE_DE_VOITURE"));
			}
			
			while(compteur >= super.reso.getClients().size() - 1) {
				wait();
			}
			
			//FIN
			MyLogger.log("[PORTE] Sortie SC sur la porte "+super.id);
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
		MyLogger.log("[PORTE] Sortie de voiture sur la porte "+super.id);
		super.placeDisponible++;
		synchronized(this) {
			notify();
		}
		try {
			Set<Integer> allClients = super.reso.getClients();

			for (Integer i : allClients) {
				if(i != super.id)
					super.reso.sendMessage(super.id, i, new Message("SORTIE_DE_VOITURE"));
			}
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		if(((Message)msg).getMessage().equals("JETON")) {
			accepteJETON();
		} else if(((Message)msg).getMessage().equals("ENTREE_DE_VOITURE_RECU")) {
			compteur++;
		} else if(((Message)msg).getMessage().equals("REQ")) {
			accepteREQ(from, ((Message)msg).getDemandeur());
		} else if(((Message)msg).getMessage().equals("ENTREE_DE_VOITURE")) {
			super.placeDisponible--;
			super.reso.sendMessage(super.id, ((Message)msg).getDemandeur(), new Message("ENTREE_DE_VOITURE_RECU"));
		} else if(((Message)msg).getMessage().equals("SORTIE_DE_VOITURE")) {
			super.placeDisponible++;
		} else {
			System.out.println("[PORTE] Error, unknown received message : "+((Message)msg).getMessage());
		}
	}
}
