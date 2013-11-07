package algorithm;

public class MessageABCAST extends Message implements Comparable<MessageABCAST> {

	private static final long serialVersionUID = 1L;
	private int estampille;
	

	public MessageABCAST(String m, int estampille) {
		super(m);
		this.estampille = estampille;
	}
	
	public int getEstampille() {
		return this.estampille;
	}

	@Override
	public int compareTo(MessageABCAST m) {
		if(m.estampille > this.estampille) return -1;
		if(m.estampille < this.estampille) return 1;
		return 0;
	}

	
	

}