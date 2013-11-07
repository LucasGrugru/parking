package reso;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import algorithm.IParking;
import algorithm.Parking;


public class Launcher {

	public static void main(String[] args) {
		int nbPlace = 10;
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			
			Reso reso = new ResoImpl();
			registry.rebind(Reso.NAME, reso);
			if( args != null && args.length != 0 )
				nbPlace = Integer.valueOf(args[0]);
			registry.rebind(IParking.NAME, new Parking(nbPlace));
			
			System.out.println("Reso successfully launched!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}