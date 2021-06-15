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
jsx3.Class.defineInterface("jsx3.chart.PointRenderer",null,function(i,b){var L=jsx3.vector;b.render=jsx3.Method.newAbstract("x1","y1","x2","y2","fill","stroke");b.areaToRadius=jsx3.Method.newAbstract("area");i.CIRCLE=i.jsxclass.newInnerClass();i.CIRCLE.areaToRadius=function(g){return Math.sqrt(g/Math.PI);};i.CIRCLE.render=function(k,r,j,q,l,h){var P=new L.Oval(k,r,j-k,q-r);P.setFill(l);P.setStroke(h);return P;};i.CROSS=i.jsxclass.newInnerClass();i.CROSS.Bq=0.6;i.CROSS.areaToRadius=function(p){return Math.sqrt(p/(1-this.Bq/Math.SQRT2))/2;};i.CROSS.render=function(k,r,j,q,l,h){var rc=j-k;var mc=this.Bq;var T=Math.round(rc*(1-mc)/2);var jb=Math.round(rc*mc/2);var vc=Math.round(rc-rc*(1-mc)/2);var ib=Math.round(rc/2);var Nc=new L.Polygon(0,0,[k,r,k+T,r,k+ib,r+jb,k+vc,r,j,r,j,r+T,j-jb,r+ib,j,r+vc,j,q,j-T,q,j-ib,q-jb,j-vc,q,k,q,k,q-T,k+jb,q-ib,k,q-vc,k,r]);Nc.setFill(l);Nc.setStroke(h);return Nc;};i.DIAMOND=i.jsxclass.newInnerClass();i.DIAMOND.Wn=1.2;i.DIAMOND.areaToRadius=function(n){return Math.sqrt(n)/2;};i.DIAMOND.render=function(a,h,s,g,r,o){var t=(a+s)/2;var Fb=(h+g)/2;var Xb=(s-a)/this.Wn;var Pb=(g-h)/this.Wn;var gc=new L.Rectangle(Math.round(t-Xb/2),Math.round(Fb-Pb/2),Math.round(Xb),Math.round(Pb));gc.setRotation(45);gc.setFill(r);gc.setStroke(o);return gc;};i.BOX=i.jsxclass.newInnerClass();i.BOX.areaToRadius=function(f){return Math.sqrt(i.DIAMOND.Wn*i.DIAMOND.Wn*f)/2;};i.BOX.render=function(l,s,k,r,m,j){var J=new L.Rectangle(l,s,k-l,r-s);J.setFill(m);J.setStroke(j);return J;};i.TRIANGLE=i.jsxclass.newInnerClass();i.TRIANGLE.areaToRadius=function(l){return Math.sqrt(2*l)/2;};i.TRIANGLE.render=function(d,k,a,h,c,s){var Kb=Math.round((d+a)/2);var E=new L.Polygon(0,0,[Kb,k,a,h,d,h,Kb,k]);E.setFill(c);E.setStroke(s);return E;};});jsx3.chart.Renderers=jsx3.chart.PointRenderer;jsx3.chart.Renderers.Circle=jsx3.chart.PointRenderer.CIRCLE;jsx3.chart.Renderers.Cross=jsx3.chart.PointRenderer.CROSS;jsx3.chart.Renderers.Diamond=jsx3.chart.PointRenderer.DIAMOND;jsx3.chart.Renderers.Box=jsx3.chart.PointRenderer.BOX;jsx3.chart.Renderers.Triangle=jsx3.chart.PointRenderer.TRIANGLE;
