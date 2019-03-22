/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author main
 */
public class BinaryExpression extends ArrayList<Value> {

    static BinaryExpression stringToExpression(String input) throws ParseException {
        System.out.println(input);
        BinaryExpression result = new BinaryExpression();
        Value value;
        int posStartOfValue = 0;
        String expr;
//        String[] types ={"start","value","operator"};
        boolean uo = false;
        String postStatus = "start";
        char[] inputInChars = input.toCharArray();
        for (int i = 0; i < inputInChars.length; i++) {
            char c = inputInChars[i];
            if (c == '-' && (i == 0 || postStatus.equals("operator"))) {
                uo = true;
                posStartOfValue = i + 1;
                postStatus = "unar-operator";
            } else if ("+-*/".contains(String.valueOf(c))
                    && postStatus.equals("value")) {
                expr = input.substring(posStartOfValue, i);
                value = new Value();
                value.expr = expr;
                value.unarOperator = uo;
                value.binaryOperator = c;
                result.add(value);
                postStatus = "operator";
                uo = false;
                posStartOfValue = i + 1;
            } else if ("0123456789.><=?".contains(String.valueOf(c))) {
                if (i == (inputInChars.length - 1)) {
                    expr = input.substring(posStartOfValue, i + 1);
                    value = new Value();
                    value.expr = expr;
                    value.unarOperator = uo;
                    value.binaryOperator = ' ';
                    result.add(value);
                }
                postStatus = "value";
            } else if (c == '('
                    && (i == 0
                    || postStatus.equals("operator")
                    || postStatus.equals("unar-operator"))) {
                i = positionOfClosingParantheses(i, input);
                if (i == (inputInChars.length - 1)) {
                    expr = input.substring(posStartOfValue, i + 1);
                    value = new Value();
                    value.expr = expr;
                    value.unarOperator = uo;
                    value.binaryOperator = ' ';
                    result.add(value);
                }
                postStatus = "value";
            } else {
                throw new ParseException("Invalid Expression", i);
            }
        }

        return result;
    }

    static int positionOfClosingParantheses(int positionOfOpenParantheses, String inString) {
        int countOfInternalParantheses = 0;
        int i;
        for (i = positionOfOpenParantheses; i < inString.length(); i++) {
            if (inString.charAt(i) == '(') {
                countOfInternalParantheses++;
            }
            if (inString.charAt(i) == ')') {
                countOfInternalParantheses--;
            }
            if (inString.charAt(i) == ')' && countOfInternalParantheses == 0) {
                break;
            }
        }
        return i;
    }

    public static boolean isFloatNumber(String expr) {
        return Pattern.matches("^(([0-9]*[.]?[0-9]+)|([0-9]+[.]?[0-9]*))$", expr);
    }

    public static boolean isSimpleTernaryExpression(String expr) {
        return Pattern.matches("^(([0-9]*[.]?[0-9]+)|([0-9]+[.]?[0-9]*))[><=]"
                + "(([0-9]*[.]?[0-9]+)|([0-9]+[.]?[0-9]*))[?]"
                + "(([0-9]*[.]?[0-9]+)|([0-9]+[.]?[0-9]*))[:]"
                + "(([0-9]*[.]?[0-9]+)|([0-9]+[.]?[0-9]*))$", expr);
    }

    static String removeExternalParantheses(String expr) {
        if (expr.startsWith("(")) {
            return expr.substring(1, expr.length() - 1);
        } else {
            return expr;
        }
    }

    static boolean isValidNumberOfParantheses(String input) {
        int countOfParantheses = 0;
        for (char c : input.toCharArray()) {
            if (c == '(') {
                countOfParantheses++;
            }
            if (c == ')') {
                countOfParantheses--;
            }
            if (countOfParantheses < 0) {
                return false;
            }
        }
        return countOfParantheses == 0;
    }

    static boolean isValidPointers(String input) {
        boolean error = Pattern.matches("(^(.)*\\.[0-9]*\\.(.)*$)"
                + "|(^(.)*[^0-9]+\\.[^0-9]+(.)*$)"
                + "|(^\\.$)", input);
        return !error;
    }
}
