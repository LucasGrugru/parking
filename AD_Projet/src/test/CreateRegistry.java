package test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import reso.Reso;
import reso.ResoImpl;

public class CreateRegistry {

	public static void main(String[] args) throws RemoteException{
		
		LocateRegistry.createRegistry(1204);
		System.out.println("Registry lancé");
	}
}
