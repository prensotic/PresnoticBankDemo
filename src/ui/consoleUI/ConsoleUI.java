package ui.consoleUI;

import models.Account;
import models.Transaction;
import models.User;

import java.util.List;

public class ConsoleUI {
    public void printSpace(){
        for(int i = 0; i < 50; i++){
            System.out.println();
        }
    }

    public void printTitle(String title){
        System.out.println();
        System.out.println("----------" + title + "----------");
        System.out.println();
    }

    public void printUserInfo(User user){
        System.out.println("Пользователь: " + user.getFirstName() + " " + user.getLastName() + "\n" +
                "Пол: " + user.getStringGender() + "\n" +
                "Дата рождения: " + user.getDateOfBirthday());

        System.out.println();
    }

    public void printAccountInfo(Account account, String userName){
        System.out.println("Счет пользователя: " + userName + "\n" +
                "ID счета: " + account.getId() + "\n" +
                "Баланс счета: " + account.getBalance() + "руб.");

        System.out.println();
    }

    public void printAccountsInfo(List<Account> accounts, String userName){
        for(Account account: accounts){
            System.out.println("Счет пользователя: " + userName + "\n" +
                    "ID счета: " + account.getId() + "\n" +
                    "Баланс счета: " + account.getBalance() + "руб.");

            System.out.println();
        }
    }

    public void printTransactionInfo(Transaction transaction){
        System.out.println("ID транзакции: " + transaction.getId() + "\n" +
                "ID счета отправителя: " + transaction.getFromAccountId() + "\n" +
                "ID счета получателя: " + transaction.getToAccountId() + "\n" +
                "Сумма перевода: " + transaction.getAmount() + "руб.");

        System.out.println();
    }

    public void printListOfItems(String... items){
        for(int i = 1; i < items.length+1; i++){
            System.out.println(i+") " + items[i-1]);
        }
    }
}
