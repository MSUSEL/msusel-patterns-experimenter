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
jsx3.Class.defineClass("jsx3.app.Monitor",jsx3.util.Logger.FormatHandler,null,function(f,k){var t=jsx3.app.Server;f.ed=false;f.lu=jsx3.net.URIResolver.DEFAULT.resolveURI("jsx:///html/jsx3.app.Monitor.html");f.ideDidLoad=function(){f.ed=true;};k.Vt=false;k.Fz=true;k.mw=null;k.Qx=false;k.jf=null;k.cd=null;k.init=function(p){this.jsxsuper(p);};k.onAfterInit=function(){if(this.mw!=null){var jb=null;if(jsx3.Class.forName("jsx3.lang.System"))jb=jsx3.System.getApp(this.mw);if(jb!=null){this.xC(jb);}else{t.subscribe(t.EVENT_SERVER_INITED,this,"ci");}}else{this.Fz=false;this.Qx=false;this.Vt=true;this.ne();}};k.xC=function(c){this.Vt=true;this.jf=c;if(this.Qx){var nc=this;c.registerHotKey(function(o){nc.pB();},"m",false,true,true);}else{this.ne();}};k.ci=function(r){var sb=r.target;if(sb.getEnv("namespace")==this.mw){if(!f.ed||!this.Fz)this.xC(sb);t.unsubscribe(t.EVENT_SERVER_INITED,this);}};k.handle=function(b){if(this.Vt&&(!f.ed||!this.Fz)){if(this.cd){if(this.cd.closed){if(!this.Qx)this.ne();}if(!this.cd.closed&&this.cd.appendMessage){var sc=this.format(b).escapeHTML();this.cd.appendMessage(sc,jsx3.util.Logger.levelAsString(b.getLevel()));}}}};k.pB=function(){if(this.cd==null||this.cd.closed)this.ne();};k.ne=function(){this.cd=window.open(f.lu,"Monitor_"+this.getName(),"directories=no,location=no,menubar=no,status=yes,personalbar=no,titlebar=yes,toolbar=no,resizable=yes,scrollbars=no,width=500,height=400");if(this.cd){if(this.jf){if(typeof(this.cd.setName)=="function")this.cd.setName(this.jf.getEnv("namespace"));else this.cd._jsxname=this.jf.getEnv("namespace");}window.focus();}};k.getDisableInIDE=function(){return this.Fz;};k.setDisableInIDE=function(n){this.Fz=n;};k.getServerNamespace=function(){return this.mw;};k.setServerNamespace=function(n){this.mw=n;};k.getActivateOnHotKey=function(){return this.Qx;};k.setActivateOnHotKey=function(g){this.Qx=g;};});jsx3.util.Logger.Handler.registerHandlerClass(jsx3.app.Monitor.jsxclass);
