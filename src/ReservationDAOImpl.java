import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAOImpl extends UnicastRemoteObject implements ReservationDAORemote {
    private static final long serialVersionUID = 1L;
    private Connection con;

    ReservationDAOImpl(String url, String username, String password) throws RemoteException {
        try {
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RemoteException("Failed to establish database connection.", e);
        }
    }

    @Override
    public int insertReservation(int idFilm, int nbPlace) throws RemoteException {
        String req = "INSERT INTO Reservation (idFilm, nbPlace) VALUES (?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(req, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, idFilm);
            ps.setInt(2, nbPlace);

            int rowsAffected = ps.executeUpdate();
            if (rowsAffected == 0) {
                throw new SQLException("L'insertion de la réservation a échoué, aucune ligne affectée.");
            }

            // Récupérer les clés générées automatiquement
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1); // Retourner l'ID généré
            } else {
                throw new SQLException("Aucune clé générée automatiquement retournée après l'insertion de la réservation.");
            }
        } catch (SQLException e) {
            throw new RemoteException("Failed to insert reservation.", e);
        }
    }

    @Override
    public int supprimerReservation(int reservationId) throws RemoteException {
        int rowsAffected = 0;
        try {
            // Supprimer les événements associés au réservation
            String deleteEventsQuery = "DELETE FROM reservation WHERE idFilm = ?";
            PreparedStatement deleteEventsStatement = con.prepareStatement(deleteEventsQuery);
            deleteEventsStatement.setInt(1, reservationId);
            rowsAffected += deleteEventsStatement.executeUpdate();
            deleteEventsStatement.close();

            String deleteQuery = "DELETE FROM Reservation WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, reservationId);
            rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            throw new RemoteException("Failed to delete reservation.", e);
        }
        return rowsAffected;
    }

    @Override
    public int modifierReservation(int id, int idFilm, int nbPlace) throws RemoteException {
        int rowsAffected = 0;
        try {
            String updateQuery = "UPDATE Reservation SET idFilm = ?, nbPlace = ? WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
            preparedStatement.setInt(1, idFilm);
            preparedStatement.setInt(2, nbPlace);
            preparedStatement.setInt(3, id);
            rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    @Override
    public void afficherAll() throws RemoteException {

    }

    @Override
    public boolean idFilmExists(int idFilm) throws RemoteException {
        try {
            String query = "SELECT COUNT(*) FROM Film WHERE idFilm = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, idFilm);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int getCapaciteSalle(int idFilm) throws RemoteException {
        int capacite = 0;
        try {

            String query = "SELECT capacite FROM Film f JOIN Salle s ON f.idSalle =s.idSalle WHERE f.idFilm = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, idFilm);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                capacite = resultSet.getInt("capacite");
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return capacite;
    }

    public List<Reservation> selection() throws RemoteException {
        List<Reservation> reservations = new ArrayList<>();
        try {
            Statement st = con.createStatement();
            String req = "Select * from Reservation";
            ResultSet rs = st.executeQuery(req);
            while (rs.next()) {
                int id = rs.getInt("id");
                int idEven = rs.getInt("idFilm");
                int nbPlace = rs.getInt("nbPlace");
                Reservation reservation = new Reservation(id, idEven, nbPlace);
                reservations.add(reservation);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return reservations;
    }

    @Override
    public int getReservationsActuelles(int idFilm) throws RemoteException {
        try {
            String query = "SELECT SUM(nbPlace) FROM Reservation WHERE idFilm = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, idFilm);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // Si aucune réservation n'est trouvée, retourne 0
    }

    @Override
    public int getNbPlace(int idReservation) throws RemoteException {
        try {
            String query = "SELECT nbPlace FROM Reservation WHERE id = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, idReservation);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("nbPlace");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0; // ou une valeur par défaut appropriée si aucune valeur n'est trouvée
    }

    // Implement other methods similarly

    @Override
    public ArrayList<Reservation> getReservationsByFilm(String eventIdOrName) throws RemoteException {
        ArrayList<Reservation> reservations = new ArrayList<>();
        String query = "SELECT Reservation.*, Film.titre " +
                "FROM Reservation " +
                "INNER JOIN Film ON Reservation.idFilm = Film.idFilm " +
                "WHERE Reservation.idFilm = ? OR Film.titre LIKE '%' || ? || '%'\n";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, eventIdOrName);
            preparedStatement.setString(2, eventIdOrName);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                // Ajouter les données de la réservation à la liste des réservations
                Reservation reservationData = new Reservation(resultSet.getInt("ID"), resultSet.getInt("idFilm"), resultSet.getInt("NbPlace"));
                reservations.add(reservationData);
            }
        } catch (SQLException e) {
            throw new RemoteException("Failed to get reservations by film.", e);
        }
        return reservations;
    }
}
