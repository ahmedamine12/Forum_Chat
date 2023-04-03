import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServeurForum {

    public static void main(String args[]) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            // Création du serveur ForumImpl
            ForumImpl forum = new ForumImpl();

            // Exportation du serveur ForumImpl
            Naming.rebind("rmi://localhost:1099/Forum",forum);

            // Création et démarrage du registry RMI


            // Association du stub du serveur au registry
            System.out.println("Serveur pret.");
        } catch (Exception e) {
            System.err.println("Erreur Serveur : " + e.toString());
            e.printStackTrace();
        }
    }
}
