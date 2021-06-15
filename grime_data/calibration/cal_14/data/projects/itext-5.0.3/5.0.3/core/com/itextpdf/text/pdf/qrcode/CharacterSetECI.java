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
package com.itextpdf.text.pdf.qrcode;

import java.util.HashMap;

/**
 * Encapsulates a Character Set ECI, according to "Extended Channel Interpretations" 5.3.1.1
 * of ISO 18004.
 *
 * @author Sean Owen
 * @since 5.0.2
 */
public final class CharacterSetECI {

  private static HashMap<String,CharacterSetECI> NAME_TO_ECI;

  private static void initialize() {
    HashMap<String,CharacterSetECI> n = new HashMap<String, CharacterSetECI>(29);
    // TODO figure out if these values are even right!
    addCharacterSet(0, "Cp437", n);
    addCharacterSet(1, new String[] {"ISO8859_1", "ISO-8859-1"}, n);
    addCharacterSet(2, "Cp437", n);
    addCharacterSet(3, new String[] {"ISO8859_1", "ISO-8859-1"}, n);
    addCharacterSet(4, new String[] {"ISO8859_2", "ISO-8859-2"}, n);
    addCharacterSet(5, new String[] {"ISO8859_3", "ISO-8859-3"}, n);
    addCharacterSet(6, new String[] {"ISO8859_4", "ISO-8859-4"}, n);
    addCharacterSet(7, new String[] {"ISO8859_5", "ISO-8859-5"}, n);
    addCharacterSet(8, new String[] {"ISO8859_6", "ISO-8859-6"}, n);
    addCharacterSet(9, new String[] {"ISO8859_7", "ISO-8859-7"}, n);
    addCharacterSet(10, new String[] {"ISO8859_8", "ISO-8859-8"}, n);
    addCharacterSet(11, new String[] {"ISO8859_9", "ISO-8859-9"}, n);
    addCharacterSet(12, new String[] {"ISO8859_10", "ISO-8859-10"}, n);
    addCharacterSet(13, new String[] {"ISO8859_11", "ISO-8859-11"}, n);
    addCharacterSet(15, new String[] {"ISO8859_13", "ISO-8859-13"}, n);
    addCharacterSet(16, new String[] {"ISO8859_14", "ISO-8859-14"}, n);
    addCharacterSet(17, new String[] {"ISO8859_15", "ISO-8859-15"}, n);
    addCharacterSet(18, new String[] {"ISO8859_16", "ISO-8859-16"}, n);
    addCharacterSet(20, new String[] {"SJIS", "Shift_JIS"}, n);
    NAME_TO_ECI = n;
  }

  private final String encodingName;
  private final int value;

  private CharacterSetECI(int value, String encodingName) {
    this.encodingName = encodingName;
    this.value = value;
  }

  public String getEncodingName() {
    return encodingName;
  }

  public int getValue() {
    return value;
  }

  private static void addCharacterSet(int value, String encodingName, HashMap<String,CharacterSetECI> n) {
    CharacterSetECI eci = new CharacterSetECI(value, encodingName);
    n.put(encodingName, eci);
  }

  private static void addCharacterSet(int value, String[] encodingNames, HashMap<String,CharacterSetECI> n) {
    CharacterSetECI eci = new CharacterSetECI(value, encodingNames[0]);
    for (int i = 0; i < encodingNames.length; i++) {
      n.put(encodingNames[i], eci);
    }
  }

  /**
   * @param name character set ECI encoding name
   * @return {@link CharacterSetECI} representing ECI for character encoding, or null if it is legal
   *   but unsupported
   */
  public static CharacterSetECI getCharacterSetECIByName(String name) {
    if (NAME_TO_ECI == null) {
      initialize();
    }
    return NAME_TO_ECI.get(name);
  }

}