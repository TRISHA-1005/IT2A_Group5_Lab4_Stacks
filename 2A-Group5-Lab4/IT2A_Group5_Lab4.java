/* This program is a menu-driven Java application that converts expressions between Infix, Postfix, and Prefix notations 
using stack-based algorithms, ensuring accurate conversions with organized and efficient method implementation.
 * Group 5
 * Authors: Daclison, Trisha Jamaica U.  (Leader)
 *          Banzuela, Francine Hope G.   (Members)
 *          Cainday, Katrina Camille B.
 * Laboratory Exercise 4
 * Date: October 26, 2025
 */

import java.util.Scanner;
import java.util.Stack;

public class IT2A_Group5_Lab4 {

public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    int choice = -1;
    String exp;

    do {
        clearScreen();
        mainMenu();  // display menu

        try {
            if (!sc.hasNextInt()) {
                System.out.println("\nPlease enter a valid number (0 to 3).");
                sc.nextLine(); // clear invalid input
                pause(sc);
                clearScreen();
                continue;
            }

            choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    clearScreen();
                    System.out.println("=== INFIX TO POSTFIX CONVERSION ===");
                    String result1;
                    do {
                        System.out.print("\nEnter an Infix expression: ");
                        exp = sc.nextLine();
                        result1 = infixToPostfix(exp);

                        if (result1.startsWith("Invalid")) {
                            System.out.println("\n" + result1);
                            System.out.println("\nPlease try again.\n");
                        } else {
                            System.out.println("\nPostfix: " + result1);
                        }
                    } while (result1.startsWith("Invalid"));
                    pause(sc);
                    break;

                case 2:
                    clearScreen();
                    System.out.println("=== INFIX TO PREFIX CONVERSION ===");
                    String result2;
                    do {
                        System.out.print("\nEnter an Infix expression: ");
                        exp = sc.nextLine();
                        result2 = infixToPrefix(exp);

                        if (result2.startsWith("Invalid")) {
                            System.out.println("\n" + result2);
                            System.out.println("\nPlease try again.\n");
                        } else {
                            System.out.println("\nPrefix: " + result2);
                        }
                    } while (result2.startsWith("Invalid"));
                    pause(sc);
                    break;

                case 3:
                    clearScreen();
                    System.out.println("=== POSTFIX TO INFIX CONVERSION ===");
                    String result3;
                    do {
                        System.out.print("\nEnter a Postfix expression: ");
                        exp = sc.nextLine();
                        result3 = postfixToInfix(exp);

                        if (result3.startsWith("Invalid")) {
                            System.out.println("\n" + result3);
                            System.out.println("\nPlease try again.\n");
                        } else {
                            System.out.println("\nInfix: " + result3);
                        }
                    } while (result3.startsWith("Invalid"));
                    pause(sc);
                    break;

                case 0:
                    System.out.println("\nProgram terminated. Thank you!");
                    break;

                default:
                    System.out.println("\nInvalid choice! Please enter 0 to 3 only.\n");
                    pause(sc);
                    clearScreen();
            }

        } catch (Exception e) {
            System.out.println("\nUnexpected error: " + e.getMessage());
            sc.nextLine(); // reset scanner input
        }

    } while (choice != 0);
}


    // Method: Pause before continuing
    public static void pause(Scanner sc) {
        System.out.println("\nPress Enter to continue...");
        sc.nextLine();
    }

    // Method: Clear Screen (for menu and conversions)
    public static void clearScreen() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception e) {
            System.out.println("Unable to clear screen.");
        }
    }

    // Method: Main Menu
    public static void mainMenu() {
        System.out.println("*******************");
        System.out.println(" Stack Application ");
        System.out.println("  Conversion Menu ");
        System.out.println("*******************");
        System.out.println("[1] Infix to Postfix");
        System.out.println("[2] Infix to Prefix");
        System.out.println("[3] Postfix to Infix");
        System.out.println("[0] Stop");
        System.out.print("Enter Choice: ");
    }

    // Helper: Remove spaces
    public static String removeSpaces(String s) {
        if (s == null) return "";
        return s.replaceAll("\\s+", "");
    }

    // Helper: Operator Precedence
    public static int precedence(char ch) {
        switch (ch) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            case '^': return 3;
        }
        return -1;
    }

    public static boolean isRightAssociative(char ch) {
        return ch == '^';
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static boolean isOperand(char c) {
        return Character.isLetter(c);
    }

    // =========================
    // VALIDATION METHODS
    // =========================

    public static String validateInfix(String raw) {
        if (raw == null) return "Invalid Infix Expression! Expression is null.";
        String exp = removeSpaces(raw);
        if (exp.isEmpty()) return "Invalid Infix Expression! Expression is empty.";

        int tokenCount = 0;
        Stack<Character> paren = new Stack<>();

        char first = exp.charAt(0);
        if (!(isOperand(first) || first == '('))
            return "Invalid Infix Expression! It must begin with an operand or '('.";

        char last = exp.charAt(exp.length() - 1);
        if (isOperator(last))
            return "Invalid Infix Expression! Expression cannot end with an operator.";

        char prev = 0;
        for (char c : exp.toCharArray()) {
            if (!(isOperand(c) || isOperator(c) || c == '(' || c == ')'))
                return "Invalid Infix Expression! Invalid character '" + c + "' found.";

            if (isOperand(c)) {
                tokenCount++;
                if (isOperand(prev)) return "Invalid Infix Expression! Two operands in sequence are not allowed.";
            } else if (isOperator(c)) {
                tokenCount++;
                if (!(isOperand(prev) || prev == ')'))
                    return "Invalid Infix Expression! Operator '" + c + "' must be preceded by an operand or ')'.";
            } else if (c == '(') {
                paren.push(c);
            } else if (c == ')') {
                if (paren.isEmpty())
                    return "Invalid Infix Expression! Unmatched closing parenthesis detected.";
                paren.pop();
            }
            prev = c;
        }

        if (!paren.isEmpty()) return "Invalid Infix Expression! Unmatched opening parenthesis detected.";
        if (tokenCount > 15) return "Invalid Infix Expression! Exceeds maximum of 15 operators/operands.";
        return null;
    }

    public static String validatePostfix(String raw) {
        if (raw == null) return "Invalid Postfix Expression! Expression is null.";
        String exp = removeSpaces(raw);
        if (exp.isEmpty()) return "Invalid Postfix Expression! Expression is empty.";

        int tokenCount = 0;
        Stack<Integer> stack = new Stack<>();

        for (char c : exp.toCharArray()) {
            if (!(isOperand(c) || isOperator(c)))
                return "Invalid Postfix Expression! Invalid character '" + c + "' found.";

            if (isOperand(c)) {
                tokenCount++;
                stack.push(1);
            } else if (isOperator(c)) {
                tokenCount++;
                if (stack.size() < 2)
                    return "Invalid Postfix Expression! The number of operators are not applicable with the number of operands.";
                stack.pop(); stack.pop(); stack.push(1);
            }

            if (tokenCount > 15)
                return "Invalid Postfix Expression! Exceeds maximum of 15 operators/operands.";
        }

        if (stack.size() != 1)
            return "Invalid Postfix Expression! The number of operators are not applicable with the number of operands.";

        return null;
    }

    public static String validatePrefix(String raw) {
        if (raw == null) return "Invalid Prefix Expression! Expression is null.";
        String exp = removeSpaces(raw);
        if (exp.isEmpty()) return "Invalid Prefix Expression! Expression is empty.";

        int tokenCount = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i = exp.length() - 1; i >= 0; i--) {
            char c = exp.charAt(i);

            if (!(isOperand(c) || isOperator(c)))
                return "Invalid Prefix Expression! Invalid character '" + c + "' found.";

            if (isOperand(c)) {
                tokenCount++;
                stack.push(1);
            } else if (isOperator(c)) {
                tokenCount++;
                if (stack.size() < 2)
                    return "Invalid Prefix Expression! The number of operators are not applicable with the number of operands.";
                stack.pop(); stack.pop(); stack.push(1);
            }

            if (tokenCount > 15)
                return "Invalid Prefix Expression! Exceeds maximum of 15 operators/operands.";
        }

        if (stack.size() != 1)
            return "Invalid Prefix Expression! The number of operators are not applicable with the number of operands.";

        return null;
    }

    // =========================
    // CONVERSION METHODS
    // =========================

    public static String infixToPostfix(String infix) {
        try {
            String v = validateInfix(infix);
            if (v != null) return v;

            String exp = removeSpaces(infix);
            Stack<Character> stack = new Stack<>();
            StringBuilder result = new StringBuilder();

            for (char c : exp.toCharArray()) {
                if (isOperand(c)) result.append(c);
                else if (c == '(') stack.push(c);
                else if (c == ')') {
                    while (!stack.isEmpty() && stack.peek() != '(')
                        result.append(stack.pop());
                    if (!stack.isEmpty()) stack.pop();
                } else if (isOperator(c)) {
                    while (!stack.isEmpty() && stack.peek() != '(' &&
                          ((precedence(c) < precedence(stack.peek())) ||
                           (precedence(c) == precedence(stack.peek()) && !isRightAssociative(c)))) {
                        result.append(stack.pop());
                    }
                    stack.push(c);
                }
            }
            while (!stack.isEmpty()) result.append(stack.pop());
            return result.toString();

        } catch (Exception e) {
            return "Error converting Infix to Postfix: " + e.getMessage();
        }
    }

    public static String reverseAndSwap(String exp) {
        StringBuilder reversed = new StringBuilder();
        for (int i = exp.length() - 1; i >= 0; i--) {
            char c = exp.charAt(i);
            if (c == '(') reversed.append(')');
            else if (c == ')') reversed.append('(');
            else reversed.append(c);
        }
        return reversed.toString();
    }

    public static String infixToPrefix(String infix) {
        try {
            String v = validateInfix(infix);
            if (v != null) return v;
            String rev = reverseAndSwap(infix);
            String post = infixToPostfix(rev);
            if (post.startsWith("Invalid")) return post;
            return new StringBuilder(post).reverse().toString();
        } catch (Exception e) {
            return "Error converting Infix to Prefix: " + e.getMessage();
        }
    }

    public static String postfixToInfix(String postfix) {
        try {
            String v = validatePostfix(postfix);
            if (v != null) return v;

            Stack<String> stack = new Stack<>();
            for (char c : removeSpaces(postfix).toCharArray()) {
                if (isOperand(c)) stack.push(String.valueOf(c));
                else if (isOperator(c)) {
                    String op2 = stack.pop();
                    String op1 = stack.pop();
                    stack.push("(" + op1 + c + op2 + ")");
                }
            }

            String result = stack.pop();
            if (result.startsWith("(") && result.endsWith(")"))
                result = result.substring(1, result.length() - 1);
            return result;

        } catch (Exception e) {
            return "Error converting Postfix to Infix: " + e.getMessage();
        }
    }
}