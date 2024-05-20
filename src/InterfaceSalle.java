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
import java.util.ArrayList;
import java.util.List;

public class InterfaceSalle {

    public JFrame frame;
    private JTextField tfNom;
    private JTextField tfCapacite;
    private JTable table;
    private JPanel panel_1;
    private JTextField textField_2;
    private JPopupMenu popupMenu;
    private SalleDAORemote dao;
    private SalleTableModel model;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InterfaceSalle window = new InterfaceSalle();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public InterfaceSalle() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 780, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setForeground(new Color(32, 178, 170));
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Ajouter Salle", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel.setBounds(11, 174, 377, 183);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("Nom");
        lblNewLabel_1.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_1.setBounds(10, 32, 146, 13);
        panel.add(lblNewLabel_1);

        JLabel lblCapacite = new JLabel("Capacité");
        lblCapacite.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblCapacite.setBounds(10, 100, 146, 26);
        panel.add(lblCapacite);

        JButton btnajouter = new JButton("Ajouter");
        btnajouter.setBounds(124, 147, 116, 26);
        panel.add(btnajouter);
        btnajouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nom = tfNom.getText().trim();
                String capaciteText = tfCapacite.getText().trim();

                // Vérifier si les champs ne sont pas vides
                if (!nom.isEmpty() && !capaciteText.isEmpty()) {
                    // Vérifier si la capacité est un entier
                    try {
                        int capacite = Integer.parseInt(capaciteText);

                        // Appel direct à la méthode insertSalle du modèle de table
                        model.insertSalle(nom, capacite);
                    } catch (NumberFormatException ex) {
                        // Afficher un message d'erreur si la capacité n'est pas un entier
                        JOptionPane.showMessageDialog(frame, "La capacité doit être un entier.", "Erreur de saisie", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Afficher un message d'erreur si l'un des champs est vide
                    JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.", "Champs vides", JOptionPane.ERROR_MESSAGE);
                }
            }
        });





        tfNom = new JTextField();
        tfNom.setBounds(193, 31, 174, 19);
        panel.add(tfNom);
        tfNom.setColumns(10);

        tfCapacite = new JTextField();
        tfCapacite.setColumns(10);
        tfCapacite.setBounds(193, 106, 174, 20);
        panel.add(tfCapacite);

        table = new JTable();
        table.setBounds(398, 35, 358, 368);
        frame.getContentPane().add(table);

        try {
            dao = (SalleDAORemote) Naming.lookup("rmi://localhost/salle");
            ArrayList<Salle> salles = dao.selection();

            // Créer un modèle de tableau avec les données chargées
            model = new SalleTableModel(salles, dao);

            // Définir les noms de colonnes
            String[] columnNames = {"idSalle", "NomSalle", "Capacite"};
            model.setColumnIdentifiers(columnNames);

            // Associer le modèle de tableau au JTable
            table.setModel(model);
        } catch (Exception e) {
            e.printStackTrace();
        }

        panel_1 = new JPanel();
        panel_1.setForeground(new Color(32, 178, 170));
        panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Recherche nom de la salle", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
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
                    List<Salle> salles = null;
                    try {
                        salles = dao.selectionRecherche(nomRecherche);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }

                    SalleTableModel rechercheModel = new SalleTableModel(new ArrayList<>(salles), dao);
                    rechercheModel.setColumnIdentifiers(new String[]{"idSalle", "NomSalle", "Capacite"});
                    table.setModel(rechercheModel);
                } else {
                    table.setModel(model);
                }
            }
        });


        btnRecherche.setBounds(254, 19, 110, 25);
        panel_1.add(btnRecherche);

        // Créer un menu contextuel
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // Check if it's a right-click event
                if (SwingUtilities.isRightMouseButton(e)) {
                    // Get the row index where the right-click occurred
                    int rowIndex = table.rowAtPoint(e.getPoint());
                    // If the row index is valid, select the row and show the popup menu
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

        JMenuItem mntmGenre = new JMenuItem("Genre");
        mnGestion.add(mntmGenre);

        JMenuItem mntmFilm = new JMenuItem("Film");
        mnGestion.add(mntmFilm);

        JMenuItem mntmReservation = new JMenuItem("Réservation");
        mnGestion.add(mntmReservation);

        mntmGenre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceGenre interfaceGenre = new InterfaceGenre();
                interfaceGenre.frame.setVisible(true);
            }
        });
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



    }

    // Method to display the popup menu at the given coordinates
    private void showPopupMenu(int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Supprimer");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRowIndex = table.getSelectedRow();
                // Get the salle's id from the selected row
                int idToDelete = (int) table.getValueAt(selectedRowIndex, model.columnIndex("idSalle"));

                // Delete the salle from the database
                deleteSalle(idToDelete);
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

    private void deleteSalle(int idToDelete) {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de supprimer cette salle ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {

                int rowsAffected = dao.supprimerSalle(idToDelete);
                if (rowsAffected > 0) {
                    // If deletion successful, remove the row from the JTable
                    model.removeRow(table.getSelectedRow());
                    JOptionPane.showMessageDialog(null, "Salle supprimé");
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la salle");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }


    private void showEditDialog(int rowIndex) {
        // Récupérer les informations de salle sélectionnée
        String nom = (String) table.getValueAt(rowIndex, model.columnIndex("NomSalle"));
        int capacite = (int) table.getValueAt(rowIndex, model.columnIndex("Capacite"));
        int id = (int) table.getValueAt(rowIndex, model.columnIndex("idSalle"));

        // Créer une boîte de dialogue pour modifier les informations
        JTextField nomField = new JTextField(nom, 10);
        JTextField capaciteField = new JTextField(String.valueOf(capacite), 10);

        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Capacité:"));
        panel.add(capaciteField);


        int result = JOptionPane.showConfirmDialog(null, panel, "Modifier la salle",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Récupérer les nouvelles valeurs
            String newNom = nomField.getText();
            int newCapacite = Integer.parseInt(capaciteField.getText());

            try {
                // Mettre à jour la salle dans la base de données
                int rowsAffected = dao.modifierSalle(id, newNom, newCapacite);

                if (rowsAffected > 0) {
                    // Mettre à jour le tableau
                    table.getModel().setValueAt(newNom, rowIndex, model.columnIndex("NomSalle"));
                    table.getModel().setValueAt(newCapacite, rowIndex, model.columnIndex("Capacite"));

                    JOptionPane.showMessageDialog(null, "Salle modifiée avec succès");
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la modification de la salle");
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
