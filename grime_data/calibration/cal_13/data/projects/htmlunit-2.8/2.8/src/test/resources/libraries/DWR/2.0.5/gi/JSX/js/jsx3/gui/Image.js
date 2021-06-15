/*
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
jsx3.require("jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Image",jsx3.gui.Block,[],function(s,h){h.paint=function(){this.applyDynamicProperties();var V=this.RL(true);var Dc=this.getUriResolver().resolveURI(this.jsxsrc);var Nb=this.getWidth()!=null?" width=\""+V.XK()+"\"":"";var F=this.getHeight()!=null?" height=\""+V.P5()+"\"":"";return this.jsxsuper("<img unselectable=\"on\" class=\"jsx30image\" src=\""+Dc+"\""+Nb+F+"/>");};h.onSetChild=function(p){return false;};h.k7=function(p,m,b){this.B_(p,m,b,1);};h.getRenderedWidth=function(){var qc=this.getRendered();return qc&&qc.childNodes[0]?qc.childNodes[0].width:null;};h.getRenderedHeight=function(){var Eb=this.getRendered();return Eb&&Eb.childNodes[0]?Eb.childNodes[0].height:null;};h.getSrc=function(){return this.jsxsrc;};h.setSrc=function(b){this.jsxsrc=b;return this;};});
