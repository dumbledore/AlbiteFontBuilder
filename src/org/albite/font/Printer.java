/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.albite.font;

/**
 *
 * @author albus
 */
public class Printer {

    private Printer() {}
    
    public static void print(String s, boolean printOutput) {
        if (printOutput) {
            System.out.print(s);
        }
    }

    public static void println(String s, boolean printOutput) {
        if (printOutput) {
            System.out.println(s);
        }
    }
}
