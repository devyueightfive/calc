/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

import java.text.ParseException;

/**
 *
 * @author yuri
 */
public class Calculator {

    public double calculate(String userInput) throws ParseException, NumberFormatException {
        if (Expression.isValidNumberOfParantheses(userInput) == false) {
            throw new ParseException("Number of parantheses is not correct", 0);
        }
        if (Expression.isValidPointers(userInput) == false) {
            throw new ParseException("Number of pointers is not correct", 0);
        }
        return eval(userInput);
    }

    public double eval(String userInput) throws ParseException, NumberFormatException {

        Expression exprs = Expression.stringToExpression(userInput);
//      Check for Simple Expressions.
//      In Simple case <value> is calculated <value> = <unar>*Double(<expr>)
        for (Value v : exprs) {
            if (Expression.isFloatNumber(v.expr)) {
                v.isSimple = true;
                int unar = v.unarOperator ? (-1) : 1;
                v.value = unar * Double.parseDouble(v.expr);
//                v.expr   = String.valueOf(v.value);
//                v.unarOperator = false;
            }
        }

        System.out.println(exprs);

        for (Value v : exprs) {
            if (v.isSimple == false) {
                int unar = v.unarOperator ? (-1) : 1;
                v.value = unar * eval(Expression.removeExternalParantheses(v.expr));
            }
        }

        return evalSimpleBinaryExpression(exprs);
    }

    /**
     * *
     *
     * @param simpleExpression represent a Simple Expression (without
     * parantheses). String like "1*2+3" represented as ["1*","2+","3"]
     * @return calculation of the Simple Expression.
     */
    double evalSimpleBinaryExpression(Expression simpleExpression) {
        int bound = simpleExpression.size();

//      Calculate all operators like "*" and "/"
        for (int i = 0; i < bound; i++) {
            Value v = simpleExpression.get(i);
            if (v.binaryOperator == '*' || v.binaryOperator == '/') {
                Value next = simpleExpression.get(i + 1);
                if (v.binaryOperator == '*') {
                    v.value *= next.value;
                } else {
                    if (next.value == 0) {
                        throw new ArithmeticException("Division by zero");
                    }
                    v.value /= next.value;
                }
                v.binaryOperator = next.binaryOperator;
                simpleExpression.remove(i + 1);
                bound--;
                i--;
            }
        }
//      Calculate all perations like "+" and "-"
        for (int i = 0; i < bound; i++) {
            Value v = simpleExpression.get(i);
            if (v.binaryOperator == '+' || v.binaryOperator == '-') {
                Value next = simpleExpression.get(i + 1);
                if (v.binaryOperator == '+') {
                    v.value += next.value;
                } else {
                    v.value -= next.value;
                }
                v.binaryOperator = next.binaryOperator;
                simpleExpression.remove(i + 1);
                bound--;
                i--;
            }
        }
        System.out.println(simpleExpression.get(0).value);
        return simpleExpression.get(0).value;
    }

}
