import java.io.File;
import java.math.BigInteger;
import java.util.Scanner;

/**
 * Program to verify mosers_powers results. Gets a file from user input and reads in 2 integers: n and x.
 * Tests: f(x) < 2^n < f(x+1) where f(x)=1/24x⁴-1/4x³+23/24x²-3/4x+1
 */
public class MosersPowersVerifier
{
    /**
     * Gets a file from user input.
     * @param scanner System.in scanner
     * @return A file from user input
     */
    private static File getFile(Scanner scanner)
    {
        boolean exists = false;
        File file;

        // Loop until valid file is found.
        do
        {
        System.out.print("Enter the name of the file:");
        String filename = scanner.nextLine();
        file = new File(filename);

        if (!file.exists()) System.out.println("ERROR: File '" + filename + "' does not exist!");
        else exists = true;

        } while (!exists);

        return file;
    }

    /**
     * Returns 2^n
     */
    private static BigInteger getPowerOfTwo(int n)
    {
        BigInteger powerOfTwo = new BigInteger("1");
        powerOfTwo = powerOfTwo.shiftLeft(n);

        //System.out.println(powerOfTwo);
        return powerOfTwo;
    }

    /**
     * Calculates f(x)=1/24x⁴-1/4x³+23/24x²-3/4x+1
     */
    private static BigInteger calculateFunction(BigInteger x)
    {
        BigInteger result = BigInteger.valueOf(0);
        result = result.add(x);

        // Calculate (x(x(x(x - 6) + 23) - 18) / 24) + 1
        result = result.subtract(BigInteger.valueOf(6));
        result = result.multiply(x);
        result = result.add(BigInteger.valueOf(23));
        result = result.multiply(x);
        result = result.subtract(BigInteger.valueOf(18));
        result = result.multiply(x);
        result = result.divide(BigInteger.valueOf(24));
        result = result.add(BigInteger.valueOf(1));

        //System.out.println(result);
        return result;
    }

    /**
     * Tests if f(x) < 2^n < f(x+1) where f(x)=1/24x⁴-1/4x³+23/24x²-3/4x+1
     */
    private static boolean verifyValues(BigInteger x, int n)
    {
        boolean valid = true;
        System.out.println("Calculating 2^n...");
        BigInteger powerOfTwo = getPowerOfTwo(n);

        System.out.println("Calculating f(x)...");
        BigInteger functionX = calculateFunction(x);
        int compResult = functionX.compareTo(powerOfTwo);
        switch (compResult) 
        {
            case 0: // Special output
                System.out.println("\n**** f(x) = 2^n ****\n");
                return false;
            case 1: // f(x) >= 2^n, still need to check f(x+1) == 2^n
                System.out.println("\tf(x) >= 2^n");
                valid = false;
        }

        System.out.println("Calculating f(x+1)...");
        BigInteger functionXPlusOne = calculateFunction(x.add(new BigInteger("1")));
        compResult = functionXPlusOne.compareTo(powerOfTwo);
        switch (compResult) 
        {
            case 0: // Special output
                System.out.println("\n**** f(x+1) = 2^n ****\n");
            case -1: // f(x+1) <= 2^n
                System.out.println("\tf(x+1) <= 2^n");
                valid = false;
        }

        // Returns if conditions are met or not. Early return if f(x) = 2^n
        return valid;
    }

    /**
     * Asks the user for a file and then tests if f(x) < 2^n < f(x+1) where f(x)=1/24x⁴-1/4x³+23/24x²-3/4x+1
     */
    public static void main(String[] args)
    {
        BigInteger x = null;
        int n = 0;

        Scanner inputScanner = new Scanner(System.in), fileReader = null;
        File file = getFile(inputScanner);
        inputScanner.close();

        try
        {
            fileReader = new Scanner(file);
            n = Integer.parseInt(fileReader.nextLine());
            System.out.println("N = " + n);
            x = new BigInteger(fileReader.nextLine());
            System.out.println("X = " + x + "\n");
            fileReader.close();
        }
        catch (Exception e)
        {
            System.out.println("ERROR: " + e.getMessage());
            System.exit(1);
        }

        boolean finalResult = verifyValues(x, n);

        // Final output
        System.out.println("---------------------------");
        if (finalResult) System.out.println("f(x) < 2^n < f(x+1) = TRUE");
        else System.out.println("f(x) < 2^n < f(x+1) = FALSE");
    }
}
