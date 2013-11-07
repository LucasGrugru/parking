package reso;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import logger.IRMILogger;
import logger.RMILogger;
import algorithm.IParking;
import algorithm.Parking;


public class Launcher {

	public static void main(String[] args) {
		int nbPlace = 5;
		int nbPorte = 5;
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			
			Reso reso = new ResoImpl();
			registry.rebind(Reso.NAME, reso);
			if( args != null && args.length > 1 ){
				nbPlace = Integer.valueOf(args[0]);
				nbPorte = Integer.valueOf(args[1]);
			}
			Parking p = new Parking(nbPlace, nbPorte);
			registry.rebind(IParking.NAME, p);
			
			registry.rebind(IRMILogger.NAME, new RMILogger());
			System.out.println("Reso successfully launched!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}