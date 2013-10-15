package algorithm;

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
	
	public PorteNaimiTrehel() {
		super();
		this.owner = 0;
		this.next = 0;
		this.jeton = false;
		this.sc = false;
	}
	
	@Override
	public void demandeEntree() {
		// TODO auto-generated method stub
	}
	
	@Override
	public void demandeSortie() {
		// TODO auto-generated method stub
	}
}
