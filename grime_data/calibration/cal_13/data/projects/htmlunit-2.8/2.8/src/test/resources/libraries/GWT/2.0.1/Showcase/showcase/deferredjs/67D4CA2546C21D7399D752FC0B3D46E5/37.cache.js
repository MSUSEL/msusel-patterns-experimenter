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
function Gqb(){}
function Sqb(){return I4}
function $Qb(a){Owb(a.b,RQb(a.a))}
function Wqb(){var a;while(Lqb){a=Lqb;Lqb=Lqb.b;!Lqb&&(Mqb=null);$Qb(a.a)}}
function lzb(a){var b,c;b=n_(a.a.nc(zed),48);if(b==null){c=$$(ucb,366,1,[J_c,K_c,L_c,M_c,P_c,R_c]);a.a.oc(zed,c);return c}else{return b}}
function kzb(a){var b,c;b=n_(a.a.nc(yed),48);if(b==null){c=$$(ucb,366,1,[vQc,rQc,tQc,wQc]);a.a.oc(yed,c);return c}else{return b}}
function Tqb(){Oqb=true;Nqb=(Qqb(),new Gqb);bp(($o(),Zo),37);!!$stats&&$stats(Hp(xed,rxc,null,null));Nqb.Ec();!!$stats&&$stats(Hp(xed,gQc,null,null))}
function RQb(a){var b,c,d,e,f,g,h;h=Vec(new Sec);Wec(h,m_b(new b_b,Aed));c=kzb(a.a);for(d=0;d<c.length;++d){b=c[d];e=E9b(new B9b,Bed,b);_Wb(e,Ced+b);d==2&&aXb(e,false);Wec(h,e)}Wec(h,m_b(new b_b,Ded));g=lzb(a.a);for(d=0;d<g.length;++d){f=g[d];e=E9b(new B9b,Eed,f);_Wb(e,Fed+Xnc(f,Lxc,fxc));d==2&&dXb(e,(Rlc(),Rlc(),Qlc),false);Wec(h,e)}return h}
var Aed='<b>Select your favorite color:<\/b>',Ded='<br><b>Select your favorite sport:<\/b>',Ged='AsyncLoader37',Bed='color',Ced='cwRadioButton-color-',Fed='cwRadioButton-sport-',yed='cwRadioButtonColors',zed='cwRadioButtonSports',xed='runCallbacks37',Eed='sport';_=Gqb.prototype=new Hqb;_.gC=Sqb;_.Ec=Wqb;_.tI=0;var I4=fmc(UJc,Ged);Tqb();