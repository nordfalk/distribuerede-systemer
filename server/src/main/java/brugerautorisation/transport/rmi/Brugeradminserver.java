package brugerautorisation.transport.rmi;
import brugerautorisation.server.Brugerdatabase;
import java.io.IOException;
import java.rmi.Naming;
public class Brugeradminserver
{
  /* Efter at det i 3 år har fungeret fint at impl er en lokal variabel dukker dette problem pludselig op:
   * https://stackoverflow.com/questions/645208/java-rmi-nosuchobjectexception-no-such-object-in-table
   * Der er ikke skiftet Java-version eller noget andet. Det begynder bare at ske. Velkommen til Distribuerede systemer.
   */
  private static BrugeradminImpl impl;
	public static void main(String[] arg) throws IOException
	{
		Brugerdatabase db = Brugerdatabase.getInstans();
		System.out.println("Publicerer Brugeradmin over RMI");
		impl = new BrugeradminImpl();
		impl.db = db;
		java.rmi.registry.LocateRegistry.createRegistry(1099); // start rmiregistry i server-JVM
		Naming.rebind("rmi://localhost/brugeradmin", impl);
		System.out.println("Brugeradmin publiceret over RMI");
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
