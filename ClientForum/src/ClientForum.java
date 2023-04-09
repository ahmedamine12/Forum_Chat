import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientForum {

    public static void main(String[] args) {
        try {
            // Try to locate the RMI registry
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);

            // Récupération du stub du serveur
            Forum stub = (Forum) registry.lookup("Forum");

            // Création d'un nouvel utilisateur
            UserImpl user = new UserImpl(stub);

            // Connexion de l'utilisateur au forum
            // int id = stub.entrer(user.getProxy());
            // System.out.println("id est " + id);
            System.out.println("user id " + user.getId());
            // user.setId(id);

        } catch (java.rmi.ConnectException e) {
            System.err.println("Impossible de se connecter au serveur : vérifiez que le serveur est bien en cours d'exécution");
        } catch (Exception e) {
            System.err.println("Erreur Client : " + e.toString());
            e.printStackTrace();
        }
    }
}
