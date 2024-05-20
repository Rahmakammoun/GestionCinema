//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        try {
            // Créer une instance de la page d'accueil
            Acceuil accueil = new Acceuil();

            // Rendre la page d'accueil visible
            accueil.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}