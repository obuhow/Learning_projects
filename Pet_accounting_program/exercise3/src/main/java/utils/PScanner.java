package utils;

import java.util.Scanner;
import java.io.InputStream;
import java.util.InputMismatchException;

public class PScanner {
    private final Scanner scanner;

    public PScanner(InputStream inputStream) {
       scanner = new Scanner(inputStream);
    }


    public int nextPositiveInt() throws InputMismatchException {
        int result = -1; // Значение, возвращаемое при ошибке

        if (hasNext()) {
            if (!hasNextInt()) {
		        throw new InputMismatchException("Could not parse a number");
	        } else {  
                result = nextInt();
                if (result <= 0) throw new InputMismatchException("Input Error. Entered number <= 0");
	        }  
	    }
	    return result;
    }

    public double nextPositiveDouble() throws InputMismatchException {
        double result = -1; // Значение, возвращаемое при ошибке

        if (hasNext()) {
            if (!hasNextDouble()) {
		        throw new InputMismatchException("Could not parse a number");
	        } else {  
                result = nextDouble();
                if (result <= 0) throw new InputMismatchException("Incorrect input. Mass <= 0");
	        }  
	    }
	    return result;
    } 
    
    public boolean hasNext() {
	    return scanner.hasNext();
    }

    public String next() {
	    return scanner.next();
    }

    public boolean hasNextInt() {
	    return scanner.hasNextInt();
    }

    public int nextInt() {
	    return scanner.nextInt();
    }

    public boolean hasNextDouble() {
        return scanner.hasNextDouble();
    }

    public double nextDouble() {
        return scanner.nextDouble();
    }
}


