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
jsx3.require("jsx3.chart.CartesianChart","jsx3.chart.PointSeries");jsx3.Class.defineClass("jsx3.chart.PlotChart",jsx3.chart.CartesianChart,null,function(p,d){p.MAG_RADIUS="radius";p.MAG_DIAMETER="diameter";p.MAG_AREA="area";p.DEFAULT_MAX_POINT_RADIUS=30;p.iu={radius:1,diameter:1,area:1};d.init=function(i,m,k,g,q){this.jsxsuper(i,m,k,g,q);this.maxPointRadius=p.DEFAULT_MAX_POINT_RADIUS;this.magnitudeMethod=p.MAG_RADIUS;};d.getMaxPointRadius=function(){return this.maxPointRadius!=null?this.maxPointRadius:Number.POSITIVE_INFINITY;};d.setMaxPointRadius=function(r){this.maxPointRadius=r;};d.getMagnitudeMethod=function(){return this.magnitudeMethod;};d.setMagnitudeMethod=function(j){if(p.iu[j]){this.magnitudeMethod=j;}else{throw new jsx3.IllegalArgumentException("magnitudeMethod",j);}};d.getXRange=function(m){return this.getRangeForField(m,"getXValue");};d.getYRange=function(q){return this.getRangeForField(q,"getYValue");};d.updateView=function(){this.jsxsuper();this.JJ();};d.JJ=function(){var xb=this.M4();var bb=this.aO();var Vb=this.gH();var W=this.getPrimaryXAxis();var G=this.getPrimaryYAxis();if(W==null||G==null||bb.length==0||Vb==null)return;if(!jsx3.chart.isValueAxis(W)){jsx3.chart.LOG.error("bad x axis type: "+W.getClass());return;}if(!jsx3.chart.isValueAxis(G)){jsx3.chart.LOG.error("bad y axis type: "+G.getClass());return;}for(var wb=0;wb<bb.length;wb++){var yc=bb[wb];yc.BV();for(var M=0;M<Vb.length;M++){var rc=Vb[M];var R=yc.getXValue(rc);var Nc=yc.getYValue(rc);if(R==null||Nc==null)continue;R=W.getCoordinateForNoClip(R);Nc=G.getCoordinateForNoClip(Nc);if(yc instanceof jsx3.chart.PointSeries){yc.PM(rc,M,R,Nc);}else{var ob=yc.getMagnitudeValue(rc);if(ob!=null)yc.PM(rc,M,R,Nc,ob);}}yc.updateView();xb.appendChild(yc.l5());}};d.wH=function(b){return b instanceof jsx3.chart.PlotSeries;};d.toString=function(){return "[PlotChart '"+this.getName()+"']";};p.getVersion=function(){return jsx3.chart.q2;};});
