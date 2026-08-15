package services;

import exceptions.InsufficientFundsException;
import exceptions.TransactionNotFoundException;
import models.Account;
import models.Transaction;
import repositories.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountService accountService;

    public TransactionService(TransactionRepository transactionRepository, AccountService accountService){
        this.transactionRepository = transactionRepository;
        this.accountService = accountService;
    }

    public List<Transaction> getAllTransactions(){ return transactionRepository.getAllTransactions(); }

    public Transaction getTransactionById(long id){
        if(id <= 0)
            throw new IllegalArgumentException("ID транзакции должен быть положительным.");

        Transaction transaction = transactionRepository.getTransactionById(id);

        if(transaction == null)
            throw new TransactionNotFoundException("Транзакция с ID: " + id + " не найдена.");

        return transaction;
    }

    public List<Transaction> getTransactionsByAccountId(long id){
        if(id <= 0)
            throw new IllegalArgumentException("ID счета должен быть положительным.");

        return transactionRepository.getTransactionsByAccountId(id);
    }

    public List<Transaction> getTransactionByFromAccountsId(long id){
        if(id <= 0)
            throw new IllegalArgumentException("ID счета должен быть положительным.");

        return transactionRepository.getTransactionsByFromAccountId(id);
    }

    public List<Transaction> getTransactionByToAccountsId(long id){
        if(id <= 0)
            throw new IllegalArgumentException("ID счета должен быть положительным.");

        return transactionRepository.getTransactionsByToAccountId(id);
    }

    public void transfer(Transaction transaction){
        if(transaction == null)
            throw new IllegalArgumentException("Транзакция не может быть null.");

        if (transaction.getFromAccountId() == transaction.getToAccountId())
            throw new IllegalArgumentException("Счет отправителя и счет получателя должны отличаться.");

        Account fromAccount = accountService.getAccountById(transaction.getFromAccountId());

        Account toAccount = accountService.getAccountById(transaction.getToAccountId());

        if(fromAccount.getBalance().compareTo(transaction.getAmount()) < 0)
            throw new InsufficientFundsException("Недостаточно средств на счете отправителя.");

        fromAccount.withdraw(transaction.getAmount());
        toAccount.deposit(transaction.getAmount());

        transactionRepository.saveTransaction(transaction);
    }

    public void deleteTransaction(long id){
        getTransactionById(id);
        transactionRepository.deleteTransaction(id);
    }
}
