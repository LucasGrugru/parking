package logger;

import java.rmi.Naming;
import java.rmi.RemoteException;

public class MyLogger {
	 private static IRMILogger logger;
	 

	 public static void createLogger(){
		 if( logger == null ){
			 try {
				logger = (IRMILogger) Naming.lookup("rmi://192.168.1.9:1099/"+IRMILogger.NAME);
			} catch (Exception e) {
				logger = null;
			} 
		 }
	 }
     public static void log( String s){
    	 createLogger();
	    try {
			logger.log(s);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
     }
     
     public static void debug( String s ){
    	 createLogger();
    	 try {
			logger.log(s);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
     }
}