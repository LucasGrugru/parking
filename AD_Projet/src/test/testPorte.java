package test;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import reso.Reso;
import reso.ResoImpl;
import algorithm.Parking;
import algorithm.PorteNaimiTrehel;

public class testPorte {

	public ResoImpl r;
	public Registry registry;
	public Parking p;
	
	@Before
	public void init() throws RemoteException {
		LocateRegistry.createRegistry(1099);
		r = new ResoImpl();
		registry = LocateRegistry.getRegistry();
	    registry.rebind(Reso.NAME, r);
	    p = new Parking(10);
	}
	
	@Test
	public void testCreation(){
		Assert.assertEquals(this.p.portes.size(), 10);
	}
	
	@Test
	public void testDemande() throws RemoteException{
		this.p.demandeEntree(0);
		System.out.println(((PorteNaimiTrehel)this.p.portes.get(0)).next);
		Assert.assertEquals(((PorteNaimiTrehel)this.p.portes.get(0)).next, -1);
	}

}
