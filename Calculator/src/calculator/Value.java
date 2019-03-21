/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package calculator;

/**
 *
 * @author main
 */
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
