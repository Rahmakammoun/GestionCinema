import java.io.Serializable;

public class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private int idFilm;
    private int nbPlace;

    public Reservation(int id, int idFilm, int nbPlace) {
        this.id = id;
        this.idFilm = idFilm;
        this.nbPlace = nbPlace;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdFilm() {
        return idFilm;
    }

    public void setIdFilm(int idEven) {
        this.idFilm = idEven;
    }

    public int getNbPlace() {
        return nbPlace;
    }

    public void setNbPlace(int nbPlace) {
        this.nbPlace = nbPlace;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", idFilm=" + idFilm +
                ", nbPlace=" + nbPlace +
                '}';
    }
}
