import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.List;

public class InterfaceGenre {

    public JFrame frame;
    private JTextField tfNom;
    private JTable table;
    private JPanel panel_1;
    private JTextField textField_2;
    private JPopupMenu popupMenu;
    private GenreDAORemote dao;
    private GenreTableModel model;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InterfaceGenre window = new InterfaceGenre();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public InterfaceGenre() {
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 780, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setForeground(new Color(32, 178, 170));
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Ajouter genre", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel.setBounds(11, 174, 377, 125);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        tfNom = new JTextField();
        tfNom.setBounds(166, 30, 195, 21);
        panel.add(tfNom);
        tfNom.setColumns(10);

        JLabel lblNewLabel_1 = new JLabel("Nom");
        lblNewLabel_1.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_1.setBounds(10, 32, 92, 13);
        panel.add(lblNewLabel_1);

        JButton btnajouter = new JButton("Ajouter");
        btnajouter.setBounds(124, 83, 116, 26);
        panel.add(btnajouter);
        btnajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nomC = tfNom.getText();
                model.insertGenre(nomC);
            }
        });

        table = new JTable();
        table.setBounds(398, 35, 358, 368);
        frame.getContentPane().add(table);

        try {
            dao = (GenreDAORemote) Naming.lookup("rmi://localhost/genre");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String req = "Select * from genre";
        ResultSet rs = null;

        List<Genre> genres = null;
        try {
            genres = dao.selection();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        model = new GenreTableModel(genres, dao);
        String[] columnNames = {"idGenre", "NomGenre"};
        model.setColumnIdentifiers(columnNames);
        table.setModel(model);

        panel_1 = new JPanel();
        panel_1.setForeground(new Color(32, 178, 170));
        panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Recherche nom du genre", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel_1.setBounds(10, 66, 377, 54);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        textField_2 = new JTextField();
        textField_2.setBounds(10, 20, 234, 24);
        panel_1.add(textField_2);
        textField_2.setColumns(10);

        JButton btnRecherche = new JButton("Rechercher");
        btnRecherche.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nomRecherche = textField_2.getText().trim();
                if (!nomRecherche.isEmpty()) {
                    String req = "SELECT * FROM Genre WHERE nomGenre LIKE ?";
                    ResultSet rs = null;
                    List<Genre> genres = null;
                    try {
                            genres = dao.selectionRecherche(nomRecherche);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }

                    GenreTableModel rechercheModel = new GenreTableModel(genres, dao);
                    rechercheModel.setColumnIdentifiers(new String[]{"idGenre", "NomGenre"});
                    table.setModel(rechercheModel);
                } else {
                    table.setModel(model);
                }
            }
        });
        btnRecherche.setBounds(254, 19, 110, 25);
        panel_1.add(btnRecherche);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    int rowIndex = table.rowAtPoint(e.getPoint());
                    if (rowIndex >= 0 && rowIndex < table.getRowCount()) {
                        table.setRowSelectionInterval(rowIndex, rowIndex);
                        showPopupMenu(e.getX(), e.getY());
                    }
                }
            }
        });

        JMenuBar menuBar = new JMenuBar();
        menuBar.setToolTipText("");
        menuBar.setBounds(10, 10, 101, 22);
        frame.getContentPane().add(menuBar);

        JMenu mnGestion = new JMenu("Menu");
        menuBar.add(mnGestion);

        JMenuItem mntmFilm = new JMenuItem("Film");
        mnGestion.add(mntmFilm);

        JMenuItem mntmSalle = new JMenuItem("Salle");
        mnGestion.add(mntmSalle);

        JMenuItem mntmReservation = new JMenuItem("Réservation");
        mnGestion.add(mntmReservation);

        mntmFilm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceFilm interfaceFilm = new InterfaceFilm();
                interfaceFilm.frame.setVisible(true);
            }
        });

        mntmReservation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceReservation interfaceReservation = new InterfaceReservation();
                interfaceReservation.frame.setVisible(true);
            }
        });

        mntmSalle.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceSalle interfaceSalle = new InterfaceSalle();
                interfaceSalle.frame.setVisible(true);
            }
        });
        mntmFilm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceFilm interfaceFilm = new InterfaceFilm();
                interfaceFilm.frame.setVisible(true);
            }
        });








    }

    private void showPopupMenu(int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Supprimer");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                int idToDelete = (int) table.getValueAt(selectedRowIndex, model.columnIndex("idGenre"));
                deleteGenre(idToDelete);
            }
        });
        popupMenu.add(deleteMenuItem);
        JMenuItem editMenuItem = new JMenuItem("Modifier");
        editMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                showEditDialog(selectedRowIndex);
            }
        });
        popupMenu.add(editMenuItem);
        popupMenu.show(table, x, y);
    }

    private void deleteGenre(int idToDelete) {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de supprimer ce genre ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            int rowsAffected = 0;
            try {
                rowsAffected = dao.supprimerGenre(idToDelete);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (rowsAffected > 0) {
                model.removeRow(table.getSelectedRow());
                JOptionPane.showMessageDialog(null, "Genre supprimé");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression genre");
            }
        }
    }

    private void showEditDialog(int rowIndex) {
        String nom = (String) table.getValueAt(rowIndex, model.columnIndex("NomGenre"));
        int id = (int) table.getValueAt(rowIndex, model.columnIndex("idGenre"));

        JTextField nomField = new JTextField(nom, 10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Modifier le genre",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newNom = nomField.getText();
            int rowsAffected = 0;
            try {
                rowsAffected = dao.modifierGenre(id, newNom);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            if (rowsAffected > 0) {
                table.getModel().setValueAt(newNom, rowIndex, model.columnIndex("NomGenre"));
                //table.getModel().setValueAt(id, rowIndex, model.columnIndex("IdGenre"));

                JOptionPane.showMessageDialog(null, "Genre modifié avec succès");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification du genre");
            }
        }
    }
}
