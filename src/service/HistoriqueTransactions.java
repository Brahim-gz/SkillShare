package service;

import model.Transaction;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HistoriqueTransactions implements Serializable {
    private static final long serialVersionUID = 1L;
    private List<Transaction> transactions = new ArrayList<>();

    public void enregistrer(Transaction t) {
        if (t != null) {
            transactions.add(t);
        }
    }

    public List<Transaction> filtrerParUtilisateur(int utilisateurId) {
        List<Transaction> resultat = new ArrayList<>();
        for (Transaction t : transactions) {
            if (t.getDemandeur().getId() == utilisateurId ||
                    t.getFournisseur().getId() == utilisateurId) {
                resultat.add(t);
            }
        }
        return resultat;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}