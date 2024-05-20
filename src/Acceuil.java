import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Acceuil extends JFrame {

    public Acceuil() {
        initComponents();
    }

    private void initComponents() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Gestion Cinema");
        setPreferredSize(new Dimension(600, 300));

        JPanel mainPanel = new JPanel(new BorderLayout());

        // Panel pour le titre
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Gestion Cinema");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);

        // Panel pour les boutons
        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JButton GenreButton = new JButton("Genre");
        JButton filmButton = new JButton("Film");
        JButton SalleButton = new JButton("Salle");
        JButton reservationButton = new JButton("Reservation");

        GenreButton.setFont(new Font("Arial", Font.PLAIN, 20));
        filmButton.setFont(new Font("Arial", Font.PLAIN, 20));
        SalleButton.setFont(new Font("Arial", Font.PLAIN, 20));
        reservationButton.setFont(new Font("Arial", Font.PLAIN, 20));

        GenreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Créer une instance de l'interface InterfaceGenre
                InterfaceGenre interfaceGenre = new InterfaceGenre();

                // Rendre l'interface visible
                interfaceGenre.frame.setVisible(true);
            }
        });

        filmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceFilm interfaceFilm = new InterfaceFilm();
                interfaceFilm.frame.setVisible(true);
            }
        });

        SalleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceSalle interfaceSalle = new InterfaceSalle();
                interfaceSalle.frame.setVisible(true);
            }
        });

        reservationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceReservation interfaceReservation = new InterfaceReservation();
                interfaceReservation.frame.setVisible(true);
            }
        });

        centerPanel.add(GenreButton);
        centerPanel.add(filmButton);
        centerPanel.add(SalleButton);
        centerPanel.add(reservationButton);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        getContentPane().add(mainPanel);

        pack();
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Acceuil().setVisible(true);
            }
        });
    }
}
