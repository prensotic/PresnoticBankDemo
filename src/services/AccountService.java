package services;

import repositories.AccountRepository;

import java.util.List;

import exceptions.AccountNotFoundException;
import models.Account;

public class AccountService {
   private final AccountRepository accountRepository;

   public AccountService(AccountRepository accountRepository){
      this.accountRepository = accountRepository;
   }

   public List<Account> getAllAccounts() {
      return accountRepository.getAllAccounts();
   }

   public List<Account> getAccountsByUserId(long userId){
      if(userId <= 0)
         throw new IllegalArgumentException("ID пользователя должен быть положительным");

      return accountRepository.getAllAccountsByUserId(userId);
   }

   public Account getAccountById(long id){
      if(id <= 0)
         throw new IllegalArgumentException("ID счета должен быть положительным");

      Account account = accountRepository.getAccountById(id);

      if(account == null)
         throw new AccountNotFoundException("Счет с ID: " + id + " не найден.");

      return account;
   }

   public void createAccount(Account account){
      if(account == null)
         throw new IllegalArgumentException("Счет не может быть null.");

      accountRepository.saveAccount(account);
   }

   public void deleteAccount(long id){
      getAccountById(id);
      accountRepository.deleteAccount(id);
   }
}
