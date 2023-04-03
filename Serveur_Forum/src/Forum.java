import java.rmi.*;
// interface of the forum server
public interface Forum extends Remote {
    public int entrer(Proxy pr) throws RemoteException;
    public void dire(int id, String msg) throws RemoteException;
    public String qui() throws RemoteException;
    public void quiter(int id) throws RemoteException;
}