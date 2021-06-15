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
jsx3.Class.defineInterface("jsx3.gui.Form",null,function(p,c){var kc=jsx3.gui.Event;p.DEFAULTDISABLEDCOLOR="#a8a8b5";p.DEFAULTDISABLEDBACKGROUNDCOLOR="#d8d8e5";p.STATEINVALID=0;p.STATEVALID=1;p.STATEDISABLED=0;p.STATEENABLED=1;p.OPTIONAL=0;p.REQUIRED=1;c.doKeyBinding=function(a,h){var wb=jsx3.gui.HotKey.valueOf(h,a);return this.ti().registerHotKey(wb);};c.ti=function(){var gb=jsx3.gui.Window!=null;var T=jsx3.gui.Dialog!=null;var Ib=this;while(Ib!=null){if(gb&&Ib instanceof jsx3.gui.Window)return Ib.getRootBlock();if(T&&Ib instanceof jsx3.gui.Dialog)return Ib;var gc=Ib.getParent();if(gc==null)return Ib.getServer();Ib=gc;}return null;};c.getKeyBinding=function(){return this.jsxkeycode==null?null:this.jsxkeycode;};c.setKeyBinding=function(b){this.jsxkeycode=b;return this;};c.getDisabledBackgroundColor=function(){return this.jsxdisabledbgcolor;};c.setDisabledBackgroundColor=function(e){this.jsxdisabledbgcolor=e;return this;};c.UZ=function(){var hb=this.getEnabled()!=p.STATEDISABLED?this.getBackgroundColor():this.getDisabledBackgroundColor();return hb?"background-color:"+hb+";":"";};c.getDisabledColor=function(){return this.jsxdisabledcolor;};c.setDisabledColor=function(m){this.jsxdisabledcolor=m;return this;};c.getEnabled=function(){return this.jsxenabled==null?p.STATEENABLED:this.jsxenabled;};c.getValue=function(){return this.jsxvalue;};c.setValue=function(a){this.jsxvalue=a;return this;};c.setEnabled=function(q,j){if(this.jsxenabled!=q){this.jsxenabled=q;if(j)this.repaint();}return this;};c.QP=function(){if(this.getEnabled()!=p.STATEDISABLED){return "color:"+(this.getColor()?this.getColor():jsx3.gui.Block.DEFAULTCOLOR)+";";}else{return "color:"+(this.getDisabledColor()?this.getDisabledColor():p.DEFAULTDISABLEDCOLOR)+";";}};c.WP=function(){return this.getEnabled()==p.STATEENABLED?"":" disabled=\"disabled\" ";};c.CI=function(){return jsx3.gui.Block.prototype.CI.call(this,this.getIndex()||Number(0));};c.getRequired=function(){return this.jsxrequired==null?p.OPTIONAL:this.jsxrequired;};c.setRequired=function(g){this.jsxrequired=g;return this;};c.getValidationState=function(){return this._jsxVf==null?p.STATEVALID:this._jsxVf;};c.setValidationState=function(a){this._jsxVf=a;return this;};c.doValidate=jsx3.Method.newAbstract();c.doReset=function(){this.setValidationState(p.STATEVALID);return this;};p.validate=function(d,q){var Hb=d.getDescendantsOfType(jsx3.gui.Form);if(d.instanceOf(jsx3.gui.Form))Hb.unshift(d);var sc=p.STATEVALID;for(var E=0;E<Hb.length;E++){var Cb=Hb[E].doValidate();if(q)q(Hb[E],Cb);if(Cb!=p.STATEVALID)sc=Cb;}return sc;};p.reset=function(o){var Nb=o.getDescendantsOfType(jsx3.gui.Form);if(o.instanceOf(jsx3.gui.Form))Nb.unshift(o);for(var sb=0;sb<Nb.length;sb++)Nb[sb].doReset();};p.getVersion=function(){return "3.00.00";};c.emInit=function(e){if(this.emGetType()==jsx3.gui.Matrix.EditMask.NORMAL){this.setRelativePosition(jsx3.gui.Block.ABSOLUTE,true);this.setDisplay(jsx3.gui.Block.DISPLAYNONE,true);}};c.emGetType=function(){return jsx3.gui.Matrix.EditMask.NORMAL;};c.emPaintTemplate=function(){throw new jsx3.Exception("Not implemented.");};c.PS=function(k,s){return "<xsl:choose xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\"><xsl:when test=\"@jsxnomask='1'\"></xsl:when><xsl:when test=\"@jsxdisabled='1'\">"+s+"</xsl:when>"+"<xsl:otherwise>"+k+"</xsl:otherwise>"+"</xsl:choose>";};c.emBeginEdit=function(q,f,i,n,h,a,o){if(this.emGetType()==jsx3.gui.Matrix.EditMask.NORMAL){this.setRelativePosition(jsx3.gui.Block.ABSOLUTE,true);this.emUpdateDisplay(f,i);this.setDisplay(jsx3.gui.Block.DISPLAYBLOCK,true);this.setZIndex(10,true);this.focus();this.emFocus();}this.emSetValue(q);};c.emEndEdit=function(){if(this.emGetType()==jsx3.gui.Matrix.EditMask.NORMAL){this.emRestoreDisplay();}var hb=this.emGetValue();return hb;};c.emSetValue=function(a){this.setValue(a);};c.emGetValue=function(){var rc=this.getValue();return rc!=null?rc.toString():null;};c.emUpdateDisplay=function(k,d){var H=this.emGetSession();var Tb=this.getWidth(),Gc=this.getHeight();H.width=Tb;H.height=Gc;var hb=isNaN(Tb)?k.W:Math.min(parseInt(Tb),k.W);var kb=isNaN(Gc)?k.H:Math.min(parseInt(Gc),k.H);this.setDimensions(k.L,k.T,hb,kb,true);};c.emRestoreDisplay=function(){this.setDisplay(jsx3.gui.Block.DISPLAYNONE,true);var Nc=this.emGetSession();this.setWidth(Nc.width,false);this.setHeight(Nc.height,false);};c.emFocus=function(){};});jsx3.Form=jsx3.gui.Form;
