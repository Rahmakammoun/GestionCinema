import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface SalleDAORemote extends Remote {
    int insertSalle(String nom,int capacite) throws RemoteException;
    int supprimerSalle(int id) throws RemoteException;
    int modifierSalle(int id, String nom, int capacite) throws RemoteException;
    void afficherAll() throws RemoteException;
    List<Salle> selectionRecherche(String recherche) throws RemoteException;
    ArrayList<Salle> selectionSalles() throws RemoteException;
    ArrayList<Salle> selection() throws RemoteException;
    ArrayList<String>  getAll()throws RemoteException;
    int getIdSalle(String SalleNom) throws RemoteException;

    String getSalleNameById(int idLieu) throws RemoteException;

    ArrayList<String> getAllSalles() throws RemoteException;
}
