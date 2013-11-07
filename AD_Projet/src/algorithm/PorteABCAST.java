package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Set;
import java.util.TreeSet;

import logger.MyLogger;

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
	 * Liste des messages triés par estampille
	 */
	private TreeSet<MessageABCAST> liste_message;
	
	/**
	 * Constructeur
	 * @param place nombre de place du parking
	 */
	protected PorteABCAST( int nbPlace) throws RemoteException, MalformedURLException, NotBoundException {
		super( nbPlace);
		this.estampille = 0;
		liste_message = new TreeSet<MessageABCAST>();
	}

	public void calculPlaceDisponible() {
		super.placeDisponible = super.placeTotal;
		for(MessageABCAST msg : liste_message) {
			if(msg.getMessage().equals("VOITURE_ENTREE")) {
				super.placeDisponible--;
			} else if(msg.getMessage().equals("VOITURE_SORTIE")) {
				super.placeDisponible++;
			} else {
				MyLogger.log("[PORTE] Probleme dans la liste des messages : un des messages contient : "+msg.getMessage());
			}
		}
	}
	
	private boolean toutesLesEstampilles() {
		int compteur = 0;
		for(MessageABCAST msg : liste_message) {
			if(compteur == msg.getEstampille()) {
				compteur++;
			} else {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public synchronized void demandeEntree() {
		try {
			MyLogger.log("[P"+super.id+"] Demande d'entrée");
			
			Set<Integer> allClients = super.reso.getClients();
	
			this.temp_compteur = 0;
			this.temp_estampille = this.estampille;
			
			for (Integer i : allClients) {
				if(i != super.id) {
					super.reso.sendMessage(super.id, i, 
							new MessageABCAST("ENTREE_DE_VOITURE", this.estampille));
				}
			}
			
			MyLogger.log("[P"+super.id+"] Attente de reception des messages de confirmation");
			while(this.temp_compteur < allClients.size() - 1) {
				wait();
			}
			
			while( ! toutesLesEstampilles()) {
				wait();
			}
			
			calculPlaceDisponible();
			
			if(super.placeDisponible > 0) {
				this.estampille = this.temp_estampille;
				MyLogger.log("[P"+super.id+"] La voiture est autorisée a entrer, avec l'estampille "+this.estampille);
				for (Integer i : allClients) {
					if(i != super.id) {
						super.reso.sendMessage(super.id, i, 
								new MessageABCAST("VOITURE_ENTREE", this.estampille));
					}
				}
				this.liste_message.add(new MessageABCAST("VOITURE_ENTREE", this.estampille));
				this.estampille++;
			} else {
				MyLogger.log("[P"+super.id+"] Pas de place disponible. La voiture est refusé");
			}
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void demandeSortie() {
		try {
			MyLogger.log("[P"+super.id+"] Demande de sortie sur la porte "+super.id);
			
			Set<Integer> allClients = super.reso.getClients();
	
			this.temp_compteur = 0;
			this.temp_estampille = this.estampille;
			MyLogger.log("[P"+super.id+"] Envoie de demande de confirmation à toutes les portes");
			for (Integer i : allClients) {
				if(i != super.id) {
					super.reso.sendMessage(super.id, i, 
							new MessageABCAST("SORTIE_DE_VOITURE", this.estampille));
				}
			}
			
			MyLogger.log("[P"+super.id+"] Attente de reception des messages de confirmation");
			while(temp_compteur < allClients.size() - 1) {
				wait();
			}
			
			this.estampille = this.temp_estampille;
			this.liste_message.add(new MessageABCAST("VOITURE_SORTIE", this.estampille));
			MyLogger.log("[P"+super.id+"] La voiture est autorisé a sortir, avec l'estampille "+this.estampille);
			for (Integer i : allClients) {
				if(i != super.id) {
					super.reso.sendMessage(super.id, i, 
							new MessageABCAST("VOITURE_SORTIE", this.estampille));
				}
			}
			this.estampille++;
			
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public synchronized void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		if(((MessageABCAST)msg).getMessage().equals("ENTREE_DE_VOITURE")) {
			MyLogger.log("[P"+super.id+"] Envoie de confirmation d'entrée avec estampille à "+this.estampille);
			super.reso.sendMessage(super.id, from, 
					new MessageABCAST("CONFIRMATION_ENTREE_DE_VOITURE", 
							Math.max(this.estampille, ((MessageABCAST)msg).getEstampille())));
			
		} else if(((MessageABCAST)msg).getMessage().equals("SORTIE_DE_VOITURE")) {
			MyLogger.log("[P"+super.id+"] Envoie de confirmation de sortie avec estampille à "+this.estampille);
			super.reso.sendMessage(super.id, from, 
					new MessageABCAST("CONFIRMATION_SORTIE_DE_VOITURE", 
							Math.max(this.estampille, ((MessageABCAST)msg).getEstampille())));
			
		} else if(((MessageABCAST)msg).getMessage().equals("CONFIRMATION_ENTREE_DE_VOITURE")) {
			this.temp_compteur++;
			this.temp_estampille = Math.max(this.temp_estampille, ((MessageABCAST)msg).getEstampille());
			notify();
			
		} else if(((MessageABCAST)msg).getMessage().equals("CONFIRMATION_SORTIE_DE_VOITURE")) {
			this.temp_compteur++;
			this.temp_estampille = Math.max(this.temp_estampille, ((MessageABCAST)msg).getEstampille());
			notify();
			
		} else if(((MessageABCAST)msg).getMessage().equals("VOITURE_ENTREE")) {
			this.liste_message.add((MessageABCAST)msg);
			
			
		} else if(((MessageABCAST)msg).getMessage().equals("VOITURE_SORTIE")) {
			this.liste_message.add((MessageABCAST)msg);
			
		} else {
			System.out.println("[P"+super.id+"] Error, unknown received message : "+((Message)msg).getMessage());
		}
	}
}