package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

import logger.MyLogger;

public class PorteRicartAgrawala extends Porte {

	private static final String MESSAGE_ENTREE = "ENTREE";
	private static final String MESSAGE_SORTIE = "SORTIE";
	private static final String MESSAGE_ACCORD = "ACK";

	private static final long serialVersionUID = 1L;
	private Set<Integer> portes;
	private boolean isEntree;
	private int horloge;
	private int lastHorloge;
	private Set<Integer> porteAttente;
	private int reponsesAttendues;
	
	public PorteRicartAgrawala(int nbPlace, int nbPorte) throws RemoteException, MalformedURLException, NotBoundException {
		super(nbPlace);
		portes = new HashSet<Integer>();
		isEntree = false;
		horloge = 0;
		lastHorloge = 0;
		porteAttente = new HashSet<Integer>();
		reponsesAttendues = 0;
		for( int i =0; i < nbPorte; i++ ){
			if( i != this.id ){
				portes.add( i );
			}
		}
		
	}


	@Override
	public synchronized void  demandeEntree() {
		printDebug("Recoit voiture");
		this.horloge++;
		if( placeDisponible == 0 ) return;
		isEntree = true;
		lastHorloge = this.horloge;
		reponsesAttendues = portes.size();
		for( Integer porte: portes ){
			try {
				printDebug("demande authorisation entree a "+porte);
				reso.sendMessage(this.id, porte, new Message(MESSAGE_ENTREE+"|"+this.horloge, this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
		while( reponsesAttendues != 0 ){
			synchronized (this) {
				try {
					wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		printDebug("Toute les reponses ok");
		isEntree = false;
		
		for( Integer porte: porteAttente ){
			if( placeDisponible > 0 )
				placeDisponible--;
			try {
				printDebug("Retourne l'accord a "+porte);
				reso.sendMessage(this.id, porte, new Message(MESSAGE_ACCORD, this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		porteAttente.clear();
		if( placeDisponible > 0){
			placeDisponible--;
			printDebug("diminue le nombre de place. now "+placeDisponible);
		}
	}
	
	@Override
	public synchronized void demandeSortie() {
		printDebug("Voiture sort now "+placeDisponible);
		for( Integer porte: portes ){
			try {
				printDebug("previent "+porte+" qu'une voiture est sortit");
				reso.sendMessage(this.id, porte, new Message(MESSAGE_SORTIE,this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		placeDisponible++;
	}
	
	public void getENTREE( int horloge, int porte){
		this.horloge = 1 + (this.horloge > horloge?this.horloge:horloge);
		if( isEntree ){
			if( ( horloge < lastHorloge ) || (( lastHorloge == horloge) && (porte < this.id))){
				if( placeDisponible > 0 )
					placeDisponible--;
				try {
					printDebug("envoi accord a "+porte);
					reso.sendMessage(this.id, porte, new Message(MESSAGE_ACCORD, this.id));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}else{
				printDebug("Met en attente "+porte);
				porteAttente.add( porte );
			}
		}else{
			if( placeDisponible > 0 )
				placeDisponible--;
			try {
				printDebug("Envoi accord a "+porte);
				reso.sendMessage(this.id, porte, new Message(MESSAGE_ACCORD, this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getAccept(){
		reponsesAttendues--;
		printDebug("reponse encore attendu "+reponsesAttendues);
		if( reponsesAttendues == 0 ){
			synchronized (this) {
				notify();
			}
		}
	}
	
	public void getSortie(){
		placeDisponible++;
		printDebug("Place disponible "+placeDisponible);
	}
	
	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		try{
		Message message = (Message) msg;
		String messageText = message.getMessage();
		if( messageText.equals(MESSAGE_ACCORD) ){
			printDebug("Recoit un message d'accord de "+from);
			getAccept();
		}else if( messageText.equals(MESSAGE_SORTIE)){
			printDebug("Recoit un message de sortie de "+from);
			getSortie();
		}else if( messageText.startsWith(MESSAGE_ENTREE)){
			int horloge = Integer.valueOf( messageText.substring(7));
			printDebug("Recoit un message d'entree: de "+from);
			getENTREE(horloge, from);
		}
		}catch( Exception e ){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			new PorteRicartAgrawala( Integer.valueOf( args[0]), Integer.valueOf( args[1]));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void printDebug(String s){
		MyLogger.debug("[P"+this.id+"] "+s);
	}
}