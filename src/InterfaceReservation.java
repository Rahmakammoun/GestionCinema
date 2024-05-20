import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


public class InterfaceReservation {

    public JFrame frame;
    private JTable table;
    private JPanel panel_1;
    private JTextField textField_2;
    private JTextField textField;
    private ReservationTableModel model;
    private ReservationDAORemote dao;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InterfaceReservation window = new InterfaceReservation();
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
    public InterfaceReservation() {
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
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Ajouter R\u00E9servation", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel.setBounds(11, 174, 377, 156);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        JLabel lblNewLabel_1 = new JLabel("Id Film");
        lblNewLabel_1.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_1.setBounds(10, 32, 146, 13);
        panel.add(lblNewLabel_1);

        JList list = new JList();
        list.setBounds(239, 72, 1, 1);
        panel.add(list);

        JSpinner spinner = new JSpinner();
        spinner.setBounds(193, 73, 174, 20);
        panel.add(spinner);


        textField = new JTextField();
        textField.setBounds(193, 31, 174, 19);
        panel.add(textField);
        textField.setColumns(10);

        JButton btnNewButton = new JButton("Ajouter");
        btnNewButton.setBounds(124, 120, 116, 26);
        panel.add(btnNewButton);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupérer les valeurs saisies par l'utilisateur
                String idFilmText = textField.getText();
                String nbPlacesText = spinner.getValue().toString();

                // Vérifier si les champs ne sont pas vides
                if (idFilmText.isEmpty() || nbPlacesText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Veuillez remplir tous les champs.");
                    return;
                }

                // Convertir les valeurs en entiers
                int idFilm = Integer.parseInt(idFilmText);
                int nbPlaces = Integer.parseInt(nbPlacesText);

                // Vérifier si le nombre de places est supérieur à zéro
                if (nbPlaces <= 0) {
                    JOptionPane.showMessageDialog(frame, "Le nombre de places doit être supérieur à zéro.");
                    return;
                }

                try {
                    dao = (ReservationDAORemote) Naming.lookup("rmi://localhost/reservation");
                } catch (NotBoundException ex) {
                    throw new RuntimeException(ex);
                } catch (MalformedURLException ex) {
                    throw new RuntimeException(ex);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

                // Vérifier si l'ID du film existe dans la table des films
                try {
                    if (!dao.idFilmExists(idFilm)) {
                        JOptionPane.showMessageDialog(frame, "L'ID du film spécifié n'existe pas dans la table des films.");
                        return;
                    }
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

                // Vérifier si la somme des réservations plus la nouvelle réservation est inférieure ou égale à la capacité de salle
                int capaciteSalle = 0;
                try {
                    capaciteSalle = dao.getCapaciteSalle(idFilm);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                int reservationsActuelles = 0;
                try {
                    reservationsActuelles = dao.getReservationsActuelles(idFilm);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }
                if (reservationsActuelles + nbPlaces <= capaciteSalle) {
                    // Appeler la méthode d'insertion de réservation de votre DAO
                    try {
                        dao.insertReservation(idFilm, nbPlaces);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Actualiser l'affichage de la JTable pour refléter les nouvelles réservations
                    refreshTable();
                } else {
                    // Afficher un message d'erreur si la capacité maximale de salle est dépassée
                    JOptionPane.showMessageDialog(frame, "La capacité maximale de salle est dépassée pour cette réservation.");
                }
            }
        });






        JLabel lblNewLabel_1_1 = new JLabel("Nb de places");
        lblNewLabel_1_1.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_1_1.setBounds(10, 73, 146, 13);
        panel.add(lblNewLabel_1_1);



        table = new JTable();
        table.setBounds(398, 35, 358, 368);
        frame.getContentPane().add(table);

        //dao = new ReservationDAO(Config.url, Config.USERNAME, Config.PASSWORD);
        String req = "Select * from Reservation";
        ResultSet rs = null;
        List<Reservation> reservations = null;
        try {
            try {
                dao = (ReservationDAORemote) Naming.lookup("rmi://localhost/reservation");
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            reservations = dao.selection();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        model = new ReservationTableModel(reservations, dao);

        refreshTable();
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


        panel_1 = new JPanel();
        panel_1.setForeground(new Color(32, 178, 170));
        panel_1.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255), new Color(160, 160, 160)), "Rechercher Film", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel_1.setBounds(10, 66, 377, 54);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

        textField_2 = new JTextField();
        textField_2.setBounds(10, 20, 234, 24);
        panel_1.add(textField_2);
        textField_2.setColumns(10);

        JButton btnNewButton_1 = new JButton("Rechercher");
        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupérer l'ID ou le nom du film saisi par l'utilisateur
                String recherche = textField_2.getText().trim();

                // Vérifier si la recherche n'est pas vide
                if (!recherche.isEmpty()) {
                    // Appeler la méthode de votre DAO pour récupérer les réservations associées à l'ID ou au nom du film
                    ArrayList<Reservation> reservations = null;
                    try {
                        reservations = (ArrayList<Reservation>) dao.getReservationsByFilm(recherche);
                    } catch (RemoteException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Afficher le nombre de réservations associées dans une boîte de dialogue
                    JOptionPane.showMessageDialog(frame, "Nombre de réservations associées : " + reservations.size());

                    ReservationTableModel rechercheModel = new ReservationTableModel(reservations, dao);

                    // Mettre à jour le modèle de tableau avec les données des réservations
                    //model.setData(reservations);
                    table.setModel(rechercheModel);


                    // Redessiner la table pour refléter les modifications
                    //table.repaint();
                } else {
                    JOptionPane.showMessageDialog(frame, "Veuillez saisir un ID ou un nom du film.");
                }
            }
        });

