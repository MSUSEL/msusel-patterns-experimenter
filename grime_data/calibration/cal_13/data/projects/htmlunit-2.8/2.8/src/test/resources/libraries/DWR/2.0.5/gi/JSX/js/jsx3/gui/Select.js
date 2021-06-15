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
jsx3.require("jsx3.xml.Cacheable","jsx3.gui.Form","jsx3.gui.Heavyweight","jsx3.gui.Block");jsx3.Class.defineClass("jsx3.gui.Select",jsx3.gui.Block,[jsx3.gui.Form,jsx3.xml.Cacheable,jsx3.xml.CDF],function(d,l){var Qb=jsx3.util.Logger.getLogger(d.jsxclass.getName());var gc=jsx3.gui.Event;d.DEFAULTBACKGROUNDCOLOR="#ffffff";d.DEFAULTTEXT="- Select -";d.SELECTXSLURL=jsx3.resolveURI("jsx:///xsl/jsxselect.xsl");d.SELECTXSLID="JSX_SELECT_XSL";d.COMBOXSLURL=jsx3.resolveURI("jsx:///xsl/jsxcombo.xsl");d.COMBOXSLID="JSX_COMBO_XSL";d.ARROWICON=jsx3.resolveURI("jsx:///images/select/arrow.gif");d.OVERIMAGE=jsx3.resolveURI("jsx:///images/select/selectover.gif");d.SELECTEDIMAGE=jsx3.resolveURI("jsx:///images/select/selected.gif");jsx3.html.loadImages(d.ARROWICON,d.OVERIMAGE,d.SELECTEDIMAGE);d.NODATAMESSAGE="<div tabIndex='0' class='jsx30select_option' onmousedown='var e = jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble();'>- data unavailable -</div>";d.NOMATCHMESSAGE="<div tabIndex='0' class='jsx30select_option' onmousedown='var e = jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble();'>- no match found -</div>";d.Gg=new jsx3.util.MessageFormat("<div tabIndex=\"0\" class=\"jsx30select_option jsx30select_none\" onmousedown=\"var e=jsx3.gui.Event.wrap(event); jsx3.gui.Event.publish(e); e.cancelBubble();\"><span style=\"left:0px;\">{0}</span></div>");d.TYPESELECT=0;d.TYPECOMBO=1;d.TYPEAHEADDELAY=250;d.Kt=null;d.NA=null;d.ZE="jsx30curvisibleoptions";d.DEFAULTCLASSNAME="jsx30select_select";l.init=function(o,j,e,m,h,f){this.jsxsuper(o,j,e,m,h);if(f!=null)this.jsxvalue=f;};l.getXSL=function(){var U=this.jsxsupermix(true);if(U!=null)return U;var X=this.getType()==d.TYPESELECT?d.SELECTXSLURL:d.COMBOXSLURL;var w=this.getType()==d.TYPESELECT?d.SELECTXSLID:d.COMBOXSLID;var mc;return (mc=this.getServer().getCache().getDocument(w))!=null?mc:this.getServer().getCache().openDocument(X,w,true);};l.doValidate=function(){this.setValidationState(this.getValue()!=null||this.getRequired()==jsx3.gui.Form.OPTIONAL?jsx3.gui.Form.STATEVALID:jsx3.gui.Form.STATEINVALID);return this.getValidationState();};l.UZ=function(){var vc=this.getEnabled()!=jsx3.gui.Form.STATEDISABLED?this.getBackgroundColor()||d.DEFAULTBACKGROUNDCOLOR:this.getDisabledBackgroundColor()||jsx3.gui.Form.DEFAULTDISABLEDBACKGROUNDCOLOR;return "background-color:"+vc+";";};l.getType=function(){return this.jsxtype==null?d.TYPESELECT:this.jsxtype;};l.setType=function(c){this.jsxtype=c;return this;};l.getDefaultText=function(){return this.jsxdefaulttext!=null&&this.jsxdefaulttext!=null?this.jsxdefaulttext:this.AQ("jsx3.gui.Select.defaultText");};l.setDefaultText=function(b){this.jsxdefaulttext=b;return this;};l.fs=function(r,n){if(this.DY(r,n))return;var Gb=n;var pb=r.srcElement();var cb=pb.getAttribute("jsxtype");var lb=r.hasModifier();if((r.spaceKey()||r.enterKey())&&pb.getAttribute("jsxid")!=null){this.Os(r,pb.getAttribute("jsxid"));this.hide(true);}else{if(cb=="Display"||cb=="Text"||cb=="Select"){if(r.downArrow()&&!lb){this.Me();}else{return;}}else{if(r.leftArrow()||r.escapeKey()){this.hide(true);}else{if(r.downArrow()){if(lb)return;if(pb==pb.parentNode.lastChild){d.doFocus(pb.parentNode.firstChild.nextSibling);}else{d.doFocus(pb.nextSibling);}}else{if(r.upArrow()){if(lb)return;if(pb==pb.parentNode.firstChild.nextSibling){d.doFocus(pb.parentNode.lastChild);}else{d.doFocus(pb.previousSibling);}}else{if(r.tabKey()){if(r.hasModifier(true))return;this.Os(r,pb.getAttribute("jsxid"));jsx3.html[r.shiftKey()?"focusPrevious":"focusNext"](this.getRendered(r),r);this.hide(false);return;}else{return;}}}}}}r.cancelAll();};l.PF=function(c,j){if(this.DY(c,j))return;var ec=j;var Eb=c.hasModifier();if(!Eb&&(c.downArrow()||c.enterKey())){var y=jsx3.gui.Heavyweight.GO(d.ZE);if(!c.enterKey()&&y!=null&&y.getRendered(c).childNodes[0].getAttribute("jsxselid")==this.getId()){d.doFocus(y.getRendered(c).childNodes[0].childNodes[1]);}else{var R=c.enterKey()?"":this.getText();this.Me(R);}c.cancelAll();}else{if(!Eb&&c.tabKey()){var y=jsx3.gui.Heavyweight.GO(d.ZE);if(y!=null&&y.getRendered(c).childNodes[0].getAttribute("jsxselid")==this.getId()){var rc=y.getRendered(c).childNodes[0].childNodes[1];if(rc){var W=rc.getAttribute("jsxid");if(W!=null)this.Os(c,W);}this.hide(true);c.cancelAll();}}else{if(c.tabKey()&&c.shiftKey()&&!c.hasModifier(true)){jsx3.html.focusPrevious(this.getRendered(c),c);}else{if(!Eb&&(c.rightArrow()||c.leftArrow())){var Jc=c.leftArrow();var Vb=this.pz();var M=Vb.value;var Dc=jsx3.html.getSelection(Vb);if(Jc&&(Dc.getStartIndex()>0||Dc.getEndIndex()>0)||!Jc&&(Dc.getStartIndex()<M.length||Dc.getEndIndex()<M.length))c.cancelBubble();}else{var J=this.pz();var yb=J.value;jsx3.sleep(function(){if(this.getParent()==null)return;var Gc=J.value;if(yb!=Gc){this.jsxvalue=Gc;if(d.NA)window.clearTimeout(d.NA);var bb=this;d.NA=window.setTimeout(function(){if(bb.getParent()==null)return;d.NA=null;bb.Me(Gc);},d.TYPEAHEADDELAY);}},null,this);}}}}};l.df=function(o,q){this.fs(o,q);};l.pz=function(n){n=this.getRendered(n);return n?this.getType()==d.TYPECOMBO?n.childNodes[0].childNodes[0].childNodes[0]:n.childNodes[0].childNodes[0]:null;};l.show=function(){var uc=this.getRendered();if(uc)this.Me();};l.Vg=function(p,i){var Mb=p.srcElement();while(Mb!=null&&(!Mb.getAttribute||Mb.getAttribute("jsxid")==null)){Mb=Mb.parentNode;if(Mb==i)Mb=null;}if(Mb!=null)this.Os(p,Mb.getAttribute("jsxid"));this.hide(true);};d.doFocus=function(e){var pc=e.getAttribute("jsxid");if(pc!=null){e.focus();e.style.backgroundImage="url("+d.OVERIMAGE+")";}};d.doBlur=function(o){o.style.backgroundImage="url("+jsx3.gui.Block.SPACE+")";};l.QB=function(i,m){var lc=m.value;var T=this.getXML().selectSingleNode("//record[@jsxtext=\""+lc.doReplace("\"","&quot")+"\" or (not(@jsxtext) and @jsxid=\""+lc.doReplace("\"","&quot")+"\")]");if(T!=null){this.Os(i,T.getAttribute("jsxid"));}else{this.jsxvalue=lc;}};l.hide=function(k){if(d.Kt==this){var Hb=jsx3.gui.Heavyweight.GO(d.ZE);if(Hb)Hb.destroy();if(k){try{this.focus();}catch(Kc){}}gc.unsubscribeLoseFocus(this);d.Kt=null;}if(d.NA){d.NA=null;window.clearTimeout(d.NA);}};d.hideOptions=function(){if(d.Kt!=null)d.Kt.hide();};l.Me=function(o){if(this._jsxopening)return;var X=this.getRendered();if(X!=null){var C=this.getType();var Ib=X.ownerDocument;if(d.Kt!=null)d.Kt.hide(false);d.Kt=this;var Hc=this.getAbsolutePosition(Ib.getElementsByTagName("body")[0]);var mc=Hc.W;var Bb={};Bb.jsxtabindex=this.getIndex()?this.getIndex():0;Bb.jsxselectedimage=d.SELECTEDIMAGE;Bb.jsxtransparentimage=jsx3.gui.Block.SPACE;Bb.jsxdragtype="JSX_GENERIC";Bb.jsxid=this.getId();Bb.jsxselectedid=this.getValue()==null?"null":this.getValue();Bb.jsxpath=jsx3.getEnv("jsxabspath");Bb.jsxpathapps=jsx3.getEnv("jsxhomepath");Bb.jsxpathprefix=this.getUriResolver().getUriPrefix();if(o!=null)Bb.jsxtext=o;var Mb=this.getXSLParams();for(var tc in Mb)Bb[tc]=Mb[tc];var Mc=this.doTransform(Bb,C==d.TYPECOMBO);if(!jsx3.xml.Processor.supports(jsx3.xml.Processor.DISABLE_OUTPUT_ESCAPING))Mc=jsx3.html.removeOutputEscapingSpan(Mc);Mc=Mc.doReplace("<[/]*JSX_FF_WELLFORMED_WRAPPER>","");if(!Mc.match(/\<div/i)){Mc=d.Gg.format(this.AQ("jsx3.gui.Select."+(C==d.TYPESELECT?"dataUnavailable":"noMatch")));}var kb="<div style=\"height:1px;width:"+(mc-4)+"px;position:relative;left:0px;top:0px;"+"padding:0px;margin:0px 0px -1px 0px;overflow:hidden;\">&#160;</div>";var N="<div jsxselid=\""+this.getId()+"\""+this.RX(gc.KEYDOWN,"df")+this.RX(gc.CLICK,"Vg")+this.RX(gc.MOUSEDOWN,"Lk")+" jsxtype=\"Options\" class=\"jsx30select_optionlist\" style=\""+this.UZ()+"min-width:"+(mc-4)+"px;"+this.oY()+this.g0()+this.eQ()+"\">"+kb+Mc+"</div>";var Ic=new jsx3.gui.Heavyweight(d.ZE,this);Ic.setHTML(N);Ic.addXRule(X,"W","W",-2);Ic.addYRule(X,"S","N",0);Ic.addYRule(X,"N","S",-2);Ic.show();if(C==d.TYPESELECT||o==null){this._jsxopening=true;jsx3.sleep(function(){if(this.getParent()==null)return;delete this._jsxopening;try{d.doFocus(Ic.getRendered(X).childNodes[0].childNodes[1]);}catch(Kc){Qb.info("Error focusing first object: "+jsx3.NativeError.wrap(Kc));}},null,this);}else{this.pz().focus();}gc.subscribeLoseFocus(this,X,"Gd");}};l.Gd=function(r){var ac=r.event.srcElement();if(!this.containsHtmlElement(ac))this.hide(false);};l.k7=function(n,k,q){var cc=this.RL(true,n);if(k){cc.recalculate(n,k,q);var uc=cc.pQ(0);uc.recalculate({parentwidth:cc.XK(),parentheight:cc.P5()},k?k.childNodes[0]:null,q);var Z=uc.pQ(0);Z.recalculate({parentwidth:uc.XK(),parentheight:uc.P5()},k?k.childNodes[0].childNodes[0]:null,q);if(this.getType()!=d.TYPESELECT){var Ec=Z.pQ(0);Ec.recalculate({parentwidth:Z.XK(),parentheight:Z.P5()},k?jsx3.html.selectSingleElm(k,0,0,0):null,q);}}};l.T5=function(j){if(this.getParent()&&(j==null||isNaN(j.parentwidth)||isNaN(j.parentheight))){j=this.getParent().IO(this);}else{if(j==null){j={};}}var Ic=this.getRelativePosition()!=0&&(!this.getRelativePosition()||this.getRelativePosition()==jsx3.gui.Block.RELATIVE);var bc,S,wc,tc,eb;j.tagname="span";j.border=(bc=this.getBorder())!=null&&bc!=""?bc:"solid 1px #a6a6af;solid 1px #e6e6e6;solid 1px #e6e6e6;solid 1px #a6a6af";j.margin=Ic&&(S=this.getMargin())!=null&&S!=""?S:null;if(j.boxtype==null)j.boxtype=Ic?"relativebox":"box";if(j.left==null)j.left=!Ic&&!jsx3.util.strEmpty(this.getLeft())?this.getLeft():0;if(j.top==null)j.top=!Ic&&!jsx3.util.strEmpty(this.getTop())?this.getTop():0;if(j.width==null)j.width=(tc=this.getWidth())!=null?tc:100;if(j.height==null)j.height=(eb=this.getHeight())!=null?eb:18;j.padding="0 0 0 0";var kb=new jsx3.gui.Painted.Box(j);var Wb={};Wb.tagname="div";Wb.boxtype="relativebox";if((wc=this.getPadding())!=null&&wc!=""){Wb.padding=wc;}else{Wb.padding="0 19 0 0";}Wb.parentwidth=kb.XK();Wb.parentheight=kb.P5();Wb.left=0;Wb.top=0;Wb.width="100%";Wb.height="100%";var K=new jsx3.gui.Painted.Box(Wb);kb.W8(K);if(this.getType()==d.TYPESELECT){Wb={};Wb.tagname="div";Wb.boxtype="relativebox";Wb.padding="2 0 0 3";Wb.parentwidth=K.XK();Wb.parentheight=K.P5();Wb.width="100%";Wb.height="100%";var Rb=new jsx3.gui.Painted.Box(Wb);K.W8(Rb);}else{Wb={};Wb.tagname="span";Wb.boxtype="relativebox";Wb.parentwidth=K.XK();Wb.parentheight=K.P5();Wb.width="100%";Wb.height="100%";Wb.padding="0 0 0 3";Wb.border="0px;solid 1px #c8c8d5;0px;0px";var Rb=new jsx3.gui.Painted.Box(Wb);K.W8(Rb);Wb={};Wb.tagname="input[text]";Wb.boxtype="relativebox";Wb.parentwidth=Rb.XK();Wb.parentheight=Rb.P5();Wb.width="100%";Wb.height="100%";Wb.padding="0 0 0 0";Wb.empty=true;Wb.border="0px";var gb=new jsx3.gui.Painted.Box(Wb);Rb.W8(gb);}return kb;};l.paint=function(){this.applyDynamicProperties();var Wb=this.getId();var yc=this.getEnabled()==jsx3.gui.Form.STATEENABLED;var lb={};if(yc){lb[gc.MOUSEDOWN]=true;if(this.getType()==d.TYPECOMBO){lb[gc.KEYDOWN]="PF";lb[gc.FOCUS]=true;}else{lb[gc.KEYDOWN]="fs";}}var mc=this.lM(lb,0);var Bb=this.renderAttributes(null,true);var rb=this.RL(true);rb.setAttributes(" id=\""+Wb+"\" label=\""+this.getName()+"\" class=\""+this.CH()+"\" jsxtype=\"Select\" "+Bb+mc+this.CI());rb.setStyles(this.QP()+this.UZ()+"background-image:url("+d.ARROWICON+");background-repeat:no-repeat;background-position:right 0px;"+this.d9()+this.T1()+this.MU()+this.iN());var Gc=rb.pQ(0);Gc.setAttributes(" class=\"jsx30select_display\" jsxtype=\"Display\" "+this.vH());Gc.setStyles("");if(this.getType()==d.TYPESELECT){var P=Gc.pQ(0);P.setAttributes(" jsxtype=\"Text\" class=\"jsx30select_text\" unselectable=\"on\" ");P.setStyles(this.oY()+this.g0()+this.QP()+this.eQ());var kc=rb.paint().join(Gc.paint().join(P.paint().join(this.AN())));}else{var P=Gc.pQ(0);P.setAttributes("class=\"jsx30combo\"");var Hb=P.pQ(0);Hb.setAttributes(this.CI()+this.paintMaxLength()+" class=\"jsx30combo_text\" value=\""+this.AN().doReplace("\"","&quot")+"\" jsxtype=\"Text\" "+this.RX(gc.BLUR,"QB",3));Hb.setStyles(this.oY()+this.g0()+this.QP()+this.eQ());var kc=rb.paint().join(Gc.paint().join(P.paint().join(Hb.paint().join(""))));}return kc;};l.mL=function(m,s){if(!m.leftButton())return;if(m.srcElement().tagName.toLowerCase()!="input")this.Me();};l.SU=function(f,g){this.pz(g).focus();};l.Lk=function(n,r){n.cancelBubble();};l.setText=function(s){this.WG(s);return this;};l.WG=function(f){var fb;if(fb=this.pz())fb[this.getType()==d.TYPECOMBO?"value":"innerHTML"]=f;};l.focus=function(){var mb=this.getType()==d.TYPECOMBO?this.pz():this.getRendered();if(mb)mb.focus();return mb;};l.setValue=function(j){this.Os(this.isOldEventProtocol(),j);return this;};l.Os=function(q,i){if(i!=this.getValue()){var P=true;if(q instanceof gc)P=this.doEvent(jsx3.gui.Interactive.BEFORE_SELECT,{objEVENT:q,strRECORDID:i});if(P===false)return;this.jsxvalue=i;this.redrawRecord(i,jsx3.xml.CDF.UPDATE);if(q)this.doEvent(jsx3.gui.Interactive.SELECT,{objEVENT:q instanceof gc?q:null,strRECORDID:i});}};l.getValue=function(){return this.jsxvalue;};l.getText=function(){var Lb=this.getRecordNode(this.getValue());if(Lb!=null){var Zb=Lb.getAttribute("jsxtext");return Zb!=null?Zb:Lb.getAttribute("jsxid");}else{return this.getType()==d.TYPECOMBO||this.getValue()!=null?this.getValue():this.getDefaultText();}};l.redrawRecord=function(q,i){if(this.getValue()==q){var t=this.getRecordNode(q);if(t!=null){var Cb=t.getAttribute("jsxtext");this.WG(Cb!=null?Cb:t.getAttribute("jsxid"));}else{this.WG(this.getType()==d.TYPESELECT?this.getDefaultText():q);}}return this;};l.onDestroy=function(j){this.jsxsuper(j);this.onDestroyCached(j);};d.getVersion=function(){return "3.0.00";};l.containsHtmlElement=function(h){var wb=jsx3.gui.Heavyweight.GO(d.ZE);return this.jsxsuper(h)||wb!=null&&wb.containsHtmlElement(h);};l.getMaxLength=function(){return this.jsxmaxlength!=null?this.jsxmaxlength:null;};l.setMaxLength=function(k){k=parseInt(k);this.jsxmaxlength=k>0?k:null;return this;};l.paintMaxLength=function(m){return this.getMaxLength()!=null?" maxlength=\""+this.getMaxLength()+"\" ":"";};l.CH=function(){var _b=this.getClassName();return d.DEFAULTCLASSNAME+(_b?" "+_b:"");};l.emInit=function(k){this.jsxsupermix(k);this.subscribe(jsx3.gui.Interactive.SELECT,this,"Ti");};l.emCollapseEdit=function(m){this.hide(false);};l.Ti=function(q){this.commitEditMask(q.context.objEVENT,true);};});jsx3.Select=jsx3.gui.Select;
