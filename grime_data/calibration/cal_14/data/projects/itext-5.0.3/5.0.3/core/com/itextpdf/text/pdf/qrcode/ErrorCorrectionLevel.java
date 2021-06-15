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

/**
 * <p>See ISO 18004:2006, 6.5.1. This enum encapsulates the four error correction levels
 * defined by the QR code standard.</p>
 *
 * @author Sean Owen
 * @since 5.0.2
 */
public final class ErrorCorrectionLevel {

  // No, we can't use an enum here. J2ME doesn't support it.

  /**
   * L = ~7% correction
   */
  public static final ErrorCorrectionLevel L = new ErrorCorrectionLevel(0, 0x01, "L");
  /**
   * M = ~15% correction
   */
  public static final ErrorCorrectionLevel M = new ErrorCorrectionLevel(1, 0x00, "M");
  /**
   * Q = ~25% correction
   */
  public static final ErrorCorrectionLevel Q = new ErrorCorrectionLevel(2, 0x03, "Q");
  /**
   * H = ~30% correction
   */
  public static final ErrorCorrectionLevel H = new ErrorCorrectionLevel(3, 0x02, "H");

  private static final ErrorCorrectionLevel[] FOR_BITS = {M, L, H, Q};

  private final int ordinal;
  private final int bits;
  private final String name;

  private ErrorCorrectionLevel(int ordinal, int bits, String name) {
    this.ordinal = ordinal;
    this.bits = bits;
    this.name = name;
  }

  public int ordinal() {
    return ordinal;
  }

  public int getBits() {
    return bits;
  }

  public String getName() {
    return name;
  }

  public String toString() {
    return name;
  }

  /**
   * @param bits int containing the two bits encoding a QR Code's error correction level
   * @return {@link ErrorCorrectionLevel} representing the encoded error correction level
   */
  public static ErrorCorrectionLevel forBits(int bits) {
    if (bits < 0 || bits >= FOR_BITS.length) {
      throw new IllegalArgumentException();
    }
    return FOR_BITS[bits];
  }


}
