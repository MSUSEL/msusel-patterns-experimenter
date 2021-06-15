/**
 * The MIT License (MIT)
 *
 * MSUSEL Arc Framework
 * Copyright (c) 2015-2019 Montana State University, Gianforte School of Computing,
 * Software Engineering Laboratory and Idaho State University, Informatics and
 * Computer Science, Empirical Software Engineering Laboratory
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.itextpdf.text;

import java.util.Properties;
import java.util.Set;

import com.itextpdf.text.error_messages.MessageLocalization;
import com.itextpdf.text.pdf.BaseFont;

/**
 * If you are using True Type fonts, you can declare the paths of the different ttf- and ttc-files
 * to this static class first and then create fonts in your code using one of the static getFont-method
 * without having to enter a path as parameter.
 *
 * @author  Bruno Lowagie
 */

public final class FontFactory {

/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER = BaseFont.COURIER;

/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_BOLD = BaseFont.COURIER_BOLD;

/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_OBLIQUE = BaseFont.COURIER_OBLIQUE;

/** This is a possible value of a base 14 type 1 font */
    public static final String COURIER_BOLDOBLIQUE = BaseFont.COURIER_BOLDOBLIQUE;

/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA = BaseFont.HELVETICA;

/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_BOLD = BaseFont.HELVETICA_BOLD;

/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_OBLIQUE = BaseFont.HELVETICA_OBLIQUE;

/** This is a possible value of a base 14 type 1 font */
    public static final String HELVETICA_BOLDOBLIQUE = BaseFont.HELVETICA_BOLDOBLIQUE;

/** This is a possible value of a base 14 type 1 font */
    public static final String SYMBOL = BaseFont.SYMBOL;

/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES = "Times";

/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_ROMAN = BaseFont.TIMES_ROMAN;

/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_BOLD = BaseFont.TIMES_BOLD;

/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_ITALIC = BaseFont.TIMES_ITALIC;

/** This is a possible value of a base 14 type 1 font */
    public static final String TIMES_BOLDITALIC = BaseFont.TIMES_BOLDITALIC;

/** This is a possible value of a base 14 type 1 font */
    public static final String ZAPFDINGBATS = BaseFont.ZAPFDINGBATS;

    private static FontFactoryImp fontImp = new FontFactoryImp();

/** This is the default encoding to use. */
    public static String defaultEncoding = BaseFont.WINANSI;

/** This is the default value of the <VAR>embedded</VAR> variable. */
    public static boolean defaultEmbedding = BaseFont.NOT_EMBEDDED;

/** Creates new FontFactory */
    private FontFactory() {
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color) {
        return fontImp.getFont(fontname, encoding, embedded, size, style, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @param	cached 		true if the font comes from the cache or is added to
 * 				the cache if new, false if the font is always created new
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style, BaseColor color, boolean cached) {
        return fontImp.getFont(fontname, encoding, embedded, size, style, color, cached);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param   attributes  the attributes of a <CODE>Font</CODE> object.
 * @return the Font constructed based on the attributes
 */

    public static Font getFont(Properties attributes) {
        fontImp.defaultEmbedding = defaultEmbedding;
        fontImp.defaultEncoding = defaultEncoding;
        return fontImp.getFont(attributes);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, boolean embedded, float size, int style) {
        return getFont(fontname, encoding, embedded, size, style, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @param	size	    the size of this font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, boolean embedded, float size) {
        return getFont(fontname, encoding, embedded, size, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param       embedded    true if the font is to be embedded in the PDF
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, boolean embedded) {
        return getFont(fontname, encoding, embedded, Font.UNDEFINED, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, float size, int style, BaseColor color) {
        return getFont(fontname, encoding, defaultEmbedding, size, style, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, float size, int style) {
        return getFont(fontname, encoding, defaultEmbedding, size, style, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @param	size	    the size of this font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding, float size) {
        return getFont(fontname, encoding, defaultEmbedding, size, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	encoding    the encoding of the font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, String encoding) {
        return getFont(fontname, encoding, defaultEmbedding, Font.UNDEFINED, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, float size, int style, BaseColor color) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	color	    the <CODE>BaseColor</CODE> of this font.
 * @return the Font constructed based on the parameters
 * @since 2.1.0
 */

    public static Font getFont(String fontname, float size, BaseColor color) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, Font.UNDEFINED, color);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @param	style	    the style of this font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, float size, int style) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, style, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @param	size	    the size of this font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname, float size) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, size, Font.UNDEFINED, null);
    }

/**
 * Constructs a <CODE>Font</CODE>-object.
 *
 * @param	fontname    the name of the font
 * @return the Font constructed based on the parameters
 */

    public static Font getFont(String fontname) {
        return getFont(fontname, defaultEncoding, defaultEmbedding, Font.UNDEFINED, Font.UNDEFINED, null);
    }

    /**
     * Register a font by giving explicitly the font family and name.
     * @param familyName the font family
     * @param fullName the font name
     * @param path the font path
     */
    public static void registerFamily(String familyName, String fullName, String path) {
        fontImp.registerFamily(familyName, fullName, path);
    }

/**
 * Register a ttf- or a ttc-file.
 *
 * @param   path    the path to a ttf- or ttc-file
 */

    public static void register(String path) {
        register(path, null);
    }

/**
 * Register a font file and use an alias for the font contained in it.
 *
 * @param   path    the path to a font file
 * @param   alias   the alias you want to use for the font
 */

    public static void register(String path, String alias) {
        fontImp.register(path, alias);
    }

    /** Register all the fonts in a directory.
     * @param dir the directory
     * @return the number of fonts registered
     */
    public static int registerDirectory(String dir) {
        return fontImp.registerDirectory(dir);
    }

    /**
     * Register all the fonts in a directory and possibly its subdirectories.
     * @param dir the directory
     * @param scanSubdirectories recursively scan subdirectories if <code>true</true>
     * @return the number of fonts registered
     * @since 2.1.2
     */
    public static int registerDirectory(String dir, boolean scanSubdirectories) {
        return fontImp.registerDirectory(dir, scanSubdirectories);
    }

    /** Register fonts in some probable directories. It usually works in Windows,
     * Linux and Solaris.
     * @return the number of fonts registered
     */
    public static int registerDirectories() {
        return fontImp.registerDirectories();
    }

/**
 * Gets a set of registered fontnames.
 * @return a set of registered fonts
 */

    public static Set<String> getRegisteredFonts() {
        return fontImp.getRegisteredFonts();
    }

/**
 * Gets a set of registered fontnames.
 * @return a set of registered font families
 */

    public static Set<String> getRegisteredFamilies() {
        return fontImp.getRegisteredFamilies();
    }

/**
 * Gets a set of registered fontnames.
 * @param fontname of a font that may or may not be registered
 * @return true if a given font is registered
 */

    public static boolean contains(String fontname) {
        return fontImp.isRegistered(fontname);
    }

/**
 * Checks if a certain font is registered.
 *
 * @param   fontname    the name of the font that has to be checked.
 * @return  true if the font is found
 */

    public static boolean isRegistered(String fontname) {
        return fontImp.isRegistered(fontname);
    }

    /**
     * Gets the font factory implementation.
     * @return the font factory implementation
     */
    public static FontFactoryImp getFontImp() {
        return fontImp;
    }

    /**
     * Sets the font factory implementation.
     * @param fontImp the font factory implementation
     */
    public static void setFontImp(FontFactoryImp fontImp) {
        if (fontImp == null)
            throw new NullPointerException(MessageLocalization.getComposedMessage("fontfactoryimp.cannot.be.null"));
        FontFactory.fontImp = fontImp;
    }
}
