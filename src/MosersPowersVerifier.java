import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Scanner;

/**
 * Program to verify mosers_powers results. Gets a file from user input and reads in 2 integers: n and x.
 * Tests: f(x) < 2^n < f(x+1) where f(x)=1/24x⁴-1/4x³+23/24x²-3/4x+1
 */
public class MosersPowersVerifier
{
    // Static variables for f(x)
    private static BigDecimal OneTwentyfourth = new BigDecimal("1");
    private static BigDecimal TwentythreeTwentyfourth = new BigDecimal("23");
    private static final BigDecimal OneFourth = new BigDecimal("0.25");
    private static final BigDecimal ThreeFourths = new BigDecimal("0.75");
    private static final BigDecimal One = new BigDecimal("1.0");
    private static final BigDecimal TwentyFour = new BigDecimal("24");

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
    private static BigDecimal getPowerOfTwo(int n)
    {
        BigInteger powerOfTwo = new BigInteger("1");
        powerOfTwo = powerOfTwo.shiftLeft(n);

        //System.out.println(powerOfTwo);
        return new BigDecimal(powerOfTwo);
    }

    /**
     * Calculates f(x)=1/24x⁴-1/4x³+23/24x²-3/4x+1
     */
    private static BigDecimal calculateFunction(BigInteger x)
    {
        BigDecimal xDecimal = new BigDecimal(x);
        BigDecimal result = new BigDecimal(x);

        // x(x(x(1/24x - 1/4) + 23/24) - 3/4) + 1
        result = result.multiply(OneTwentyfourth);
        result = result.subtract(OneFourth);
        result = result.multiply(xDecimal);
        result = result.add(TwentythreeTwentyfourth);
        result = result.multiply(xDecimal);
        result = result.subtract(ThreeFourths);
        result = result.multiply(xDecimal);
        result = result.add(One);

        result = result.setScale(0, RoundingMode.HALF_UP);
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
        BigDecimal powerOfTwo = getPowerOfTwo(n);

        System.out.println("Calculating f(x)...");
        BigDecimal functionX = calculateFunction(x);
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
        BigDecimal functionXPlusOne = calculateFunction(x.add(new BigInteger("1")));
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

        // Get more accurate 1/24 and 23/24
        OneTwentyfourth = OneTwentyfourth.divide(TwentyFour, n, RoundingMode.HALF_UP);
        TwentythreeTwentyfourth = TwentythreeTwentyfourth.divide(TwentyFour, n, RoundingMode.HALF_UP);

        boolean finalResult = verifyValues(x, n);

        // Final output
        System.out.println("---------------------------");
        if (finalResult) System.out.println("f(x) < 2^n < f(x+1) = TRUE");
        else System.out.println("f(x) < 2^n < f(x+1) = FALSE");
    }
}
