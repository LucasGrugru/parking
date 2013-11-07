package logger;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMILogger extends Remote {
	public static final String NAME = "logger.IRMILOGGER";
	public void log( String s ) throws RemoteException;
}
