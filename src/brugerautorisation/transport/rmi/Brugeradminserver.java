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
ant -q
rsync -a dist/* deltagere.html gmail-adgangskode.txt  javabog.dk:DistribueredeSystemer/

// På serveren javabog.dk - start med
cd DistribueredeSystemer
java -jar DistribueredeSystemer.jar
*/
