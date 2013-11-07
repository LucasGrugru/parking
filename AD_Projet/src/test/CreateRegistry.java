package test;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import reso.Reso;
import reso.ResoImpl;

public class CreateRegistry {

	public static void main(String[] args) throws RemoteException{
		
		Registry registry = LocateRegistry.createRegistry(1099);
		System.out.println("Registry lancé");
		try {
			registry.bind(Reso.NAME, new ResoImpl());
		} catch (AlreadyBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
}
