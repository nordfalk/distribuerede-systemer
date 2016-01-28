package kapitel_19;
import java.rmi.Naming;
public class Kontoserver
{
	public static void main(String[] arg) throws Exception
	{
		// Enten: Kør programmet 'rmiregistry' fra mappen med .class-filerne, eller:
		java.rmi.registry.LocateRegistry.createRegistry(1099); // start i server-JVM

		KontoI k = new KontoImpl();
		Naming.rebind("rmi://localhost/kontotjeneste", k);
		System.out.println("Kontotjeneste registreret.");
	}
}
/*
	// På serveren javabog.dk - start med f.eks.:
	// java -Djava.rmi.server.hostname=javabog.dk -cp oop-projekt.jar kapitel_19.Kontoserver
		java.rmi.registry.LocateRegistry.createRegistry(20099); // lyt på port 20099
		KontoI k = new KontoImpl();
		System.setProperty("java.rmi.server.hostname", "javabog.dk");
		Naming.rebind("rmi://javabog.dk:20099/kontotjeneste", k);
*/
