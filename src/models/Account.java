package models;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Account {
   private static final AtomicLong ID_GENERATOR = new AtomicLong(); // Атомик-объект для работы с id
   private final long id;
   private final long userId;
//   private BigDecimal balance;
   private final AtomicReference<BigDecimal> balance = new AtomicReference<>(BigDecimal.ZERO);

   public Account(long userId){
      if(userId <= 0)
         throw new IllegalArgumentException("ID пользователя должен быть положительным.");

      this.id = ID_GENERATOR.incrementAndGet();
      this.userId = userId;
//      this.balance = BigDecimal.ZERO; //Чтоб при создании счета был 0, а не null
   }

   /*
   void deposit(BigDecimal amount) - для пополнения баланса
   void withdraw(BigDecimal amount) - для уменьшения баланса

   после успешной демки, нужно сделать synchronized, для корректной работы в нескольких потоках
   */

   /*
   * ПРИ ИСПОЛЬЗОВАНИИ synchronized поток блокирует остальные потоки, пока не закончит операцию
   * */

//   public synchronized void deposit(BigDecimal amount){
//         balance = balance.add(amount);
//   }
//
//   public synchronized void withdraw(BigDecimal amount){
//      balance = balance.subtract(amount);
//   }

   public void deposit(BigDecimal amount){
      balance.updateAndGet(current -> current.add(amount));
   }

   public void withdraw(BigDecimal amount){
      balance.updateAndGet(current -> current.subtract(amount));
   }

   public BigDecimal getBalance(){
      return balance.get();
   }

   public long getId(){
      return id;
   }

   public long getUserId() {
      return userId;
   }
}
