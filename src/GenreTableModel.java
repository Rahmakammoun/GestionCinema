import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

class GenreTableModel extends AbstractTableModel {

    private List<Object[]> data = new ArrayList<>();
    private String[] columnNames = {"idGenre", "nomGenre"};
    private GenreDAORemote dao;

    public GenreTableModel(List<Genre> genres, GenreDAORemote dao) {
        this.dao = dao;
        for (Genre genre : genres) {
            Object[] ligne = {genre.getId(), genre.getNom()};
            data.add(ligne);
        }
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data.get(rowIndex)[columnIndex];
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        try {
            int id = (int) data.get(rowIndex)[0]; // Obtenez l'ID du genre
            String nom = (String) aValue; // Convertissez la nouvelle valeur en String
            int result = dao.modifierGenre(id, nom); // Mettez à jour le genre dans la base de données
            if (result > 0) {
                // Si la mise à jour réussit, mettez à jour la valeur dans data
                data.get(rowIndex)[columnIndex] = aValue;
                fireTableDataChanged();
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification du genre");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
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

    public void removeRow(int rowIndex) {
        data.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    void insertGenre(String nom){
        try {
            int a = dao.insertGenre(nom);
            if(a > 0) {
                // Ajouter une nouvelle ligne au modèle de table
                data.add(new Object[]{a, nom}); // Suppose que le premier élément du tableau est l'ID du genre
                // Actualiser l'affichage de la JTable
                fireTableRowsInserted(getRowCount() - 1, getRowCount() - 1);
                JOptionPane.showMessageDialog(null, "genre ajouté avec succès");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'insertion du genre");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
