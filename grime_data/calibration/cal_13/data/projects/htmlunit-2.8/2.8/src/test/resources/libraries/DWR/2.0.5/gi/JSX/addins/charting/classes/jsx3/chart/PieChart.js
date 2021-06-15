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
jsx3.require("jsx3.chart.RadialChart","jsx3.chart.PieSeries");jsx3.Class.defineClass("jsx3.chart.PieChart",jsx3.chart.RadialChart,null,function(n,g){g.init=function(r,c,f,q,i){this.jsxsuper(r,c,f,q,i);this.innerRadius=0;this.seriesPadding=0.1;this.totalAngle=360;this.startAngle=0;this.categoryField=null;this.colors=null;this.colorFunction="jsx3.chart.PieChart.defaultColoring";this.seriesStroke=null;};g.getInnerRadius=function(){return this.innerRadius!=null?this.innerRadius:0;};g.setInnerRadius=function(c){this.innerRadius=c==null?null:Math.max(0,Math.min(1,c));};g.getSeriesPadding=function(){return this.seriesPadding!=null?this.seriesPadding:0.1;};g.setSeriesPadding=function(c){this.seriesPadding=c;};g.getTotalAngle=function(){return this.totalAngle!=null?this.totalAngle:360;};g.setTotalAngle=function(h){this.totalAngle=h==null?null:Math.max(0,Math.min(360,h));};g.getStartAngle=function(){return this.startAngle!=null?this.startAngle:0;};g.setStartAngle=function(m){this.startAngle=m;};g.getCategoryField=function(){return this.categoryField;};g.setCategoryField=function(c){this.categoryField=c;};g.getColors=function(){return this.colors;};g.setColors=function(j){this.colors=j;};g.getColorFunction=function(){return this.o_("colorFunction");};g.setColorFunction=function(r){this.uR("colorFunction",r);};g.getSeriesStroke=function(){return this.seriesStroke;};g.setSeriesStroke=function(h){this.seriesStroke=h;};g.t0=function(e,o){var qc=this.getColors();if(qc!=null&&qc.length>0)return jsx3.vector.Fill.valueOf(qc[o%qc.length]);var Jc=this.getColorFunction();if(Jc!=null)return Jc.call(null,e,o);return new jsx3.vector.Fill();};g.updateView=function(){this.jsxsuper();var _=this.M4();var bc=this.aO();var Bc=this.gH();if(bc.length<1)return;if(Bc==null||Bc.length<1)return;var Fb=bc[0].getWidth();var vb=bc[0].getHeight();var J=Math.round(Fb/2);var xc=Math.round(vb/2);var Bb=Math.floor(Math.min(Fb,vb)/2);var G=this.tK(bc,"getValue",true);var t=Bb*(1-this.getInnerRadius())/(bc.length+(bc.length-1)*this.getSeriesPadding());var fc=Bb*this.getInnerRadius();for(var ac=0;ac<bc.length;ac++){var Sb=bc[ac];Sb.NY();var u=Sb.getStartAngle();if(u==null)u=this.getStartAngle();var uc=Sb.getTotalAngle();if(uc==null)uc=this.getTotalAngle();var ub=fc+t;for(var P=0;P<Bc.length;P++){var D=Bc[P];var W=Sb.getValue(D);if(W==null||W<=0){Sb.TJ();continue;}var tb=uc*W/G[ac];Sb.YV(D,J,xc,u,u+tb,Math.round(fc),Math.round(ub),100*W/G[ac]);u=u+tb;}Sb.updateView();_.appendChild(Sb.l5());fc=fc+t*(1+this.getSeriesPadding());}};g.VO=function(){return true;};g.wH=function(a){return a instanceof jsx3.chart.PieSeries;};g.getLegendEntryType=function(){jsx3.require("jsx3.chart.Legend");return jsx3.chart.Legend.SHOW_CATEGORIES;};g.toString=function(){return "[PieChart '"+this.getName()+"']";};n.defaultColoring=function(s,j){return jsx3.chart.Chart.DEFAULT_FILLS[j%jsx3.chart.Chart.DEFAULT_FILLS.length];};n.getVersion=function(){return jsx3.chart.q2;};});
