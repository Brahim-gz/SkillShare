package service;

import model.*;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class DataManager {
    private static final String DATA_DIR = "data";
    private static final String ETUDIANTS_FILE = "etudiants.ser";
    private static final String ADMIN_FILE = "administrateurs.ser";
    private static final String DEMANDES_FILE = "demandes.ser";
    private static final String SERVICES_FILE = "services.ser";
    private static final String TRANSACTIONS_FILE = "transactions.ser";
    private static final String HISTORIQUE_FILE = "historique.ser";

    // Créer le dossier data s'il n'existe pas
    static {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            boolean created = dataDir.mkdir();
            if (!created) {
                System.err.println("Erreur : Impossible de créer le dossier data");
            }
        }
    }

    // Sauvegarder les données
    public static void sauvegarderDonnees(List<Etudiant> etudiants,
            List<Administrateur> administrateurs,
            List<DemandeAide> demandes,
            List<Service> services,
            List<Transaction> transactions,
            List<HistoriqueTransactions> historique) {
        try {
            // Vérifier que le dossier data existe
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }

            // Sauvegarder les données
            sauvegarderListe(etudiants, ETUDIANTS_FILE);
            sauvegarderListe(administrateurs, ADMIN_FILE);
            sauvegarderListe(demandes, DEMANDES_FILE);
            sauvegarderListe(services, SERVICES_FILE);
            sauvegarderListe(transactions, TRANSACTIONS_FILE);
            sauvegarderListe(historique, HISTORIQUE_FILE);

            System.out.println("Données sauvegardées avec succès !");
        } catch (IOException e) {
            System.err.println("Erreur lors de la sauvegarde des données : " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode utilitaire pour sauvegarder une liste
    private static <T> void sauvegarderListe(List<T> liste, String nomFichier) throws IOException {
        if (liste != null) {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(DATA_DIR + File.separator + nomFichier));
            oos.writeObject(liste);
            oos.close();
        }
    }

    // Charger les données
    @SuppressWarnings("unchecked")
    public static Object[] chargerDonnees() {
        Object[] result = new Object[6];
        try {
            // Vérifier que le dossier data existe
            File dataDir = new File(DATA_DIR);
            if (!dataDir.exists()) {
                dataDir.mkdir();
            }

            // Charger les données
            result[0] = chargerListe(ETUDIANTS_FILE);
            result[1] = chargerListe(ADMIN_FILE);
            result[2] = chargerListe(DEMANDES_FILE);
            result[3] = chargerListe(SERVICES_FILE);
            result[4] = chargerListe(TRANSACTIONS_FILE);
            result[5] = chargerListe(HISTORIQUE_FILE);

            System.out.println("Données chargées avec succès !");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Erreur lors du chargement des données : " + e.getMessage());
            e.printStackTrace();
            // Retourner des listes vides en cas d'erreur
            for (int i = 0; i < result.length; i++) {
                result[i] = new ArrayList<>();
            }
        }
        return result;
    }

    // Méthode utilitaire pour charger une liste
    @SuppressWarnings("unchecked")
    private static <T> List<T> chargerListe(String nomFichier) throws IOException, ClassNotFoundException {
        File fichier = new File(DATA_DIR + File.separator + nomFichier);
        if (fichier.exists()) {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fichier));
            List<T> result = (List<T>) ois.readObject();
            ois.close();
            return result;
        }
        return new ArrayList<>();
    }
}