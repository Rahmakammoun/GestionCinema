import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmDAOImpl extends UnicastRemoteObject implements FilmDAORemote {
    private static final long serialVersionUID = 1L;
    private Connection con;

    public FilmDAOImpl(String url, String username, String password) throws RemoteException {
        super();
        con = Connexion.getConnection(url, username, password);
    }

    @Override
    public int insertFilm(String titre, int idGenre, int idSalle, double prix, Date date, Time heureDeb, Time heureFin) throws RemoteException {
        String req = "INSERT INTO Film (titre, idGenre, idSalle, prix, date, heureDeb, heureFin) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(req);
            ps.setString(1, titre);
            ps.setInt(2, idGenre);
            ps.setInt(3, idSalle);
            ps.setDouble(4, prix);
            ps.setDate(5, date);
            ps.setTime(6, heureDeb);
            ps.setTime(7, heureFin);
            return ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int supprimerFilm(int id) throws RemoteException {
        int rowsAffected = 0;
        try {
            String deleteEventsQuery = "DELETE FROM reservation WHERE idFilm = ?";
            PreparedStatement deleteEventsStatement = con.prepareStatement(deleteEventsQuery);
            deleteEventsStatement.setInt(1, id);
            rowsAffected += deleteEventsStatement.executeUpdate();
            deleteEventsStatement.close();
            String deleteQuery = "DELETE FROM Film WHERE idFilm = ?";
            PreparedStatement preparedStatement = con.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, id);
            rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    @Override
    public int modifierFilm(int id, String titre, int idGenre, int idSalle, BigDecimal prix, Date date, Time heureDeb, Time heureFin) throws RemoteException {
        int rowsAffected = 0;
        try {
            String updateQuery = "UPDATE Film SET titre = ?, idGenre = ?, idSalle = ?, prix = ?, date = ?, heureDeb = ?, heureFin = ? WHERE idFilm = ?";
            PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
            preparedStatement.setString(1, titre);
            preparedStatement.setInt(2, idGenre);
            preparedStatement.setInt(3, idSalle);
            preparedStatement.setBigDecimal(4, prix);
            preparedStatement.setDate(5, date);
            preparedStatement.setTime(6, heureDeb);
            preparedStatement.setTime(7, heureFin);
            preparedStatement.setInt(8, id);
            rowsAffected = preparedStatement.executeUpdate();
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    @Override
    public void afficherAll() throws RemoteException {
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Film");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("idFilm") + ", Titre: " + rs.getString("titre") + ", ID Genre: " + rs.getInt("idGenre") + ", ID Salle: " + rs.getInt("idSalle") + ", Prix: " + rs.getDouble("prix") + ", Date: " + rs.getDate("date") + ", Heure Début: " + rs.getTime("heureDeb") + ", Heure Fin: " + rs.getTime("heureFin"));
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Film> selection() throws RemoteException {
        List<Film> films = new ArrayList<>();
        String req = "SELECT * FROM Film";
        try {
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery(req);
            while (rs.next()) {
                Film film = new Film(rs.getInt("idFilm"), rs.getString("titre"), rs.getInt("idGenre"), rs.getInt("idSalle"), rs.getBigDecimal("prix"), rs.getDate("date"), rs.getTime("heureDeb"), rs.getTime("heureFin"));
                films.add(film);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return films;
    }

    @Override
    public List<Film> getAllFilms() throws RemoteException {
        List<Film> films = new ArrayList<>();
        try {
            Statement st=con.createStatement();
            ResultSet rs=st.executeQuery("SELECT * FROM Film");
            while (rs.next()) {
                Film film = new Film(rs.getInt("idFilm"), rs.getString("titre"), rs.getInt("idGenre"), rs.getInt("idSalle"), rs.getBigDecimal("prix"), rs.getDate("date"), rs.getTime("heureDeb"), rs.getTime("heureFin"));
                films.add(film);
            }
            rs.close();
            st.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return films;
    }

    @Override
    public List<Film> getFilmsByGenre(int idGenre) throws RemoteException {
        List<Film> films = new ArrayList<>();
        String req = "SELECT * FROM Film WHERE idGenre = (SELECT idGenre FROM Genre WHERE idGenre = ?)";

        try (PreparedStatement ps = con.prepareStatement(req)) {
            ps.setInt(1, idGenre);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("idFilm");
                    String titre = rs.getString("titre");
                    int idCat = rs.getInt("idGenre");
                    int idSalle = rs.getInt("idSalle");
                    BigDecimal prix = rs.getBigDecimal("prix");
                    Date date = rs.getDate("date");
                    Time heureDeb = rs.getTime("heureDeb");
                    Time heureFin = rs.getTime("heureFin");
                    Film film = new Film(id, titre, idCat, idSalle, prix, date, heureDeb, heureFin);
                    films.add(film);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return films;
    }

}
