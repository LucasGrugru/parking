package logger;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class RMILogger extends UnicastRemoteObject implements IRMILogger{

	private static final long serialVersionUID = 1L;
	private static Logger logger;
	
	public RMILogger() throws RemoteException {
		super();
	}
	
	 public static void createLogger() {
		if( logger != null ){
			return;
	 	}
		logger = Logger.getLogger("MyLog");
		logger.setUseParentHandlers(false);
	    FileHandler fh;
       try {
			fh = new FileHandler("./out.log");
			System.out.println("Emplacement du log: "+new File("./out.log").getAbsolutePath());
	        logger.addHandler(fh);
	        fh.setFormatter(new MyFormatter()); 
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	 }
	 
	@Override
	public void log(String s) throws RemoteException{
   	 	createLogger();
	    logger.info(s);  
	}

}
