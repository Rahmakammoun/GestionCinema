import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InterfaceFilm {

    public JFrame frame;
    private JTextField textField;
    private JTable table;
    private JPanel panel_1;
    private JTextField textField_2;
    private JTextField textField_3;
    private JTextField textField_1;
    private JTextField textField_4;
    private JTextField textField_5;
    private JComboBox<String> comboBoxGenre;
    private JComboBox<String> comboBoxSalle;
    private JPopupMenu popupMenu;
    private FilmTableModel model;
    private FilmDAORemote dao;
    private GenreDAORemote daoGenre;
    private SalleDAORemote daoSalle;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    InterfaceFilm window = new InterfaceFilm();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public InterfaceFilm() {
        initialize();
    }

    public void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 780, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JPanel panel = new JPanel();
        panel.setForeground(new Color(32, 178, 170));
        panel.setBorder(new TitledBorder(new EtchedBorder(EtchedBorder.LOWERED, new Color(255, 255, 255),
                new Color(160, 160, 160)), "Film", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
        panel.setBounds(10, 94, 377, 279);
        frame.getContentPane().add(panel);
        panel.setLayout(null);

        textField = new JTextField();
        textField.setBounds(166, 30, 195, 21);
        panel.add(textField);
        textField.setColumns(10);

        JLabel lblNewLabel = new JLabel("Genre");
        lblNewLabel.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel.setBounds(10, 62, 92, 28);
        panel.add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("Titre");
        lblNewLabel_1.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_1.setBounds(10, 32, 92, 13);
        panel.add(lblNewLabel_1);

        comboBoxGenre = new JComboBox<String>();
        comboBoxGenre.setBounds(166, 69, 195, 21);
        panel.add(comboBoxGenre);

        JLabel lblDateDbut = new JLabel("Date");
        lblDateDbut.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblDateDbut.setBounds(10, 141, 167, 28);
        panel.add(lblDateDbut);

        JLabel lblNewLabel_3_1_1 = new JLabel("Heure début");
        lblNewLabel_3_1_1.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_3_1_1.setBounds(10, 179, 152, 28);
        panel.add(lblNewLabel_3_1_1);

        JLabel lblNewLabel_3_1 = new JLabel("Heure fin");
        lblNewLabel_3_1.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_3_1.setBounds(10, 210, 92, 28);
        panel.add(lblNewLabel_3_1);

        JLabel lblNewLabel_3_1_2 = new JLabel("Prix");
        lblNewLabel_3_1_2.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblNewLabel_3_1_2.setBounds(10, 241, 92, 28);
        panel.add(lblNewLabel_3_1_2);

        textField_3 = new JTextField();
        textField_3.setBounds(166, 247, 195, 21);
        panel.add(textField_3);
        textField_3.setColumns(10);

        textField_1 = new JTextField();
        textField_1.setColumns(10);
        textField_1.setBounds(166, 147, 195, 21);
        panel.add(textField_1);

        textField_4 = new JTextField();
        textField_4.setColumns(10);
        textField_4.setBounds(166, 216, 195, 21);
        panel.add(textField_4);

        textField_5 = new JTextField();
        textField_5.setColumns(10);
        textField_5.setBounds(166, 185, 195, 21);
        panel.add(textField_5);

        JLabel lblSalle = new JLabel("Salle");
        lblSalle.setFont(new Font("Verdana Pro", Font.BOLD, 14));
        lblSalle.setBounds(10, 100, 167, 28);
        panel.add(lblSalle);

        comboBoxSalle = new JComboBox<String>();
        comboBoxSalle.setBounds(166, 106, 195, 21);
        panel.add(comboBoxSalle);

        table = new JTable();
        table.setBounds(398, 35, 358, 368);
        frame.getContentPane().add(table);

        try {
            dao = (FilmDAORemote) Naming.lookup("rmi://localhost/film");
        } catch (Exception e) {
            e.printStackTrace();
        }

        model = new FilmTableModel(new ArrayList<>(), dao);

        panel_1 = new JPanel();
        panel_1.setForeground(new Color(32, 178, 170));
        panel_1.setBorder(new TitledBorder(null, "Recherche par genre", TitledBorder.LEADING, TitledBorder.TOP,
                null, null));
        panel_1.setBounds(10, 35, 377, 54);
        frame.getContentPane().add(panel_1);
        panel_1.setLayout(null);

       textField_2 = new JTextField();
       textField_2.setBounds(10, 20, 234, 24);
       panel_1.add(textField_2);
        textField_2.setColumns(10);


        JButton btnNewButton_1 = new JButton("Rechercher");

        btnNewButton_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupérer la catégorie sélectionnée dans le JComboBox
                String genreNom = textField_2.getText().trim();


                // Appeler la méthode getFilmsByGenre pour obtenir les films du genre sélectionné
                try {
                    List<Film> films = dao.getFilmsByGenre(daoGenre.getIdGenre(genreNom));

                    // Vérifier si des films ont été trouvés
                    if (!films.isEmpty()) {
                        // Mettre à jour le modèle de table avec les nouveaux films
                        FilmTableModel rechercheModel = new FilmTableModel(films, dao);

                        // Rafraîchir la vue de la table
                        table.setModel(rechercheModel);
                    } else {
                        // Afficher un message si aucun film n'a été trouvé
                        JOptionPane.showMessageDialog(null, "Aucun film trouvé pour le genre spécifié");
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });







        btnNewButton_1.setBounds(254, 19, 110, 25);
        panel_1.add(btnNewButton_1);


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

        JButton btnNewButton = new JButton("Ajouter");
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Récupérer les données de l'événement à partir des champs
                String titre = textField.getText();
                String genreNom = (String) comboBoxGenre.getSelectedItem();
                String salleNom = (String) comboBoxSalle.getSelectedItem();

                // Vérifier si le champ textField_3 n'est pas vide
                String prixText = textField_3.getText();
                if (prixText.isEmpty()) {
                    // Afficher un message d'erreur
                    JOptionPane.showMessageDialog(null, "Le champ Prix ne peut pas être vide");
                    return; // Sortir de la méthode actionPerformed
                }

                double prix = 0; // Initialiser le prix à 0 par défaut
                try {
                    // Tenter de convertir la chaîne en double
                    prix = Double.parseDouble(prixText);
                } catch (NumberFormatException ex) {
                    // Afficher un message d'erreur si la conversion échoue
                    JOptionPane.showMessageDialog(null, "Le champ Prix doit être un nombre valide");
                    return; // Sortir de la méthode actionPerformed
                }

                // Récupérer la date sous forme de java.sql.Date
                java.sql.Date date = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = sdf.parse(textField_1.getText());
                    date = new java.sql.Date(parsedDate.getTime());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                // Récupérer les heures de début et de fin sous forme de java.sql.Time
                java.sql.Time heureDeb = null;
                java.sql.Time heureFin = null;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    java.util.Date parsedHeureDeb = sdf.parse(textField_5.getText());
                    java.util.Date parsedHeureFin = sdf.parse(textField_4.getText());
                    heureDeb = new java.sql.Time(parsedHeureDeb.getTime());
                    heureFin = new java.sql.Time(parsedHeureFin.getTime());
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }

                try {
                    // Récupérer les IDs de genre et de salle à partir des noms

                    try {
                        daoGenre = (GenreDAORemote) Naming.lookup("rmi://localhost/genre");
                    } catch (NotBoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    }

                    try {
                        daoSalle = (SalleDAORemote) Naming.lookup("rmi://localhost/salle");
                    } catch (NotBoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    }

                    int idGenre = daoGenre.getIdGenre(genreNom);
                    int idSalle = daoSalle.getIdSalle(salleNom);

                    // Insérer l'événement dans la base de données via le DAO
                    try {
                        dao = (FilmDAORemote) Naming.lookup("rmi://localhost/film");
                    } catch (NotBoundException ex) {
                        throw new RuntimeException(ex);
                    } catch (MalformedURLException ex) {
                        throw new RuntimeException(ex);
                    }

                    int rowsAffected = dao.insertFilm(titre, idGenre, idSalle, prix, date, heureDeb, heureFin);
                    if (rowsAffected > 0) {
                        // Afficher un message de succès
                        JOptionPane.showMessageDialog(null, "Film ajouté avec succès");
                        // Actualiser la table des films
                        refreshTable();
                    } else {
                        // Afficher un message d'erreur
                        JOptionPane.showMessageDialog(null, "Erreur lors de l'ajout du filmt");
                    }
                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }
            }
        });



        btnNewButton.setBounds(128, 377, 116, 26);
        frame.getContentPane().add(btnNewButton);

        initializeComboBoxes();
        refreshTable();

        JMenuBar menuBar = new JMenuBar();
        menuBar.setToolTipText("");
        menuBar.setBounds(10, 10, 101, 22);
        frame.getContentPane().add(menuBar);

        JMenu mnGestion = new JMenu("Menu");
        menuBar.add(mnGestion);

        JMenuItem mntmGenre = new JMenuItem("Genres");
        mnGestion.add(mntmGenre);

        JMenuItem mntmReservation = new JMenuItem("Réservation");
        mnGestion.add(mntmReservation);

        JMenuItem mntmSalle = new JMenuItem("Salles");
        mnGestion.add(mntmSalle);

        mntmGenre.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                InterfaceGenre interfaceGenre = new InterfaceGenre();
                interfaceGenre.frame.setVisible(true);
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

    }

    private void showPopupMenu(int x, int y) {
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteMenuItem = new JMenuItem("Supprimer");
        deleteMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRowIndex = table.getSelectedRow();
                Integer idToDelete = (Integer) table.getValueAt(selectedRowIndex, model.columnIndex("idFilm"));
                deleteFilm(idToDelete);
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


    private void initializeComboBoxes() {
        try {

            try {
                daoGenre = (GenreDAORemote) Naming.lookup("rmi://localhost/genre");


            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            List<String> genres = daoGenre.getAllGenres();
            for (String genre : genres) {
                comboBoxGenre.addItem(genre);

            }


            try {
                daoSalle = (SalleDAORemote) Naming.lookup("rmi://localhost/salle");
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            List<String> salles = daoSalle.getAllSalles();
            for (String salle : salles) {
                comboBoxSalle.addItem(salle);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void refreshTable() {
        try {
            List<Film>  rs = dao.getAllFilms();
            try {
                dao = (FilmDAORemote) Naming.lookup("rmi://localhost/film");
            } catch (NotBoundException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            model = new FilmTableModel(rs, dao);
            table.setModel(model);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void showEditDialog(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= table.getRowCount()) {
            JOptionPane.showMessageDialog(null, "Veuillez sélectionner une ligne valide.");
            return;
        }

        // Récupérer les informations de la ligne sélectionnée
        int id = (Integer) table.getValueAt(rowIndex, model.columnIndex("idFilm"));
        String titre = (String) table.getValueAt(rowIndex, model.columnIndex("Titre"));
        BigDecimal prix = (BigDecimal) table.getValueAt(rowIndex, model.columnIndex("Prix"));
        Date date = (Date) table.getValueAt(rowIndex, model.columnIndex("Date"));
        Time heureDeb = (Time) table.getValueAt(rowIndex, model.columnIndex("heureDeb"));
        Time heureFin = (Time) table.getValueAt(rowIndex, model.columnIndex("heureFin"));

        int idGenre = (int) table.getValueAt(rowIndex, model.columnIndex("idGenre"));
        int idSalle = (int) table.getValueAt(rowIndex, model.columnIndex("idSalle"));

        // Obtenir les noms du genre et de salle à partir de leurs IDs
        String genreNom = null;
        try {
            genreNom = daoGenre.getGenreNameById(idGenre);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        String salleNom = null;
        try {
            salleNom = daoSalle.getSalleNameById(idSalle);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Créer les JComboBox pour les genres et les salles
        JComboBox<String> genreComboBox = createGenreComboBox();
        JComboBox<String> salleComboBox = createSalleComboBox();

        // Sélectionner le genre et la salle actuels dans les JComboBox
        genreComboBox.setSelectedItem(genreNom);
        salleComboBox.setSelectedItem(salleNom);

        // Afficher la boîte de dialogue pour modifier les informations
        JTextField titreField = new JTextField(titre, 10);
        JTextField prixField = new JTextField(prix.toString(), 10);
        JTextField dateField = new JTextField(date.toString(), 10);
        JTextField heureDebField = new JTextField((heureDeb != null) ? heureDeb.toString() : "", 10);
        JTextField heureFinField = new JTextField((heureFin != null) ? heureFin.toString() : "", 10);

        JPanel panel = new JPanel(new GridLayout(8, 9));
        panel.add(new JLabel("Titre:"));
        panel.add(titreField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreComboBox);
        panel.add(new JLabel("Salle:"));
        panel.add(salleComboBox);
        panel.add(new JLabel("Prix:"));
        panel.add(prixField);
        panel.add(new JLabel("Date (YYYY-MM-DD):"));
        panel.add(dateField);
        panel.add(new JLabel("Heure de début (HH:MM):"));
        panel.add(heureDebField);
        panel.add(new JLabel("Heure de fin (HH:MM):"));
        panel.add(heureFinField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Modifier le film", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String newTitre = titreField.getText();
            BigDecimal newPrix = new BigDecimal(prixField.getText());
            Date newDate = Date.valueOf(dateField.getText());
            Time newHeureDeb = Time.valueOf(heureDebField.getText());
            Time newHeureFin = Time.valueOf(heureFinField.getText());

            // Récupérer les IDs du genre et de salle sélectionnés dans les JComboBox
            int newIdGenre = 0;
            try {
                newIdGenre = daoGenre.getIdGenre((String) genreComboBox.getSelectedItem());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
            int newIdSalle = 0;
            try {
                newIdSalle = daoSalle.getIdSalle((String) salleComboBox.getSelectedItem());
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            int rowsAffected = 0;
            try {
                rowsAffected = dao.modifierFilm(id, newTitre, newIdGenre, newIdSalle, newPrix, newDate, newHeureDeb, newHeureFin);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            if (rowsAffected > 0) {
                table.getModel().setValueAt(newTitre, rowIndex, model.columnIndex("Titre"));
                table.getModel().setValueAt(newPrix, rowIndex, model.columnIndex("Prix"));
                table.getModel().setValueAt(newDate, rowIndex, model.columnIndex("Date"));
                table.getModel().setValueAt(newHeureDeb, rowIndex, model.columnIndex("heureDeb"));
                table.getModel().setValueAt(newHeureFin, rowIndex, model.columnIndex("heureFin"));
                table.getModel().setValueAt(newIdGenre, rowIndex, model.columnIndex("idGenre"));
                table.getModel().setValueAt(newIdSalle, rowIndex, model.columnIndex("idSalle"));

                JOptionPane.showMessageDialog(null, "Film modifié avec succès");
            } else {
                JOptionPane.showMessageDialog(null, "Erreur lors de la modification du film");
            }
        }
    }
    private JComboBox<String> createGenreComboBox() {
        // Créer un nouveau JComboBox
        JComboBox<String> comboBox = new JComboBox<>();

        // Récupérer toutes les genres depuis la base de données
        ArrayList<String> genres = null;
        try {
            genres = daoGenre.getAllGenres();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Ajouter chaque genre à la liste déroulante
        for (String genre : genres) {
            comboBox.addItem(genre);
        }

        // Retourner le JComboBox créé
        return comboBox;
    }

    private JComboBox<String> createSalleComboBox() {
        // Créer un nouveau JComboBox
        JComboBox<String> comboBox = new JComboBox<>();

        // Récupérer tous les salles depuis la base de données
        ArrayList<String> salles = null;
        try {
            salles = daoSalle.getAllSalles();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }

        // Ajouter chaque salle à la liste déroulante
        for (String salle: salles) {
            comboBox.addItem(salle);
        }

        // Retourner le JComboBox créé
        return comboBox;
    }

    private void deleteFilm(int idToDelete) {
        int dialogResult = JOptionPane.showConfirmDialog(null, "Êtes-vous sûr de supprimer ce film ?", "Confirmation de suppression", JOptionPane.YES_NO_OPTION);
        if (dialogResult == JOptionPane.YES_OPTION) {
            try {
                int rowsAffected = dao.supprimerFilm(idToDelete);
                if (rowsAffected > 0) {
                    refreshTable();
                    JOptionPane.showMessageDialog(null, "Film supprimé avec succès");
                } else {
                    JOptionPane.showMessageDialog(null, "Erreur lors de la suppression du film", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}
