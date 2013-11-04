package algorithm;

import java.io.Serializable;

public class Message implements Serializable {

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
}
