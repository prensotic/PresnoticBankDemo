package repositories;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Account;

public class AccountRepository {
   private final Map<Long, Account> accounts = new HashMap<>();

   public List<Account> getAllAccounts(){
      return new ArrayList<>(accounts.values());
   }

   public List<Account> getAllAccountsByUserId(long userId){
      return accounts.values()
              .stream()
              .filter(a -> a.getUserId() == userId).toList();
   }

   public Account getAccountById(long id){
      return accounts.get(id);
   }

   public void saveAccount(Account account){
      accounts.put(account.getId(), account);
   }

   public void deleteAccount(long id){
      accounts.remove(id);
   }
}
