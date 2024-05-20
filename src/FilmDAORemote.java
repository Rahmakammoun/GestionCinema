import java.math.BigDecimal;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface FilmDAORemote extends Remote {
    int insertFilm(String titre, int idGenre, int idSalle, double prix, Date date, Time heureDeb, Time heureFin) throws RemoteException;
    int supprimerFilm(int id) throws RemoteException;
    int modifierFilm(int id, String titre, int idGenre, int idSalle, BigDecimal prix, Date date, Time heureDeb, Time heureFin) throws RemoteException;
    void afficherAll() throws RemoteException;
    List<Film> selection() throws RemoteException;
    List<Film> getAllFilms() throws RemoteException;
    List<Film> getFilmsByGenre(int idGenre) throws RemoteException;


}
