package ui.consoleUI;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class ConsoleInput {
    private final Scanner scanner;

    public ConsoleInput(){
        this.scanner = new Scanner(System.in);
    }

    public int readInt() {
        while (true) {
            try {
                int result = scanner.nextInt();
                scanner.nextLine(); // съедаем Enter
                return result;
            } catch (InputMismatchException e) {
                System.out.println("Введите целое число.");
                scanner.nextLine();
            }
        }
    }

    public String readString(){
        while (true) {
            try {
                return scanner.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Введите строку.");
                scanner.next();
            }
        }
    }

    public long readLong() {
        while (true) {
            try {
                long result = scanner.nextLong();
                scanner.nextLine();
                return result;
            } catch (InputMismatchException e) {
                System.out.println("Введите целое число.");
                scanner.nextLine();
            }
        }
    }

    public BigDecimal readBigDecimal() {
        while (true) {
            try {
                return scanner.nextBigDecimal();
            } catch (InputMismatchException e) {
                System.out.println("Введите корректную сумму.");
                scanner.next();
            }
        }
    }
}
