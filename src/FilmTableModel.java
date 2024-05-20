import javax.swing.table.AbstractTableModel;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FilmTableModel extends AbstractTableModel {

    private ResultSetMetaData rsmd;
    private List<Film> films;
    FilmDAORemote dao;
    private ArrayList<Object[]> data = new ArrayList<>();
    private String[] columnNames = {"idFilm", "Titre", "idGenre", "idSalle", "Prix", "Date", "HeureDeb", "HeureFin"};
    private int columnCount = columnNames.length;

    public FilmTableModel(List<Film> films, FilmDAORemote dao) {
        this.dao = dao;
        for (Film film : films) {
            Object[] ligne = {film.getId(), film.getTitre(),film.getIdGenre(),film.getIdSalle(),film.getPrix(),film.getDate(),film.getHeureDeb(),film.getHeureFin()};
            data.add(ligne);
        }
    }


    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < getRowCount() && columnIndex >= 0 && columnIndex < getColumnCount()) {
            return data.get(rowIndex)[columnIndex];
        } else {
            return null; // Ou une valeur par défaut appropriée si nécessaire
        }
    }


    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    // Méthode pour définir les noms de colonnes
    public void setColumnIdentifiers(String[] columnNames) {
        this.columnNames = columnNames;
    }
    public int columnIndex(String colName){
        for (int i=0; i<getColumnCount();i++){
            if(getColumnName(i).equalsIgnoreCase(colName))
                return i;
        }
        return -1;
    }
    // Méthode pour supprimer une ligne
    public void removeRow(int rowIndex) {
        data.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    // Méthode pour insérer un événement
    void insertFilm(Object[] filmData) {
        data.add(filmData);
        fireTableDataChanged();
    }
    // Méthode pour mettre à jour une ligne
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < getRowCount() && columnIndex >= 0 && columnIndex < getColumnCount()) {
            data.get(rowIndex)[columnIndex] = value;
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }
    public void setFilm(List<Film> films) {
        this.films = films;
        fireTableDataChanged(); // Notifier la table que les données ont changé
    }



}
