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
 * @author satorux@google.com (Satoru Takabayashi) - creator
 * @author dswitkin@google.com (Daniel Switkin) - ported from C++
 * @since 5.0.2
 */
public final class QRCode {

  public static final int NUM_MASK_PATTERNS = 8;

  private Mode mode;
  private ErrorCorrectionLevel ecLevel;
  private int version;
  private int matrixWidth;
  private int maskPattern;
  private int numTotalBytes;
  private int numDataBytes;
  private int numECBytes;
  private int numRSBlocks;
  private ByteMatrix matrix;

  public QRCode() {
    mode = null;
    ecLevel = null;
    version = -1;
    matrixWidth = -1;
    maskPattern = -1;
    numTotalBytes = -1;
    numDataBytes = -1;
    numECBytes = -1;
    numRSBlocks = -1;
    matrix = null;
  }

  // Mode of the QR Code.
  public Mode getMode() {
    return mode;
  }

  // Error correction level of the QR Code.
  public ErrorCorrectionLevel getECLevel() {
    return ecLevel;
  }

  // Version of the QR Code.  The bigger size, the bigger version.
  public int getVersion() {
    return version;
  }

  // ByteMatrix width of the QR Code.
  public int getMatrixWidth() {
    return matrixWidth;
  }

  // Mask pattern of the QR Code.
  public int getMaskPattern() {
    return maskPattern;
  }

  // Number of total bytes in the QR Code.
  public int getNumTotalBytes() {
    return numTotalBytes;
  }

  // Number of data bytes in the QR Code.
  public int getNumDataBytes() {
    return numDataBytes;
  }

  // Number of error correction bytes in the QR Code.
  public int getNumECBytes() {
    return numECBytes;
  }

  // Number of Reedsolomon blocks in the QR Code.
  public int getNumRSBlocks() {
    return numRSBlocks;
  }

  // ByteMatrix data of the QR Code.
  public ByteMatrix getMatrix() {
    return matrix;
  }
  

  // Return the value of the module (cell) pointed by "x" and "y" in the matrix of the QR Code. They
  // call cells in the matrix "modules". 1 represents a black cell, and 0 represents a white cell.
  public int at(int x, int y) {
    // The value must be zero or one.
    int value = matrix.get(x, y);
    if (!(value == 0 || value == 1)) {
      // this is really like an assert... not sure what better exception to use?
      throw new RuntimeException("Bad value");
    }
    return value;
  }

  // Checks all the member variables are set properly. Returns true on success. Otherwise, returns
  // false.
  public boolean isValid() {
    return
        // First check if all version are not uninitialized.
        mode != null &&
        ecLevel != null &&
        version != -1 &&
        matrixWidth != -1 &&
        maskPattern != -1 &&
        numTotalBytes != -1 &&
        numDataBytes != -1 &&
        numECBytes != -1 &&
        numRSBlocks != -1 &&
        // Then check them in other ways..
        isValidMaskPattern(maskPattern) &&
        numTotalBytes == numDataBytes + numECBytes &&
        // ByteMatrix stuff.
        matrix != null &&
        matrixWidth == matrix.getWidth() &&
        // See 7.3.1 of JISX0510:2004 (p.5).
        matrix.getWidth() == matrix.getHeight(); // Must be square.
  }

  // Return debug String.
  public String toString() {
    StringBuffer result = new StringBuffer(200);
    result.append("<<\n");
    result.append(" mode: ");
    result.append(mode);
    result.append("\n ecLevel: ");
    result.append(ecLevel);
    result.append("\n version: ");
    result.append(version);
    result.append("\n matrixWidth: ");
    result.append(matrixWidth);
    result.append("\n maskPattern: ");
    result.append(maskPattern);
    result.append("\n numTotalBytes: ");
    result.append(numTotalBytes);
    result.append("\n numDataBytes: ");
    result.append(numDataBytes);
    result.append("\n numECBytes: ");
    result.append(numECBytes);
    result.append("\n numRSBlocks: ");
    result.append(numRSBlocks);
    if (matrix == null) {
      result.append("\n matrix: null\n");
    } else {
      result.append("\n matrix:\n");
      result.append(matrix.toString());
    }
    result.append(">>\n");
    return result.toString();
  }

  public void setMode(Mode value) {
    mode = value;
  }

  public void setECLevel(ErrorCorrectionLevel value) {
    ecLevel = value;
  }

  public void setVersion(int value) {
    version = value;
  }

  public void setMatrixWidth(int value) {
    matrixWidth = value;
  }

  public void setMaskPattern(int value) {
    maskPattern = value;
  }

  public void setNumTotalBytes(int value) {
    numTotalBytes = value;
  }

  public void setNumDataBytes(int value) {
    numDataBytes = value;
  }

  public void setNumECBytes(int value) {
    numECBytes = value;
  }

  public void setNumRSBlocks(int value) {
    numRSBlocks = value;
  }

  // This takes ownership of the 2D array.
  public void setMatrix(ByteMatrix value) {
    matrix = value;
  }

  // Check if "mask_pattern" is valid.
  public static boolean isValidMaskPattern(int maskPattern) {
    return maskPattern >= 0 && maskPattern < NUM_MASK_PATTERNS;
  }

  // Return true if the all values in the matrix are binary numbers.
  //
  // JAVAPORT: This is going to be super expensive and unnecessary, we should not call this in
  // production. I'm leaving it because it may be useful for testing. It should be removed entirely
  // if ByteMatrix is changed never to contain a -1.
  /*
  private static boolean EverythingIsBinary(final ByteMatrix matrix) {
    for (int y = 0; y < matrix.height(); ++y) {
      for (int x = 0; x < matrix.width(); ++x) {
        int value = matrix.get(y, x);
        if (!(value == 0 || value == 1)) {
          // Found non zero/one value.
          return false;
        }
      }
    }
    return true;
  }
   */

}
