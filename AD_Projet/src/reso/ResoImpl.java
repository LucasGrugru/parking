package reso;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ResoImpl extends UnicastRemoteObject implements Reso {

	private static class Message {

		public final int id;
		public final int from;
		public final int to;
		public final Serializable content;

		public Message(int id, int from, int to, Serializable content) {
			this.id = id;
			this.from = from;
			this.to = to;
			this.content = content;
		}

		@Override
		public String toString() {
			return "message(" + id + ") from " + from + " to " + to;
		}

	}

	private static final long serialVersionUID = 8972046147564128682L;

	private final static int MAX_DELAY_IN_SECONDS = 5;


	private final AtomicInteger currentId;
	private final AtomicInteger currentMessageId;
	private final Map<Integer, Client> clients;
	private final ScheduledThreadPoolExecutor scheduler;
	private final Random random;

	public ResoImpl() throws RemoteException {
		super();
		this.currentId = new AtomicInteger(0);
		this.currentMessageId = new AtomicInteger(0);
		this.clients = Collections.synchronizedMap(new HashMap<Integer, Client>());
		this.scheduler = new ScheduledThreadPoolExecutor(20);
		this.random = new Random(System.currentTimeMillis());
	}

	@Override
	public int declareClient(Client client) throws RemoteException {
		int id = currentId.getAndIncrement();
		clients.put(id, client);
		return id;
	}

	@Override
	public Set<Integer> getClients() throws RemoteException {
		// need to build a new set because clients.keySet() is not Serializable
		return new TreeSet<Integer>(clients.keySet());
	}

	@Override
	public void sendMessage(int from, int to, Serializable content) throws RemoteException {

		final Message msg = new Message(currentMessageId.getAndIncrement(), from, to, content);
		int delay = 0;
		synchronized (random) {
			delay = (int) (random.nextDouble() * MAX_DELAY_IN_SECONDS * 1000);
		}
		System.out.println("[RESO] " + msg + " arrived (delay: " + delay + "ms)");

		scheduler.schedule(new Runnable() {
			@Override
			public void run() {
				Client client = clients.get(msg.to);
				
				if (client == null) {
					System.err.println("[RESO] error, unknown client id: " + msg.to);
					return;
				}

				try {
					System.out.println("[RESO] " + msg + " transmitted");
					client.receiveMessage(msg.from, msg.to, msg.content);
				} catch (RemoteException e) {
					System.err.println("[RESO] error while sending " + msg);
					e.printStackTrace();
				}
			}
		}, delay, TimeUnit.MILLISECONDS);
	}


}