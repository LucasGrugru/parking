package algorithm;

import java.io.Serializable;

public class CaracParking implements Serializable{
	private int nbPlace;
	private int nbPorte;
	
	public int getNbPlace() {
		return nbPlace;
	}

	public int getNbPorte() {
		return nbPorte;
	}

	public CaracParking(int nbPlace, int nbPorte) {
		super();
		this.nbPlace = nbPlace;
		this.nbPorte = nbPorte;
	}

}
