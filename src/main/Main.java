/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import org.albite.font.FontBuilder;

/**
 *
 * @author Albus Dumbledore
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String workingDirectoryName = System.getProperty("user.dir");
        FontBuilder.buildFonts(workingDirectoryName, true);
    }
}
