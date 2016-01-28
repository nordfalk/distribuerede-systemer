package kapitel_19;
import java.rmi.Naming;
import java.util.ArrayList;

public class Kontoklient
{
	public static void main(String[] arg) throws Exception
	{
		//  KontoI k =(KontoI) Naming.lookup("rmi://javabog.dk:20099/kontotjeneste");
		KontoI k =(KontoI) Naming.lookup("rmi://localhost/kontotjeneste");
    k.overførsel(100);
    k.overførsel(50);
		System.out.println( "Saldo er: "+ k.saldo() );
		k.overførsel(-200);
		k.overførsel(51);
		System.out.println( "Saldo er: "+ k.saldo() );
		ArrayList<String> bevægelser = k.bevægelser();

		System.out.println( "Bevægelser er: "+ bevægelser );
	}
}