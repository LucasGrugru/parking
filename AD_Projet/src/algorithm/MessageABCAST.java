package algorithm;

public class MessageABCAST extends Message implements Comparable {

	private int estampille;
	

	public MessageABCAST(String m, int estampille) {
		super(m);
		this.estampille = estampille;
	}
	
	public int getEstampille() {
		return this.estampille;
	}

	@Override
	public int compareTo(Object o) {
		if(((MessageABCAST)o).estampille > this.estampille) return -1;
		if(((MessageABCAST)o).estampille < this.estampille) return 1;
		return 0;
	}

	
	

}