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
jsx3.require("jsx3.gui.LayoutGrid","jsx3.gui.Stack");jsx3.Class.defineClass("jsx3.gui.StackGroup",jsx3.gui.LayoutGrid,null,function(c,p){var jc=jsx3.gui.LayoutGrid;c.ORIENTATIONV=jc.ORIENTATIONCOL;c.ORIENTATIONH=jc.ORIENTATIONROW;c.DEFAULTSTACKCOUNT=2;c.DEFAULTDIMENSIONS=["27","*"];c.DEFAULTBARSIZE=27;p.init=function(h,q,l,f,d){this.jsxsuper(h,q,l,f,d);this.setRepeat(c.DEFAULTSTACKCOUNT);this.setDimensionArray(c.DEFAULTDIMENSIONS);};p.getBarSize=function(){return this.jsxbarsize;};p.setBarSize=function(r){this.jsxbarsize=r;return this;};p.IO=function(a){var v=this.getParent().IO(this);var zb=v.width?v.width:v.parentwidth;var _=v.height?v.height:v.parentheight;var Zb=this.paintBarSize();var lc=a.getChildIndex();var Eb=this.getOrientation()==jsx3.gui.StackGroup.ORIENTATIONV?_:zb;var Hc=lc*Zb+(this.getSelectedIndex()<lc?1:0)*(Eb-(this.getChildren().length-1)*Zb-Zb);if(a.isFront()){var gb=Zb*(this.getChildren().length-1);return this.getOrientation()==c.ORIENTATIONV?{left:0,top:Hc,parentwidth:zb,parentheight:_-gb}:{left:Hc,top:0,parentwidth:zb-gb,parentheight:_};}else{return this.getOrientation()==c.ORIENTATIONV?{left:0,top:Hc,parentwidth:zb,parentheight:Zb}:{left:Hc,top:0,parentwidth:Zb,parentheight:_};}};p.k7=function(f,g,l){var Vb=this.RL(true,f);if(g){Vb.recalculate(f,g,l);g.style.overflow="auto";}var eb=0;var Mb=this.getChildren().length;for(var Hc=0;Hc<Mb;Hc++){var Ac=this.getChild(Hc);var y=this.IO(Ac);l.add(Ac,y,g!=null,true);}};p.paint=function(){var F=this.getChild(this.getSelectedIndex());if(F==null){F=this.getChild(0);this.setSelectedIndex(0);}if(F!=null){var hc=this.getChildren().length;var Hb=[];var N=this.paintBarSize();var x=this.getSelectedIndex();for(var Nc=0;Nc<hc;Nc++){if(x!=Nc){Hb[Nc]=N;}else{Hb[Nc]="*";}}this.setDimensionArray(Hb);}this.setBestGuess(0);this.setRepeat(hc);return this.jsxsuper();};p.paintBarSize=function(a){return this.getBarSize()?this.getBarSize():c.DEFAULTBARSIZE;};p.getSelectedIndex=function(){return this.jsxselectedindex==null?0:this.jsxselectedindex>this.getChildren().length-1?this.getChildren().length-1:this.jsxselectedindex;};p.setSelectedIndex=function(o){this.jsxselectedindex=o;};p.paintChild=function(h,g){var lb=this.getRendered();if(lb!=null)jsx3.html.insertAdjacentHTML(lb,"beforeEnd",h.paint());if(!g)this.Rv();};p.onSetChild=function(s){return s instanceof jsx3.gui.Stack;};p.onRemoveChild=function(h,n){this.jsxsuper(h,n);if(h instanceof Array){this.repaint();}else{var Wb=this.getChildren().length;var W=n==this.jsxselectedindex;if(n<=this.jsxselectedindex&&(this.jsxselectedindex>0||Wb==0))this.jsxselectedindex--;if(W&&this.jsxselectedindex>=0){this.Rv();this.getChild(this.jsxselectedindex).doShowStack();}else{this.Rv();}}};p.Rv=function(){var zb=[];var Sb=this.paintBarSize();var rb=this.getSelectedIndex();var K=this.getChildren().length;for(var Ib=0;Ib<K;Ib++){zb[Ib]=rb!=Ib?Sb:"*";}this.setDimensionArray(zb,true);};c.getVersion=function(){return "3.0.00";};});jsx3.StackGroup=jsx3.gui.StackGroup;
