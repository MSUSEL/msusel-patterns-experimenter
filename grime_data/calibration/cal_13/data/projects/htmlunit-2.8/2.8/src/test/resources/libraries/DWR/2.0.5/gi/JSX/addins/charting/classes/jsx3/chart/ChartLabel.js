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
jsx3.require("jsx3.chart.ChartComponent");jsx3.Class.defineClass("jsx3.chart.ChartLabel",jsx3.chart.ChartComponent,null,function(j,p){var mc=jsx3.vector;j.DEFAULT_WIDTH=100;j.ROTATION_NORMAL=0;j.ROTATION_CW=90;j.ROTATION_CCW=270;p.init=function(n,s){this.jsxsuper(n);this.jsxtext=s;this.alpha=null;this.borderStroke=null;this.preferredWidth=null;this.preferredHeight=null;this.labelRotation=j.ROTATION_NORMAL;};p.getText=function(){return this.jsxtext;};p.setText=function(i){this.jsxtext=i;};p.getPreferredWidth=function(){if(this.preferredWidth!=null){return this.preferredWidth;}else{if(this.isRotated()){return this.In();}else{var Dc=this.getPaddingDimensions();return j.DEFAULT_WIDTH+Dc[0]+Dc[2];}}};p.setPreferredWidth=function(m){this.preferredWidth=m;};p.getPreferredHeight=function(){if(this.preferredHeight!=null){return this.preferredHeight;}else{if(this.isRotated()){var Ub=this.getPaddingDimensions();return j.DEFAULT_WIDTH+Ub[1]+Ub[3];}else{return this.In();}}};p.setPreferredHeight=function(c){this.preferredHeight=c;};p.In=function(){var N=this.getPaddingDimensions();var Qb=this.getFontSize()!=null?this.getFontSize():10;return Math.round(Qb*1.5)+(this.isRotated()?N[1]+N[3]:N[0]+N[2]);};p.getAlpha=function(){return this.alpha;};p.setAlpha=function(f){this.alpha=f!=null?mc.F0(f):null;};p.getBorderStroke=function(){return this.borderStroke;};p.setBorderStroke=function(m){this.borderStroke=m;};p.getLabelRotation=function(){return this.labelRotation;};p.setLabelRotation=function(q){this.labelRotation=q;};p.isRotated=function(){return this.labelRotation==j.ROTATION_CW||this.labelRotation==j.ROTATION_CCW;};p.updateView=function(){this.jsxsuper();var Vb=this.l5();var lc=this.getWidth();var Y=this.getHeight();var gb=this.getPaddingDimensions();this.A3();var ib=new mc.Rectangle(0,0,lc,Y);Vb.appendChild(ib);this.AV(ib);var T=ib.getFill();var Eb=mc.Stroke.valueOf(this.borderStroke);if(Eb!=null){ib.setStroke(Eb);}else{if(T!=null&&(this.alpha==null||this.alpha==1)){ib.setStroke(new mc.Stroke(T.getColor()));}}var O=0,Bc=0,Ub=0,hb=0;if(this.isRotated()){Ub=(hb=Math.round(gb[3]+(lc-gb[1]-gb[3])/2));if(this.labelRotation==j.ROTATION_CW){Bc=Y;}else{O=Y;}}else{O=(Bc=Math.round(Y/2));Ub=0;hb=lc;}var Lb=new mc.TextLine(Ub,O,hb,Bc,this.jsxtext);Lb.setColor(this.getColor());Lb.setClassName(this.getClassName());Lb.setFontFamily(this.jsxfontname);Lb.setFontWeight(this.jsxfontweight);Lb.setFontSize(this.jsxfontsize);Lb.setTextAlign(this.jsxtextalign);Vb.appendChild(Lb);};p.onResize=function(){var F=this.getParent();if(F!=null){if(jsx3.chart.Axis&&F instanceof jsx3.chart.Axis)this.getChart().repaint();else F.repaint();}};p.onSetChild=function(){return false;};p.onSetParent=function(r){return r instanceof jsx3.chart.ChartComponent;};p.toString=function(){return "[jsx3.chart.ChartLabel '"+this.getName()+"']";};j.getVersion=function(){return jsx3.chart.q2;};});
