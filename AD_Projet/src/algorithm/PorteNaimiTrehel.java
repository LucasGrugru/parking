package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class PorteNaimiTrehel  extends Porte {
	
	/**
	 * Identifiant supposé du processus possédant le jeton
	 */
	public int owner;
	
	/**
	 * Identifiant du processus suivant à qui envoyer le jeton
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
	
	public PorteNaimiTrehel() throws RemoteException, MalformedURLException, NotBoundException {
		super();
		this.owner = 0;
		if(this.owner == super.id)
			this.owner = -1;
		
		this.next = 0;
		
		if(super.id == 0)
			this.jeton = true;
		else
			this.jeton = false;
		
		this.sc = false;
	}
	
	private void demandeSectionCritique() throws RemoteException, InterruptedException {
		this.sc = true;
		if (this.owner != -1) {
			super.reso.sendMessage(super.id, this.owner, new Message("REQ", super.id));
			this.owner = -1;
			// TODO c'est moche !
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
		try {
			sortieSectionCritique();
		} catch (RemoteException e) {
			System.out.println("[PORTE] error, RemoteException dans DemandeSortie()");
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
		} else {
			System.out.println("[PORTE] Error, unknown receive message.");
		}
	}
}
