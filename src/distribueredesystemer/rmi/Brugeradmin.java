package distribueredesystemer.rmi;
import distribueredesystemer.data.Bruger;
import distribueredesystemer.data.Brugerdatabase;
import java.util.ArrayList;

public interface Brugeradmin extends java.rmi.Remote
{
	Bruger hentBruger(String brugernavn, String adgangskode) throws java.rmi.RemoteException;

	Bruger ændrAdgangskode(String brugernavn, String adgangskode, String nyAdgangskode) throws java.rmi.RemoteException;

	void sendEmail(String brugernavn, String adgangskode, String emne, String tekst) throws java.rmi.RemoteException;
}