import java.rmi.Remote;
import java.rmi.RemoteException;
// remote interface proxy
public interface Proxy extends Remote
{
    public void ecouter(String msg) throws RemoteException;
}