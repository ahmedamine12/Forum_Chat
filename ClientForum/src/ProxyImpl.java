import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ProxyImpl extends UnicastRemoteObject implements Proxy {
    private User client;

    public ProxyImpl(User client) throws RemoteException {
        this.client = client;
    }

    public void ecouter(String msg) throws RemoteException {
        client.ecrire(msg);
    }
}
