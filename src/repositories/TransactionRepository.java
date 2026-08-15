package repositories;

import models.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransactionRepository {
    private final Map<Long, Transaction> transactions = new HashMap<>();

    public List<Transaction> getAllTransactions(){ return new ArrayList<>(transactions.values()); }

    public Transaction getTransactionById(long id){ return transactions.get(id); }

    public List<Transaction> getTransactionsByFromAccountId(long fromAccountId){
        return transactions.values()
                .stream()
                .filter(t -> t.getFromAccountId() == fromAccountId).toList();
    }

    public List<Transaction> getTransactionsByAccountId(long accountId){
        return transactions.values()
                .stream()
                .filter(t ->
                        t.getFromAccountId() == accountId || t.getToAccountId() == accountId).toList();
    }

    public List<Transaction> getTransactionsByToAccountId(long toAccountId){
        return transactions.values().stream().filter(t -> t.getToAccountId() == toAccountId).toList();
    }

    public void saveTransaction(Transaction transaction){ transactions.put(transaction.getId(), transaction); }

    public void deleteTransaction(long id){ transactions.remove(id); }
}