import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreDAOImpl extends UnicastRemoteObject implements GenreDAORemote {

        static Connection con;

    GenreDAOImpl(String url, String username, String password) throws RemoteException {
            con=Connexion.getConnection(url,username,password);
        }

        @Override
        public int insertGenre(String nom) throws RemoteException {
            String req = "INSERT INTO Genre (nomGenre) VALUES (?)";

            try {
                PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, nom);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected == 0) {
                    throw new SQLException("L'insertion du genre a échoué, aucune ligne affectée.");
                }

                // Récupérer les clés générées automatiquement
                ResultSet generatedKeys = ps.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1); // Retourner l'ID généré
                } else {
                    throw new SQLException("Aucune clé générée automatiquement retournée après l'insertion du genre.");
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    @Override
    public int supprimerGenre(int id) throws RemoteException {
        int rowsAffected = 0;
        try {
            // Supprimer les événements associés au salle
            String deleteEventsQuery = "DELETE FROM Film WHERE idGenre = ?";
            PreparedStatement deleteEventsStatement = con.prepareStatement(deleteEventsQuery);
            deleteEventsStatement.setInt(1, id);
            rowsAffected += deleteEventsStatement.executeUpdate();
            deleteEventsStatement.close();

            // Supprimer le genre
            String deleteCategoryQuery = "DELETE FROM Genre WHERE idGenre = ?";
            PreparedStatement deleteCategoryStatement = con.prepareStatement(deleteCategoryQuery);
            deleteCategoryStatement.setInt(1, id);
            rowsAffected += deleteCategoryStatement.executeUpdate();
            deleteCategoryStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }


    @Override
        public int modifierGenre(int id, String nom) throws RemoteException{
            int rowsAffected = 0;
            try {
                // Créer votre requête SQL UPDATE
                String updateQuery = "UPDATE Genre SET nomGenre = ? WHERE idGenre = ?";
                // Préparer la déclaration
                PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
                // Définir les paramètres
                preparedStatement.setString(1, nom);
                preparedStatement.setInt(2, id);

                // Exécuter la requête
                rowsAffected = preparedStatement.executeUpdate();
                // Fermer la déclaration
                preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return rowsAffected;
        }

        @Override
        public void afficherAll() throws RemoteException {

        }
    public List<Genre> selectionRecherche(String recherche) throws RemoteException {
        List<Genre> categories = new ArrayList<>();
        String req = "SELECT idGenre, nomGenre FROM Genre WHERE nomGenre =?";
        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setString(1,  recherche );
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("idGenre");
                    String nom = rs.getString("nomGenre");
                    categories.add(new Genre(id, nom));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }


    public List<Genre> selection() throws RemoteException {
        List<Genre> categories = new ArrayList<>();
        String req = "SELECT idGenre, nomGenre FROM Genre";
        try (PreparedStatement ps = con.prepareStatement(req);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("idGenre");
                String nom = rs.getString("nomGenre");
                categories.add(new Genre(id, nom));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return categories;
    }
        public ArrayList<String> getAllGenres() throws RemoteException {
            ArrayList<String> genres = new ArrayList<>();
            String req = "SELECT nomGenre FROM Genre";
            try {
                PreparedStatement ps = con.prepareStatement(req);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    genres.add(rs.getString("nomGenre"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return genres;
        }
        public int getIdGenre(String genreNom) throws RemoteException {
            int idGenre = -1; // Valeur par défaut si le genre n'est pas trouvée
            try {
                // Utiliser une requête SQL pour récupérer l'ID du genre
                String query = "SELECT idGenre FROM Genre WHERE nomGenre = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setString(1, genreNom);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    idGenre = resultSet.getInt("idGenre");
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return idGenre;
        }
        public String getGenreNameById(int idGenre) throws RemoteException{
            String genreNom = null;
            try {
                // Utiliser une requête SQL pour récupérer le nom du genre
                String query = "SELECT nomGenre FROM Genre WHERE idGenre = ?";
                PreparedStatement statement = con.prepareStatement(query);
                statement.setInt(1, idGenre);
                ResultSet resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    genreNom = resultSet.getString("nomGenre");
                }
                resultSet.close();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return genreNom;
        }







}
