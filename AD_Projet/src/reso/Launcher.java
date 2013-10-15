package reso;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class Launcher {

	public static void main(String[] args) {

		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			
			Reso reso = new ResoImpl();
			registry.rebind(Reso.NAME, reso);
			
			System.out.println("Reso successfully launched!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

}
