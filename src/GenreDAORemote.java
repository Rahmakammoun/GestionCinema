import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface GenreDAORemote extends Remote {
    public int insertGenre(String nom) throws RemoteException;
    public int supprimerGenre(int id) throws RemoteException;
    public int modifierGenre(int id, String nom) throws RemoteException;
    public void afficherAll() throws RemoteException;
    public List<Genre> selectionRecherche(String recherche) throws RemoteException;
    public List<Genre> selection() throws RemoteException;
    public ArrayList<String> getAllGenres() throws RemoteException;
    public int getIdGenre(String genreNom) throws RemoteException;
    public String getGenreNameById(int idgenre) throws RemoteException;
}
