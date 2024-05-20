import java.io.Serializable;

public class Genre implements Serializable {
    private int id;
    private String nom;

    public Genre(int id, String nom) {
        this.id = id;
        this.nom = nom;
    }

    // Getters et setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                '}';
    }
}
