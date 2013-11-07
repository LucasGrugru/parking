package algorithm;

public class MessageABCAST extends Message {

	private int estampille;

	public MessageABCAST(String m, int estampille) {
		super(m);
		this.estampille = estampille;
	}
	
	public int getEstampille() {
		return this.estampille;
	}
	
	

}
