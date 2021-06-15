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
package org.apache.hadoop.examples.dancing;

/**
 * Of the "normal" 12 pentominos, 6 of them have distinct shapes when flipped.
 * This class includes both variants of the "flippable" shapes and the
 * unflippable shapes for a total of 18 pieces. Clearly, the boards must have
 * 18*5=90 boxes to hold all of the solutions.
 */
public class OneSidedPentomino extends Pentomino {

  public OneSidedPentomino() {}

  public OneSidedPentomino(int width, int height) {
    super(width, height);
  }

  /**
   * Define the one sided pieces. The flipped pieces have the same name with
   * a capital letter.
   */
  protected void initializePieces() {
    pieces.add(new Piece("x", " x /xxx/ x ", false, oneRotation));
    pieces.add(new Piece("v", "x  /x  /xxx", false, fourRotations));
    pieces.add(new Piece("t", "xxx/ x / x ", false, fourRotations));
    pieces.add(new Piece("w", "  x/ xx/xx ", false, fourRotations));
    pieces.add(new Piece("u", "x x/xxx", false, fourRotations));
    pieces.add(new Piece("i", "xxxxx", false, twoRotations));
    pieces.add(new Piece("f", " xx/xx / x ", false, fourRotations));
    pieces.add(new Piece("p", "xx/xx/x ", false, fourRotations));
    pieces.add(new Piece("z", "xx / x / xx", false, twoRotations));
    pieces.add(new Piece("n", "xx  / xxx", false, fourRotations));
    pieces.add(new Piece("y", "  x /xxxx", false, fourRotations));
    pieces.add(new Piece("l", "   x/xxxx", false, fourRotations));
    pieces.add(new Piece("F", "xx / xx/ x ", false, fourRotations));
    pieces.add(new Piece("P", "xx/xx/ x", false, fourRotations));
    pieces.add(new Piece("Z", " xx/ x /xx ", false, twoRotations));
    pieces.add(new Piece("N", "  xx/xxx ", false, fourRotations));
    pieces.add(new Piece("Y", " x  /xxxx", false, fourRotations));
    pieces.add(new Piece("L", "x   /xxxx", false, fourRotations));
  }
  
  /**
   * Solve the 3x30 puzzle.
   * @param args
   */
  public static void main(String[] args) {
    Pentomino model = new OneSidedPentomino(3, 30);
    int solutions = model.solve();
    System.out.println(solutions + " solutions found.");
  }

}
