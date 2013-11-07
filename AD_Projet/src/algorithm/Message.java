package algorithm;

import java.io.Serializable;

public class Message implements Serializable {

	private static final long serialVersionUID = 1L;
	public String message;
	public int demandeur;
	
	public Message(String m, int d) {
		this.message = m;
		this.demandeur = d;
	}
	
	public Message(String m) {
		this.message = m;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getDemandeur() {
		return this.demandeur;
	}
}
