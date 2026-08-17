package io.dlrwee.todocli.util;

import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Predicate;

public final class ConsoleReader {

    private ConsoleReader() {
        throw new AssertionError("ConsoleReader is a util class and cannot be instantiated");
    }

    public static <T> T readValidInput(Function<String, T> mapper,
                                       Predicate<T> predicate,
                                       String errorMessage,
                                       Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine();
                T value = mapper.apply(input);

                if (predicate.test(value)) {
                    return value;
                } else {
                    System.out.println(errorMessage);
                }
            } catch (Exception e) {
                System.out.println(errorMessage);
            }
        }
    }

    public static int readValidInt(Predicate<Integer> predicate, String errorMessage, Scanner scanner) {
        return readValidInput(Integer::parseInt, predicate, errorMessage, scanner);
    }

    public static String readValidString(Predicate<String> predicate, String errorMessage, Scanner scanner) {
        return readValidInput(Function.identity(), predicate, errorMessage, scanner);
    }
}
