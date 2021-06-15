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
jsx3.require("jsx3.gui.Form","jsx3.gui.Heavyweight","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.DatePicker",jsx3.gui.Block,[jsx3.gui.Form],function(f,g){var x=jsx3.gui.Event;var Dc=jsx3.gui.Interactive;var v=jsx3.util.DateFormat;f.DEFAULT_FORMAT="M/d/yyyy";f.DEFAULT_WEEK_START=0;f.Mp=jsx3.resolveURI("jsx:///images/jsxdatepicker/next.gif");f.mr=jsx3.resolveURI("jsx:///images/jsxdatepicker/previous.gif");f.Cz=jsx3.resolveURI("jsx:///images/jsxdatepicker/open.gif");jsx3.html.loadImages(f.Mp,f.mr,f.Cz);g.jsxformat=null;g.jsxfirstweekday=null;g._jsxtB=null;g._jsxot=null;g._jsxdh=false;g._jsxvz=null;g.init=function(o,l,h,j,i){this.jsxsuper(o,l,h,j,i);this.jsxyear=1970;this.jsxmonth=0;this.jsxdate=1;};g.iA=function(){var S=this.tl(true);this.IC();S.show();var Y=S.getRendered();Y.childNodes[0].focus();this.doEvent(Dc.SHOW);this._jsxdh=true;x.subscribeLoseFocus(this,Y,"bv");};g.bv=function(p,o){var xb=this.tl();if(xb!=null){xb.destroy();this.doEvent(Dc.HIDE);x.unsubscribeLoseFocus(this);}this._jsxdh=false;if(o){var Cc=this.iv();if(Cc)Cc.focus();}};g.getFormat=function(){return this.jsxformat!=null?this.jsxformat:0;};g.setFormat=function(d){this.jsxformat=d;delete this._jsxvz;};g.tr=function(){if(this._jsxvz==null||!this._jsxvz.getLocale().equals(this.b2())){try{var Bb=this.getFormat();if(typeof(Bb)=="number")this._jsxvz=v.getDateInstance(Bb,this.b2());else this._jsxvz=new v(Bb,this.b2());}catch(Kc){jsx3.util.Logger.GLOBAL.warn("Invalid date format: "+this.getFormat(),jsx3.NativeError.wrap(Kc));this._jsxvz=v.getDateInstance(null,this.b2());}}return this._jsxvz;};g.getFirstDayOfWeek=function(){return this.jsxfirstweekday!=null?this.jsxfirstweekday:this.AQ("date.firstWeekDay");};g.setFirstDayOfWeek=function(r){if(r>=0&&r<=6){this.jsxfirstweekday=r;}else{throw new jsx3.IllegalArgumentException("jsxfirstweekday",r);}};g.getDate=function(){if(this.jsxyear!=null)return new Date(this.jsxyear,this.jsxmonth,this.jsxdate);else return null;};g._m=function(){var Ob=this.getDate();if(Ob!=null){return this.tr().format(Ob);}else{var A=this.getFormat();if(typeof(A)=="number")return v.getDateInstance(A,this.b2());else return this.getFormat();}};g.getValue=function(){var ac=this.iv();return ac!=null?ac.value:null;};g.setDate=function(d){if(d!=null){this.jsxyear=d.getFullYear();this.jsxmonth=d.getMonth();this.jsxdate=d.getDate();}else{this.jsxyear=this.jsxmonth=this.jsxdate=null;}var nb=this.iv();if(nb!=null)nb.value=this._m();};g.Or=function(q){var E=new Date(this._jsxtB+q,this._jsxot,1);this._jsxtB=E.getFullYear();this._jsxot=E.getMonth();this.IC();this.CD(true,q>0);};g.ir=function(m){var Pb=new Date(this._jsxtB,this._jsxot+m,1);this._jsxtB=Pb.getFullYear();this._jsxot=Pb.getMonth();this.IC();this.CD(false,m>0);};g.CD=function(k,m){var qb=this.tl();if(qb!=null){var bb=qb.getRendered();if(bb!=null){var Ac=jsx3.html.getElementById(bb,this.getId()+"_"+(m?"u":"d")+(k?"y":"m"),true);Ac.focus();}}};g.wj=function(e,h){var Hb=h.id.substring(h.id.lastIndexOf("_")+1).split("-");var Cc=this.getDate();var H=new Date(Hb[0],Hb[1],Hb[2]);if(Cc==null||H.getTime()!=Cc.getTime()){if(this.doEvent(Dc.CHANGE,{objEVENT:e,oldDATE:Cc,newDATE:H})!==false){this.setDate(H);this.IC();var J=this.iv();J.value=this._m();}}this.bv(null,true);};g.IC=function(){var Ob=this.tl();if(Ob!=null)Ob.setHTML(this.sq(this._jsxtB,this._jsxot),true);};g.yD=function(o,q){if(this._jsxdh)return;if(this.getEnabled()!=jsx3.gui.Form.STATEENABLED)return;var Db=this.getDate();if(this.jsxyear!=null){this._jsxtB=this.jsxyear;this._jsxot=this.jsxmonth;}else{Db=new Date();this._jsxtB=Db.getFullYear();this._jsxot=Db.getMonth();}this.iA();};g.gI=function(j,c){if(c.value==""){this.setDate(null);}else{try{var mb=this.tr().parse(c.value);if(this.doEvent(Dc.CHANGE,{objEVENT:j,oldDATE:this.getDate(),newDATE:mb})!==false)this.setDate(mb);}catch(Kc){c.value=this._m();}}};g.U2=function(c,j){var F=c.getWheelDelta();if(F!=0){var jb=this.getDate(),Ic=null;if(jb!=null){Ic=new Date(jb.getFullYear(),jb.getMonth(),jb.getDate()+(F>0?1:-1));}else{jb=new Date();Ic=new Date(jb.getFullYear(),jb.getMonth(),jb.getDate());}if(this.doEvent(Dc.CHANGE,{objEVENT:c,oldDATE:jb,newDATE:Ic})!==false)this.setDate(Ic);}};g._4=function(r,n){if(r.rightButton()){this.bv();this.jsxsupermix(r,n);}};g.DY=function(e,h){if(!e.hasModifier()&&(e.downArrow()||e.upArrow()||e.enterKey())){this.yD(e,h);e.cancelAll();}};g.Rf=function(h,e){if(h.enterKey()||h.spaceKey())this.yD(h,e);};g.zz=function(a,l){var Ic=a.getType()=="keypress";if(!Ic&&a.escapeKey()){this.bv(null,true);}else{if(!Ic&&a.enterKey()){var qb=a.srcElement();x.dispatchMouseEvent(qb,x.CLICK);}else{if(a.tabKey()){if(a.srcElement()==l){if(a.shiftKey()){a.cancelAll();this.bv(null,true);}}else{if(a.srcElement().getAttribute("tabreturn")){a.cancelAll();this.CD(true,false);}}}}}};g.focus=function(){var t=this.iv();if(t)t.focus();};g.iv=function(r){if(r==null)r=this.getRendered();if(r)return r.childNodes[0].childNodes[0];};g.repaint=function(){delete this._jsxvz;return this.jsxsuper();};g.k7=function(p,m,b){var Ob=this.RL(true,p);if(m){Ob.recalculate(p,m,b);var lc=Ob.pQ(0);lc.recalculate({width:Ob.XK(),height:Ob.P5()},m?m.childNodes[0]:null,b);var zb=lc.pQ(0);zb.recalculate({width:lc.XK()-16,height:lc.P5()},m?this.iv(m):null,b);var pc=lc.pQ(1);pc.recalculate({left:lc.XK()-16},m?m.childNodes[0].childNodes[1]:null,b);}};g.T5=function(e){if(this.getParent()&&(e==null||isNaN(e.parentwidth)||isNaN(e.parentheight))){e=this.getParent().IO(this);}else{if(e==null){e={};}}var vc=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==jsx3.gui.Block.RELATIVE);var Y=vc?null:this.getLeft();var z=vc?null:this.getTop();var W,H,Ac,Lc,Ab;e.boxtype=vc?"relativebox":"box";e.tagname="span";if(vc&&(Ac=this.getMargin())!=null&&Ac!="")e.margin=Ac;if(e.left==null&&Y!=null)e.left=Y;if(e.top==null&&z!=null)e.top=z;if(e.width==null)e.width=(Lc=this.getWidth())!=null&&Lc!=""?Lc:100;if(e.height==null)e.height=(Ab=this.getHeight())!=null&&Ab!=""?Ab:18;var bc=new jsx3.gui.Painted.Box(e);var fb={};fb.tagname="div";fb.boxtype="inline";fb.width=bc.XK();fb.height=bc.P5();if((W=this.getPadding())!=null&&W!="")fb.padding=W;if((H=this.getBorder())!=null&&H!="")fb.border=H;var U=new jsx3.gui.Painted.Box(fb);bc.W8(U);fb={};fb.tagname="input[text]";fb.empty=true;fb.boxtype="box";fb.left=0;fb.top=0;fb.width=U.XK()-16;fb.height=U.P5();fb.padding="0 0 0 2";fb.border="solid 1px #9898a5;solid 1px #d8d8e5;solid 1px #d8d8e5;solid 1px #9898a5";var R=new jsx3.gui.Painted.Box(fb);U.W8(R);fb={};fb.tagname="span";fb.boxtype="box";fb.left=U.XK()-16;fb.top=0;fb.width=13;fb.height=18;fb.padding="1 1 1 1";var Ec=new jsx3.gui.Painted.Box(fb);U.W8(Ec);return bc;};g.paint=function(){this.applyDynamicProperties();var qc="jsx3.GO('"+this.getId()+"')";var cb=this.getEnabled()==jsx3.gui.Form.STATEENABLED?this.getBackgroundColor():this.getDisabledBackgroundColor();var Fb={};Fb[x.CHANGE]=true;Fb[x.MOUSEWHEEL]=true;Fb[x.KEYDOWN]="DY";var ac=this.lM(Fb,2);var t=this.renderAttributes(null,true);var Q=this.RL(true);Q.setAttributes("id=\""+this.getId()+"\" class=\"jsx30datepicker\" "+"label=\""+this.getName()+"\" "+t);Q.setStyles(this.d9()+this.T1()+this.MU()+this.iN());var lc=Q.pQ(0);var ib=lc.pQ(0);ib.setAttributes(" type=\"text\" value=\""+this._m()+"\" "+this.CI()+this.vH()+this.WP()+ac);ib.setStyles(this.oY()+this.QP()+this.D6()+this.eQ()+this.g0()+(cb!=null?"background-color:"+cb+";":"")+(this.getBackground()!=null?this.getBackground()+";":""));var dc=lc.pQ(1);dc.setAttributes(" class=\"open\" "+this.RX(x.CLICK,"yD",2)+this.RX(x.KEYDOWN,"Rf",2)+this.CI());dc.setStyles("background-image:url("+f.Cz+");");return Q.paint().join(lc.paint().join(ib.paint().join("")+dc.paint().join("&#160;")+"&#160;"));};g.sq=function(o,s){var wc=this.getId();var kb="jsx3.GO('"+wc+"')";var t="Or";var Fb="ir";var xb=this.CI();return "<span class=\"jsx3_dp_cal\""+xb+" style=\"z-index:5000;position:absolute;left:0px;top:0px;\""+this.RX(x.KEYDOWN,"zz")+this.RX(x.KEYPRESS,"zz")+">"+"<table cellspacing=\"0\" class=\"jsx3_dp_cal\">"+"<tr class=\"year\">"+"<td class=\"prev\""+xb+" id=\""+wc+"_dy\" onclick=\""+kb+"."+t+"(-1);\"><img src=\""+f.mr+"\"/></td>"+"<td class=\"title\">"+o+"</td>"+"<td class=\"next\""+xb+" id=\""+wc+"_uy\" onclick=\""+kb+"."+t+"(1);\"><img src=\""+f.Mp+"\"/></td>"+"</tr>"+"<tr class=\"month\">"+"<td class=\"prev\""+xb+" id=\""+wc+"_dm\" onclick=\""+kb+"."+Fb+"(-1);\"><img src=\""+f.mr+"\"/></td>"+"<td class=\"title\">"+this.yo(s)+"</td>"+"<td class=\"next\""+xb+" id=\""+wc+"_um\" onclick=\""+kb+"."+Fb+"(1);\"><img src=\""+f.Mp+"\"/></td>"+"</tr>"+"<tr class=\"days\"><td class=\"cal\" colspan=\"3\">"+this.rl(o,s)+"</td></tr></table>"+"</span>";};f.Ju=function(a){var Nc=a.getMonth();return f.qp[Nc]||((new Date(a.getYear(),Nc,29)).getMonth()==Nc?29:28);};f.qp=[31,null,31,30,31,30,31,31,30,31,30,31];g.rl=function(h,m){var Jc=this.getServer();var fb=new Date(h,m,1);var Ac=this.getFirstDayOfWeek();var Qb=fb.getDay();var z=f.Ju(fb);var rc=this.getId();var vb="jsx3.GO('"+rc+"')";var Hb=[];Hb.push("<table cellspacing=\"0\" class=\"jsx3_dp_month\">");Hb.push("<tr>");for(var cc=Ac;cc<Ac+7;cc++){Hb.push("<th>"+this.ar(cc%7,Jc)+"</th>");}Hb.push("</tr>");var sc=new Date(h,m,1-(Qb-Ac+7)%7);var ob=sc.getDate();var gb=new Date(h,m+1,1);var Gc=gb.getDate();var Lb=new Date();var Cc=this.CI();var dc=this.RX(x.CLICK,"wj");var cc=0;while(cc<=z){Hb.push("<tr>");for(var U=0;U<7;U++){var _=h;var Gb=m;var vc=null;var bb=null;var Bb="over";if(cc==0){if((U+Ac)%7==Qb){cc=1;}else{_=sc.getFullYear();Gb=sc.getMonth();bb="prev";vc=ob;ob++;}}if(cc>0){if(cc<=z){bb="normal";vc=cc;cc++;}else{_=gb.getFullYear();Gb=gb.getMonth();bb="next";vc=Gc;Gc++;}}var W=U==6&&cc>z;if(this.jsxyear==_&&this.jsxmonth==Gb&&this.jsxdate==vc)bb="selected";var Ob=vc==Lb.getDate()&&Gb==Lb.getMonth()&&_==Lb.getFullYear();if(Ob){bb=bb+" today";Bb=Bb+" today";}Hb.push("<td id=\""+rc+"_"+_+"-"+Gb+"-"+vc+"\""+(W?" tabreturn=\"true\"":"")+Cc+" class=\""+bb+"\" onmouseover=\"this.className='"+Bb+"'\" onmouseout=\"this.className='"+bb+"'\""+dc+">"+vc+"</td>");}Hb.push("</tr>");}Hb.push("</table>");return Hb.join("");};g.ar=function(i,j){if(j==null)j=this.getServer();return this.AQ("date.day")[i%7].substring(0,2);};g.yo=function(j,q){if(q==null)q=this.getServer();return this.AQ("date.month")[j%12];};g.tl=function(p){var Ib="jsxDatePicker"+this.getId();var Wb=jsx3.gui.Heavyweight.GO(Ib);if(p){if(Wb!=null)Wb.destroy();var R=this.iv();Wb=new jsx3.gui.Heavyweight(Ib,this);Wb.addXRule(R,"E","W",0);Wb.addXRule(R,"W","E",0);Wb.addYRule(R,"N","N",0);Wb.addYRule(R,"S","S",0);}return Wb;};g.doValidate=function(){var Q=this.getDate()!=null||this.getRequired()==jsx3.gui.Form.OPTIONAL?jsx3.gui.Form.STATEVALID:jsx3.gui.Form.STATEINVALID;this.setValidationState(Q);return Q;};g.containsHtmlElement=function(l){var Eb=this.tl();return this.jsxsuper(l)||Eb!=null&&Eb.containsHtmlElement(l);};g.emSetValue=function(s){var Ic=this.emGetSession();var G=null;if(jsx3.util.strEmpty(s==null)){Ic.datetype="int";}else{if(!isNaN(s)&&!isNaN(parseInt(s))){G=new Date();G.setTime(parseInt(s));Ic.datetype="int";}else{var lc=this.tr();try{G=lc.parse(s);Ic.datetype="format";}catch(Kc){G=new Date(s);if(isNaN(G)){G=null;}else{Ic.datetype="native";}}}}this.setDate(G);};g.emGetValue=function(){var I=this.getDate();if(I==null)return null;var eb=this.emGetSession().datetype||"";switch(eb){case "format":return this.tr().format(I);case "native":return I.toString();default:return I.getTime().toString();}};g.emCollapseEdit=function(k){this.bv(k,false);};});jsx3.DatePicker=jsx3.gui.DatePicker;
