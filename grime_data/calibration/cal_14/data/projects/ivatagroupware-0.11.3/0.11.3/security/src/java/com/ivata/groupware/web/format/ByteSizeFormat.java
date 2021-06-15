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
package com.ivata.groupware.web.format;

import com.ivata.mask.util.StringHandling;
import com.ivata.mask.web.format.HTMLFormat;


/**
 * <p>Format the size of a file or other object as bytes, kb, or Mb
 * depending if the number is less than a kilobyte, or less than a
 * megabyte in size..</p>
 *
 * @since 2002-09-18
 * @author Colin MacLeod
 * <a href='mailto:colin.macleod@ivata.com'>colin.macleod@ivata.com</a>
 * @version $Revision: 1.3 $
 */
public class ByteSizeFormat implements HTMLFormat {
    /**
     * <p>The size under which the number appears just as it is, a raw
     * number. If the numer is greater than this size (but less than
     * <code>megaSize</code>) it will be displayed as a multiple of this
     * number.</p>
     */
    static final int kiloSize = 1024;
    /**
     * <p>The size above which the number appears as a multiple of this
     * number, a multiple of <i>megabytes</i>.</p>
     */
    static final int megaSize = 1048576;
    /**
     * <p>The number of decimal places to show for <i>kilobyte</i> or
     * <i>megabyte</i> sizes.</p>
     */
    private int decimals = 1;
    /**
     * <p>String to append to the number if it is expressed in
     * <i>bytes</i>.</p>
     */
    private String byteUnits = "";
    /**
     * <p>String to append to the number if it is expressed in
     * <i>kilobytes</i>.</p>
     */
    private String kiloUnits = "k";
    /**
     * <p>String to append to the number if it is expressed in
     * <i>Megabytes</i>.</p>
     */
    private String megaUnits = "M";
    /**
     * <p>The character to use as a decimal point.</p>
     */
    private char decimalCharacter = '.';

    /**
     * <p>Convert  the number the given string represents according to
     * whether or not the number represents just <i>bytes</i>,
     * <i>kilobytes</i> or <i>megabytes<i>.</p>
     *
     * @param byteTextParam string number to convert.
     * @return a string representing the size, expressed in the
     * appropriate units.
     */
    public String format(final String byteTextParam) {
        // first convert the string to an integer
        Integer bytesInteger = StringHandling.integerValue(byteTextParam);
        int bytes;
        String byteText = byteTextParam;
        if(bytesInteger == null) {
            bytes = 0;
        } else {
            bytes = bytesInteger.intValue();
        }
        // is this less than the kilo size?
        if(bytes < kiloSize) {
            byteText = bytes + byteUnits;
        } else {
            // both the kilo and mega ranges need to do rounding
            int rounding = 10;
            for(int exp = 0; exp < decimals; ++exp) {
                rounding *= 10;
            }
            long newValue = (long) bytes * rounding;
            String units;
            // if this is in the kilo range, divide by that amount
            if(bytes < megaSize) {
                newValue /= kiloSize;
                units = kiloUnits;
            } else {
                newValue /= megaSize;
                units = megaUnits;
            }
            // the clever bit - this does the rounding up or down whichever is
            // nearest
            newValue += 5;
            newValue /= 10;

            // now to add the decimal point
            String noDecimal = Long.toString(newValue);
            int length = noDecimal.length();
            byteText = noDecimal.substring(0, length - decimals)
                + decimalCharacter
                + noDecimal.substring(length - decimals)
                + units;
        }
        return byteText;
    }

    /**
     * <p>The number of decimal places to show for <i>kilobyte</i> or
     * <i>megabyte</i> sizes.</p>
     *
     * @return the current value of decimal places.
     */
    public final int getDecimals() {
        return decimals;
    }

    /**
     * <p>The number of decimal places to show for <i>kilobyte</i> or
     * <i>megabyte</i> sizes.</p>
     *
     * @param decimals the new value of decimal places to display.
     */
    public final void setDecimals(final int decimals) {
        this.decimals = decimals;
    }

    /**
     * <p>String to append to the number if it is expressed in
     * <i>bytes</i>.</p>
     *
     * @return the current value of byte units.
     */
    public final String getByteUnits() {
        return byteUnits;
    }

    /**
     * <p>String to append to the number if it is expressed in
     * <i>bytes</i>.</p>
     *
     * @param byteUnits the new value of units to append to numbers
     * expressed in bytes.
     */
    public final void setByteUnits(final String byteUnits) {
        this.byteUnits = byteUnits;
    }

    /**
     * <p>String to append to the number if it is expressed in
     * <i>kilobytes</i>.</p>
     *
     * @return the current value of kilo units.
     */
    public final String getKiloUnits() {
        return kiloUnits;
    }

    /**
     * <p>String to append to the number if it is expressed in
     * <i>kilobytes</i>.</p>
     *
     * @param kiloUnits the new value of units to append to numbers
     * expressed in kilobytes.
     */
    public final void setKiloUnits(final String kiloUnits) {
        this.kiloUnits = kiloUnits;
    }

    /**
     * <p>String to append to the number if it is expressed in
     * <i>Megabytes</i>.</p>
     *
     * @return the current value of mega units.
     */
    public final String getMegaUnits() {
        return megaUnits;
    }

    /**
     * <p>String to append to the number if it is expressed in
     * <i>Megabytes</i>.</p>
     *
     * @param megaUnits the new value of units to append to numbers
     * expressed in megabytes.
     */
    public final void setMegaUnits(final String megaUnits) {
        this.megaUnits = megaUnits;
    }

    /**
     * <p>The character to use as a decimal point.</p>
     *
     * @return the current value of decimal character.
     */
    public final char getDecimalCharacter() {
        return decimalCharacter;
    }

    /**
     * <p>The character to use as a decimal point.</p>
     *
     * @param decimalCharacter the new value of decimal character.
     */
    public final void setDecimalCharacter(final char decimalCharacter) {
        this.decimalCharacter = decimalCharacter;
    }
}
