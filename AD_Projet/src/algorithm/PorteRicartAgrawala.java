package algorithm;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.HashSet;
import java.util.Set;

public class PorteRicartAgrawala extends Porte {

	private static final long serialVersionUID = 1L;
	private int hsc;
	private int h;
	private boolean r;
	private Set<Integer> x;
	private int nrel;
	private Set<Integer> v;
	
	protected PorteRicartAgrawala(int place) throws RemoteException, MalformedURLException, NotBoundException {
		super(place);
		this.hsc = 0;
		this.h = 0;
		this.r = false;
		this.x = new HashSet<Integer>();
		this.nrel = 0;
		this.v = new HashSet<Integer>();
	}

	protected PorteRicartAgrawala(int place, int nombreVoisin) throws RemoteException, MalformedURLException, NotBoundException {
		this(place);
		for( int i = 0; i < nombreVoisin; i++ ){
			if( i != this.id ){
				this.v.add( i );
			}
		}
	}
	@Override
	public void demandeEntree() {
		printDebug("demande d'entree");
		accepteSC(true);
	}
	
	@Override
	public void demandeSortie() {
		printDebug("demande de sortie");
		accepteSC(false);
	}
	
	public void accepteSC( boolean isEntree){
		this.r = true;
		this.hsc = this.h + 1;
		this.nrel = this.v.size();
		for( Integer voisin: this.v){
			try {
				printDebug("envoi : "+"REQ-"+this.hsc);
				this.reso.sendMessage(this.id, voisin, new Message("REQ-"+this.hsc,this.id));
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(Thread.currentThread().getId());
		while( this.nrel != 0 ){
			System.out.println("NREL: "+this.nrel);
			synchronized (this) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		//ACCES SECTION CRITIQUE
		System.out.println("ZONE CRITIQUE");
		if( isEntree ){
			
		}else{
			
		}
	}
	
	public void accepteREQ( int vh, int demandeur){
		this.h = 1 + (this.hsc>vh?this.hsc:vh);
		if( this.r && ((this.hsc < vh) || (this.hsc == vh) && this.id < demandeur)){
			synchronized (this.x) {
				this.x.add( demandeur );
			}
		}else{
			try {
				printDebug("envoi : "+"REL-");
				this.reso.sendMessage(this.id, demandeur, new Message("REL-", this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void accepteREL( int demandeur){
		this.nrel--;
	}
	
	public void liberationSC(){
		this.r = false;
		for( Integer i: this.x ){
			try {
				printDebug("envoi : "+"REL-");
				this.reso.sendMessage(this.id, i, new Message("REL-", this.id));
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		this.x.clear();
	}
	
	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		System.out.println(Thread.currentThread().getId());
		Message message = (Message) msg;
		String messageText = message.getMessage();
		MyLogger.debug("porte n"+this.id+": RECEPTION : "+messageText);
		if( messageText.startsWith("REL-") ){
			accepteREL( from );
			synchronized (this) {
				notifyAll();				
			}
		}else if (messageText.startsWith("REQ-")){
			int hdemandeur = Integer.valueOf( messageText.substring(4));
			accepteREQ(hdemandeur, from);
		}
	}
	
	public void printDebug(String s){
		MyLogger.debug("Porte n°"+this.id+" "+s);
	}
}