import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface ReservationDAORemote extends Remote {
    int insertReservation(int idFilm, int nbPlace) throws RemoteException;

    int supprimerReservation(int reservationId) throws RemoteException;

    int modifierReservation(int id, int idFilm, int nbPlace) throws RemoteException;

    void afficherAll() throws RemoteException;

    boolean idFilmExists(int idFilm) throws RemoteException;

    int getCapaciteSalle(int idFilm) throws RemoteException;

    List<Reservation> selection() throws RemoteException;

    int getReservationsActuelles(int idFilm) throws RemoteException;

    int getNbPlace(int idReservation) throws RemoteException;

    ArrayList<Reservation> getReservationsByFilm(String eventIdOrName) throws RemoteException;
}
