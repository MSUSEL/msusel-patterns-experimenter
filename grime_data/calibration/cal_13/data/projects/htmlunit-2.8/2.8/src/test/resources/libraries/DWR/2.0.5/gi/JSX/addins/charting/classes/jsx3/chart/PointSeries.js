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
jsx3.require("jsx3.chart.Series","jsx3.chart.PointRenderer");jsx3.Class.defineClass("jsx3.chart.PlotSeries",jsx3.chart.Series,null,function(m,g){m.hy=25;g.init=function(c,j){this.jsxsuper(c,j);this.xField=null;this.yField=null;this.magnitude=null;this.renderer="jsx3.chart.PointRenderer.CIRCLE";};g.Xs=function(){var Yb=this.Q0("VX");if(Yb==null){Yb=[];this.xI("VX",Yb);}return Yb;};g.updateView=function(){this.jsxsuper();var Lc=this.D7();var Db=this.qT(Lc);this.xI("OH",Lc);this.xI("f3",Db);var V=this.Xs();for(var fc=0;fc<V.length;fc++){this.LD(V[fc]);}};g.MA=function(b,l,j,f,c){var Kb=this.l5();var Nc=this.getChart();if(Nc==null)return;var _=Nc.getMaxPointRadius();var rb=this.getWidth();var nc=this.getHeight();if(j>rb||j<0)return;if(f>nc||f<0)return;var uc=this.getRenderer();if(uc==null)uc=jsx3.chart.PointRenderer.CIRCLE;var Ic=null,Ib=null,xb=null,x=null;var tb=Nc.getMagnitudeMethod();if(tb==jsx3.chart.PlotChart.MAG_DIAMETER){c=Math.min(_*2,c);Ic=j-Math.round(c/2);Ib=f-Math.round(c/2);xb=Ic+c;x=Ib+c;}else{var z=tb==jsx3.chart.PlotChart.MAG_AREA?uc.areaToRadius(c*m.hy):c;z=Math.min(_,z);Ic=j-Math.round(z);Ib=f-Math.round(z);xb=Ic+Math.round(2*z);x=Ib+Math.round(2*z);}var N=this.getColorFunction();var gb=N!=null?N.call(null,b,l):this.Q0("OH");var nb=uc.render(Ic,Ib,xb,x,gb,this.Q0("f3"));nb.setId(this.getId()+"_p"+l);this.A3(nb,l,b.getAttribute("jsxid"));Kb.appendChild(nb);var F=this.getTooltipFunction();if(F!=null)nb.setToolTip(F.call(null,this,b));};g.BV=function(){var E=this.Xs();E.splice(0,E.length);};g.getXValue=function(d){if(this.xField)return jsx3.chart.asNumber(d.getAttribute(this.xField));return null;};g.getYValue=function(r){if(this.yField)return jsx3.chart.asNumber(r.getAttribute(this.yField));return null;};g.getXField=function(){return this.xField;};g.setXField=function(f){this.xField=f;};g.getYField=function(){return this.yField;};g.setYField=function(n){this.yField=n;};g.getRenderer=function(){return this.d8("renderer");};g.setRenderer=function(h){this.uR("renderer",h);};g.getLegendRenderer=function(){var cb=this.getRenderer();return cb!=null?cb:this.jsxsuper();};m.getVersion=function(){return jsx3.chart.q2;};});jsx3.Class.defineClass("jsx3.chart.PointSeries",jsx3.chart.PlotSeries,null,function(r,j){r.DEFAULT_MAGNITUDE=4;j.init=function(k,b){this.jsxsuper(k,b);this.magnitude=null;this.setTooltipFunction("jsx3.chart.PointSeries.tooltip");};j.PM=function(q,h,k,n){this.Xs().push([q,h,k,n]);};j.LD=function(q){this.MA(q[0],q[1],q[2],q[3],this.getMagnitude());};j.getMagnitude=function(){return this.magnitude!=null?this.magnitude:r.DEFAULT_MAGNITUDE;};j.setMagnitude=function(c){this.magnitude=c;};j.toString=function(){return "[PointSeries '"+this.getName()+"']";};r.tooltip=function(k,o){var x=k.getXValue(o);var Sb=k.getYValue(o);return "{"+x+","+Sb+"}";};});jsx3.Class.defineClass("jsx3.chart.BubbleSeries",jsx3.chart.PlotSeries,null,function(j,d){d.init=function(g,f){this.jsxsuper(g,f);this.magnitudeField=null;this.setTooltipFunction("jsx3.chart.BubbleSeries.tooltip");};d.PM=function(s,p,i,h,c){this.Xs().push([s,p,i,h,c]);};d.LD=function(i){this.MA(i[0],i[1],i[2],i[3],i[4]);};d.toString=function(){return "[BubbleSeries '"+this.getName()+"']";};j.tooltip=function(a,f){var Lb=a.getXValue(f);var Cb=a.getYValue(f);var Fc=a.getMagnitudeValue(f);return "{"+Lb+","+Cb+","+Fc+"}";};d.getMagnitudeField=function(){return this.magnitudeField;};d.setMagnitudeField=function(r){this.magnitudeField=r;};d.getMagnitudeValue=function(m){if(this.magnitudeField)return jsx3.chart.asNumber(m.getAttribute(this.magnitudeField));return null;};});
