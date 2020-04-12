/*
 * Created by:
 * Karan Patoliya
 */

import java.util.Scanner;

public class Karatsuba {
    // Hardcode the times tables for 0-9
    private static final String[][] TIMES_TABLE = new String[][] {
        { "0", "0", "0", "0", "0", "0", "0", "0", "0", "0" },
        { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" },
        { "0", "2", "4", "6", "8", "10", "12", "14", "16", "18" },
        { "0", "3", "6", "9", "12", "15", "18", "21", "24", "27" },
        { "0", "4", "8", "12", "16", "20", "24", "28", "32", "36" },
        { "0", "5", "10", "15", "20", "25", "30", "35", "40", "45" },
        { "0", "6", "12", "18", "24", "30", "36", "42", "48", "54" },
        { "0", "7", "14", "21", "28", "35", "42", "49", "56", "63" },
        { "0", "8", "16", "24", "32", "40", "48", "56", "64", "72" },
        { "0", "9", "18", "27", "36", "45", "54", "63", "72", "81" }
    };
    
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        
        // read our two factors
        String factor1 = sc.nextLine();
        String factor2 = sc.nextLine();
        
        // print the product of the two factors
        System.out.println(multiply(factor1, factor2));
    }
    
    /**
     * Implements Karatsuba's algorithm to multiply number1 and number2.
     */
    private static String multiply(String number1, String number2) {
        // base case (1-digit numbers)
        if (number1.length() == 1 && number2.length() == 1) {
            return multiplyOneDigit(number1, number2);
        }
        
        // normalize lengths by adding leading zeroes to number1 or number2
        String[] numbersWithLeadingZeroes = addLeadingZeroes(number1, number2);
        number1 = numbersWithLeadingZeroes[0];
        number2 = numbersWithLeadingZeroes[1];
        int n = number1.length();
        
        // split number1 into (a*10^(n/2)+b)
        String a = number1.substring(0, n/2);
        String b = number1.substring(n/2);
        
        // split number2 into (c*10^(n/2)+d)
        String c = number2.substring(0, n/2);
        String d = number2.substring(n/2);
        
        // find ac
        String ac = multiply(a, c);
        
        // find bd
        String bd = multiply(b, d);
        
        // find (a+b)(c+d) - ac - bd, which we call middleTerm
        String aPlusB = add(a, b);
        String cPlusD = add(c, d);
        String middleTerm = multiply(aPlusB, cPlusD);
        middleTerm = subtract(middleTerm, ac);
        middleTerm = subtract(middleTerm, bd);
        
        // combine the three terms into the formula
        // ac(10)^n + [(a+b)(c+d) - ac - bd](10)^(n/2) + bd
        String firstTerm = multiplyByTenToTheN(ac, 2*((n+1)/2));
        String secondTerm = multiplyByTenToTheN(middleTerm, (n+1)/2);
        String thirdTerm = bd;
        return add(add(firstTerm, secondTerm), thirdTerm);
    }
    
    /**
     * Helper function to multiply a number by 10 to a given power
     * Here, we note that multiplying by 10^n is the same as adding
     * n trailing zeroes
     */
    private static String multiplyByTenToTheN(String number, int n) {
        // build a string of n zeroes
        StringBuilder zerosSb = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            zerosSb.append("0");
        }
        
        // combine the original number with the n zeroes
        return number + zerosSb.toString();
    }
    
    /**
     * Given two numbers represented by strings, add the minimal number of
     * leading zeroes such that the lengths of the strings are equal
     * Returns an array of { number1, number2 } with enough leading zeroes
     * to make the numbers equal.
     */
    private static String[] addLeadingZeroes(String number1, String number2) {
        if (number1.length() > number2.length()) {
            int difference = number1.length() - number2.length();
            StringBuilder zerosSb = new StringBuilder(difference);
            for (int i = 0; i < difference; i++) {
                zerosSb.append("0");
            }
            return new String[] { number1, zerosSb.toString() + number2 };
        } else if (number2.length() > number1.length()) {
            int difference = number2.length() - number1.length();
            StringBuilder zerosSb = new StringBuilder(difference);
            for (int i = 0; i < difference; i++) {
                zerosSb.append("0");
            }
            return new String[] { zerosSb.toString() + number1, number2 };
        } else {
            return new String[] { number1, number2 };
        }
    }
    
    /**
     * Implements the elementary school addition algorithm
     * on two numbers represented by strings
     * Here, we expect that number1.length() == number2.length().
     */
    private static String add(String number1, String number2) {
        String[] numbersWithLeadingZeroes = addLeadingZeroes(number1, number2);
        number1 = numbersWithLeadingZeroes[0];
        number2 = numbersWithLeadingZeroes[1];
        
        // Represent the sum as an int array
        int[] sumArray = new int[number1.length()+1];
        
        // track a carry bit
        int carryBit = 0;
        
        // go through the numbers backwards and add each pair of digits
        for (int i = number1.length()-1; i >=0; i--) {
            int digit1 = number1.charAt(i) - '0';
            int digit2 = number2.charAt(i) - '0';
            
            sumArray[i+1] = (digit1 + digit2 + carryBit) % 10;
            carryBit = (digit1 + digit2 + carryBit ) / 10;
        }
        
        // set the first digit to the remaining carry bit
        sumArray[0] = carryBit;
        
        // convert sumArray to a string
        StringBuilder resultSb = new StringBuilder(sumArray.length);
        for (int i = 0; i < sumArray.length; i++) {
            resultSb.append(sumArray[i]);
        }
        String result = resultSb.toString();
        
        // strip leading zero if necessary
        if (result.charAt(0) == '0') {
            return result.substring(1);
        } else {
            return result;
        }
    }
    
    /**
     * Implements the elementary school subtraction algorithm
     * on two numbers represented by strings.
     * Here, we assume that number1 > number2 and that the number
     * of digits in number1 and number2 are the same.
     */
    private static String subtract(String number1, String number2) {
        String[] numbersWithLeadingZeroes = addLeadingZeroes(number1, number2);
        number1 = numbersWithLeadingZeroes[0];
        number2 = numbersWithLeadingZeroes[1];
        
        // Represent the difference as an int array
        int[] differenceArray = new int[number1.length()];
        
        // track a borrow bit
        int borrowBit = 0;
        
        //go through the numbers backwards and subtract each pair of digits
        for (int i = number1.length()-1; i >=0; i--) {
            int digit1 = number1.charAt(i) - '0';
            int digit2 = number2.charAt(i) - '0';
            
            if (digit1 - borrowBit < digit2) {
                digit1 += 10;
                differenceArray[i] = digit1 - borrowBit - digit2;
                borrowBit = 1;
            } else {
                differenceArray[i] = digit1 - borrowBit - digit2;
                borrowBit = 0;
            }
        }
        
        // convert differenceArray to string
        StringBuilder resultSb = new StringBuilder(differenceArray.length);
        for (int i = 0; i < differenceArray.length; i++) {
            resultSb.append(differenceArray[i]);
        }
        String result = resultSb.toString();
        
        // strip leading zeroes and return
        for (int i = 0; i < result.length(); i++) {
            if (result.charAt(i) != '0') {
                return result.substring(i);
            }
        }
        return "0";
    }
    
    /**
     * Multiplies two strings containing one-digit integers
     */
    private static String multiplyOneDigit(String number1, String number2) {
        return
            TIMES_TABLE[Integer.parseInt(number1)][Integer.parseInt(number2)];
    }
}
