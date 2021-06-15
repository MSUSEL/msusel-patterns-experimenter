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
function Ueb(){}
function mZb(){}
function JZb(){}
function MZb(){}
function RZb(){}
function GZb(){return P5}
function efb(){return s0}
function LZb(){return M5}
function QZb(){return N5}
function TZb(){return O5}
function HZb(a){yZb(this,a)}
function EZb(a){if(a==pZb){return true}YH();return a==tZb}
function DZb(a){if(a==qZb){return true}YH();return a==oZb}
function IZb(a){var b;b=TSb(this,a);if(b){a==this.b&&(this.b=null);zZb(this)}return b}
function AZb(a,b){var c;c=a.I;c.c=b.b;!!c.d&&(c.d[dxc]=b.b,undefined)}
function BZb(a,b){var c;c=a.I;c.e=b.b;!!c.d&&(c.d.style[xDc]=b.b,undefined)}
function PZb(a,b){a.c=(g0b(),d0b).b;a.e=(r0b(),q0b).b;a.b=b;return a}
function wZb(a){uZb();MTb(a);a.c=(g0b(),d0b);a.d=(r0b(),q0b);a.f[Wwc]=0;a.f[Vwc]=0;return a}
function ffb(){afb=true;_eb=(cfb(),new Ueb);Zo((Wo(),Vo),19);!!$stats&&$stats(Dp(M1c,_tc,null,null));_eb.tc();!!$stats&&$stats(Dp(M1c,PMc,null,null))}
function uZb(){uZb=xsc;nZb=new JZb;qZb=new JZb;pZb=new JZb;oZb=new JZb;rZb=new JZb;sZb=new JZb;tZb=new JZb}
function xZb(a,b,c){var d;if(c==nZb){if(b==a.b){return}else if(a.b){throw ojc(new ljc,Y1c)}}Yrb(b);wcc(a.g,b);c==nZb&&(a.b=b);d=PZb(new MZb,c);b.I=d;AZb(b,a.c);BZb(b,a.d);zZb(a);$rb(b,a)}
function ifb(){var a,b,c,d;while(Zeb){a=Zeb;Zeb=Zeb.c;!Zeb&&($eb=null);Vtb(a.b.b,(c=wZb(new mZb),c.K[Jwc]=N1c,c.f[Wwc]=4,c.c=(g0b(),c0b),xZb(c,lYb(new aYb,O1c),(uZb(),rZb)),xZb(c,lYb(new aYb,P1c),sZb),xZb(c,lYb(new aYb,Q1c),oZb),xZb(c,lYb(new aYb,R1c),tZb),xZb(c,lYb(new aYb,S1c),rZb),xZb(c,lYb(new aYb,T1c),sZb),b=lYb(new aYb,U1c),d=F7b(new C7b,b),d.K.style[Owc]=V1c,d.K.style[Kwc]=W1c,xZb(c,d,nZb),yZb(c,X1c),c))}}
function yZb(a,b){var c,d,e,f,g,h,i;Hbc(a.K,Ptc,b);h=lqc(new jqc);i=Icc(new Fcc,a.g);while(i.b<i.c.d-1){c=Kcc(i);g=c.I.b;e=AY(h.cc(g),41);d=!e?1:e.b;f=FZb(g,d);Hbc(xPb(c.K),b,f);h.dc(g,Njc(d+1))}}
function zZb(a){var b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q;b=a.e;while(ERb(b)>0){b.removeChild(DRb(b,0))}n=1;e=1;for(h=Icc(new Fcc,a.g);h.b<h.c.d-1;){d=Kcc(h);f=d.I.b;f==rZb||f==sZb?++n:(f==oZb||f==tZb||f==qZb||f==pZb)&&++e}o=kY(v9,356,32,n,0);for(g=0;g<n;++g){o[g]=new RZb;o[g].c=(cr(),$doc).createElement(cDc);b.appendChild(o[g].c)}j=0;k=e-1;l=0;p=n-1;c=null;for(h=Icc(new Fcc,a.g);h.b<h.c.d-1;){d=Kcc(h);i=d.I;q=(cr(),$doc).createElement(aDc);i.d=q;i.d[dxc]=i.c;i.d.style[xDc]=i.e;i.d[Owc]=Ptc;i.d[Kwc]=Ptc;if(i.b==rZb){HRb(o[l].c,q,o[l].b);q.appendChild(d.K);q[Zwc]=k-j+1;++l}else if(i.b==sZb){HRb(o[p].c,q,o[p].b);q.appendChild(d.K);q[Zwc]=k-j+1;--p}else if(i.b==nZb){c=q}else if(DZb(i.b)){m=o[l];HRb(m.c,q,m.b++);q.appendChild(d.K);q[Z1c]=p-l+1;++j}else if(EZb(i.b)){m=o[l];HRb(m.c,q,m.b);q.appendChild(d.K);q[Z1c]=p-l+1;--k}}if(a.b){m=o[l];HRb(m.c,c,m.b);c.appendChild(a.b.K)}}
function FZb(a,b){if(a==rZb){return $1c+b}else if(a==sZb){return _1c+b}else if(a==tZb){return a2c+b}else if(a==oZb){return b2c+b}else if(a==qZb){return c2c+b}else if(a==pZb){return d2c+b}else{return yDc}}
var W1c='100px',e2c='AsyncLoader19',h2c='DockPanel',i2c='DockPanel$DockLayoutConstant',j2c='DockPanel$LayoutData',f2c='DockPanel$TmpRow',g2c='DockPanel$TmpRow;',Y1c='Only one CENTER widget may be added',N1c='cw-DockPanel',X1c='cwDockPanel',b2c='east',d2c='lineend',c2c='linestart',$1c='north',M1c='runCallbacks19',_1c='south',a2c='west',U1c='\u0647\u0630\u0627 \u0647\u0648 <code>scrollpanel<\/code> \u0627\u0644\u0645\u062F\u0648\u0646\u0647 \u0627\u0644\u0648\u0627\u0631\u062F\u0629 \u0641\u064A \u0647\u0630\u0627 \u0627\u0644\u0645\u0631\u0643\u0632 \u0645\u0646 <code>dockpanel<\/code> \u0627\u0644\u0645\u062F\u0648\u0646\u0647>. \u0645\u0646 \u062E\u0644\u0627\u0644 \u0648\u0636\u0639 \u0628\u0639\u0636 \u0627\u0644\u0645\u062D\u062A\u0648\u064A\u0627\u062A \u0627\u0644\u0649 \u062D\u062F \u0643\u0628\u064A\u0631 \u0641\u064A \u062E\u0637 \u0627\u0644\u0648\u0633\u0637 \u0648\u062A\u062D\u062F\u064A\u062F \u062D\u062C\u0645\u0647 \u0635\u0631\u0627\u062D\u0629 , \u0641\u0627\u0646\u0647 \u064A\u0635\u0628\u062D \u0627\u0644\u0645\u062C\u0627\u0644 \u0642\u0627\u0628\u0644 \u0644\u0644\u062A\u062F\u0631\u062C \u0636\u0645\u0646 \u0635\u0641\u062D\u0629 , \u0648\u0644\u0643\u0646 \u062F\u0648\u0646 \u0627\u0646 \u064A\u062A\u0637\u0644\u0628 \u0630\u0644\u0643 \u0627\u0633\u062A\u062E\u062F\u0627\u0645 \u0648\u0633\u064A\u0644\u0629 iframe. <br><br>\u0627\u0644\u064A\u0643 \u0644\u0627 \u0628\u0623\u0633 \u0628\u0647 \u0627\u0643\u062B\u0631 \u0645\u0639\u0646\u0649 \u0627\u0644\u0646\u0635 \u0645\u0646 \u0634\u0623\u0646\u0647 \u0627\u0646 \u064A\u062E\u062F\u0645 \u0641\u064A \u0627\u0644\u0645\u0642\u0627\u0645 \u0627\u0644\u0627\u0648\u0644 \u0644\u062C\u0639\u0644 \u0647\u0630\u0627 \u0627\u0644\u0634\u064A\u0621 \u0644\u0641\u064A\u0641\u0647 \u0645\u0646 \u0627\u0633\u0641\u0644 \u0645\u0646\u0637\u0642\u0629 \u0627\u0639\u0645\u0627\u0644\u0647\u0627 \u0644\u0644\u0639\u064A\u0627\u0646. \u062E\u0644\u0627\u0641 \u0630\u0644\u0643 , \u0627\u0646\u0643 \u0642\u062F \u062A\u0636\u0637\u0631 \u0627\u0644\u0649 \u062C\u0639\u0644\u0647\u0627 \u062D\u0642\u0627 , \u062D\u0642\u0627 \u0627\u0644\u0635\u063A\u064A\u0631\u0629 \u0643\u064A \u0646\u0631\u0649 nifty \u0641\u064A\u0641\u0647 \u0627\u0644\u0642\u0636\u0628\u0627\u0646!',P1c='\u0647\u0630\u0627 \u0647\u0648 \u0627\u0644\u0639\u0646\u0635\u0631 \u0627\u0644\u062C\u0646\u0648\u0628\u064A \u0627\u0644\u0627\u0648\u0644',T1c='\u0647\u0630\u0627 \u0647\u0648 \u0627\u0644\u0639\u0646\u0635\u0631 \u0627\u0644\u062C\u0646\u0648\u0628\u064A \u0627\u0644\u062B\u0627\u0646\u064A',Q1c='\u0647\u0630\u0627 \u0647\u0648 \u0627\u0644\u0639\u0646\u0635\u0631 \u0627\u0644\u0634\u0631\u0642',O1c='\u0647\u0630\u0627 \u0647\u0648 \u0627\u0644\u0639\u0646\u0635\u0631 \u0627\u0644\u0634\u0645\u0627\u0644\u064A \u0627\u0644\u0627\u0648\u0644',S1c='\u0647\u0630\u0627 \u0647\u0648 \u0627\u0644\u0639\u0646\u0635\u0631 \u0627\u0644\u0634\u0645\u0627\u0644\u064A \u0627\u0644\u062B\u0627\u0646\u064A',R1c='\u0647\u0630\u0627 \u0647\u0648 \u0627\u0644\u0639\u0646\u0635\u0631 \u0627\u0644\u063A\u0631\u0628\u064A';_=Ueb.prototype=new Veb;_.gC=efb;_.tc=ifb;_.tI=0;_=mZb.prototype=new KTb;_.gC=GZb;_.xc=HZb;_.Mc=IZb;_.tI=226;_.b=null;var nZb,oZb,pZb,qZb,rZb,sZb,tZb;_=JZb.prototype=new Um;_.gC=LZb;_.tI=0;_=MZb.prototype=new Um;_.gC=QZb;_.tI=0;_.b=null;_.d=null;_=RZb.prototype=new Um;_.gC=TZb;_.tI=227;_.b=0;_.c=null;var s0=Pic(AGc,e2c),O5=Pic(FIc,f2c),v9=Oic(gLc,g2c),P5=Pic(FIc,h2c),M5=Pic(FIc,i2c),N5=Pic(FIc,j2c);ffb();