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
package com.itextpdf.text.error_messages;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;

import com.itextpdf.text.pdf.BaseFont;

/**
 * Localizes error messages. The messages are located in the package
 * com.itextpdf.text.error_messages in the form language_country.lng.
 * The internal file encoding is UTF-8 without any escape chars, it's not a
 * normal property file. See en.lng for more information on the internal format.
 * @author Paulo Soares (psoares@glintt.com)
 */
public final class MessageLocalization {
    private static HashMap<String, String> defaultLanguage = new HashMap<String, String>();
    private static HashMap<String, String> currentLanguage;
    private static final String BASE_PATH = "com/itextpdf/text/error_messages/";

    private MessageLocalization() {
    }

    static {
        try {
            defaultLanguage = getLanguageMessages("en", null);
        } catch (Exception ex) {
            // do nothing
        }
        if (defaultLanguage == null)
            defaultLanguage = new HashMap<String, String>();
    }

    /**
     * Get a message without parameters.
     * @param key the key to the message
     * @return the message
     */
    public static String getMessage(String key) {
        HashMap<String, String> cl = currentLanguage;
        String val;
        if (cl != null) {
            val = cl.get(key);
            if (val != null)
                return val;
        }
        cl = defaultLanguage;
        val = cl.get(key);
        if (val != null)
            return val;
        return "No message found for " + key;
    }

    /**
     * Get a message without parameters.
     * @param key the key to the message
     * @return the message
     */
    public static String getComposedMessage(String key) {
        return getComposedMessage(key, null, null, null, null);
    }

    /**
     * Get a message with one parameter. The parameter will replace the string
     * "{1}" found in the message.
     * @param key the key to the message
     * @param p1 the parameter
     * @return the message
     */
    public static String getComposedMessage(String key, Object p1) {
        return getComposedMessage(key, p1, null, null, null);
    }

    /**
     * Get a message with one parameter. The parameter will replace the string
     * "{1}" found in the message.
     * @param key the key to the message
     * @param p1 the parameter
     * @return the message
     */
    public static String getComposedMessage(String key, int p1) {
        return getComposedMessage(key, String.valueOf(p1), null, null, null);
    }

    /**
     * Get a message with one parameter. The parameter will replace the string
     * "{1}", "{2}" found in the message.
     * @param key the key to the message
     * @param p1 the parameter
     * @param p2 the parameter
     * @return the message
     */
    public static String getComposedMessage(String key, Object p1, Object p2) {
        return getComposedMessage(key, p1, p2, null, null);
    }

    /**
     * Get a message with one parameter. The parameter will replace the string
     * "{1}", "{2}", "{3}" found in the message.
     * @param key the key to the message
     * @param p1 the parameter
     * @param p2 the parameter
     * @param p3 the parameter
     * @return the message
     */
    public static String getComposedMessage(String key, Object p1, Object p2, Object p3) {
        return getComposedMessage(key, p1, p2, p3, null);
    }

    /**
     * Get a message with two parameters. The parameters will replace the strings
     * "{1}", "{2}", "{3}", "{4}" found in the message.
     * @param key the key to the message
     * @param p1 the parameter
     * @param p2 the parameter
     * @param p3 the parameter
     * @param p4 the parameter
     * @return the message
     */
    public static String getComposedMessage(String key, Object p1, Object p2, Object p3, Object p4) {
        String msg = getMessage(key);
        if (p1 != null) {
            msg = msg.replaceAll("\\{1\\}", p1.toString());
        }
        if (p2 != null) {
            msg = msg.replaceAll("\\{2\\}", p2.toString());
        }
        if (p3 != null) {
            msg = msg.replaceAll("\\{3\\}", p3.toString());
        }
        if (p4 != null) {
            msg = msg.replaceAll("\\{4\\}", p4.toString());
        }
        return msg;
    }

    /**
     * Sets the language to be used globally for the error messages. The language
     * is a two letter lowercase country designation like "en" or "pt". The country
     * is an optional two letter uppercase code like "US" or "PT".
     * @param language the language
     * @param country the country
     * @return true if the language was found, false otherwise
     * @throws IOException on error
     */
    public static boolean setLanguage(String language, String country) throws IOException {
        HashMap<String, String> lang = getLanguageMessages(language, country);
        if (lang == null)
            return false;
        currentLanguage = lang;
        return true;
    }

    /**
     * Sets the error messages directly from a Reader.
     * @param r the Reader
     * @throws IOException on error
     */
    public static void setMessages(Reader r) throws IOException {
        currentLanguage = readLanguageStream(r);
    }

    private static HashMap<String, String> getLanguageMessages(String language, String country) throws IOException {
        if (language == null)
            throw new IllegalArgumentException("The language cannot be null.");
        InputStream is = null;
        try {
            String file;
            if (country != null)
                file = language + "_" + country + ".lng";
            else
                file = language + ".lng";
            is = BaseFont.getResourceStream(BASE_PATH + file, new MessageLocalization().getClass().getClassLoader());
            if (is != null)
                return readLanguageStream(is);
            if (country == null)
                return null;
            file = language + ".lng";
            is = BaseFont.getResourceStream(BASE_PATH + file, new MessageLocalization().getClass().getClassLoader());
            if (is != null)
                return readLanguageStream(is);
            else
                return null;
        }
        finally {
            try {
                is.close();
            } catch (Exception exx) {
            }
            // do nothing
        }
    }

    private static HashMap<String, String> readLanguageStream(InputStream is) throws IOException {
        return readLanguageStream(new InputStreamReader(is, "UTF-8"));
    }

    private static HashMap<String, String> readLanguageStream(Reader r) throws IOException {
        HashMap<String, String> lang = new HashMap<String, String>();
        BufferedReader br = new BufferedReader(r);
        String line;
        while ((line = br.readLine()) != null) {
            int idxeq = line.indexOf('=');
            if (idxeq < 0)
                continue;
            String key = line.substring(0, idxeq).trim();
            if (key.startsWith("#"))
                continue;
            lang.put(key, line.substring(idxeq + 1));
        }
        return lang;
    }
}
