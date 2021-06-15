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
package com.itextpdf.text.pdf;

public class PdfTransition {
    /**
     *  Out Vertical Split
     */
    public static final int SPLITVOUT      = 1;
    /**
     *  Out Horizontal Split
     */
    public static final int SPLITHOUT      = 2;
    /**
     *  In Vertical Split
     */
    public static final int SPLITVIN      = 3;
    /**
     *  IN Horizontal Split
     */
    public static final int SPLITHIN      = 4;
    /**
     *  Vertical Blinds
     */
    public static final int BLINDV      = 5;
    /**
     *  Vertical Blinds
     */
    public static final int BLINDH      = 6;
    /**
     *  Inward Box
     */
    public static final int INBOX       = 7;
    /**
     *  Outward Box
     */
    public static final int OUTBOX      = 8;
    /**
     *  Left-Right Wipe
     */
    public static final int LRWIPE      = 9;
    /**
     *  Right-Left Wipe
     */
    public static final int RLWIPE     = 10;
    /**
     *  Bottom-Top Wipe
     */
    public static final int BTWIPE     = 11;
    /**
     *  Top-Bottom Wipe
     */
    public static final int TBWIPE     = 12;
    /**
     *  Dissolve
     */
    public static final int DISSOLVE    = 13;
    /**
     *  Left-Right Glitter
     */
    public static final int LRGLITTER   = 14;
    /**
     *  Top-Bottom Glitter
     */
    public static final int TBGLITTER  = 15;
    /**
     *  Diagonal Glitter
     */
    public static final int DGLITTER  = 16;
    
    /**
     *  duration of the transition effect
     */
    protected int duration;
    /**
     *  type of the transition effect
     */
    protected int type;
    
    /**
     *  Constructs a <CODE>Transition</CODE>.
     *
     */
    public PdfTransition() {
        this(BLINDH);
    }
    
    /**
     *  Constructs a <CODE>Transition</CODE>.
     *
     *@param  type      type of the transition effect
     */
    public PdfTransition(int type) {
        this(type,1);
    }
    
    /**
     *  Constructs a <CODE>Transition</CODE>.
     *
     *@param  type      type of the transition effect
     *@param  duration  duration of the transition effect
     */
    public PdfTransition(int type, int duration) {
        this.duration = duration;
        this.type = type;
    }
    
    
    public int getDuration() {
        return duration;
    }
    
    
    public int getType() {
        return type;
    }
    
    public PdfDictionary getTransitionDictionary() {
        PdfDictionary trans = new PdfDictionary(PdfName.TRANS);
        switch (type) {
            case SPLITVOUT:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.V);
                trans.put(PdfName.M,PdfName.O);
                break;
            case SPLITHOUT:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.H);
                trans.put(PdfName.M,PdfName.O);
                break;
            case SPLITVIN:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.V);
                trans.put(PdfName.M,PdfName.I);
                break;
            case SPLITHIN:
                trans.put(PdfName.S,PdfName.SPLIT);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.H);
                trans.put(PdfName.M,PdfName.I);
                break;
            case BLINDV:
                trans.put(PdfName.S,PdfName.BLINDS);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.V);
                break;
            case BLINDH:
                trans.put(PdfName.S,PdfName.BLINDS);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DM,PdfName.H);
                break;
            case INBOX:
                trans.put(PdfName.S,PdfName.BOX);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.M,PdfName.I);
                break;
            case OUTBOX:
                trans.put(PdfName.S,PdfName.BOX);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.M,PdfName.O);
                break;
            case LRWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(0));
                break;
            case RLWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(180));
                break;
            case BTWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(90));
                break;
            case TBWIPE:
                trans.put(PdfName.S,PdfName.WIPE);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(270));
                break;
            case DISSOLVE:
                trans.put(PdfName.S,PdfName.DISSOLVE);
                trans.put(PdfName.D,new PdfNumber(duration));
                break;
            case LRGLITTER:
                trans.put(PdfName.S,PdfName.GLITTER);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(0));
                break;
            case TBGLITTER:
                trans.put(PdfName.S,PdfName.GLITTER);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(270));
                break;
            case DGLITTER:
                trans.put(PdfName.S,PdfName.GLITTER);
                trans.put(PdfName.D,new PdfNumber(duration));
                trans.put(PdfName.DI,new PdfNumber(315));
                break;
        }
        return trans;
    }
}

