package brugerautorisation.transport.soap;

import javax.xml.ws.Endpoint;

class Kontoserver {
	public static void main(String[] args) {
		System.out.println("publicerer kontotjeneste");
		KontoI k = new KontoImpl();
    // Ipv6-addressen [::] svarer til Ipv4-adressen 0.0.0.0, der matcher alle maskinens netkort og 
		Endpoint.publish("http://[::]:9901/kontotjeneste", k);
		System.out.println("Kontotjeneste publiceret.");
	}
}
