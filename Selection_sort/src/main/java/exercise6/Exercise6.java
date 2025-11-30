package exercise6;

import java.util.Scanner;
import java.util.Locale;

public class Exercise6 {

    private static Scanner scanner;

    public static void main(String[] args) {
        int numberOfNumbers;
        double[] arrayOfNumbers;

        Locale.setDefault(Locale.US);

        scanner = new Scanner(System.in);
        numberOfNumbers = scanPositiveNumber();
        arrayOfNumbers = new double[numberOfNumbers];
        scanArrayOfNumbers(arrayOfNumbers, numberOfNumbers);
        selectionSort(arrayOfNumbers, numberOfNumbers);
        printArrayOfNumbers(arrayOfNumbers, numberOfNumbers);
    }

    public static void selectionSort(double[] arrayOfNumbers, int sizeOfArray) {
        for (int j = 0; j < sizeOfArray / 2 + 1; j++) {
            double maxValue = arrayOfNumbers[0];
            int maxValuePosition = 0;
            for (int i = 1; i < sizeOfArray - j; i++) {
                if (arrayOfNumbers[i] > maxValue) {
                    maxValue = arrayOfNumbers[i];
                    maxValuePosition = i;
                }
            }
            swap(arrayOfNumbers, maxValuePosition, sizeOfArray - 1 - j);
        }
    }

    public static void swap(double[] arrayOfNumbers, int a, int b) {
        double temp = arrayOfNumbers[a];
        arrayOfNumbers[a] = arrayOfNumbers[b];
        arrayOfNumbers[b] = temp;
    }

    public static int scanPositiveNumber() {
        int result = 0;
        while (true) {
            if (scanner.hasNextInt()) {
                result = scanner.nextInt();
                if (result <= 0) {
                    System.err.println("Input Error. Size <= 0");
                } else {
                    break;
                }
            } else {
                System.err.println("Could not parse a number. Please, try again");
                scanner.next();
            }
        }
        return result;
    }

    public static void scanArrayOfNumbers(double[] arrayOfNumbers, int sizeOfArray) {
        for (int i = 0; i < sizeOfArray;) {
            if (scanner.hasNextDouble()) {
                arrayOfNumbers[i] = scanner.nextDouble();
                i++;
            } else {
                System.err.println("Could not parse a number. Please, try again");
                scanner.next();
            }
        }
    }

    public static void printArrayOfNumbers(double[] arrayOfNumbers, int sizeOfArray) {
        for (int i = 0; i < sizeOfArray; i++) {
            System.out.printf("%.2f ", arrayOfNumbers[i]);
        }
    }
}