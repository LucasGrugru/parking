package algorithm;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IParking extends Remote {
	public static final String NAME = "algorithm.IPARKING";

	boolean declarePorte(Porte porte) throws RemoteException;
	boolean undeclarePorte(Porte porte) throws RemoteException;
}
