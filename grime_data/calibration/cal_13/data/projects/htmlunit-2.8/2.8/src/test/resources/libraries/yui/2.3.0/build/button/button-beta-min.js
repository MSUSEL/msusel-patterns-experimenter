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
/*
Copyright (c) 2007, Yahoo! Inc. All rights reserved.
Code licensed under the BSD License:
http://developer.yahoo.net/yui/license.txt
version: 2.3.0
*/

(function(){var Dom=YAHOO.util.Dom,Event=YAHOO.util.Event,Lang=YAHOO.lang,Overlay=YAHOO.widget.Overlay,Menu=YAHOO.widget.Menu,m_oButtons={},m_oOverlayManager=null,m_oSubmitTrigger=null,m_oFocusedButton=null;function createInputElement(p_sType,p_sName,p_sValue,p_bChecked){var oInput,sInput;if(Lang.isString(p_sType)&&Lang.isString(p_sName)){if(YAHOO.env.ua.ie){sInput="<input type=\""+p_sType+"\" name=\""+
p_sName+"\"";if(p_bChecked){sInput+=" checked";}
sInput+=">";oInput=document.createElement(sInput);}
else{oInput=document.createElement("input");oInput.name=p_sName;oInput.type=p_sType;if(p_bChecked){oInput.checked=true;}}
oInput.value=p_sValue;return oInput;}}
function setAttributesFromSrcElement(p_oElement,p_oAttributes){var sSrcElementNodeName=p_oElement.nodeName.toUpperCase(),me=this,oAttribute,oRootNode,sText;function setAttributeFromDOMAttribute(p_sAttribute){if(!(p_sAttribute in p_oAttributes)){oAttribute=p_oElement.getAttributeNode(p_sAttribute);if(oAttribute&&("value"in oAttribute)){p_oAttributes[p_sAttribute]=oAttribute.value;}}}
function setFormElementProperties(){setAttributeFromDOMAttribute("type");if(p_oAttributes.type=="button"){p_oAttributes.type="push";}
if(!("disabled"in p_oAttributes)){p_oAttributes.disabled=p_oElement.disabled;}
setAttributeFromDOMAttribute("name");setAttributeFromDOMAttribute("value");setAttributeFromDOMAttribute("title");}
switch(sSrcElementNodeName){case"A":p_oAttributes.type="link";setAttributeFromDOMAttribute("href");setAttributeFromDOMAttribute("target");break;case"INPUT":setFormElementProperties();if(!("checked"in p_oAttributes)){p_oAttributes.checked=p_oElement.checked;}
break;case"BUTTON":setFormElementProperties();oRootNode=p_oElement.parentNode.parentNode;if(Dom.hasClass(oRootNode,this.CSS_CLASS_NAME+"-checked")){p_oAttributes.checked=true;}
if(Dom.hasClass(oRootNode,this.CSS_CLASS_NAME+"-disabled")){p_oAttributes.disabled=true;}
p_oElement.removeAttribute("value");p_oElement.setAttribute("type","button");break;}
p_oElement.removeAttribute("id");p_oElement.removeAttribute("name");if(!("tabindex"in p_oAttributes)){p_oAttributes.tabindex=p_oElement.tabIndex;}
if(!("label"in p_oAttributes)){sText=sSrcElementNodeName=="INPUT"?p_oElement.value:p_oElement.innerHTML;if(sText&&sText.length>0){p_oAttributes.label=sText;}}}
function initConfig(p_oConfig){var oAttributes=p_oConfig.attributes,oSrcElement=oAttributes.srcelement,sSrcElementNodeName=oSrcElement.nodeName.toUpperCase(),me=this;if(sSrcElementNodeName==this.NODE_NAME){p_oConfig.element=oSrcElement;p_oConfig.id=oSrcElement.id;Dom.getElementsBy(function(p_oElement){switch(p_oElement.nodeName.toUpperCase()){case"BUTTON":case"A":case"INPUT":setAttributesFromSrcElement.call(me,p_oElement,oAttributes);break;}},"*",oSrcElement);}
else{switch(sSrcElementNodeName){case"BUTTON":case"A":case"INPUT":setAttributesFromSrcElement.call(this,oSrcElement,oAttributes);break;}}}
YAHOO.widget.Button=function(p_oElement,p_oAttributes){var fnSuperClass=YAHOO.widget.Button.superclass.constructor,oConfig,oElement;if(arguments.length==1&&!Lang.isString(p_oElement)&&!p_oElement.nodeName){if(!p_oElement.id){p_oElement.id=Dom.generateId();}
fnSuperClass.call(this,(this.createButtonElement(p_oElement.type)),p_oElement);}
else{oConfig={element:null,attributes:(p_oAttributes||{})};if(Lang.isString(p_oElement)){oElement=Dom.get(p_oElement);if(oElement){if(!oConfig.attributes.id){oConfig.attributes.id=p_oElement;}
oConfig.attributes.srcelement=oElement;initConfig.call(this,oConfig);if(!oConfig.element){oConfig.element=this.createButtonElement(oConfig.attributes.type);}
fnSuperClass.call(this,oConfig.element,oConfig.attributes);}}
else if(p_oElement.nodeName){if(!oConfig.attributes.id){if(p_oElement.id){oConfig.attributes.id=p_oElement.id;}
else{oConfig.attributes.id=Dom.generateId();}}
oConfig.attributes.srcelement=p_oElement;initConfig.call(this,oConfig);if(!oConfig.element){oConfig.element=this.createButtonElement(oConfig.attributes.type);}
fnSuperClass.call(this,oConfig.element,oConfig.attributes);}}};YAHOO.extend(YAHOO.widget.Button,YAHOO.util.Element,{_button:null,_menu:null,_hiddenFields:null,_onclickAttributeValue:null,_activationKeyPressed:false,_activationButtonPressed:false,_hasKeyEventHandlers:false,_hasMouseEventHandlers:false,NODE_NAME:"SPAN",CHECK_ACTIVATION_KEYS:[32],ACTIVATION_KEYS:[13,32],OPTION_AREA_WIDTH:20,CSS_CLASS_NAME:"yui-button",RADIO_DEFAULT_TITLE:"Unchecked.  Click to check.",RADIO_CHECKED_TITLE:"Checked.  Click to uncheck.",CHECKBOX_DEFAULT_TITLE:"Unchecked.  Click to check.",CHECKBOX_CHECKED_TITLE:"Checked.  Click to uncheck.",MENUBUTTON_DEFAULT_TITLE:"Menu collapsed.  Click to expand.",MENUBUTTON_MENU_VISIBLE_TITLE:"Menu expanded.  Click or press Esc to collapse.",SPLITBUTTON_DEFAULT_TITLE:("Menu collapsed.  Click inside option "+"region or press Ctrl + Shift + M to show the menu."),SPLITBUTTON_OPTION_VISIBLE_TITLE:"Menu expanded.  Press Esc or Ctrl + Shift + M to hide the menu.",SUBMIT_TITLE:"Click to submit form.",_setType:function(p_sType){if(p_sType=="split"){this.on("option",this._onOption);}},_setLabel:function(p_sLabel){this._button.innerHTML=p_sLabel;},_setTabIndex:function(p_nTabIndex){this._button.tabIndex=p_nTabIndex;},_setTitle:function(p_sTitle){var sTitle=p_sTitle;if(this.get("type")!="link"){if(!sTitle){switch(this.get("type")){case"radio":sTitle=this.RADIO_DEFAULT_TITLE;break;case"checkbox":sTitle=this.CHECKBOX_DEFAULT_TITLE;break;case"menu":sTitle=this.MENUBUTTON_DEFAULT_TITLE;break;case"split":sTitle=this.SPLITBUTTON_DEFAULT_TITLE;break;case"submit":sTitle=this.SUBMIT_TITLE;break;}}
this._button.title=sTitle;}},_setDisabled:function(p_bDisabled){if(this.get("type")!="link"){if(p_bDisabled){if(this._menu){this._menu.hide();}
if(this.hasFocus()){this.blur();}
this._button.setAttribute("disabled","disabled");this.addStateCSSClasses("disabled");}
else{this._button.removeAttribute("disabled");this.removeStateCSSClasses("disabled");}}},_setHref:function(p_sHref){if(this.get("type")=="link"){this._button.href=p_sHref;}},_setTarget:function(p_sTarget){if(this.get("type")=="link"){this._button.setAttribute("target",p_sTarget);}},_setChecked:function(p_bChecked){var sType=this.get("type"),sTitle;if(sType=="checkbox"||sType=="radio"){if(p_bChecked){this.addStateCSSClasses("checked");sTitle=(sType=="radio")?this.RADIO_CHECKED_TITLE:this.CHECKBOX_CHECKED_TITLE;}
else{this.removeStateCSSClasses("checked");sTitle=(sType=="radio")?this.RADIO_DEFAULT_TITLE:this.CHECKBOX_DEFAULT_TITLE;}
this.set("title",sTitle);}},_setMenu:function(p_oMenu){var bLazyLoad=this.get("lazyloadmenu"),oButtonElement=this.get("element"),bInstance=false,oMenu,oMenuElement,oSrcElement,aItems,nItems,oItem,i;if(!Overlay){return false;}
if(!Menu){return false;}
function onAppendTo(){oMenu.render(oButtonElement.parentNode);this.removeListener("appendTo",onAppendTo);}
function initMenu(){if(oMenu){Dom.addClass(oMenu.element,this.get("menuclassname"));Dom.addClass(oMenu.element,"yui-"+this.get("type")+"-button-menu");oMenu.showEvent.subscribe(this._onMenuShow,null,this);oMenu.hideEvent.subscribe(this._onMenuHide,null,this);oMenu.renderEvent.subscribe(this._onMenuRender,null,this);if(oMenu instanceof Menu){oMenu.keyDownEvent.subscribe(this._onMenuKeyDown,this,true);oMenu.clickEvent.subscribe(this._onMenuClick,this,true);oMenu.itemAddedEvent.subscribe(this._onMenuItemAdded,this,true);oSrcElement=oMenu.srcElement;if(oSrcElement&&oSrcElement.nodeName.toUpperCase()=="SELECT"){oSrcElement.style.display="none";oSrcElement.parentNode.removeChild(oSrcElement);}}
else if(oMenu instanceof Overlay){if(!m_oOverlayManager){m_oOverlayManager=new YAHOO.widget.OverlayManager();}
m_oOverlayManager.register(oMenu);}
this._menu=oMenu;if(!bInstance){if(bLazyLoad&&!(oMenu instanceof Menu)){oMenu.beforeShowEvent.subscribe(this._onOverlayBeforeShow,null,this);}
else if(!bLazyLoad){if(Dom.inDocument(oButtonElement)){oMenu.render(oButtonElement.parentNode);}
else{this.on("appendTo",onAppendTo);}}}}}
if(p_oMenu&&(p_oMenu instanceof Menu)){oMenu=p_oMenu;aItems=oMenu.getItems();nItems=aItems.length;bInstance=true;if(nItems>0){i=nItems-1;do{oItem=aItems[i];if(oItem){oItem.cfg.subscribeToConfigEvent("selected",this._onMenuItemSelected,oItem,this);}}
while(i--);}
initMenu.call(this);}
else if(p_oMenu&&(p_oMenu instanceof Overlay)){oMenu=p_oMenu;bInstance=true;oMenu.cfg.setProperty("visible",false);oMenu.cfg.setProperty("context",[oButtonElement,"tl","bl"]);initMenu.call(this);}
else if(Lang.isArray(p_oMenu)){this.on("appendTo",function(){oMenu=new Menu(Dom.generateId(),{lazyload:bLazyLoad,itemdata:p_oMenu});initMenu.call(this);});}
else if(Lang.isString(p_oMenu)){oMenuElement=Dom.get(p_oMenu);if(oMenuElement){if(Dom.hasClass(oMenuElement,Menu.prototype.CSS_CLASS_NAME)||oMenuElement.nodeName=="SELECT"){oMenu=new Menu(p_oMenu,{lazyload:bLazyLoad});initMenu.call(this);}
else{oMenu=new Overlay(p_oMenu,{visible:false,context:[oButtonElement,"tl","bl"]});initMenu.call(this);}}}
else if(p_oMenu&&p_oMenu.nodeName){if(Dom.hasClass(p_oMenu,Menu.prototype.CSS_CLASS_NAME)||p_oMenu.nodeName=="SELECT"){oMenu=new Menu(p_oMenu,{lazyload:bLazyLoad});initMenu.call(this);}
else{if(!p_oMenu.id){Dom.generateId(p_oMenu);}
oMenu=new Overlay(p_oMenu,{visible:false,context:[oButtonElement,"tl","bl"]});initMenu.call(this);}}},_setOnClick:function(p_oObject){if(this._onclickAttributeValue&&(this._onclickAttributeValue!=p_oObject)){this.removeListener("click",this._onclickAttributeValue.fn);this._onclickAttributeValue=null;}
if(!this._onclickAttributeValue&&Lang.isObject(p_oObject)&&Lang.isFunction(p_oObject.fn)){this.on("click",p_oObject.fn,p_oObject.obj,p_oObject.scope);this._onclickAttributeValue=p_oObject;}},_setSelectedMenuItem:function(p_nIndex){var oMenu=this._menu,oMenuItem;if(oMenu&&oMenu instanceof Menu){oMenuItem=oMenu.getItem(p_nIndex);if(oMenuItem&&!oMenuItem.cfg.getProperty("selected")){oMenuItem.cfg.setProperty("selected",true);}}},_isActivationKey:function(p_nKeyCode){var sType=this.get("type"),aKeyCodes=(sType=="checkbox"||sType=="radio")?this.CHECK_ACTIVATION_KEYS:this.ACTIVATION_KEYS,nKeyCodes=aKeyCodes.length,i;if(nKeyCodes>0){i=nKeyCodes-1;do{if(p_nKeyCode==aKeyCodes[i]){return true;}}
while(i--);}},_isSplitButtonOptionKey:function(p_oEvent){return(p_oEvent.ctrlKey&&p_oEvent.shiftKey&&Event.getCharCode(p_oEvent)==77);},_addListenersToForm:function(){var oForm=this.getForm(),oSrcElement,aListeners,nListeners,i,bHasKeyPressListener;if(oForm){Event.on(oForm,"reset",this._onFormReset,null,this);Event.on(oForm,"submit",this.createHiddenFields,null,this);oSrcElement=this.get("srcelement");if(this.get("type")=="submit"||(oSrcElement&&oSrcElement.type=="submit"))
{aListeners=Event.getListeners(oForm,"keypress");bHasKeyPressListener=false;if(aListeners){nListeners=aListeners.length;if(nListeners>0){i=nListeners-1;do{if(aListeners[i].fn==YAHOO.widget.Button.onFormKeyPress)
{bHasKeyPressListener=true;break;}}
while(i--);}}
if(!bHasKeyPressListener){Event.on(oForm,"keypress",YAHOO.widget.Button.onFormKeyPress);}}}},_originalMaxHeight:-1,_showMenu:function(p_oEvent){YAHOO.widget.MenuManager.hideVisible();if(m_oOverlayManager){m_oOverlayManager.hideAll();}
var oMenu=this._menu,nViewportHeight=Dom.getViewportHeight(),nMenuHeight,nScrollTop,nY;if(oMenu&&(oMenu instanceof Menu)){oMenu.cfg.applyConfig({context:[this.get("id"),"tl","bl"],constraintoviewport:false,clicktohide:false,visible:true});oMenu.cfg.fireQueue();oMenu.align("tl","bl");if(p_oEvent.type=="mousedown"){Event.stopPropagation(p_oEvent);}
if(this.get("focusmenu")){this._menu.focus();}
nMenuHeight=oMenu.element.offsetHeight;if((oMenu.cfg.getProperty("y")+nMenuHeight)>nViewportHeight){oMenu.align("bl","tl");nY=oMenu.cfg.getProperty("y");nScrollTop=Dom.getDocumentScrollTop();if(nScrollTop>=nY){if(this._originalMaxHeight==-1){this._originalMaxHeight=oMenu.cfg.getProperty("maxheight");}
oMenu.cfg.setProperty("maxheight",(nMenuHeight-((nScrollTop-nY)+20)));oMenu.align("bl","tl");}}}
else if(oMenu&&(oMenu instanceof Overlay)){oMenu.show();oMenu.align("tl","bl");nMenuHeight=oMenu.element.offsetHeight;if((oMenu.cfg.getProperty("y")+nMenuHeight)>nViewportHeight){oMenu.align("bl","tl");}}},_hideMenu:function(){var oMenu=this._menu;if(oMenu){oMenu.hide();}},_onMouseOver:function(p_oEvent){if(!this._hasMouseEventHandlers){this.on("mouseout",this._onMouseOut);this.on("mousedown",this._onMouseDown);this.on("mouseup",this._onMouseUp);this._hasMouseEventHandlers=true;}
this.addStateCSSClasses("hover");if(this._activationButtonPressed){this.addStateCSSClasses("active");}
if(this._bOptionPressed){this.addStateCSSClasses("activeoption");}},_onMouseOut:function(p_oEvent){this.removeStateCSSClasses("hover");if(this.get("type")!="menu"){this.removeStateCSSClasses("active");}
if(this._activationButtonPressed||this._bOptionPressed){Event.on(document,"mouseup",this._onDocumentMouseUp,null,this);}},_onDocumentMouseUp:function(p_oEvent){this._activationButtonPressed=false;this._bOptionPressed=false;var sType=this.get("type");if(sType=="menu"||sType=="split"){this.removeStateCSSClasses((sType=="menu"?"active":"activeoption"));this._hideMenu();}
Event.removeListener(document,"mouseup",this._onDocumentMouseUp);},_onMouseDown:function(p_oEvent){var sType,oElement,nX,me;function onMouseUp(){this._hideMenu();this.removeListener("mouseup",onMouseUp);}
if((p_oEvent.which||p_oEvent.button)==1){if(!this.hasFocus()){this.focus();}
sType=this.get("type");if(sType=="split"){oElement=this.get("element");nX=Event.getPageX(p_oEvent)-Dom.getX(oElement);if((oElement.offsetWidth-this.OPTION_AREA_WIDTH)<nX){this.fireEvent("option",p_oEvent);}
else{this.addStateCSSClasses("active");this._activationButtonPressed=true;}}
else if(sType=="menu"){if(this.isActive()){this._hideMenu();this._activationButtonPressed=false;}
else{this._showMenu(p_oEvent);this._activationButtonPressed=true;}}
else{this.addStateCSSClasses("active");this._activationButtonPressed=true;}
if(sType=="split"||sType=="menu"){me=this;this._hideMenuTimerId=window.setTimeout(function(){me.on("mouseup",onMouseUp);},250);}}},_onMouseUp:function(p_oEvent){var sType=this.get("type");if(this._hideMenuTimerId){window.clearTimeout(this._hideMenuTimerId);}
if(sType=="checkbox"||sType=="radio"){this.set("checked",!(this.get("checked")));}
this._activationButtonPressed=false;if(this.get("type")!="menu"){this.removeStateCSSClasses("active");}},_onFocus:function(p_oEvent){var oElement;this.addStateCSSClasses("focus");if(this._activationKeyPressed){this.addStateCSSClasses("active");}
m_oFocusedButton=this;if(!this._hasKeyEventHandlers){oElement=this._button;Event.on(oElement,"blur",this._onBlur,null,this);Event.on(oElement,"keydown",this._onKeyDown,null,this);Event.on(oElement,"keyup",this._onKeyUp,null,this);this._hasKeyEventHandlers=true;}
this.fireEvent("focus",p_oEvent);},_onBlur:function(p_oEvent){this.removeStateCSSClasses("focus");if(this.get("type")!="menu"){this.removeStateCSSClasses("active");}
if(this._activationKeyPressed){Event.on(document,"keyup",this._onDocumentKeyUp,null,this);}
m_oFocusedButton=null;this.fireEvent("blur",p_oEvent);},_onDocumentKeyUp:function(p_oEvent){if(this._isActivationKey(Event.getCharCode(p_oEvent))){this._activationKeyPressed=false;Event.removeListener(document,"keyup",this._onDocumentKeyUp);}},_onKeyDown:function(p_oEvent){var oMenu=this._menu;if(this.get("type")=="split"&&this._isSplitButtonOptionKey(p_oEvent)){this.fireEvent("option",p_oEvent);}
else if(this._isActivationKey(Event.getCharCode(p_oEvent))){if(this.get("type")=="menu"){this._showMenu(p_oEvent);}
else{this._activationKeyPressed=true;this.addStateCSSClasses("active");}}
if(oMenu&&oMenu.cfg.getProperty("visible")&&Event.getCharCode(p_oEvent)==27){oMenu.hide();this.focus();}},_onKeyUp:function(p_oEvent){var sType;if(this._isActivationKey(Event.getCharCode(p_oEvent))){sType=this.get("type");if(sType=="checkbox"||sType=="radio"){this.set("checked",!(this.get("checked")));}
this._activationKeyPressed=false;if(this.get("type")!="menu"){this.removeStateCSSClasses("active");}}},_onClick:function(p_oEvent){var sType=this.get("type"),sTitle,oForm,oSrcElement,oElement,nX;switch(sType){case"radio":case"checkbox":if(this.get("checked")){sTitle=(sType=="radio")?this.RADIO_CHECKED_TITLE:this.CHECKBOX_CHECKED_TITLE;}
else{sTitle=(sType=="radio")?this.RADIO_DEFAULT_TITLE:this.CHECKBOX_DEFAULT_TITLE;}
this.set("title",sTitle);break;case"submit":this.submitForm();break;case"reset":oForm=this.getForm();if(oForm){oForm.reset();}
break;case"menu":sTitle=this._menu.cfg.getProperty("visible")?this.MENUBUTTON_MENU_VISIBLE_TITLE:this.MENUBUTTON_DEFAULT_TITLE;this.set("title",sTitle);break;case"split":oElement=this.get("element");nX=Event.getPageX(p_oEvent)-Dom.getX(oElement);if((oElement.offsetWidth-this.OPTION_AREA_WIDTH)<nX){return false;}
else{this._hideMenu();oSrcElement=this.get("srcelement");if(oSrcElement&&oSrcElement.type=="submit"){this.submitForm();}}
sTitle=this._menu.cfg.getProperty("visible")?this.SPLITBUTTON_OPTION_VISIBLE_TITLE:this.SPLITBUTTON_DEFAULT_TITLE;this.set("title",sTitle);break;}},_onAppendTo:function(p_oEvent){var me=this;window.setTimeout(function(){me._addListenersToForm();},0);},_onFormReset:function(p_oEvent){var sType=this.get("type"),oMenu=this._menu;if(sType=="checkbox"||sType=="radio"){this.resetValue("checked");}
if(oMenu&&(oMenu instanceof Menu)){this.resetValue("selectedMenuItem");}},_onDocumentMouseDown:function(p_oEvent){var oTarget=Event.getTarget(p_oEvent),oButtonElement=this.get("element"),oMenuElement=this._menu.element;if(oTarget!=oButtonElement&&!Dom.isAncestor(oButtonElement,oTarget)&&oTarget!=oMenuElement&&!Dom.isAncestor(oMenuElement,oTarget)){this._hideMenu();Event.removeListener(document,"mousedown",this._onDocumentMouseDown);}},_onOption:function(p_oEvent){if(this.hasClass("yui-split-button-activeoption")){this._hideMenu();this._bOptionPressed=false;}
else{this._showMenu(p_oEvent);this._bOptionPressed=true;}},_onOverlayBeforeShow:function(p_sType){var oMenu=this._menu;oMenu.render(this.get("element").parentNode);oMenu.beforeShowEvent.unsubscribe(this._onOverlayBeforeShow);},_onMenuShow:function(p_sType){Event.on(document,"mousedown",this._onDocumentMouseDown,null,this);var sTitle,sState;if(this.get("type")=="split"){sTitle=this.SPLITBUTTON_OPTION_VISIBLE_TITLE;sState="activeoption";}
else{sTitle=this.MENUBUTTON_MENU_VISIBLE_TITLE;sState="active";}
this.addStateCSSClasses(sState);this.set("title",sTitle);},_onMenuHide:function(p_sType){var oMenu=this._menu,sTitle,sState;if(oMenu&&(oMenu instanceof Menu)&&this._originalMaxHeight!=-1){this._menu.cfg.setProperty("maxheight",this._originalMaxHeight);}
if(this.get("type")=="split"){sTitle=this.SPLITBUTTON_DEFAULT_TITLE;sState="activeoption";}
else{sTitle=this.MENUBUTTON_DEFAULT_TITLE;sState="active";}
this.removeStateCSSClasses(sState);this.set("title",sTitle);if(this.get("type")=="split"){this._bOptionPressed=false;}},_onMenuKeyDown:function(p_sType,p_aArgs){var oEvent=p_aArgs[0];if(Event.getCharCode(oEvent)==27){this.focus();if(this.get("type")=="split"){this._bOptionPressed=false;}}},_onMenuRender:function(p_sType){var oButtonElement=this.get("element"),oButtonParent=oButtonElement.parentNode,oMenuElement=this._menu.element;if(oButtonParent!=oMenuElement.parentNode){oButtonParent.appendChild(oMenuElement);}
this.set("selectedMenuItem",this.get("selectedMenuItem"));},_onMenuItemSelected:function(p_sType,p_aArgs,p_nItem){var bSelected=p_aArgs[0];if(bSelected){this.set("selectedMenuItem",p_nItem);}},_onMenuItemAdded:function(p_sType,p_aArgs,p_oItem){var oItem=p_aArgs[0];oItem.cfg.subscribeToConfigEvent("selected",this._onMenuItemSelected,oItem.index,this);},_onMenuClick:function(p_sType,p_aArgs){var oItem=p_aArgs[1],oSrcElement;if(oItem){oSrcElement=this.get("srcelement");if(oSrcElement&&oSrcElement.type=="submit"){this.submitForm();}
this._hideMenu();}},createButtonElement:function(p_sType){var sNodeName=this.NODE_NAME,oElement=document.createElement(sNodeName);oElement.innerHTML="<"+sNodeName+" class=\"first-child\">"+
(p_sType=="link"?"<a></a>":"<button type=\"button\"></button>")+"</"+sNodeName+">";return oElement;},addStateCSSClasses:function(p_sState){var sType=this.get("type");if(Lang.isString(p_sState)){if(p_sState!="activeoption"){this.addClass(this.CSS_CLASS_NAME+("-"+p_sState));}
this.addClass("yui-"+sType+("-button-"+p_sState));}},removeStateCSSClasses:function(p_sState){var sType=this.get("type");if(Lang.isString(p_sState)){this.removeClass(this.CSS_CLASS_NAME+("-"+p_sState));this.removeClass("yui-"+sType+("-button-"+p_sState));}},createHiddenFields:function(){this.removeHiddenFields();var oForm=this.getForm(),oButtonField,sType,bCheckable,oMenu,oMenuItem,sName,oValue,oMenuField;if(oForm&&!this.get("disabled")){sType=this.get("type");bCheckable=(sType=="checkbox"||sType=="radio");if(bCheckable||(m_oSubmitTrigger==this)){oButtonField=createInputElement((bCheckable?sType:"hidden"),this.get("name"),this.get("value"),this.get("checked"));if(oButtonField){if(bCheckable){oButtonField.style.display="none";}
oForm.appendChild(oButtonField);}}
oMenu=this._menu;if(oMenu&&(oMenu instanceof Menu)){oMenuField=oMenu.srcElement;oMenuItem=oMenu.getItem(this.get("selectedMenuItem"));if(oMenuField&&oMenuField.nodeName.toUpperCase()=="SELECT"){oForm.appendChild(oMenuField);oMenuField.selectedIndex=oMenuItem.index;}
else{oValue=(oMenuItem.value===null||oMenuItem.value==="")?oMenuItem.cfg.getProperty("text"):oMenuItem.value;sName=this.get("name");if(oValue&&sName){oMenuField=createInputElement("hidden",(sName+"_options"),oValue);oForm.appendChild(oMenuField);}}}
if(oButtonField&&oMenuField){this._hiddenFields=[oButtonField,oMenuField];}
else if(!oButtonField&&oMenuField){this._hiddenFields=oMenuField;}
else if(oButtonField&&!oMenuField){this._hiddenFields=oButtonField;}
return this._hiddenFields;}},removeHiddenFields:function(){var oField=this._hiddenFields,nFields,i;function removeChild(p_oElement){if(Dom.inDocument(p_oElement)){p_oElement.parentNode.removeChild(p_oElement);}}
if(oField){if(Lang.isArray(oField)){nFields=oField.length;if(nFields>0){i=nFields-1;do{removeChild(oField[i]);}
while(i--);}}
else{removeChild(oField);}
this._hiddenFields=null;}},submitForm:function(){var oForm=this.getForm(),oSrcElement=this.get("srcelement"),bSubmitForm=false,oEvent;if(oForm){if(this.get("type")=="submit"||(oSrcElement&&oSrcElement.type=="submit"))
{m_oSubmitTrigger=this;}
if(YAHOO.env.ua.ie){bSubmitForm=oForm.fireEvent("onsubmit");}
else{oEvent=document.createEvent("HTMLEvents");oEvent.initEvent("submit",true,true);bSubmitForm=oForm.dispatchEvent(oEvent);}
if((YAHOO.env.ua.ie||YAHOO.env.ua.webkit)&&bSubmitForm){oForm.submit();}}
return bSubmitForm;},init:function(p_oElement,p_oAttributes){var sNodeName=p_oAttributes.type=="link"?"A":"BUTTON",oSrcElement=p_oAttributes.srcelement,oButton=p_oElement.getElementsByTagName(sNodeName)[0],oInput;if(!oButton){oInput=p_oElement.getElementsByTagName("INPUT")[0];if(oInput){oButton=document.createElement("BUTTON");oButton.setAttribute("type","button");oInput.parentNode.replaceChild(oButton,oInput);}}
this._button=oButton;YAHOO.widget.Button.superclass.init.call(this,p_oElement,p_oAttributes);m_oButtons[this.get("id")]=this;this.addClass(this.CSS_CLASS_NAME);this.addClass("yui-"+this.get("type")+"-button");Event.on(this._button,"focus",this._onFocus,null,this);this.on("mouseover",this._onMouseOver);this.on("click",this._onClick);this.on("appendTo",this._onAppendTo);var oContainer=this.get("container"),oElement=this.get("element"),bElInDoc=Dom.inDocument(oElement),oParentNode;if(oContainer){if(oSrcElement&&oSrcElement!=oElement){oParentNode=oSrcElement.parentNode;if(oParentNode){oParentNode.removeChild(oSrcElement);}}
if(Lang.isString(oContainer)){Event.onContentReady(oContainer,function(){this.appendTo(oContainer);},null,this);}
else{this.appendTo(oContainer);}}
else if(!bElInDoc&&oSrcElement&&oSrcElement!=oElement){oParentNode=oSrcElement.parentNode;if(oParentNode){this.fireEvent("beforeAppendTo",{type:"beforeAppendTo",target:oParentNode});oParentNode.replaceChild(oElement,oSrcElement);this.fireEvent("appendTo",{type:"appendTo",target:oParentNode});}}
else if(this.get("type")!="link"&&bElInDoc&&oSrcElement&&oSrcElement==oElement){this._addListenersToForm();}},initAttributes:function(p_oAttributes){var oAttributes=p_oAttributes||{};YAHOO.widget.Button.superclass.initAttributes.call(this,oAttributes);this.setAttributeConfig("type",{value:(oAttributes.type||"push"),validator:Lang.isString,writeOnce:true,method:this._setType});this.setAttributeConfig("label",{value:oAttributes.label,validator:Lang.isString,method:this._setLabel});this.setAttributeConfig("value",{value:oAttributes.value});this.setAttributeConfig("name",{value:oAttributes.name,validator:Lang.isString});this.setAttributeConfig("tabindex",{value:oAttributes.tabindex,validator:Lang.isNumber,method:this._setTabIndex});this.configureAttribute("title",{value:oAttributes.title,validator:Lang.isString,method:this._setTitle});this.setAttributeConfig("disabled",{value:(oAttributes.disabled||false),validator:Lang.isBoolean,method:this._setDisabled});this.setAttributeConfig("href",{value:oAttributes.href,validator:Lang.isString,method:this._setHref});this.setAttributeConfig("target",{value:oAttributes.target,validator:Lang.isString,method:this._setTarget});this.setAttributeConfig("checked",{value:(oAttributes.checked||false),validator:Lang.isBoolean,method:this._setChecked});this.setAttributeConfig("container",{value:oAttributes.container,writeOnce:true});this.setAttributeConfig("srcelement",{value:oAttributes.srcelement,writeOnce:true});this.setAttributeConfig("menu",{value:null,method:this._setMenu,writeOnce:true});this.setAttributeConfig("lazyloadmenu",{value:(oAttributes.lazyloadmenu===false?false:true),validator:Lang.isBoolean,writeOnce:true});this.setAttributeConfig("menuclassname",{value:(oAttributes.menuclassname||"yui-button-menu"),validator:Lang.isString,method:this._setMenuClassName,writeOnce:true});this.setAttributeConfig("selectedMenuItem",{value:0,validator:Lang.isNumber,method:this._setSelectedMenuItem});this.setAttributeConfig("onclick",{value:oAttributes.onclick,method:this._setOnClick});this.setAttributeConfig("focusmenu",{value:(oAttributes.focusmenu===false?false:true),validator:Lang.isBoolean});},focus:function(){if(!this.get("disabled")){this._button.focus();}},blur:function(){if(!this.get("disabled")){this._button.blur();}},hasFocus:function(){return(m_oFocusedButton==this);},isActive:function(){return this.hasClass(this.CSS_CLASS_NAME+"-active");},getMenu:function(){return this._menu;},getForm:function(){return this._button.form;},getHiddenFields:function(){return this._hiddenFields;},destroy:function(){var oElement=this.get("element"),oParentNode=oElement.parentNode,oMenu=this._menu;if(oMenu){oMenu.destroy();}
Event.purgeElement(oElement);Event.purgeElement(this._button);Event.removeListener(document,"mouseup",this._onDocumentMouseUp);Event.removeListener(document,"keyup",this._onDocumentKeyUp);Event.removeListener(document,"mousedown",this._onDocumentMouseDown);var oForm=this.getForm();if(oForm){Event.removeListener(oForm,"reset",this._onFormReset);Event.removeListener(oForm,"submit",this.createHiddenFields);}
oParentNode.removeChild(oElement);delete m_oButtons[this.get("id")];},fireEvent:function(p_sType,p_aArgs){if(this.DOM_EVENTS[p_sType]&&this.get("disabled")){return;}
YAHOO.widget.Button.superclass.fireEvent.call(this,p_sType,p_aArgs);},toString:function(){return("Button "+this.get("id"));}});YAHOO.widget.Button.onFormKeyPress=function(p_oEvent){var oTarget=Event.getTarget(p_oEvent),nCharCode=Event.getCharCode(p_oEvent),sNodeName=oTarget.nodeName&&oTarget.nodeName.toUpperCase(),sType=oTarget.type,bFormContainsYUIButtons=false,oButton,oYUISubmitButton,oPrecedingSubmitButton,oFollowingSubmitButton;function isSubmitButton(p_oElement){var sId,oSrcElement;switch(p_oElement.nodeName.toUpperCase()){case"INPUT":case"BUTTON":if(p_oElement.type=="submit"&&!p_oElement.disabled){if(!bFormContainsYUIButtons&&!oPrecedingSubmitButton){oPrecedingSubmitButton=p_oElement;}
if(oYUISubmitButton&&!oFollowingSubmitButton){oFollowingSubmitButton=p_oElement;}}
break;default:sId=p_oElement.id;if(sId){oButton=m_oButtons[sId];if(oButton){bFormContainsYUIButtons=true;if(!oButton.get("disabled")){oSrcElement=oButton.get("srcelement");if(!oYUISubmitButton&&(oButton.get("type")=="submit"||(oSrcElement&&oSrcElement.type=="submit")))
{oYUISubmitButton=oButton;}}}}
break;}}
if(nCharCode==13&&((sNodeName=="INPUT"&&(sType=="text"||sType=="password"||sType=="checkbox"||sType=="radio"||sType=="file"))||sNodeName=="SELECT"))
{Dom.getElementsBy(isSubmitButton,"*",this);if(oPrecedingSubmitButton){oPrecedingSubmitButton.focus();}
else if(!oPrecedingSubmitButton&&oYUISubmitButton){if(oFollowingSubmitButton){Event.preventDefault(p_oEvent);}
oYUISubmitButton.submitForm();}}};YAHOO.widget.Button.addHiddenFieldsToForm=function(p_oForm){var aButtons=Dom.getElementsByClassName(YAHOO.widget.Button.prototype.CSS_CLASS_NAME,"*",p_oForm),nButtons=aButtons.length,oButton,sId,i;if(nButtons>0){for(i=0;i<nButtons;i++){sId=aButtons[i].id;if(sId){oButton=m_oButtons[sId];if(oButton){oButton.createHiddenFields();}}}}};})();(function(){var Dom=YAHOO.util.Dom,Event=YAHOO.util.Event,Lang=YAHOO.lang,Button=YAHOO.widget.Button,m_oButtons={};YAHOO.widget.ButtonGroup=function(p_oElement,p_oAttributes){var fnSuperClass=YAHOO.widget.ButtonGroup.superclass.constructor,sNodeName,oElement,sId;if(arguments.length==1&&!Lang.isString(p_oElement)&&!p_oElement.nodeName){if(!p_oElement.id){sId=Dom.generateId();p_oElement.id=sId;}
fnSuperClass.call(this,(this._createGroupElement()),p_oElement);}
else if(Lang.isString(p_oElement)){oElement=Dom.get(p_oElement);if(oElement){if(oElement.nodeName.toUpperCase()==this.NODE_NAME){fnSuperClass.call(this,oElement,p_oAttributes);}}}
else{sNodeName=p_oElement.nodeName;if(sNodeName&&sNodeName==this.NODE_NAME){if(!p_oElement.id){p_oElement.id=Dom.generateId();}
fnSuperClass.call(this,p_oElement,p_oAttributes);}}};YAHOO.extend(YAHOO.widget.ButtonGroup,YAHOO.util.Element,{_buttons:null,NODE_NAME:"DIV",CSS_CLASS_NAME:"yui-buttongroup",_createGroupElement:function(){var oElement=document.createElement(this.NODE_NAME);return oElement;},_setDisabled:function(p_bDisabled){var nButtons=this.getCount(),i;if(nButtons>0){i=nButtons-1;do{this._buttons[i].set("disabled",p_bDisabled);}
while(i--);}},_onKeyDown:function(p_oEvent){var oTarget=Event.getTarget(p_oEvent),nCharCode=Event.getCharCode(p_oEvent),sId=oTarget.parentNode.parentNode.id,oButton=m_oButtons[sId],nIndex=-1;if(nCharCode==37||nCharCode==38){nIndex=(oButton.index===0)?(this._buttons.length-1):(oButton.index-1);}
else if(nCharCode==39||nCharCode==40){nIndex=(oButton.index===(this._buttons.length-1))?0:(oButton.index+1);}
if(nIndex>-1){this.check(nIndex);this.getButton(nIndex).focus();}},_onAppendTo:function(p_oEvent){var aButtons=this._buttons,nButtons=aButtons.length,i;for(i=0;i<nButtons;i++){aButtons[i].appendTo(this.get("element"));}},_onButtonCheckedChange:function(p_oEvent,p_oButton){var bChecked=p_oEvent.newValue,oCheckedButton=this.get("checkedButton");if(bChecked&&oCheckedButton!=p_oButton){if(oCheckedButton){oCheckedButton.set("checked",false,true);}
this.set("checkedButton",p_oButton);this.set("value",p_oButton.get("value"));}
else if(oCheckedButton&&!oCheckedButton.set("checked")){oCheckedButton.set("checked",true,true);}},init:function(p_oElement,p_oAttributes){this._buttons=[];YAHOO.widget.ButtonGroup.superclass.init.call(this,p_oElement,p_oAttributes);this.addClass(this.CSS_CLASS_NAME);var aButtons=this.getElementsByClassName("yui-radio-button");if(aButtons.length>0){this.addButtons(aButtons);}
function isRadioButton(p_oElement){return(p_oElement.type=="radio");}
aButtons=Dom.getElementsBy(isRadioButton,"input",this.get("element"));if(aButtons.length>0){this.addButtons(aButtons);}
this.on("keydown",this._onKeyDown);this.on("appendTo",this._onAppendTo);var oContainer=this.get("container");if(oContainer){if(Lang.isString(oContainer)){Event.onContentReady(oContainer,function(){this.appendTo(oContainer);},null,this);}
else{this.appendTo(oContainer);}}},initAttributes:function(p_oAttributes){var oAttributes=p_oAttributes||{};YAHOO.widget.ButtonGroup.superclass.initAttributes.call(this,oAttributes);this.setAttributeConfig("name",{value:oAttributes.name,validator:Lang.isString});this.setAttributeConfig("disabled",{value:(oAttributes.disabled||false),validator:Lang.isBoolean,method:this._setDisabled});this.setAttributeConfig("value",{value:oAttributes.value});this.setAttributeConfig("container",{value:oAttributes.container,writeOnce:true});this.setAttributeConfig("checkedButton",{value:null});},addButton:function(p_oButton){var oButton,oButtonElement,oGroupElement,nIndex,sButtonName,sGroupName;if(p_oButton instanceof Button&&p_oButton.get("type")=="radio"){oButton=p_oButton;}
else if(!Lang.isString(p_oButton)&&!p_oButton.nodeName){p_oButton.type="radio";oButton=new Button(p_oButton);}
else{oButton=new Button(p_oButton,{type:"radio"});}
if(oButton){nIndex=this._buttons.length;sButtonName=oButton.get("name");sGroupName=this.get("name");oButton.index=nIndex;this._buttons[nIndex]=oButton;m_oButtons[oButton.get("id")]=oButton;if(sButtonName!=sGroupName){oButton.set("name",sGroupName);}
if(this.get("disabled")){oButton.set("disabled",true);}
if(oButton.get("checked")){this.set("checkedButton",oButton);}
oButtonElement=oButton.get("element");oGroupElement=this.get("element");if(oButtonElement.parentNode!=oGroupElement){oGroupElement.appendChild(oButtonElement);}
oButton.on("checkedChange",this._onButtonCheckedChange,oButton,this);return oButton;}},addButtons:function(p_aButtons){var nButtons,oButton,aButtons,i;if(Lang.isArray(p_aButtons)){nButtons=p_aButtons.length;aButtons=[];if(nButtons>0){for(i=0;i<nButtons;i++){oButton=this.addButton(p_aButtons[i]);if(oButton){aButtons[aButtons.length]=oButton;}}
if(aButtons.length>0){return aButtons;}}}},removeButton:function(p_nIndex){var oButton=this.getButton(p_nIndex),nButtons,i;if(oButton){this._buttons.splice(p_nIndex,1);delete m_oButtons[oButton.get("id")];oButton.removeListener("checkedChange",this._onButtonCheckedChange);oButton.destroy();nButtons=this._buttons.length;if(nButtons>0){i=this._buttons.length-1;do{this._buttons[i].index=i;}
while(i--);}}},getButton:function(p_nIndex){if(Lang.isNumber(p_nIndex)){return this._buttons[p_nIndex];}},getButtons:function(){return this._buttons;},getCount:function(){return this._buttons.length;},focus:function(p_nIndex){var oButton,nButtons,i;if(Lang.isNumber(p_nIndex)){oButton=this._buttons[p_nIndex];if(oButton){oButton.focus();}}
else{nButtons=this.getCount();for(i=0;i<nButtons;i++){oButton=this._buttons[i];if(!oButton.get("disabled")){oButton.focus();break;}}}},check:function(p_nIndex){var oButton=this.getButton(p_nIndex);if(oButton){oButton.set("checked",true);}},destroy:function(){var nButtons=this._buttons.length,oElement=this.get("element"),oParentNode=oElement.parentNode,i;if(nButtons>0){i=this._buttons.length-1;do{this._buttons[i].destroy();}
while(i--);}
Event.purgeElement(oElement);oParentNode.removeChild(oElement);},toString:function(){return("ButtonGroup "+this.get("id"));}});})();YAHOO.register("button",YAHOO.widget.Button,{version:"2.3.0",build:"442"});