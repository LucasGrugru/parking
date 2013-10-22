package exemple;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import reso.Client;
import reso.Reso;

public class Receiver extends UnicastRemoteObject implements Client {

	private static final long serialVersionUID = 842940924381059064L;

	protected Receiver() throws RemoteException {
		super();
	}

	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		System.out.println("Receiver received a message from " + from + ": " + msg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Reso reso = (Reso) Naming.lookup(Reso.NAME);
			
			int id = reso.declareClient(new Receiver());
			System.out.println("Receiver has id: " + id);
			
			Set<Integer> allClients = reso.getClients();

			System.out.print("All clients:");
			for (Integer i : allClients) {
				System.out.print(" " + i);
			}
			System.out.println();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
