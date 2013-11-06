package algorithm;

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
			fh = new FileHandler("/afs/deptinfo-st.univ-fcomte.fr/users/aginhoux/git/parking/AD_Projet/out.txt");
	        logger.addHandler(fh);
	        SimpleFormatter formatter = new SimpleFormatter();  
	        fh.setFormatter(formatter); 
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
    	 logger.info("[D] "+s);
     }
}