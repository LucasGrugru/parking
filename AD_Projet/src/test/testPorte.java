package test;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.After;
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
		r = new ResoImpl();
		registry = LocateRegistry.createRegistry(1199);
		registry.rebind(Reso.NAME, r);
		p = new Parking(10);
	}
	
	@After
	public void finish() throws AccessException, RemoteException, NotBoundException {
		registry.unbind(Reso.NAME);
	}
	
	@Test
	public void testCreation() throws RemoteException{
		Assert.assertEquals(this.p.portes.size(), 10);
	}
	
	@Test
	public void testDemande() throws RemoteException{
		this.p.demandeEntree(1);
		Assert.assertEquals(((PorteNaimiTrehel)this.p.portes.get(1)).next, -1);
	}
	
	

}
