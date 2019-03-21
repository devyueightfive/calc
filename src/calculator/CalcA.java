/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author yuri
 */
public class CalcA {

    //init Opeartors as HashMaps <key,value> = <name,priority>
    ArrayList<Expression> operations = new ArrayList();

    public static boolean isSimple(String expr) {
        StringBuilder sb = new StringBuilder();
        for (char c : expr.toCharArray()) {
            if (sb.indexOf(String.valueOf(c)) == -1) {
                sb.append(c);
            }
        }

        return "0123456789.".contains(sb.toString());
    }

    public double eval(String userInput) throws ParseException {

        Expression exprs = toExternalExpression(userInput);
        for (Value v : exprs) {
            if (isSimple(v.expr)) {
                v.isSimple = true;
                int unar = v.unarOperator ? (-1) : 1;
                v.value = unar * Double.parseDouble(v.expr);
            }
        }

        System.out.println(exprs);

        for (Value v : exprs) {
            if (v.isSimple == false) {
                int unar = v.unarOperator ? (-1) : 1;
                v.value = unar *eval(removeExternalParantheses(v.expr));
            }
        }

        return evalSimpleExpression(exprs);
    }

    public String removeExternalParantheses(String expr) {
        if (expr.startsWith("(")) {
            return expr.substring(1, expr.length() - 1);
        } else {
            return expr;
        }
    }

    double evalSimpleExpression(Expression expr) {
        int bound = expr.size();
        for (int i = 0; i < bound; i++) {
            Value v = expr.get(i);
            if (v.binaryOperator == '*' || v.binaryOperator == '/') {
                Value next = expr.get(i + 1);
                if (v.binaryOperator == '*') {
                    v.value *= next.value;
                } else {
                    v.value /= next.value;
                }
                v.binaryOperator = next.binaryOperator;
                expr.remove(i + 1);
                bound--;
                i--;
            }
        }

        for (int i = 0; i < bound; i++) {
            Value v = expr.get(i);
            if (v.binaryOperator == '+' || v.binaryOperator == '-') {
                Value next = expr.get(i + 1);
                if (v.binaryOperator == '+') {
                    v.value += next.value;
                } else {
                    v.value -= next.value;
                }
                v.binaryOperator = next.binaryOperator;
                expr.remove(i + 1);
                bound--;
                i--;
            }
        }
        System.out.println(expr.get(0).value);
        return expr.get(0).value;
    }

    class Value {

        Double value = null;
        boolean unarOperator = false;
        char binaryOperator = ' ';
        String expr = "";
        boolean isSimple = false;

        public String toString() {
            String unar = "";
            String operator;
            if (this.unarOperator) {
                unar = "(-)";
            }
            if (binaryOperator == ' ') {
                operator = "";
            } else {
                operator = String.valueOf(this.binaryOperator);
            }
            return unar + this.expr + operator + "[" + isSimple + "]";
        }
    }

    class Expression extends ArrayList<Value> {

    }

    public Expression toExternalExpression(String input) throws ParseException {
        System.out.println(input);
        Expression result = new Expression();
        Value value;
        int posStartOfValue = 0;
        String expr = "";
//        String[] types ={"start","value","operator"};
        boolean uo = false;
        String postStatus = "start";
        char[] inputInChars = input.toCharArray();
        for (int i = 0; i < inputInChars.length; i++) {
            char c = inputInChars[i];
            if (c == '-'
                    && (i == 0 || postStatus == "operator")) {
                uo = true;
                posStartOfValue = i + 1;
                postStatus = "unar-operator";
            } else if ("-+/*".contains(String.valueOf(c))
                    && postStatus == "value") {
                expr = input.substring(posStartOfValue, i);
                value = new Value();
                value.expr = expr;
                value.unarOperator = uo;
                value.binaryOperator = c;
                result.add(value);
                postStatus = "operator";
                uo = false;
                posStartOfValue = i + 1;
            } else if ("0123456789.".contains(String.valueOf(c))) {
                postStatus = "value";
                if (i == (inputInChars.length - 1)) {
                    expr = input.substring(posStartOfValue, i + 1);
                    value = new Value();
                    value.expr = expr;
                    value.unarOperator = uo;
                    value.binaryOperator = ' ';
                    result.add(value);
                }

            } else if (c == '(') {
                i = positionOfClosingParantheses(i, input);
                postStatus = "value";
                if (i == (inputInChars.length - 1)) {
                    expr = input.substring(posStartOfValue, i + 1);
                    value = new Value();
                    value.expr = expr;
                    value.unarOperator = uo;
                    value.binaryOperator = ' ';
                    result.add(value);
                }
            } else {
                throw new ParseException("Invalid Expression", i);
            }
        }

        return result;
    }

    int positionOfClosingParantheses(int start, String str) {
        int countOfInternalParantheses = 0;
        int i;
        for (i = start; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                countOfInternalParantheses++;
            }
            if (str.charAt(i) == ')') {
                countOfInternalParantheses--;
            }
            if (str.charAt(i) == ')' && countOfInternalParantheses == 0) {
                break;
            }
        }
        return i;
    }

}
