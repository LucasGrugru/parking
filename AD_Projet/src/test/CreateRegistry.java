package test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class CreateRegistry {

	public static void main(String[] args) throws RemoteException{
		LocateRegistry.createRegistry(1201);
		System.out.println("Registry lancé");
	}
}
