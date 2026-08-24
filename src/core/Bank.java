package core;

import enums.Gender;
import enums.TransactionType;
import exceptions.AccountNotFoundException;
import exceptions.InsufficientFundsException;
import exceptions.UserNotFoundException;
import models.Account;
import models.Transaction;
import models.User;
import repositories.AccountRepository;
import repositories.TransactionRepository;
import repositories.UserRepository;
import services.*;
import ui.consoleUI.ConsoleInput;
import ui.consoleUI.ConsoleUI;

import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Bank {
    private final UserService userService;
    private final AccountService accountService;
    private final TransactionService transactionService;
    private final AuthService authService;

    private final ConsoleUI consoleUI;
    private final ConsoleInput consoleInput;

    public Bank() {
        UserRepository userRepository = new UserRepository();
        AccountRepository accountRepository = new AccountRepository();
        TransactionRepository transactionRepository = new TransactionRepository();

        this.consoleUI = new ConsoleUI();
        this.consoleInput = new ConsoleInput();

        this.userService = new UserService(userRepository);
        this.accountService = new AccountService(accountRepository);
        this.transactionService = new TransactionService(transactionRepository, accountService);
        this.authService = new AuthService(userService, userRepository);
    }

    public void test() throws NoSuchAlgorithmException, InvalidKeySpecException {
        consoleUI.printTitle("Тестирование");

        byte[] salt1 = PasswordService.generateSalt();
        byte[] salt2 = PasswordService.generateSalt();

        String hash1 = PasswordService.hashPassword("Ilya150906", salt1);

        String hash2 = PasswordService.hashPassword("Adelya180806", salt2);

        userService.createUser(new User("Илья", "Логинов", LocalDate.of(2006, 9, 15), Gender.MALE, "89877116595", hash1, salt1));
        User user1 = userService.getUserById(1);
        consoleUI.printUserInfo(user1);

        Account account1 = new Account(user1.getId());
        accountService.createAccount(account1);
        account1.deposit(new BigDecimal("3224550"));
        consoleUI.printAccountInfo(account1, user1.getFirstName());

        userService.createUser(new User("Аделия", "Гибадуллина", LocalDate.of(2006, 8, 18), Gender.FEMALE, "89877131382", hash2, salt2));
        User user2 = userService.getUserById(2);
        consoleUI.printUserInfo(user2);

        Account account2 = new Account(user2.getId());
        accountService.createAccount(account2);
        account2.deposit(new BigDecimal("299000"));
        consoleUI.printAccountInfo(account2, user2.getFirstName());

        transactionService.transfer(new Transaction(account1.getId(), account2.getId(), new BigDecimal("224000"), TransactionType.TRANSFER));
        Transaction transaction1 = transactionService.getTransactionById(1);
        consoleUI.printTransactionInfo(transaction1);

        consoleUI.printAccountInfo(account1, user1.getFirstName());
        consoleUI.printAccountInfo(account2, user2.getFirstName());
    }

    public void start(){
        consoleUI.printTitle("Добро пожаловать в Presnotic Bank Demo (PB)");

        String state = "start";
        int action = 0;
        boolean isWorks = true;
        User currentUser = null;
        long firstAccountId;
        long secondAccountId;
        BigDecimal amount;

        while(isWorks){
            switch (state){
                case "start":
                    System.out.println("Выберете действие которое хотите совершить, а затем введите цифру:");
                    consoleUI.printListOfItems("Войти в банк.", "Зарегистрироваться.", "Завершить работу.");
                    System.out.print("Введите действие: ");
                    action = consoleInput.readInt();
                    state = switch (action) {
                        case 1 -> "login";
                        case 2 -> "registration";
                        case 3 -> "stop";
                        default -> state;
                    };
                    consoleUI.printSpace();
                    break;
                case "stop":
                    isWorks = false;
                    break;
                case "registration":
                    consoleUI.printTitle("Регистрация");
                    boolean userIsRegistered = false;
                    while(!userIsRegistered){
                        System.out.print("Введите имя: ");
                        String firstName = consoleInput.readString();
                        System.out.println();
                        System.out.print("Введите фамилию: ");
                        String lastName = consoleInput.readString();

                        System.out.print("Введите дату рождения в формате 01.01.2000: ");
                        String[] dateString = consoleInput.readString().split("\\.");
                        LocalDate date = LocalDate.parse(dateString[2] + "-" + dateString[1] + "-" + dateString[0]);

                        System.out.print("Введите номер телефона: ");
                        String phoneNumber = consoleInput.readString();

                        System.out.print("""
                            Укажите пол: 
                            1) Мужской
                            2) Женский
                            Введите цифру: 
                            """);
                        int genderNumber = consoleInput.readInt();
                        Gender gender = genderNumber == 1 ? Gender.MALE : Gender.FEMALE;

                        System.out.print("Введите пароль: ");
                        String password = consoleInput.readString();

                        userIsRegistered = authService.registration(firstName, lastName, date, gender, phoneNumber, password);
                    }
                    state = "start";
                    break;
                case "login":
                    consoleUI.printTitle("Вход");
                    System.out.print("Введите номер телефона: ");
                    String checkPhoneNumber = consoleInput.readString();

                    System.out.print("Введите пароль: ");
                    String checkPassword = consoleInput.readString();

                    try {
                        currentUser = authService.login(checkPhoneNumber, checkPassword);
                        state = "profile";
                    } catch (IllegalArgumentException | NoSuchAlgorithmException | InvalidKeySpecException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "profile":
                    consoleUI.printTitle("Профиль");
                    consoleUI.printUserInfo(currentUser);
                    consoleUI.printListOfItems("Посмотреть свои счета.", "Сделать перевод.", "Выйти из системы.");
                    System.out.print("Выберете действие: ");
                    action = consoleInput.readInt();
                    state = switch (action) {
                        case 1 -> "my accounts";
                        case 2 -> "new transfer";
                        case 3 -> {
                            currentUser = null;
                            yield "start";
                        }
                        default -> state;
                    };
                    consoleUI.printSpace();
                    break;
                case "my accounts":
                    consoleUI.printTitle("Мои счета");
                    consoleUI.printAccountsInfo(accountService.getAccountsByUserId(currentUser.getId()), currentUser.getFirstName());
                    consoleUI.printListOfItems("Добавить счет.", "Удалить счет.", "Выйти в профиль");
                    System.out.print("Выберете действие: ");
                    action = consoleInput.readInt();
                    state = switch (action) {
                        case 1 -> "add account";
                        case 2 -> "delete account";
                        case 3 -> "profile";
                        default -> state;
                    };
                    consoleUI.printSpace();
                    break;
                case "add account":
                    consoleUI.printTitle("Новый счет");

                    System.out.print("Введите начальную сумму счета: ");
                    BigDecimal startBalance = consoleInput.readBigDecimal();

                    Account newAccount = new Account(currentUser.getId());
                    newAccount.deposit(startBalance);
                    accountService.createAccount(newAccount);

                    state = "my accounts";
                    break;
                case "delete account":
                    consoleUI.printTitle("Удаление счета");

                    System.out.print("Введите номер счета, который желаете удалить: ");
                    long accountId = consoleInput.readLong();

                    boolean hasAccount = accountService
                            .getAccountsByUserId(currentUser.getId())
                            .stream()
                            .anyMatch(account -> account.getId() == accountId);

                    if (!hasAccount) {
                        System.out.println("Вы ввели не свой номер счета.");
                        break;
                    }

                    accountService.deleteAccount(accountId);
                    state = "my accounts";
                    break;
                case "new transfer":
                    consoleUI.printTitle("Новый перевод");
                    System.out.print("Укажите свой номер счета: ");

                    firstAccountId = consoleInput.readInt();

                    // Мой вариант проверки id в списке - получение всех счетов с id пользователя, и проверка с помощью метода contains()
                    List<Long> idsAccountsOfCurrentUser = accountService
                            .getAccountsByUserId(currentUser.getId())
                            .stream()
                            .map(Account::getId)
                            .toList();

                    if(!idsAccountsOfCurrentUser.contains(firstAccountId)){
                        System.out.println("Вы ввели не свой номер счета.");

                        while(action!=0){
                            System.out.println("Введите 0 для выхода.");
                            action = consoleInput.readInt();
                        }
                        break;
                    }

                    // Вариант ChatGPT
//                    boolean hasAccount = accountService
//                            .getAccountsByUserId(currentUser.getId())
//                            .stream()
//                            .anyMatch(account -> account.getId() == firstAccountId);
//
//                    if (!hasAccount) {
//                        System.out.println("Вы ввели не свой номер счета.");
//                        break;
//                    }

                    System.out.println();
                    System.out.print("Укажите номер счета получателя: ");

                    secondAccountId = consoleInput.readInt();

                    System.out.println();
                    System.out.print("Укажите сумму перевода: ");

                    amount = consoleInput.readBigDecimal();

                    try{
                        transactionService.transfer(new Transaction(firstAccountId, secondAccountId, amount, TransactionType.TRANSFER));
                        while(action!=0){
                            System.out.println("Перевод доставлен! Введите 0, чтобы продолжить.");
                            action = consoleInput.readInt();
                        }
                    }
                    catch(AccountNotFoundException | InsufficientFundsException e){
                        System.out.println(e.getMessage());

                        while(action!=0){
                            System.out.println("Введите 0 для выхода.");
                            action = consoleInput.readInt();
                        }
                    }
                    state = "profile";
                    consoleUI.printSpace();
                    break;
            }
        }
    }

    public void testThreads(){
        User user1 = userService.getUserById(1); // Илья, начальный счет 3000550
        User user2 = userService.getUserById(2); //Аделия, начальный счет 523000

        List<Account> accountsUser1 = new ArrayList<>(accountService.getAccountsByUserId(user1.getId()));
        List<Account> accountsUser2 = new ArrayList<>(accountService.getAccountsByUserId(user2.getId()));

        ExecutorService executor = Executors.newCachedThreadPool();

        List<Runnable> tasks = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            Runnable task = () -> {
                for(Account account: accountsUser1){
                    account.deposit(new BigDecimal("1000"));
                }

                for(Account account: accountsUser2){
                    account.withdraw(new BigDecimal("500"));
                }
            };
            tasks.add(task);
        }

        for(Runnable task: tasks){
            executor.execute(task);
        }

        executor.shutdown();

        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) { //Ждем пока текущие задачи закончатся
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        consoleUI.printAccountInfo(accountsUser1.getFirst(), user1.getFirstName());
        consoleUI.printAccountInfo(accountsUser2.getFirst(), user2.getFirstName());
    }
}
