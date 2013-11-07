package algorithm;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MyLogger {
	 private static Logger logger;
	 
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
     public static void log( String s){
    	 createLogger();
	    logger.info(s);  
     }
     
     public static void debug( String s ){
    	 createLogger();
    	 logger.info(s);
     }
}