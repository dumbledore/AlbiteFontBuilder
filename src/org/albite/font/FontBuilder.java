/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.albite.font;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import org.albite.image.converter.ImageConverter;
import org.kxml2.io.KXmlParser;
import org.kxml2.kdom.Document;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

/**
 *
 * @author albus
 */
public class FontBuilder {
    public static final int MAGIC_NUMBER_ALBF = 1095516742;
    public static final String FILE_EXTENSION = ".alf";

    private static String FONT_DESCRIPTOR_ERROR_MSG =
            "ERROR. Cannot build font descriptor.\n";
    private static int[] obligatoryCharIDs = {32, 45, 63};

    private FontBuilder() {}

    public static void buildFonts(String workingDirectoryName,
            boolean printOutput) {
        File workingDirectory = new File(workingDirectoryName);

        File[] xmlFiles = workingDirectory.listFiles(
                new ExtensionFileFilter("xml"));
        Printer.println("Building font descriptors...", printOutput);
        for (int fi=0; fi<xmlFiles.length; fi++) {
            File fileIn = xmlFiles[fi];
            String fileOutName = fileIn.getPath().substring(0,
                    fileIn.getPath().lastIndexOf('.')) + FILE_EXTENSION;
            buildFontDescriptor(fileIn, fileOutName, true);
        }

        File[] pngFiles = workingDirectory.listFiles(
                new ExtensionFileFilter("png"));
        Printer.println("Building font images...", printOutput);
        for (int fi=0; fi<pngFiles.length; fi++) {
            File fileIn = pngFiles[fi];
            String fileOutName = fileIn.getPath().substring(0,
                    fileIn.getPath().lastIndexOf('.')) + ImageConverter.FILE_EXTENSION;
            ImageConverter.fromPNG(fileIn, fileOutName, true);
        }

        Printer.println("All fonts were builded successfully!", printOutput);
    }

    /**
     * Builds font descriptor (font.alf) from XML file (font.xml)
     * These XML descriptors are a slightly modified versions of those
     * produced by Angelcode's Bitmap Font Generator
     *
     * @link http://www.angelcode.com/products/bmfont/
     */
    private static void buildFontDescriptor(File fileIn, String fileOutName, 
            boolean printOutput) {

        Printer.println("Building file " + fileIn.getName(),
                printOutput);

        /*
         * Some glyphs are needed by ALL fonts. These are #32 (space),
         * #45 (dash) and #64 (question mark).
         */
        final boolean[] obligatoryCharIDExists =
                new boolean[obligatoryCharIDs.length];
        for (int i=0; i<obligatoryCharIDExists.length; i++) {
            obligatoryCharIDExists[i] = false;
        }

        /* Try opening the input stream. */
        try {
            FileInputStream f = new FileInputStream(fileIn);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            DataOutputStream data = new DataOutputStream(bos);

            KXmlParser parser = null;
            Document doc = null;
            Element root;
            Element kid;

            try {
                parser = new KXmlParser();
                parser.setInput(new InputStreamReader(f));

                doc = new Document();
                doc.parse(parser);
                parser = null;
            } catch (XmlPullParserException xppe) {
                parser = null;
                doc = null;
            }

            root = doc.getRootElement();
            int child_count = root.getChildCount();
            int charCount = 0;

            int maximumCharID = -1;
            short maximumWidth = -1;

            int id = -1;
            short x, y, w, h, xo, yo, xa;

            byte lineheight = Byte.parseByte(root.getAttributeValue(
                    KXmlParser.NO_NAMESPACE, "lineHeight"));
            byte linespacing = Byte.parseByte(root.getAttributeValue(
                    KXmlParser.NO_NAMESPACE, "lineSpacing"));

            for (int i = 0; i < child_count ; i++ ) {
                if (root.getType(i) != Node.ELEMENT) {
                        continue;
                }

                kid = root.getElement(i);
                if (kid.getName().equals("char")) {
                    id = Integer.parseInt(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "id"));

                    if (id > maximumCharID) {
                        maximumCharID = id;
                    }

                    updateObligatoryCharIDs(obligatoryCharIDExists, id);

                    x  = Short.parseShort(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "x"));
                    y  = Short.parseShort(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "y"));
                    w  = Short.parseShort(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "width"));
                    h  = Short.parseShort(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "height"));
                    xo = Short.parseShort(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "xoffset"));
                    yo = Short.parseShort(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "yoffset"));
                    xa = Short.parseShort(kid.getAttributeValue(
                            KXmlParser.NO_NAMESPACE, "xadvance"));

                    if (w > maximumWidth) {
                        maximumWidth = w;
                    }

                    data.writeInt(id);
                    data.writeShort(x);
                    data.writeShort(y);
                    data.writeShort(w);
                    data.writeShort(h);
                    data.writeShort(xo);
                    data.writeShort(yo);
                    data.writeShort(xa);
                    charCount++;
                }
            }

            if (!allObligatoryCharsExist(obligatoryCharIDExists)) {
                Printer.println("ERROR: couldn't build the font. "
                        + "Missing Obligatory characters.\nAll fonts "
                        + "must have these " + obligatoryCharIDs.length + " characters: "
                        + listObligatoryChars(),
                        printOutput);
                return; //finally will be used, don't worry
            }

            /* try opening the output stream */
            try {
                FileOutputStream fo =
                    new FileOutputStream(fileOutName);

                /* try writing data to output */
                try {
                    DataOutputStream dataFile = new DataOutputStream(fo);

                    /* magic number */
                    dataFile.writeInt(MAGIC_NUMBER_ALBF);

                    /* write common font options */
                    dataFile.writeByte(linespacing);
                    dataFile.writeByte(lineheight);
                    dataFile.writeInt(maximumCharID);
                    dataFile.writeShort(maximumWidth);
                    dataFile.writeInt(charCount);

                    /* writing char data */
                    dataFile.write(bos.toByteArray());
                    Printer.println("Maximum width: " + maximumWidth,
                            printOutput);
                    Printer.println("Font built.\n", printOutput);
                } finally {
                    fo.close();
                }
            } finally {
                f.close();
            }
        } catch (IOException ioe) {
            Printer.println(FONT_DESCRIPTOR_ERROR_MSG, printOutput);
        }
    }

    private static void updateObligatoryCharIDs(
            boolean[] obligatoryCharIDExists, int id) {

        for (int i=0; i < obligatoryCharIDs.length; i++) {
            if (obligatoryCharIDs[i] == id) {
                obligatoryCharIDExists[i] = true;
            }
        }
    }

    private static boolean allObligatoryCharsExist(
            boolean[] obligatoryCharIDExists) {
        for (int i=0; i < obligatoryCharIDExists.length; i++) {
            if (obligatoryCharIDExists[i] == false) {
                return false;
            }
        }
        System.out.println("All chars are here!");
        return true;
    }

    private static String listObligatoryChars() {
        StringBuffer buffer = new StringBuffer();
        buffer.append('<');
        for (int i=0; i<obligatoryCharIDs.length; i++) {
            buffer.append((char)obligatoryCharIDs[i]);
        }
        buffer.append('>');

        return buffer.toString();
    }
}
