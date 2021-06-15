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
jsx3.require("jsx3.gui.Interactive");jsx3.Class.defineInterface("jsx3.gui.Alerts",null,function(h,i){var G=jsx3.gui.Interactive;i.getAlertsParent=jsx3.Method.newAbstract();i.alert=function(c,r,g,o,s){var hb=jsx3.net.URIResolver.JSX;var Kc=this.getAlertsParent().loadAndCache("xml/components/dialog_alert.xml",false,null,hb);var vc=Kc.getDescendantOfName("ok");if(c!=null)Kc.getDescendantOfName("title").setText(c);if(r!=null)Kc.getDescendantOfName("message").setText(r);if(o===false)Kc.hideButton();else{if(o!=null)vc.setText(o);}if(g!=null){var tb="Ye";vc.Ye=g;vc.setEvent("this."+tb+"(this.getAncestorOfType(jsx3.gui.Dialog));",G.EXECUTE);}this.configureAlert(Kc,s);this.getAlertsParent().paintChild(Kc);Kc.focus();return Kc;};i.prompt=function(g,c,k,e,m,q,o){var lb=jsx3.net.URIResolver.JSX;var tb=this.getAlertsParent().loadAndCache("xml/components/dialog_prompt.xml",false,null,lb);var kc=tb.getDescendantOfName("ok");var vc=tb.getDescendantOfName("cancel");if(g!=null)tb.getDescendantOfName("title").setText(g);if(c!=null)tb.getDescendantOfName("message").setText(c);if(m!=null)kc.setText(m);if(q!=null)vc.setText(q);if(k!=null){var _="Ye";kc.Ye=k;kc.setEvent("var d = this.getAncestorOfType(jsx3.gui.Dialog); this."+_+"(d, d.getDescendantOfName('value').getValue());",G.EXECUTE);}if(e!=null){var _="Ye";vc.Ye=e;vc.setEvent("this."+_+"(this.getAncestorOfType(jsx3.gui.Dialog));",G.EXECUTE);}this.configureAlert(tb,o);this.getAlertsParent().paintChild(tb);jsx3.sleep(function(){tb.getDescendantOfName("value").focus();});return tb;};i.confirm=function(n,j,r,q,d,o,m,g,a,s){var ub=jsx3.net.URIResolver.JSX;var Bc=this.getAlertsParent().loadAndCache("xml/components/dialog_confirm.xml",false,null,ub);var rc=Bc.getDescendantOfName("ok");var lb=Bc.getDescendantOfName("cancel");var xb=Bc.getDescendantOfName("no");var jc=[rc,lb,xb];m=m!=null?m-1:0;if(n!=null)Bc.getDescendantOfName("title").setText(n);if(j!=null)Bc.getDescendantOfName("message").setText(j);if(d!=null)rc.setText(d);if(o!=null)lb.setText(o);if(q!=null){var xc="Ye";lb.Ye=q;lb.setEvent("this."+xc+"(this.getAncestorOfType(jsx3.gui.Dialog));",G.EXECUTE);}if(r!=null){var xc="Ye";rc.Ye=r;rc.setEvent("this."+xc+"(this.getAncestorOfType(jsx3.gui.Dialog));",G.EXECUTE);}if(g!=null||a!=null||m==3){if(a)xb.setText(a);if(g){var xc="Ye";xb.Ye=g;xb.setEvent("this."+xc+"(this.getAncestorOfType(jsx3.gui.Dialog));",G.EXECUTE);}xb.setDisplay(jsx3.gui.Block.DISPLAYBLOCK);}var uc=jc[m];if(uc){uc.setFontWeight("bold");Bc.registerHotKey(function(b){if(b.enterKey()){this.getDescendantOfName(uc.getName()).doExecute(b);b.cancelBubble();}},jsx3.gui.Event.KEY_ENTER,false,false,false);}this.configureAlert(Bc,s);this.getAlertsParent().paintChild(Bc);Bc.focus();return Bc;};i.configureAlert=function(m,k){if(k==null)return;if(k.width)m.setWidth(k.width,false);if(k.height)m.setHeight(k.height,false);if(k.noTitle)m.removeChild(m.getChild("title"));if(k.nonModal)m.setModal(jsx3.gui.Dialog.NONMODAL);};});jsx3.Alerts=jsx3.gui.Alerts;
