package app;

import core.Bank;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class Main {
    static void main() throws NoSuchAlgorithmException, InvalidKeySpecException {
        Bank bank = new Bank();
        bank.test();
        bank.start();
//        bank.testThreads();
    }
}