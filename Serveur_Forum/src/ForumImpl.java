import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ForumImpl extends UnicastRemoteObject implements Forum {
    private ArrayList<Proxy> users;

    public ForumImpl() throws RemoteException {
        users = new ArrayList<Proxy>();
    }

    public int entrer(Proxy pr) throws RemoteException {
        int id = users.size();
        users.add(pr);
        this.dire(id, " participe au forum");
        return id;
    }

    public void dire(int id, String msg) throws RemoteException {
    	if(users.get(id) != null) {
    		for (Proxy pr : users) {
    			pr.ecouter("User " + id + ": " + msg);
    		}
    	}
    }

    public String qui() throws RemoteException {
        String usersStr = "";
        for (int i = 0; i < users.size(); i++) {
            usersStr += "User " + i + "\n";
        }
        return usersStr;
    }

    public void quiter(int id) throws RemoteException {
        users.remove(id);
    }
}
