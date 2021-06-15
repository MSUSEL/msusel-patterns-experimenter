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
function IF(){}
function pdb(){}
function Bdb(){return ZS}
function SF(){return lQ}
function WF(){return this.c}
function VF(a){throw Mhc(new Khc,PDc+a+VDc)}
function KF(){KF=Zhc;JF=Nfc(new Lfc)}
function MF(d,a){var b=d.b;for(var c in b){b.hasOwnProperty(c)&&a.hc(c)}}
function TF(a){KF();var b;b=kO(JF.ec(a),51);if(!b){b=LF(new IF,a);JF.fc(a,b)}return b}
function UF(a){var b,c,d;c=(d=Tfc(new Rfc),MF(this,d),d);b=RDc+a+SDc+this;c.a.gc()<20&&(b+=TDc+c);b+=UDc+this.a;throw Mhc(new Khc,b)}
function Fdb(){var a;while(udb){a=udb;udb=udb.b;!udb&&(vdb=null);Yib(a.a.a,zob())}}
function NF(c,b){try{typeof $wnd[b]!=QDc&&VF(b);c.b=$wnd[b]}catch(a){VF(b)}}
function dRb(a,b,c){(a.a.zd(b),a.a.h.rows[b])[nmc]=c}
function LF(a,b){KF();if(b==null||X9b(pjc,b)){throw Q8b(new N8b,MDc)}a.c=NDc+b;NF(a,b);if(!a.b){throw Mhc(new Khc,ODc+b+PDc)}a.a=new Array;return a}
function zob(){var a,b,c,d,e,f,g,h,i,l,m,n,o,p,q,r;e=d1b(new a1b);f=wNb(new lNb,XDc);f.J.dir=Mkc;f.J.style[_mc]=anc;e1b(e,wNb(new lNb,YDc));e1b(e,f);h=RPb(new kPb);g=TF(ZDc);d=(l=Tfc(new Rfc),MF(g,l),l);a=0;for(c=(m=_K(d.a).b.ib(),$cc(new Ycc,m));c.a.lb();){b=kO((n=kO(c.a.mb(),47),n.lc()),1);i=PF(g,b);h.yd(0,a);o=(p=h.i.a.h.rows[0].cells[a],zPb(h,p,b==null),p);b!=null&&(o.innerHTML=b||pjc,undefined);h.yd(1,a);q=(r=h.i.a.h.rows[1].cells[a],zPb(h,r,i==null),r);i!=null&&(q.innerHTML=i||pjc,undefined);++a}dRb(h.k,0,$Dc);dRb(h.k,1,_Dc);e1b(e,wNb(new lNb,aEc));e1b(e,h);return e}
function Cdb(){xdb=true;wdb=(zdb(),new pdb);oo((lo(),ko),4);!!$stats&&$stats(Uo(WDc,Bjc,null,null));wdb.vc();!!$stats&&$stats(Uo(WDc,sCc,null,null))}
function PF(e,a){a=String(a);var b=e.b;var c=b[a];var d=e.a;d.unshift(a);d.length>60&&d.splice(30);(c==null||!b.hasOwnProperty(a))&&e.tb(a);return String(c)}
var UDc='\n accessed keys: ',TDc='\n keys found: ',SDc="' in ",VDc="' is not a JavaScript object and cannot be used as a Dictionary",YDc='<b>Cet exemple interagit avec le JavaScript variable suivant:<\/b>',aEc='<br><br>',XDc='<pre>var userInfo = {\n&nbsp;&nbsp;name: "Amelie Crutcher",\n&nbsp;&nbsp;timeZone: "EST",\n&nbsp;&nbsp;userID: "123",\n&nbsp;&nbsp;lastLogOn: "2/2/2006"\n};<\/pre>\n',cEc='AsyncLoader4',MDc='Cannot create a Dictionary with a null or empty name',RDc="Cannot find '",ODc="Cannot find JavaScript object with the name '",bEc='Dictionary',NDc='Dictionary ',_Dc='cw-DictionaryExample-dataRow',$Dc='cw-DictionaryExample-headerRow',WDc='runCallbacks4',ZDc='userInfo';_=IF.prototype=new km;_.gC=SF;_.tb=UF;_.tS=WF;_.tI=42;_.a=null;_.b=null;_.c=null;var JF;_=pdb.prototype=new qdb;_.gC=Bdb;_.vc=Fdb;_.tI=0;var lQ=p8b(Yvc,bEc),ZS=p8b(cwc,cEc);Cdb();