import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

            try {
                // Créer le registre RMI sur le port spécifié (par exemple, 1099)
                Registry registry = LocateRegistry.
                        createRegistry(Config.RMI_PORT);

                // Créer une instance de l'implémentation du service
                GenreDAORemote genre = new GenreDAOImpl(Config.url, Config.USERNAME, Config.PASSWORD);
                SalleDAORemote salle = new SalleDAOImpl(Config.url, Config.USERNAME, Config.PASSWORD);
                FilmDAORemote film = new FilmDAOImpl(Config.url, Config.USERNAME, Config.PASSWORD);
               ReservationDAORemote reservation = new ReservationDAOImpl(Config.url, Config.USERNAME, Config.PASSWORD);



                // Lier l'implémentation du service avec un nom dans le registre RMI
                registry.bind("genre", genre);
                registry.bind("salle", salle);
                registry.bind("film", film);
                registry.bind("reservation", reservation);


                System.out.println("Serveur RMI démarré avec succès.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
}