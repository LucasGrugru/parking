package algorithm;

public enum ParkingAlgo {
	ABCAST, NAIMI_TREHEL, RICART_AGRAWALA;
	
	public static ParkingAlgo StringToEnumerate( String algo){
		return ParkingAlgo.valueOf(algo);
	}
}
