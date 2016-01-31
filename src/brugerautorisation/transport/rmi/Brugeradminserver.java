package brugerautorisation.transport.rmi;
import brugerautorisation.data.Brugerdatabase;
import java.rmi.Naming;
public class Brugeradminserver
{
	public static void main(String[] arg) throws Exception
	{
		Brugerdatabase db = Brugerdatabase.getInstans();
		BrugeradminImpl k = new BrugeradminImpl();
		k.db = db;

		java.rmi.registry.LocateRegistry.createRegistry(1099); // start i server-JVM

		Naming.rebind("rmi://localhost/brugeradmin", k);
		System.out.println("Brugeradmin registreret.");
	}
}
/* Overfør til server med f.eks.:

cd /home/j/DistribueredeSystemer/DistribueredeSystemer/
rsync dist/* deltagere.html javabog.dk:DistribueredeSystemer/

// På serveren javabog.dk - start med f.eks.:
java -Djava.rmi.server.hostname=javabog.dk -cp DistribueredeSystemer/DistribueredeSystemer.jar kapitel_19.Brugeradminserver

		java.rmi.registry.LocateRegistry.createRegistry(20099); // lyt på port 20099
		Brugeradmin k = new BrugeradminImpl();
		System.setProperty("java.rmi.server.hostname", "javabog.dk");
		Naming.rebind("rmi://javabog.dk:20099/kontotjeneste", k);
*/
