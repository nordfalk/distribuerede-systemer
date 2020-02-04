package kapitel_19_soap;

import javax.xml.ws.Endpoint;

class Kontoserver {
	public static void main(String[] args) {
		System.out.println("publicerer kontotjeneste");
		KontoImpl k = new KontoImpl();
		k.bevægelser();
		k.overførsel(0);
		k.saldo();
		// Ipv6-addressen [::] svarer til Ipv4-adressen 0.0.0.0, der matcher alle maskinens netkort og IP-adresser
		Endpoint.publish("http://[::]:9901/kontotjeneste", k);
		System.out.println("Kontotjeneste publiceret.");
	}
}
