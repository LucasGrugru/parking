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
	public Parking p;
	
	@Test
	public void testCreation() throws RemoteException, NotBoundException{
		r = new ResoImpl();
		Registry registry = LocateRegistry.createRegistry(1099);
		registry.rebind(Reso.NAME, r);
		p = new Parking(10);
		Assert.assertEquals(this.p.portes.size(), 10);
		//this.p.demandeEntree(1);
		registry.unbind(Reso.NAME);;
	}
	
//	@Test
//	public void testDemande() throws RemoteException{
//		this.p.demandeEntree(1);
//		Assert.assertEquals(((PorteNaimiTrehel)this.p.portes.get(1)).next, -1);
//	}
	
	

}
