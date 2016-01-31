package distribueredesystemer.transport.rmi;
import distribueredesystemer.data.Bruger;
import distribueredesystemer.data.Brugerdatabase;
import java.util.ArrayList;

public interface Brugeradmin extends java.rmi.Remote
{
	/**
	Henter alle en brugers data
	@return et Bruger-objekt med alle data
	*/
	Bruger hentBruger(String brugernavn, String adgangskode) throws java.rmi.RemoteException;

	/**
	Ændrer en brugers adgangskode
	@return et Bruger-objekt med alle data
	*/
	Bruger ændrAdgangskode(String brugernavn, String adgangskode, String nyAdgangskode) throws java.rmi.RemoteException;

	/**
	Sender en email til en bruger
	@param brugernavn Brugeren, som emailen skal sendes til
	@param emne Emnet - teksten DIST: bliver foranstillet i mailen
	@param tekst Brødteksten - teksten 'Sendt fra xxxx ' bliver tilføjet  i mailen
	@throws java.rmi.RemoteException
	*/
	void sendEmail(String brugernavn, String adgangskode, String emne, String tekst) throws java.rmi.RemoteException;

	void sendGlemtAdgangskodeEmail(String brugernavn) throws java.rmi.RemoteException;

	Object getEkstraFelt(String brugernavn, String adgangskode, String feltnavn) throws java.rmi.RemoteException;

	void setEkstraFelt(String brugernavn, String adgangskode, String feltnavn, Object værdi) throws java.rmi.RemoteException;
}