import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ClientForum {

    public static void main(String[] args) {
        try {
            
            // Récupération du stub du serveur
            Forum stub = (Forum) Naming.lookup("rmi://localhost:1099/Forum");

            // Création d'un nouvel utilisateur
            UserImpl user = new UserImpl(stub);

            // Connexion de l'utilisateur au forum
          //  int id = stub.entrer(user.getProxy());
        //    System.out.println("id est " + id);
            System.out.println("user id " + user.getId());
          //  user.setId(id);


        } catch (Exception e) {
            System.err.println("Erreur Client : " + e.toString());
            e.printStackTrace();
        }
    }
}
