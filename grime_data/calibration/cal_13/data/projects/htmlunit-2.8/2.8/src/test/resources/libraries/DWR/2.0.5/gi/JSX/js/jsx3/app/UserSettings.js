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
jsx3.Class.defineClass("jsx3.app.UserSettings",null,null,function(o,g){var C=jsx3.xml.Entity;var A=jsx3.util.Logger.getLogger(o.jsxclass.getName());o.PERSIST_SESSION=1;o.PERSIST_INDEFINITE=2;g.mE=null;g.El=null;g.Ge=null;g.init=function(r,e){if(e==null)e=o.PERSIST_INDEFINITE;this.mE=r;this.El=e;var ac=this.Yz();this.Ge=o._d(ac);};g.get=function(r){var B=this.Ge;for(var lb=0;lb<arguments.length;lb++){if(typeof(B)!="object"||B instanceof Array)return null;B=B[arguments[lb]];}return B;};g.set=function(r,m){var Mc=this.Ge;for(var E=0;E<arguments.length-2;E++){var Sb=Mc[arguments[E]];if(typeof(Sb)!="object"||Sb instanceof Array){Sb=Mc[arguments[E]]=null;}if(Sb==null){Sb=Mc[arguments[E]]={};}Mc=Sb;}Mc[arguments[arguments.length-2]]=arguments[arguments.length-1];return Mc;};g.remove=function(f){var sb=this.Ge;for(var M=0;M<arguments.length-1;M++){var Bb=sb[arguments[M]];if(Bb==null||typeof(Bb)!="object"||Bb instanceof Array)return;sb=Bb;}delete sb[arguments[arguments.length-1]];};g.clear=function(){this.Ge={};var bb=this.Zl();var J=this.mE.getSettings();var rb=J.get("user-settings","domain");var _=J.get("user-settings","path");this.mE.deleteCookie(bb,_,rb);};g.save=function(){var gc=o.FE(this.Ge);gc=gc.replace(/\*/g,"%2A");gc=gc.replace(/\+/g,"%2B");gc=gc.replace(/</g,"*");gc=gc.replace(/>/g,"+");gc=escape(gc);var jb=gc.length;if(jb>4096)A.warn("Cookie is dangerously large: "+jb+" bytes.");this.kl(gc);};g.Zl=function(){return this.mE.getEnv("NAMESPACE")+(this.El==o.PERSIST_SESSION?"_ses":"_ind");};g.Yz=function(){var Rb=this.Zl();var V=this.mE.getCookie(Rb,true);if(V){V=V.replace(/\*/g,"<");V=V.replace(/\+/g,">");V=unescape(V);}return V;};g.kl=function(l){var Sb=this.Zl();var fc=this.mE.getSettings();var Nc=fc.get("user-settings","domain");var I=fc.get("user-settings","path");var u=new Date();var Hc=this.El==o.PERSIST_SESSION?null:new Date(u.getFullYear()+1,u.getMonth(),u.getDate());this.mE.setCookie(Sb,l,Hc,I,Nc,null,true);};o._d=function(n){if(!n)return {};var x=new jsx3.xml.Document();x.loadXML(n);return o.Ie(x.getRootNode());};o.Ie=function(i){var yb=i.getNodeName();if(yb=="s"){return i.getValue();}else{if(yb=="n"){return Number(i.getValue());}else{if(yb=="m"){var jc={};var yc=i.getChildNodes();for(var Z=0;Z<yc.getLength();Z++){var I=yc.getItem(Z);jc[I.getAttribute("n")]=o.Ie(I);}return jc;}else{if(yb=="a"){var Vb=[];var yc=i.getChildNodes();for(var Z=0;Z<yc.getLength();Z++){Vb.push(o.Ie(yc.getItem(Z)));}return Vb;}else{if(yb=="b"){return i.getValue()=="1";}else{if(yb=="u"){return null;}else{A.warn("Cannot deserialize node name '"+yb+"'");return null;}}}}}}};o.FE=function(i){var W=new jsx3.xml.Document();W.loadXML("<m/>");var fb=W.getRootNode();for(var qc in i)o._D(i[qc],qc,fb);return fb.getXML();};o._D=function(k,c,j){var Cb=null;var ub=typeof(k);if(k==null||ub=="undefined"){Cb=j.createNode(C.TYPEELEMENT,"u");}else{if(ub=="string"){Cb=j.createNode(C.TYPEELEMENT,"s");Cb.setValue(k);}else{if(ub=="number"){Cb=j.createNode(C.TYPEELEMENT,"n");Cb.setValue(k);}else{if(ub=="boolean"){Cb=j.createNode(C.TYPEELEMENT,"b");Cb.setValue(k?"1":"0");}else{if(ub=="object"){if(k instanceof Array){Cb=j.createNode(C.TYPEELEMENT,"a");for(var Rb=0;Rb<k.length;Rb++){o._D(k[Rb],Rb.toString(),Cb);}}else{Cb=j.createNode(C.TYPEELEMENT,"m");for(var Ec in k)o._D(k[Ec],Ec,Cb);}}else{if(ub=="function"){}else{throw new jsx3.Exception("Cannot persist object of type "+ub);}}}}}}Cb.setAttribute("n",c);j.appendChild(Cb);};g.toString=function(){return "[jsx3.app.UserSettings "+this.mE.getAppPath()+"]";};});jsx3.UserSettings=jsx3.app.UserSettings;
