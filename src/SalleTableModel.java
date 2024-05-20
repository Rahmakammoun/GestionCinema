import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.rmi.RemoteException;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

class SalleTableModel extends AbstractTableModel {

    private ResultSetMetaData rsmd;
    private SalleDAORemote dao;
    private ArrayList<Salle> data = new ArrayList<>();
    private String[] columnNames = {"idSalle", "NomSalle","Capacite"};

    public SalleTableModel(ArrayList<Salle> salles, SalleDAORemote dao) {
        this.dao = dao;
        for (Salle salle : salles) {
            data.add(salle);

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
        Salle salle= data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return salle.getId();
            case 1:
                return salle.getNom();
            case 2:
                return salle.getCapacite();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
       Salle salle = data.get(rowIndex);
        switch (columnIndex) {
            case 1:
                salle.setNom((String) aValue);
                break;
            case 2:
                salle.setCapacite((int) aValue);
                break;
            default:
                break;
        }

        int rowsAffected = 0;
        try {
            rowsAffected = dao.modifierSalle(salle.getId(), salle.getNom(),salle.getCapacite());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        if (rowsAffected > 0) {
            fireTableCellUpdated(rowIndex, columnIndex);
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    public void setColumnIdentifiers(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public int columnIndex(String colName) {
        for (int i = 0; i < getColumnCount(); i++) {
            if (getColumnName(i).equalsIgnoreCase(colName))
                return i;
        }
        return -1;
    }

    public void removeRow(int rowIndex) {
        data.remove(rowIndex);
        fireTableRowsDeleted(rowIndex, rowIndex);
    }

    void insertSalle(String nom, int capacite) {
        try {
            int id = dao.insertSalle(nom, capacite);
            if (id > 0) {
                Salle newSalle = new Salle(id, nom, capacite);
                data.add(newSalle);
                fireTableRowsInserted(data.size() - 1, data.size() - 1);
                JOptionPane.showMessageDialog(null, "Salle ajoutée avec succès");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de l'insertion de la salle");
            }
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }



}
