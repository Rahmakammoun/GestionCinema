import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SalleDAOImpl extends UnicastRemoteObject implements SalleDAORemote {
    private Connection con;

    SalleDAOImpl(String url, String username, String password) throws RemoteException {
        con = Connexion.getConnection(url, username, password);
    }

    @Override
    public int insertSalle(String nom, int capacite) throws RemoteException {
        String req = "INSERT INTO Salle (nomSalle, capacite) VALUES ( ?, ?)";

        try {
            PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, nom);
            ps.setInt(2, capacite);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("L'insertion de la salle a échoué, aucune ligne affectée.");
            }

            // Récupérer les clés générées automatiquement
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Retourner l'ID généré
            } else {
                throw new SQLException("Aucune clé générée automatiquement retournée après l'insertion de la salle.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'insertion de la salle ", e);
        }
    }

    @Override
    public int supprimerSalle(int id) throws RemoteException {
        int rowsAffected = 0;
        try {
           // Supprimer les événements associés au salle
            String deleteEventsQuery = "DELETE FROM Film WHERE idSalle = ?";
            PreparedStatement deleteEventsStatement = con.prepareStatement(deleteEventsQuery);
            deleteEventsStatement.setInt(1, id);
            rowsAffected += deleteEventsStatement.executeUpdate();
            deleteEventsStatement.close();

            // Supprimer la salle lui-même
            String deleteQuery = "DELETE FROM Salle WHERE idSalle = ?";
            PreparedStatement preparedStatement = con.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);
            rowsAffected += preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la suppression de la salle", e);
        }
        return rowsAffected;
    }


    @Override
    public int modifierSalle(int id, String nom, int capacite) throws RemoteException {
        int rowsAffected = 0;
        try {
            String updateQuery = "UPDATE Salle SET nomSalle = ?, capacite = ? WHERE idSalle = ?";
            PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
            preparedStatement.setString(1, nom);
            preparedStatement.setInt(2, capacite);
            preparedStatement.setInt(3, id);

            rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la modification de la salle", e);
        }
        return rowsAffected;
    }

    @Override
    public void afficherAll() throws RemoteException {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Salle");
            // Traiter le ResultSet pour afficher les Salles
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idSalle") + ", NomSalle: " + rs.getString("NomSalle")  + ", Capacité: " + rs.getInt("capacite"));
            }
            // Fermer le ResultSet et la déclaration
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'affichage de tous les salles", e);
        }
    }

    @Override
    public List<Salle> selectionRecherche(String recherche) throws RemoteException {
        List<Salle> salles= new ArrayList<>();
        String req = "SELECT idSalle, nomSalle, capacite FROM Salle WHERE nomSalle LIKE ?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1, "%" + recherche + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("idSalle");
                    String nom = rs.getString("nomSalle");
                    int capacite = rs.getInt("capacite");
                    salles.add(new Salle(id, nom,  capacite));
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la sélection des salles par recherche", e);
        }
        return salles;
    }



    @Override
    public ArrayList<Salle> selection() throws RemoteException {
        ArrayList<Salle> salles = new ArrayList<>();
        String req = "SELECT * FROM Salle";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(req);
            // Parcourir le ResultSet et ajouter chaque salle à la liste
            while (rs.next()) {
                Salle salle = new Salle(rs.getInt("idSalle"), rs.getString("nomSalle"),  rs.getInt("capacite"));
                salles.add(salle);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la sélection des salles", e);
        }
        return salles;
    }


    @Override
    public ArrayList<String> getAllSalles() throws RemoteException {
        ArrayList<String> salles = new ArrayList<>();
        String req = "SELECT nomSalle FROM Salle";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                salles.add(rs.getString("nomSalle"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return salles;
    }

    public  int getIdSalle(String SalleNom) throws RemoteException{
        int idSalle = -1; // Valeur par défaut si la salle n'est pas trouvée
        try {
            // Utiliser une requête SQL pour récupérer l'ID de salle
            String query = "SELECT idSalle FROM Salle WHERE nomSalle = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, SalleNom);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                idSalle = resultSet.getInt("idSalle");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return idSalle;
    }

    @Override
    public String getSalleNameById(int idSalle) throws RemoteException{
        String salleNom = null;
        try {
            // Utiliser une requête SQL pour récupérer le nom de la salle
            String query = "SELECT nomSalle FROM salle WHERE idSalle = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setInt(1, idSalle);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                salleNom = resultSet.getString("nomSalle");
            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salleNom;
    }

}
