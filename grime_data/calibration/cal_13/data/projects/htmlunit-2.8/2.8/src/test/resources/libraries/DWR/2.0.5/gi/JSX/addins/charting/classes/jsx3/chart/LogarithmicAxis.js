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
jsx3.require("jsx3.chart.Axis");jsx3.Class.defineClass("jsx3.chart.LogarithmicAxis",jsx3.chart.Axis,null,function(b,m){b.Tf=200;b.Qs=1;b.eo=0;b.jo=2;m.init=function(k,p,e){this.jsxsuper(k,p,e);this.autoAdjust=jsx3.Boolean.TRUE;this.baseAtZero=jsx3.Boolean.TRUE;this.showNegativeValues=jsx3.Boolean.FALSE;this.minExponent=null;this.maxExponent=null;this.base=10;this.majorDivisions=1;this.xI("wR",b.eo);this.xI("tQ",b.jo);};m.getAutoAdjust=function(){return this.autoAdjust;};m.setAutoAdjust=function(f){this.autoAdjust=f;};m.getBaseAtZero=function(){return this.baseAtZero;};m.setBaseAtZero=function(c){this.baseAtZero=c;};m.getShowNegativeValues=function(){return this.showNegativeValues;};m.setShowNegativeValues=function(o){this.showNegativeValues=o;};m.getMinExponent=function(){return this.minExponent;};m.setMinExponent=function(g){this.minExponent=g;};m.getMaxExponent=function(){return this.maxExponent;};m.setMaxExponent=function(j){this.maxExponent=j;};m.getBase=function(){return this.base;};m.setBase=function(l){this.base=l;};m.getMajorDivisions=function(){return this.majorDivisions;};m.setMajorDivisions=function(a){this.majorDivisions=a;};m.g6=function(){var S=false;if(this.autoAdjust)S=this.Pd();if(!S){this.xI("wR",this.minExponent!=null?this.minExponent:b.eo);this.xI("tQ",this.maxExponent!=null?this.maxExponent:b.jo);}};m.Pd=function(){var u=this.getChart();if(u==null)return false;var Db=u.getRangeForAxis(this);if(Db==null){jsx3.chart.LOG.debug("no range for axis "+this+" in chart "+u);return false;}var wb=Math.max(0,Db[0]);var I=Math.max(0,Db[1]);if(I==0){jsx3.chart.LOG.debug("range of axis "+this+" is all negative "+u);return false;}var qb=null,vb=null;if(this.minExponent!=null){qb=this.minExponent;}else{if(this.baseAtZero){qb=0;}}if(this.maxExponent!=null){vb=this.maxExponent;}wb=wb*b.Qs;I=I*b.Qs;if(qb==null){if(wb==0)qb=0;else qb=Math.floor(Math.log(wb)/Math.log(this.base));}if(vb==null){vb=Math.ceil(Math.log(I)/Math.log(this.base));}this.xI("wR",qb);this.xI("tQ",vb);return true;};m.a1=function(c){var fb=this.Q0("wR");var uc=Math.floor(fb+c/this.majorDivisions);var jb=c%this.majorDivisions;if(jb==0){return Math.pow(this.base,uc);}else{var Bc=Math.pow(this.base,uc);var Mb=Math.pow(this.base,uc+1);return Bc+jb*(Mb-Bc)/this.majorDivisions;}};m.RQ=function(){var S=this.Q0("wR");var fb=this.Q0("tQ");var wc=[];var vb=0;for(var gb=S;gb<=fb&&vb<b.Tf;gb++){var G=Math.pow(this.base,gb);if(gb>S){var Sb=Math.pow(this.base,gb-1);for(var Ic=1;Ic<this.majorDivisions;Ic++){var Cb=Sb+Ic*(G-Sb)/this.majorDivisions;wc.push(this.getCoordinateFor(Cb));vb++;}}wc.push(this.getCoordinateFor(G));vb++;}return wc;};m.RY=function(n,i){var B=[];if(i==0){return [];}else{if(i==n.length){return [];}else{var bc=this.a1(i-1);var rc=this.a1(i);for(var gb=1;gb<this.minorTickDivisions;gb++){var nb=bc+gb*(rc-bc)/this.minorTickDivisions;B.push(this.getCoordinateFor(nb));}}}return B;};m.vX=function(){return false;};m.getCoordinateFor=function(n){var Cc=this.Q0("wR");var hb=this.Q0("tQ");var B=null;if(n<=0){B=0;}else{var Y=Math.log(n)/Math.log(this.base);if(Y<Cc){B=0;}else{if(Y>hb){B=this.length;}else{B=Math.round(this.length*(Y-Cc)/(hb-Cc));}}}return this.horizontal?B:this.length-B;};m.getCoordinateForNoClip=function(o){var Eb=this.Q0("wR");var M=this.Q0("tQ");var E=null;if(o<=0){E=-1;}else{var X=Math.log(o)/Math.log(this.base);E=Math.round(this.length*(X-Eb)/(M-Eb));}return this.horizontal?E:this.length-E;};m.toString=function(){return "[LogarithmicAxis '"+this.getName()+"']";};b.getVersion=function(){return jsx3.chart.q2;};});
