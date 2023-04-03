import java.rmi.*;
// remote interface proxy
public interface Proxy extends Remote
{
    public void ecouter(String msg) throws RemoteException;
}