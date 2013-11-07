package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class PorteRicartAgrawala extends Porte {

	private static final String MESSAGE_ENTREE = "ENTREE";
	private static final String MESSAGE_SORTIE = "SORTIE";
	private static final String MESSAGE_ACCORD = "ACK";

	private static final long serialVersionUID = 1L;
	private Set<Integer> portes;
	private int nbPlace;
	private boolean isEntree;
	private int horloge;
	private int lastHorloge;
	private int lastPorte;
	private Set<Integer> porteAttente;
	private int reponsesAttendues;
	
	protected PorteRicartAgrawala(int place) throws RemoteException, MalformedURLException, NotBoundException {
		super(place);
		portes = new HashSet<Integer>();
		nbPlace = place;
		isEntree = false;
		horloge = 0;
		lastHorloge = 0;
		lastPorte = -1;
		porteAttente = new HashSet<Integer>();
		reponsesAttendues = 0;
	}

	protected PorteRicartAgrawala(int place, int nombreVoisin) throws RemoteException, MalformedURLException, NotBoundException {
		this(place);
		for( int i = 0; i < nombreVoisin; i++ ){
			if( i != this.id ){
				portes.add( i );
			}
		}
	}
	@Override
	public void demandeEntree() {
		this.horloge++;
		if( nbPlace == 0 ) return;
		isEntree = true;
		lastHorloge = this.horloge;
		reponsesAttendues = portes.size();
		for( Integer porte: portes ){
			try {
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
		
		isEntree = false;
		
		for( Integer porte: porteAttente ){
			if( nbPlace > 0 )
				nbPlace--;
			try {
				reso.sendMessage(this.id, porte, new Message(MESSAGE_ACCORD, this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		porteAttente.clear();
		if( nbPlace == 0 )
			return;
		else
			nbPlace--;
	}
	
	@Override
	public void demandeSortie() {
		for( Integer porte: portes ){
			try {
				reso.sendMessage(this.id, porte, new Message(MESSAGE_SORTIE,this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getENTREE( int horloge, int porte){
		this.horloge = 1 + (this.horloge > horloge?this.horloge:horloge);
		if( isEntree ){
			if( ( horloge < lastHorloge ) || (( lastHorloge == horloge) && (porte < this.id))){
				if( nbPlace > 0 )
					nbPlace--;
				try {
					reso.sendMessage(this.id, porte, new Message(MESSAGE_ACCORD, this.id));
				} catch (RemoteException e) {
					e.printStackTrace();
				}
			}else{
				porteAttente.add( porte );
			}
		}else{
			if( nbPlace > 0 )
				nbPlace--;
			try {
				reso.sendMessage(this.id, porte, new Message(MESSAGE_ACCORD, this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void getAccept(){
		reponsesAttendues--;
	}
	
	public void getSortie(){
		nbPlace++;
	}
	
	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		Message message = (Message) msg;
		String messageText = message.getMessage();
		if( messageText.equals(MESSAGE_ACCORD) ){
			getAccept();
		}else if( messageText.equals(MESSAGE_SORTIE)){
			getSortie();
		}else if( messageText.startsWith(MESSAGE_ENTREE)){
			int horloge = Integer.valueOf( messageText.substring(6));
			getENTREE(horloge, from);
		}
	}
	
	public void printDebug(String s){
		MyLogger.debug("Porte n°"+this.id+" "+s);
	}
}