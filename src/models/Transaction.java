package models;

import enums.TransactionType;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

public class Transaction {
   private static final AtomicLong ID_GENERATOR = new AtomicLong();
   private final long id;
   private final long fromAccountId;
   private final long toAccountId;
   private final BigDecimal amount;
   private final TransactionType transactionType;
   
   public Transaction(long fromAccountId, long toAccountId, BigDecimal amount, TransactionType transactionType){
      if(fromAccountId <= 0) 
         throw new IllegalArgumentException("ID счета отправителя должен быть положительным.");

      if(toAccountId <= 0) 
         throw new IllegalArgumentException("ID счета получателя должен быть положительным.");

      if(amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) 
         throw new IllegalArgumentException("Сумма транзакции должна быть больше нуля.");

      this.id = ID_GENERATOR.incrementAndGet();
      this.fromAccountId = fromAccountId;
      this.toAccountId = toAccountId;
      this.amount = amount;
      this.transactionType = transactionType;
   }

   public long getId(){
      return id;
   }

   public long getFromAccountId(){
      return fromAccountId;
   }

   public long getToAccountId(){
      return toAccountId;
   }

   public BigDecimal getAmount(){
      return amount;
   }

   public TransactionType getTransactionType(){ return transactionType; }
}
