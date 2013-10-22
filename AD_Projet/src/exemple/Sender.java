package exemple;

import java.io.Serializable;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Set;

import reso.Client;
import reso.Reso;

public class Sender extends UnicastRemoteObject implements Client {

	private static final long serialVersionUID = -3879530234484702428L;

	protected Sender() throws RemoteException {
		super();
	}

	@Override
	public void receiveMessage(int from, int to, Serializable msg)
			throws RemoteException {
		System.out.println("Sender received a message from " + from + ": " + msg);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Reso reso = (Reso) Naming.lookup(Reso.NAME);
			
			int id = reso.declareClient(new Sender());
			System.out.println("Sender has id: " + id);
			
			Set<Integer> allClients = reso.getClients();

			System.out.print("All clients:");
			for (Integer i : allClients) {
				System.out.print(" " + i);
			}
			System.out.println();
			
			for (int i = 0; i < 10; i++) {
				reso.sendMessage(id, id - 1, "Hello you!");
			}
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
