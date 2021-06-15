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
jsx3.require("jsx3.chart.CartesianChart","jsx3.chart.LineSeries");jsx3.Class.defineClass("jsx3.chart.LineChart",jsx3.chart.CartesianChart,null,function(e,n){e.TYPE_OVERLAY="overlay";e.TYPE_STACKED="stacked";e.TYPE_STACKED100="stacked100";e.jv={overlay:1,stacked:1,stacked100:1};n.init=function(h,l,o,r,p){this.jsxsuper(h,l,o,r,p);this.type=e.TYPE_OVERLAY;};n.getType=function(){return this.type;};n.setType=function(r){if(e.jv[r]){this.type=r;}else{throw new jsx3.IllegalArgumentException("type",r);}};n.getYRange=function(h){if(this.type==e.TYPE_OVERLAY){return this.getRangeForField(h,"getYValue");}else{if(this.type==e.TYPE_STACKED){return this.getStackedRangeForField(h,"getYValue");}else{if(this.type==e.TYPE_STACKED100){return this.getStacked100RangeForField(h,"getYValue");}else{jsx3.chart.LOG.error("unsupported Line Chart type: "+this.type);return null;}}}};n.getXRange=function(i){return this.getRangeForField(i,"getXValue");};n.updateView=function(){this.jsxsuper();this.JJ();};n.JJ=function(){var Zb=this.M4();var Db=this.aO();var hb=this.gH();var Cc=this.getPrimaryXAxis();var L=this.getPrimaryYAxis();if(Cc==null||L==null||Db.length==0||hb==null)return;if(!jsx3.chart.isValueAxis(L)){jsx3.chart.LOG.error("bad y axis type: "+L.getClass());return;}var kb=null;if(this.type==e.TYPE_STACKED100)kb=this.Z6(Db,"getYValue");var _=null,rb=null;if(this.type==e.TYPE_STACKED||this.type==e.TYPE_STACKED100){_=new Array(hb.length);rb=new Array(hb.length);for(var X=0;X<hb.length;X++){_[X]=rb[X]=0;}}for(var Bb=0;Bb<Db.length;Bb++){var qc=Db[Bb];qc.clear();for(var X=0;X<hb.length;X++){var Gc=hb[X];var uc=null,ab=null;var P=qc.getYValue(Gc);if(P!=null&&(this.type==e.TYPE_STACKED||this.type==e.TYPE_STACKED100)){if(P>=0)P=_[X]=_[X]+P;else P=rb[X]=rb[X]+P;if(this.type==e.TYPE_STACKED100)P=100*(P/kb[X]);}if(P!=null)ab=L.getCoordinateForNoClip(P);if(jsx3.chart.isValueAxis(Cc)){var C=qc.getXValue(Gc);if(C!=null)uc=Cc.getCoordinateForNoClip(C);}else{if(jsx3.chart.isCategoryAxis(Cc)){uc=Cc.getPointForCategory(X);}}if(uc!=null&&ab!=null)qc.PM(Gc,uc,ab,X);else qc.AS(Gc,uc,ab,X);}qc.updateView();Zb.appendChild(qc.l5());}};n.VO=function(){return this.type==e.TYPE_STACKED||this.type==e.TYPE_STACKED100;};n.wH=function(o){return o instanceof jsx3.chart.LineSeries;};n.toString=function(){return "[LineChart '"+this.getName()+"']";};e.getVersion=function(){return jsx3.chart.q2;};});
