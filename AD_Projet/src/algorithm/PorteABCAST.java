package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;

/**
 * Porte utilisant les horloges logiques pour l'acces en section critique
 * @author lucas & matin
 */
public class PorteABCAST extends Porte {
	
	/**
	 * Estampille local de la porte
	 */
	public int estampille;
	
	/**
	 * Estampille temporaire lors de la diffusion d'une demande
	 */
	private int temp_estampille;
	
	/**
	 * Compteur temporaire de reception des confirmations
	 */
	private int temp_compteur;
	
	/**
	 * 
	 * @param place
	 * @throws RemoteException
	 * @throws MalformedURLException
	 * @throws NotBoundException
	 */
	protected PorteABCAST(int place) throws RemoteException, MalformedURLException, NotBoundException {
		super(place);
		this.estampille = 0;
	}

	@Override
	public void demandeEntree() {
		try {
			MyLogger.log("[PORTE] Demande d'entrée sur la porte "+super.id);
			
			Set<Integer> allClients = super.reso.getClients();
	
			this.temp_compteur = 0;
			this.temp_estampille = this.estampille;
			
			for (Integer i : allClients) {
				if(i != super.id) {
					super.reso.sendMessage(super.id, i, 
							new MessageABCAST("ENTREE_DE_VOITURE", this.estampille));
				}
			}
			
			while(temp_compteur != allClients.size() - 1) {
				wait();
			}
		
		
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void demandeSortie() {
		// TODO auto-generated method stub
	}
	
	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		if(((Message)msg).getMessage().equals("ENTREE_DE_VOITURE")) {
			MyLogger.log("[PORTE] Envoie de confirmation d'entrée sur la porte "+super.id+ "avec estampille à "+this.estampille);
			super.reso.sendMessage(super.id, from, 
					new MessageABCAST("CONFIRMATION_ENTREE_DE_VOITURE", 
							Math.max(this.estampille, ((MessageABCAST)msg).getEstampille())));
			notify();
			
		} else if(((Message)msg).getMessage().equals("CONFIRMATION_ENTREE_DE_VOITURE")) {
			this.temp_compteur++;
			this.temp_estampille = Math.max(this.temp_estampille, ((MessageABCAST)msg).getEstampille());
			
		} else {
			System.out.println("[PORTE] Error, unknown received message : "+((Message)msg).getMessage());
		}
	}
}
