/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.albite.font;

import java.io.File;
import java.io.FileFilter;

/**
 *
 * @author albus
 */
public class ExtensionFileFilter implements FileFilter {
    private String extension;

    public ExtensionFileFilter(String extension) {
        this.extension = "." + extension.toLowerCase();
    }

    public boolean accept(File file) {
        if (file.getName().toLowerCase().endsWith(extension)) {
            return true;
        }

        return false;
    }
}
