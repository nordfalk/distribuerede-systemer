package kapitel_19;
import java.util.ArrayList;

public interface KontoI extends java.rmi.Remote
{
	void overførsel(int kroner)    throws java.rmi.RemoteException;
	int saldo()                    throws java.rmi.RemoteException;
	ArrayList<String> bevægelser() throws java.rmi.RemoteException;
}