        btnNewButton_1.setBounds(254, 19, 110, 25);
        panel_1.add(btnNewButton_1);

        JMenuBar menuBar = new JMenuBar();
        menuBar.setToolTipText("");
        menuBar.setBounds(10, 10, 101, 22);
        frame.getContentPane().add(menuBar);

        JMenu mnGestion = new JMenu("Menu");
        menuBar.add(mnGestion);

        JMenuItem mntmGenre = new JMenuItem("Genre");
        mnGestion.add(mntmGenre);

        JMenuItem mntmSalle = new JMenuItem("Salle");
        mnGestion.add(mntmSalle);

        JMenuItem mntmFilm = new JMenuItem("Film");
        mnGestion.add(mntmFilm);

        mntmGenre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceGenre interfaceGenre = new InterfaceGenre();
                interfaceGenre.frame.setVisible(true);
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

        // Option "Modifier"
        JMenuItem editMenuItem = new JMenuItem("Modifier");
        editMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                // Insérez ici la logique pour modifier une réservation
                // Utilisez l'index de ligne sélectionné pour obtenir les données de la réservation
                // Affichez une boîte de dialogue pour modifier les données
                showEditDialog(selectedRowIndex);

            }
        });
        popupMenu.add(editMenuItem);


        // Option "Supprimer"
        JMenuItem deleteMenuItem = new JMenuItem("Supprimer");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected row index
                int selectedRowIndex = table.getSelectedRow();
                // Get the student's cin from the selected row
                int idToDelete = (int) table.getValueAt(selectedRowIndex, model.columnIndex("id"));

                // Delete the student from the database
                deleteReservation(idToDelete);
            }
        });
        popupMenu.add(deleteMenuItem);

        // Affichez le menu contextuel à la position du clic
        popupMenu.show(table, x, y);
    }

    private void showEditDialog(int rowIndex) {
        // Récupérez les informations de la réservation sélectionnée
        int id = (int) table.getValueAt(rowIndex, model.columnIndex("ID"));
        int idEvenement = (int) table.getValueAt(rowIndex, model.columnIndex("IdFilm"));
        int nbPlaces = (int) table.getValueAt(rowIndex, model.columnIndex("NbPlace"));

        // Créez une boîte de dialogue pour modifier les informations
        JTextField idEvenementField = new JTextField(String.valueOf(idEvenement), 10);
        JTextField nbPlacesField = new JTextField(String.valueOf(nbPlaces), 10);

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("ID film:"));
        panel.add(idEvenementField);
        panel.add(new JLabel("Nb de places:"));
        panel.add(nbPlacesField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Modifier la réservation",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // Vérifiez si les champs ne sont pas vides
            String idFilmText = idEvenementField.getText();
            String nbPlacesText = nbPlacesField.getText();
            if (idFilmText.isEmpty() || nbPlacesText.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir tous les champs.");
                return;
            }

            // Convertir les valeurs en entiers
            int newIdFilm = Integer.parseInt(idFilmText);
            int newNbPlaces = Integer.parseInt(nbPlacesText);

            // Vérifiez si l'ID du film existe dans la table des films
            try {
                dao = (ReservationDAORemote) Naming.lookup("rmi://localhost/reservation");
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            try {
                if (!dao.idFilmExists(newIdFilm)) {
                    JOptionPane.showMessageDialog(null, "L'ID du film spécifié n'existe pas dans la table des films.");
                    return;
                }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            // Vérifiez si le nombre de places est supérieur à zéro
            if (newNbPlaces <= 0) {
                JOptionPane.showMessageDialog(null, "Le nombre de places doit être supérieur à zéro.");
                return;
            }

            // Vérifiez si la somme des réservations actuelles et la nouvelle réservation ne dépasse pas la capacité du salle
            int nbPlace= 0;
            try {
                nbPlace = dao.getNbPlace(id);

            int reservationsActuelles = dao.getReservationsActuelles(newIdFilm);
            int capaciteSalle = dao.getCapaciteSalle(newIdFilm);
            if (reservationsActuelles -nbPlace + newNbPlaces > capaciteSalle) {
                JOptionPane.showMessageDialog(null, "La capacité de la salle  est insuffisante pour ajouter cette réservation.");
                return;
            }

            // Mettez à jour la réservation dans la base de données
            int rowsAffected = dao.modifierReservation(id, newIdFilm, newNbPlaces);

            if (rowsAffected > 0) {
                // Mettez à jour le tableau
                table.getModel().setValueAt(newIdFilm, rowIndex, model.columnIndex("idFilm"));
                table.getModel().setValueAt(newNbPlaces, rowIndex, model.columnIndex("NbPlace"));

                JOptionPane.showMessageDialog(null, "Réservation modifiée avec succès");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification de la réservation");
            }
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }





    private void deleteReservation(int idToDelete) {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de supprimer cette réservation ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {

            int rowsAffected = 0;
            try {
                rowsAffected = dao.supprimerReservation(idToDelete);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            if (rowsAffected > 0) {
                // If deletion successful, remove the row from the JTable
                ((ReservationTableModel) table.getModel()).removeRow(table.getSelectedRow());

                JOptionPane.showMessageDialog(null, "réservation supprimée");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la suppression de la réservation");
            }
        }
        refreshTable();
    }


    private void refreshTable() {
        // Récupérer les données actualisées de la base de données
        try {
            dao = (ReservationDAORemote) Naming.lookup("rmi://localhost/reservation");
        } catch (NotBoundException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        String req = "SELECT * FROM Reservation";
        ResultSet rs = null;
        List<Reservation> reservations = null;
        try {
            reservations = dao.selection();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Mettre à jour le modèle de table avec les nouvelles données
        ReservationTableModel model = new ReservationTableModel(reservations, dao);
        table.setModel(model);

        // Redessiner la table pour refléter les modifications
        table.repaint();
    }


}

