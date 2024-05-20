import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

public class Film implements Serializable {
    private int id;
    private String titre;
    private int idGenre;
    private int idSalle;
    private BigDecimal prix;
    private Date date;
    private Time heureDeb;
    private Time heureFin;

    // Constructeur
    public Film(int id, String titre, int idGenre, int idSalle, BigDecimal prix, Date date, Time heureDeb, Time heureFin) {
        this.id = id;
        this.titre = titre;
        this.idGenre = idGenre;
        this.idSalle = idSalle;
        this.prix = prix;
        this.date = date;
        this.heureDeb = heureDeb;
        this.heureFin = heureFin;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getTitre() {
        return titre;
    }

    public int getIdGenre() {
        return idGenre;
    }

    public int getIdSalle() {
        return idSalle;
    }

    public BigDecimal getPrix() {
        return prix;
    }

    public Date getDate() {
        return date;
    }

    public Time getHeureDeb() {
        return heureDeb;
    }

    public Time getHeureFin() {
        return heureFin;
    }
}
