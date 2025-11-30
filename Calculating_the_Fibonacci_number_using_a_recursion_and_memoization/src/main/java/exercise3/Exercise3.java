package exercise3;

import java.util.Scanner;
import java.lang.ArithmeticException;

public class Exercise3 {

    private static long[] fibonacciCache;
    private static boolean[] is_calculated;

    public static void main (String[] args) {
        long fibonacci_number;
        int fibonacci_number_ordinal;

        fibonacci_number_ordinal = scan_number();

        fibonacciCache = new long[fibonacci_number_ordinal + 1];
        is_calculated = new boolean[fibonacci_number_ordinal + 1];

        try {
            fibonacci_number = calculate_fibonacci_number(fibonacci_number_ordinal);
            System.out.println(fibonacci_number);
        } catch (ArithmeticException e) {
            System.err.println(e.getMessage());
        }

    }

    private static int scan_number () {
        int result;
        Scanner scanner = new Scanner(System.in);
        while(!scanner.hasNextInt()) {
            System.out.println("Couldn't parse a number. Please, try again");
        }
        result = scanner.nextInt();
        return result;
    }

    private static long calculate_fibonacci_number (int fibonacci_number_ordinal) {
        long result;

        if (is_calculated[fibonacci_number_ordinal] == true) return fibonacciCache[fibonacci_number_ordinal];

        if (fibonacci_number_ordinal < 0) throw new ArithmeticException("This program can't work with negative fibonacci numbers");

        if (fibonacci_number_ordinal <= 1) {
            result = fibonacci_number_ordinal;
        } else {
            result = calculate_fibonacci_number(fibonacci_number_ordinal - 1) +
                    calculate_fibonacci_number(fibonacci_number_ordinal - 2);
            if (result < 0) throw new ArithmeticException("Too large n");
        }

        fibonacciCache[fibonacci_number_ordinal] = result;
        is_calculated[fibonacci_number_ordinal] = true;
        return result;
    }
}
