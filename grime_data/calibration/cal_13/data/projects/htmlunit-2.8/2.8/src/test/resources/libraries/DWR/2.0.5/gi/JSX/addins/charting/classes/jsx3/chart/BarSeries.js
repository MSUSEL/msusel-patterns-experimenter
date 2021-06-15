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
jsx3.require("jsx3.chart.Series");jsx3.Class.defineClass("jsx3.chart.BCSeries",jsx3.chart.Series,null,function(l,d){d.init=function(c,a){this.jsxsuper(c,a);this.xField=null;this.yField=null;this.minField=null;};d.getParallelValue=jsx3.Method.newAbstract();d.getNormalValue=jsx3.Method.newAbstract();d.getNormalWidth=jsx3.Method.newAbstract();d.getXValue=function(m){if(this.xField)return jsx3.chart.asNumber(m.getAttribute(this.xField));return null;};d.getYValue=function(a){if(this.yField)return jsx3.chart.asNumber(a.getAttribute(this.yField));return null;};d.getMinValue=function(i){if(this.minField)return jsx3.chart.asNumber(i.getAttribute(this.minField));return null;};d.getShownAreas=function(){var lb=this.Q0("OY");if(lb==null){lb=[];this.xI("OY",lb);}return lb;};d.updateView=function(){this.jsxsuper();var nc=this.D7();var uc=this.qT(nc);this.xI("OH",nc);this.xI("f3",uc);var cb=this.getShownAreas();for(var A=0;A<cb.length;A++){var Dc=cb[A];this.ZW(Dc[0],Dc[1],Dc[2],Dc[3],Dc[4],Dc[5],true);}};d.ZW=function(a,k,g,n,f,m,r){if(!r)this.getShownAreas().push([a,k,g,n,f,m]);var C=this.l5();var Ob=this.getWidth();var Kc=this.getHeight();if(g>f){var Rb=g;g=f;f=Rb;}if(n>m){var Rb=n;n=m;m=Rb;}if(g>Ob||f<0)return;if(n>Kc||m<0)return;var gc=new jsx3.vector.Rectangle(g,n,f-g,m-n);gc.setId(this.getId()+"_r"+k);gc.ET(0,0,Ob,Kc);var Dc=this.getColorFunction();var M=Dc!=null?Dc.call(null,a,k):this.Q0("OH");gc.setFill(M);gc.setStroke(this.Q0("f3"));var yc=this.getTooltipFunction();if(yc!=null)gc.setToolTip(yc.call(null,this,a));this.A3(gc,k,a.getAttribute("jsxid"));C.appendChild(gc);};d.MY=function(){var Nc=this.getShownAreas();Nc.splice(0,Nc.length);};d.getXField=function(){return this.xField;};d.setXField=function(b){this.xField=b;};d.getYField=function(){return this.yField;};d.setYField=function(r){this.yField=r;};d.getMinField=function(){return this.minField;};d.setMinField=function(s){this.minField=s;};l.getVersion=function(){return jsx3.chart.q2;};});jsx3.chart.BCSeries.prototype.drawBar=jsx3.chart.BCSeries.prototype.ZW;jsx3.chart.BCSeries.prototype.drawColumn=jsx3.chart.BCSeries.prototype.ZW;jsx3.Class.defineClass("jsx3.chart.BarSeries",jsx3.chart.BCSeries,null,function(b,k){b.DEFAULT_BARHEIGHT=10;k.init=function(o,q){this.jsxsuper(o,q);this.barHeight=null;this.setTooltipFunction("jsx3.chart.BarSeries.tooltip");};k.getParallelValue=function(i){return this.getXValue(i);};k.getNormalValue=function(l){return this.getYValue(l);};k.getNormalWidth=function(){return this.getBarHeight();};k.getBarHeight=function(){return this.barHeight!=null?this.barHeight:b.DEFAULT_BARHEIGHT;};k.setBarHeight=function(l){this.barHeight=l;};k.toString=function(){return "[BarSeries '"+this.getName()+"']";};b.tooltip=function(f,o){var x=f.getXValue(o);var Sb=f.getYValue(o);var Y=f.getMinValue(o);var u=Y!=null?"{"+Y+","+x+"}":x;if(Sb!=null)u=u+(", y = "+Sb);return u;};});jsx3.Class.defineClass("jsx3.chart.ColumnSeries",jsx3.chart.BCSeries,null,function(j,e){j.DEFAULT_COLUMNWIDTH=10;e.init=function(b,k){this.jsxsuper(b,k);this.columnWidth=null;this.setTooltipFunction("jsx3.chart.ColumnSeries.tooltip");};e.getParallelValue=function(d){return this.getYValue(d);};e.getNormalValue=function(c){return this.getXValue(c);};e.getNormalWidth=function(){return this.getColumnWidth();};e.getColumnWidth=function(){return this.columnWidth!=null?this.columnWidth:j.DEFAULT_COLUMNWIDTH;};e.setColumnWidth=function(i){this.columnWidth=i;};e.toString=function(){return "[ColumnSeries '"+this.getName()+"']";};e.ZW=function(q,h,k,p,n,o,g){if(g)this.jsxsuper(q,h,k,p,n,o,g);else this.jsxsuper(q,h,p,n,o,k,g);};j.tooltip=function(q,i){var jb=q.getXValue(i);var y=q.getYValue(i);var yb=q.getMinValue(i);var Lb=yb!=null?"{"+yb+","+y+"}":y;if(jb!=null)Lb=Lb+(", x = "+jb);return Lb;};});
