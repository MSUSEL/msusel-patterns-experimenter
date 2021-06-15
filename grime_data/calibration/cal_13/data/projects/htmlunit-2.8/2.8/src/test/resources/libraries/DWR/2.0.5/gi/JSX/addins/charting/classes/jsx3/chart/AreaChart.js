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
jsx3.require("jsx3.chart.CartesianChart","jsx3.chart.AreaSeries");jsx3.Class.defineClass("jsx3.chart.AreaChart",jsx3.chart.CartesianChart,null,function(r,q){r.TYPE_OVERLAY="overlay";r.TYPE_STACKED="stacked";r.TYPE_STACKED100="stacked100";q.init=function(n,s,b,a,c){this.jsxsuper(n,s,b,a,c);this.type=r.TYPE_OVERLAY;};q.getType=function(){return this.type;};q.setType=function(n){if(r.ALLOWED_TYPES[n]){this.type=n;}else{throw new jsx3.IllegalArgumentException("type",n);}};q.getYRange=function(d){if(this.type==r.TYPE_OVERLAY){return this.getRangeForField(d,"getYValue");}else{if(this.type==r.TYPE_STACKED){return this.getStackedRangeForField(d,"getYValue");}else{if(this.type==r.TYPE_STACKED100){return this.getStacked100RangeForField(d,"getYValue");}else{jsx3.chart.LOG.error("unsupported Line Chart type: "+this.type);return null;}}}};q.getXRange=function(m){return this.getRangeForField(m,"getXValue");};q.updateView=function(){this.jsxsuper();this.JJ();};q.JJ=function(){var ac=this.M4();var Lc=this.aO();var jb=this.gH();var wb=this.getPrimaryXAxis();var kc=this.getPrimaryYAxis();if(wb==null||kc==null||Lc.length==0||jb==null)return;if(!jsx3.chart.isValueAxis(kc)){jsx3.chart.LOG.error("bad y axis type: "+kc.getClass());return;}var mc=null;if(this.type==r.TYPE_STACKED100)mc=this.Z6(Lc,"getYValue");var Kc=null,Vb=null;if(this.type==r.TYPE_STACKED||this.type==r.TYPE_STACKED100){Kc=new Array(jb.length);Vb=new Array(jb.length);for(var xb=0;xb<jb.length;xb++){Kc[xb]=Vb[xb]=0;}}for(var L=0;L<Lc.length;L++){var bb=Lc[L];bb.BV();for(var xb=0;xb<jb.length;xb++){var S=jb[xb];var wc=null,gc=null,db=null;var Zb=bb.getYValue(S);var Ic=bb.getMinValue(S);if(Zb==null)continue;if(this.type==r.TYPE_STACKED||this.type==r.TYPE_STACKED100){if(Zb>=0){Ic=Kc[xb];Zb=Kc[xb]+Zb;Kc[xb]=Zb;}else{Ic=Vb[xb];Zb=Vb[xb]+Zb;Vb[xb]=Zb;}if(this.type==r.TYPE_STACKED100){Zb=100*(Zb/mc[xb]);Ic=100*(Ic/mc[xb]);}}if(Ic==null)Ic=0;gc=kc.getCoordinateForNoClip(Zb);db=kc.getCoordinateFor(Ic);if(jsx3.chart.isValueAxis(wb)){var _=bb.getXValue(S);if(_!=null)wc=wb.getCoordinateForNoClip(_);}else{if(jsx3.chart.isCategoryAxis(wb)){wc=wb.getPointForCategory(xb);}}if(wc==null)continue;bb.PM(S,wc,gc,xb);bb.YZ(wc,db);}bb.updateView();ac.appendChild(bb.l5());}};q.VO=function(){return this.type==r.TYPE_STACKED||this.type==r.TYPE_STACKED100;};q.wH=function(j){return j instanceof jsx3.chart.AreaSeries;};q.toString=function(){return "[AreaChart '"+this.getName()+"']";};r.getVersion=function(){return jsx3.chart.q2;};});
