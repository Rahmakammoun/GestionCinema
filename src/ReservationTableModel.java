import javax.swing.table.AbstractTableModel;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

class ReservationTableModel extends AbstractTableModel {

    ResultSetMetaData rsmd;
    ReservationDAORemote dao;

    ArrayList<Reservation> data = new ArrayList<>();
    private String[] columnNames = {"id", "idFilm", "NbPlace"};

    public ReservationTableModel(List<Reservation> reservations, ReservationDAORemote dao) {
        this.dao = dao;
        for (Reservation reservation : reservations) {
            data.add(reservation);
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
        Reservation reservation = data.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return reservation.getId();
            case 1:
                return reservation.getIdFilm();
            case 2:
                return reservation.getNbPlace();
            default:
                return null;
        }
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex >= 0 && rowIndex < getRowCount() && columnIndex >= 0 && columnIndex < getColumnCount()) {
            Reservation reservation = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    reservation.setId((int) aValue);
                    break;
                case 1:
                    reservation.setIdFilm((int) aValue);
                    break;
                case 2:
                    reservation.setNbPlace((int) aValue);
                    break;
                default:
                    break;
            }
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


    public void setData(ArrayList<Reservation> newData) {
        data = newData;
        fireTableDataChanged(); // Notifie le JTable que les données ont changé
    }


    }

