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

YAHOO.util.Color=function(){var HCHARS="0123456789ABCDEF",lang=YAHOO.lang;return{real2dec:function(n){return Math.min(255,Math.round(n*256));},hsv2rgb:function(h,s,v){if(lang.isArray(h)){return this.hsv2rgb.call(this,h[0],h[1],h[2]);}
var r,g,b,i,f,p,q,t;i=Math.floor((h/60)%6);f=(h/60)-i;p=v*(1-s);q=v*(1-f*s);t=v*(1-(1-f)*s);switch(i){case 0:r=v;g=t;b=p;break;case 1:r=q;g=v;b=p;break;case 2:r=p;g=v;b=t;break;case 3:r=p;g=q;b=v;break;case 4:r=t;g=p;b=v;break;case 5:r=v;g=p;b=q;break;}
var fn=this.real2dec;return[fn(r),fn(g),fn(b)];},rgb2hsv:function(r,g,b){if(lang.isArray(r)){return this.rgb2hsv.call(this,r[0],r[1],r[2]);}
r=r/255;g=g/255;b=b/255;var min,max,delta,h,s,v;min=Math.min(Math.min(r,g),b);max=Math.max(Math.max(r,g),b);delta=max-min;switch(max){case min:h=0;break;case r:h=60*(g-b)/delta;if(g<b){h+=360;}
break;case g:h=(60*(b-r)/delta)+120;break;case b:h=(60*(r-g)/delta)+240;break;}
s=(max===0)?0:1-(min/max);var hsv=[Math.round(h),s,max];return hsv;},rgb2hex:function(r,g,b){if(lang.isArray(r)){return this.rgb2hex.call(this,r[0],r[1],r[2]);}
var f=this.dec2hex;return f(r)+f(g)+f(b);},dec2hex:function(n){n=parseInt(n,10);n=(lang.isNumber(n))?n:0;n=(n>255||n<0)?0:n;return HCHARS.charAt((n-n%16)/16)+HCHARS.charAt(n%16);},hex2dec:function(str){var f=function(c){return HCHARS.indexOf(c.toUpperCase());};var s=str.split('');return((f(s[0])*16)+f(s[1]));},hex2rgb:function(s){var f=this.hex2dec;return[f(s.substr(0,2)),f(s.substr(2,2)),f(s.substr(4,2))];},websafe:function(r,g,b){if(lang.isArray(r)){return this.websafe.call(this,r[0],r[1],r[2]);}
var f=function(v){if(lang.isNumber(v)){v=Math.min(Math.max(0,v),255);var i,next;for(i=0;i<256;i=i+51){next=i+51;if(v>=i&&v<=next){return(v-i>25)?next:i;}}}
return v;};return[f(r),f(g),f(b)];}};}();(function(){var pickercount=0;YAHOO.widget.ColorPicker=function(el,attr){pickercount=pickercount+1;attr=attr||{};if(arguments.length===1&&!YAHOO.lang.isString(el)&&!el.nodeName){attr=el;el=attr.element||null;}
if(!el&&!attr.element){el=_createHostElement.call(this,attr);}
YAHOO.widget.ColorPicker.superclass.constructor.call(this,el,attr);};YAHOO.extend(YAHOO.widget.ColorPicker,YAHOO.util.Element);var proto=YAHOO.widget.ColorPicker.prototype,Slider=YAHOO.widget.Slider,Color=YAHOO.util.Color,Dom=YAHOO.util.Dom,Event=YAHOO.util.Event,lang=YAHOO.lang,sub=lang.substitute;var b="yui-picker";proto.ID={R:b+"-r",R_HEX:b+"-rhex",G:b+"-g",G_HEX:b+"-ghex",B:b+"-b",B_HEX:b+"-bhex",H:b+"-h",S:b+"-s",V:b+"-v",PICKER_BG:b+"-bg",PICKER_THUMB:b+"-thumb",HUE_BG:b+"-hue-bg",HUE_THUMB:b+"-hue-thumb",HEX:b+"-hex",SWATCH:b+"-swatch",WEBSAFE_SWATCH:b+"-websafe-swatch",CONTROLS:b+"-controls",RGB_CONTROLS:b+"-rgb-controls",HSV_CONTROLS:b+"-hsv-controls",HEX_CONTROLS:b+"-hex-controls",HEX_SUMMARY:b+"-hex-summary",CONTROLS_LABEL:b+"-controls-label"};proto.TXT={ILLEGAL_HEX:"Illegal hex value entered",SHOW_CONTROLS:"Show color details",HIDE_CONTROLS:"Hide color details",CURRENT_COLOR:"Currently selected color: {rgb}",CLOSEST_WEBSAFE:"Closest websafe color: {rgb}. Click to select.",R:"R",G:"G",B:"B",H:"H",S:"S",V:"V",HEX:"#",DEG:"\u00B0",PERCENT:"%"};proto.IMAGE={PICKER_THUMB:"../../build/colorpicker/assets/picker_thumb.png",HUE_THUMB:"../../build/colorpicker/assets/hue_thumb.png"};proto.DEFAULT={PICKER_SIZE:180};proto.OPT={HUE:"hue",SATURATION:"saturation",VALUE:"value",RED:"red",GREEN:"green",BLUE:"blue",HSV:"hsv",RGB:"rgb",WEBSAFE:"websafe",HEX:"hex",PICKER_SIZE:"pickersize",SHOW_CONTROLS:"showcontrols",SHOW_RGB_CONTROLS:"showrgbcontrols",SHOW_HSV_CONTROLS:"showhsvcontrols",SHOW_HEX_CONTROLS:"showhexcontrols",SHOW_HEX_SUMMARY:"showhexsummary",SHOW_WEBSAFE:"showwebsafe",CONTAINER:"container",IDS:"ids",ELEMENTS:"elements",TXT:"txt",IMAGES:"images",ANIMATE:"animate"};proto.setValue=function(rgb,silent){silent=(silent)||false;this.set(this.OPT.RGB,rgb,silent);_updateSliders.call(this);};proto.hueSlider=null;proto.pickerSlider=null;var _getH=function(){var size=this.get(this.OPT.PICKER_SIZE),h=(size-this.hueSlider.getValue())/size;h=Math.round(h*360);return(h===360)?0:h;};var _getS=function(){return this.pickerSlider.getXValue()/this.get(this.OPT.PICKER_SIZE);};var _getV=function(){var size=this.get(this.OPT.PICKER_SIZE);return(size-this.pickerSlider.getYValue())/size;};var _updateSwatch=function(){var rgb=this.get(this.OPT.RGB),websafe=this.get(this.OPT.WEBSAFE),el=this.getElement(this.ID.SWATCH),color=rgb.join(","),txt=this.get(this.OPT.TXT);Dom.setStyle(el,"background-color","rgb("+color+")");el.title=lang.substitute(txt.CURRENT_COLOR,{"rgb":"#"+this.get(this.OPT.HEX)});el=this.getElement(this.ID.WEBSAFE_SWATCH);color=websafe.join(",");Dom.setStyle(el,"background-color","rgb("+color+")");el.title=lang.substitute(txt.CLOSEST_WEBSAFE,{"rgb":"#"+Color.rgb2hex(websafe)});};var _getValuesFromSliders=function(){var h=_getH.call(this),s=_getS.call(this),v=_getV.call(this);rgb=Color.hsv2rgb(h,s,v);var websafe=Color.websafe(rgb);var hex=Color.rgb2hex(rgb[0],rgb[1],rgb[2]);this.set(this.OPT.RGB,rgb);};var _updateFormFields=function(){this.getElement(this.ID.H).value=this.get(this.OPT.HUE);this.getElement(this.ID.S).value=this.get(this.OPT.SATURATION);this.getElement(this.ID.V).value=this.get(this.OPT.VALUE);this.getElement(this.ID.R).value=this.get(this.OPT.RED);this.getElement(this.ID.R_HEX).innerHTML=Color.dec2hex(this.get(this.OPT.RED));this.getElement(this.ID.G).value=this.get(this.OPT.GREEN);this.getElement(this.ID.G_HEX).innerHTML=Color.dec2hex(this.get(this.OPT.GREEN));this.getElement(this.ID.B).value=this.get(this.OPT.BLUE);this.getElement(this.ID.B_HEX).innerHTML=Color.dec2hex(this.get(this.OPT.BLUE));this.getElement(this.ID.HEX).value=this.get(this.OPT.HEX);};var _onHueSliderChange=function(newOffset){var h=_getH.call(this);this.set(this.OPT.HUE,h,true);var rgb=Color.hsv2rgb(h,1,1);var styleDef="rgb("+rgb.join(",")+")";Dom.setStyle(this.getElement(this.ID.PICKER_BG),"background-color",styleDef);if(this.hueSlider.valueChangeSource===this.hueSlider.SOURCE_UI_EVENT){_getValuesFromSliders.call(this);}
_updateFormFields.call(this);_updateSwatch.call(this);};var _onPickerSliderChange=function(newOffset){var s=_getS.call(this),v=_getV.call(this);this.set(this.OPT.SATURATION,Math.round(s*100),true);this.set(this.OPT.VALUE,Math.round(v*100),true);if(this.pickerSlider.valueChangeSource===this.pickerSlider.SOURCE_UI_EVENT){_getValuesFromSliders.call(this);}
_updateFormFields.call(this);_updateSwatch.call(this);};var _getCommand=function(e){var c=Event.getCharCode(e);if(c===38){return 3;}else if(c===13){return 6;}else if(c===40){return 4;}else if(c>=48&&c<=57){return 1;}else if(c>=97&&c<=102){return 2;}else if(c>=65&&c<=70){return 2;}else if("8, 9, 13, 27, 37, 39".indexOf(c)>-1){return 5;}else{return 0;}};var _rgbFieldKeypress=function(e,el,prop){var command=_getCommand(e);var inc=(e.shiftKey)?10:1;switch(command){case 6:_useFieldValue.apply(this,arguments);break;case 3:this.set(prop,Math.min(this.get(prop)+inc,255));_updateFormFields.call(this);break;case 4:this.set(prop,Math.max(this.get(prop)-inc,0));_updateFormFields.call(this);break;default:}};var _hexFieldKeypress=function(e,el,prop){var command=_getCommand(e);if(command===6){_useFieldValue.apply(this,arguments);}};var _useFieldValue=function(e,el,prop){var val=el.value;if(prop!==this.OPT.HEX){val=parseInt(val,10);}
if(val!==this.get(prop)){this.set(prop,val);}};var _numbersOnly=function(e){return _hexOnly(e,true);};var _hexOnly=function(e,numbersOnly){var command=_getCommand(e);switch(command){case 6:case 5:case 1:break;case 2:if(numbersOnly!==true){break;}
default:Event.stopEvent(e);return false;}};proto.getElement=function(id){return this.get(this.OPT.ELEMENTS)[this.get(this.OPT.IDS)[id]];};_createElements=function(){var el,child,img,fld,i,ids=this.get(this.OPT.IDS),txt=this.get(this.OPT.TXT),images=this.get(this.OPT.IMAGES),Elem=function(type,o){var n=document.createElement(type);if(o){lang.augmentObject(n,o,true);}
return n;},RGBElem=function(type,obj){var o=lang.merge({autocomplete:"off",value:"0",size:3,maxlength:3},obj);o.name=o.id;return new Elem(type,o);};var p=this.get("element");el=new Elem("div",{id:ids[this.ID.PICKER_BG],className:"yui-picker-bg",tabIndex:-1,hideFocus:true});child=new Elem("div",{id:ids[this.ID.PICKER_THUMB],className:"yui-picker-thumb"});img=new Elem("img",{src:images.PICKER_THUMB});child.appendChild(img);el.appendChild(child);p.appendChild(el);el=new Elem("div",{id:ids[this.ID.HUE_BG],className:"yui-picker-hue-bg",tabIndex:-1,hideFocus:true});child=new Elem("div",{id:ids[this.ID.HUE_THUMB],className:"yui-picker-hue-thumb"});img=new Elem("img",{src:images.HUE_THUMB});child.appendChild(img);el.appendChild(child);p.appendChild(el);el=new Elem("div",{id:ids[this.ID.CONTROLS],className:"yui-picker-controls"});p.appendChild(el);p=el;el=new Elem("div",{className:"hd"});child=new Elem("a",{id:ids[this.ID.CONTROLS_LABEL],href:"#"});el.appendChild(child);p.appendChild(el);el=new Elem("div",{className:"bd"});p.appendChild(el);p=el;el=new Elem("ul",{id:ids[this.ID.RGB_CONTROLS],className:"yui-picker-rgb-controls"});child=new Elem("li");child.appendChild(document.createTextNode(txt.R+" "));fld=new RGBElem("input",{id:ids[this.ID.R],className:"yui-picker-r"});child.appendChild(fld);el.appendChild(child);child=new Elem("li");child.appendChild(document.createTextNode(txt.G+" "));fld=new RGBElem("input",{id:ids[this.ID.G],className:"yui-picker-g"});child.appendChild(fld);el.appendChild(child);child=new Elem("li");child.appendChild(document.createTextNode(txt.B+" "));fld=new RGBElem("input",{id:ids[this.ID.B],className:"yui-picker-b"});child.appendChild(fld);el.appendChild(child);p.appendChild(el);el=new Elem("ul",{id:ids[this.ID.HSV_CONTROLS],className:"yui-picker-hsv-controls"});child=new Elem("li");child.appendChild(document.createTextNode(txt.H+" "));fld=new RGBElem("input",{id:ids[this.ID.H],className:"yui-picker-h"});child.appendChild(fld);child.appendChild(document.createTextNode(" "+txt.DEG));el.appendChild(child);child=new Elem("li");child.appendChild(document.createTextNode(txt.S+" "));fld=new RGBElem("input",{id:ids[this.ID.S],className:"yui-picker-s"});child.appendChild(fld);child.appendChild(document.createTextNode(" "+txt.PERCENT));el.appendChild(child);child=new Elem("li");child.appendChild(document.createTextNode(txt.V+" "));fld=new RGBElem("input",{id:ids[this.ID.V],className:"yui-picker-v"});child.appendChild(fld);child.appendChild(document.createTextNode(" "+txt.PERCENT));el.appendChild(child);p.appendChild(el);el=new Elem("ul",{id:ids[this.ID.HEX_SUMMARY],className:"yui-picker-hex_summary"});child=new Elem("li",{id:ids[this.ID.R_HEX]});el.appendChild(child);child=new Elem("li",{id:ids[this.ID.G_HEX]});el.appendChild(child);child=new Elem("li",{id:ids[this.ID.B_HEX]});el.appendChild(child);p.appendChild(el);el=new Elem("div",{id:ids[this.ID.HEX_CONTROLS],className:"yui-picker-hex-controls"});el.appendChild(document.createTextNode(txt.HEX+" "));child=new RGBElem("input",{id:ids[this.ID.HEX],className:"yui-picker-hex",size:6,maxlength:6});el.appendChild(child);p.appendChild(el);p=this.get("element");el=new Elem("div",{id:ids[this.ID.SWATCH],className:"yui-picker-swatch"});p.appendChild(el);el=new Elem("div",{id:ids[this.ID.WEBSAFE_SWATCH],className:"yui-picker-websafe-swatch"});p.appendChild(el);};proto.initPicker=function(){var o=this.OPT,ids=this.get(o.IDS),els=this.get(o.ELEMENTS),i,el,id;for(i in this.ID){if(lang.hasOwnProperty(this.ID,i)){ids[this.ID[i]]=ids[i];}}
el=Dom.get(ids[this.ID.PICKER_BG]);if(!el){_createElements.call(this);}else{}
for(i in ids){if(lang.hasOwnProperty(ids,i)){el=Dom.get(ids[i]);id=Dom.generateId(el);ids[i]=id;ids[ids[i]]=id;els[id]=el;}}
els=[o.SHOW_CONTROLS,o.SHOW_RGB_CONTROLS,o.SHOW_HSV_CONTROLS,o.SHOW_HEX_CONTROLS,o.SHOW_HEX_SUMMARY,o.SHOW_WEBSAFE];for(i=0;i<els.length;i=i+1){this.set(els[i],this.get(els[i]));}
var s=this.get(o.PICKER_SIZE);this.hueSlider=Slider.getVertSlider(this.getElement(this.ID.HUE_BG),this.getElement(this.ID.HUE_THUMB),0,s);this.hueSlider.subscribe("change",_onHueSliderChange,this,true);this.pickerSlider=Slider.getSliderRegion(this.getElement(this.ID.PICKER_BG),this.getElement(this.ID.PICKER_THUMB),0,s,0,s);this.pickerSlider.subscribe("change",_onPickerSliderChange,this,true);Event.on(this.getElement(this.ID.WEBSAFE_SWATCH),"click",function(e){this.setValue(this.get(o.WEBSAFE));},this,true);Event.on(this.getElement(this.ID.CONTROLS_LABEL),"click",function(e){this.set(o.SHOW_CONTROLS,!this.get(o.SHOW_CONTROLS));Event.preventDefault(e);},this,true);_attachRGBHSV.call(this,this.ID.R,this.OPT.RED);_attachRGBHSV.call(this,this.ID.G,this.OPT.GREEN);_attachRGBHSV.call(this,this.ID.B,this.OPT.BLUE);_attachRGBHSV.call(this,this.ID.H,this.OPT.HUE);_attachRGBHSV.call(this,this.ID.S,this.OPT.SATURATION);_attachRGBHSV.call(this,this.ID.V,this.OPT.VALUE);Event.on(this.getElement(this.ID.HEX),"keydown",function(e,me){_hexFieldKeypress.call(me,e,this,me.OPT.HEX);},this);Event.on(this.getElement(this.ID.HEX),"keypress",_hexOnly,this);Event.on(this.getElement(this.ID.HEX),"blur",function(e,me){_useFieldValue.call(me,e,this,me.OPT.HEX);},this);};_attachRGBHSV=function(id,config){Event.on(this.getElement(id),"keydown",function(e,me){_rgbFieldKeypress.call(me,e,this,config);},this);Event.on(this.getElement(id),"keypress",_numbersOnly,this);Event.on(this.getElement(id),"blur",function(e,me){_useFieldValue.call(me,e,this,config);},this);};proto.initAttributes=function(attr){attr=attr||{};YAHOO.widget.ColorPicker.superclass.initAttributes.call(this,attr);this.setAttributeConfig(this.OPT.PICKER_SIZE,{value:attr.size||this.DEFAULT.PICKER_SIZE});this.setAttributeConfig(this.OPT.HUE,{value:attr.hue||0,validator:lang.isNumber});this.setAttributeConfig(this.OPT.SATURATION,{value:attr.saturation||0,validator:lang.isNumber});this.setAttributeConfig(this.OPT.VALUE,{value:attr.value||100,validator:lang.isNumber});this.setAttributeConfig(this.OPT.RED,{value:attr.red||255,validator:lang.isNumber});this.setAttributeConfig(this.OPT.GREEN,{value:attr.red||255,validator:lang.isNumber});this.setAttributeConfig(this.OPT.BLUE,{value:attr.blue||255,validator:lang.isNumber});this.setAttributeConfig(this.OPT.HEX,{value:attr.hex||"FFFFFF",validator:lang.isString});this.setAttributeConfig(this.OPT.RGB,{value:attr.rgb||[255,255,255],method:function(rgb){this.set(this.OPT.RED,rgb[0],true);this.set(this.OPT.GREEN,rgb[1],true);this.set(this.OPT.BLUE,rgb[2],true);var websafe=Color.websafe(rgb);this.set(this.OPT.WEBSAFE,websafe,true);var hex=Color.rgb2hex(rgb);this.set(this.OPT.HEX,hex,true);var hsv=Color.rgb2hsv(rgb);this.set(this.OPT.HUE,hsv[0],true);this.set(this.OPT.SATURATION,Math.round(hsv[1]*100),true);this.set(this.OPT.VALUE,Math.round(hsv[2]*100),true);},readonly:true});this.setAttributeConfig(this.OPT.CONTAINER,{value:null,method:function(container){if(container){container.showEvent.subscribe(function(){this.pickerSlider.focus();},this,true);}}});this.setAttributeConfig(this.OPT.WEBSAFE,{value:attr.websafe||[255,255,255]});ids=attr.ids||lang.merge({},this.ID);if(!attr.ids&&pickercount>1){for(var i in ids){if(lang.hasOwnProperty(ids,i)){ids[i]=ids[i]+pickercount;}}}
this.setAttributeConfig(this.OPT.IDS,{value:ids,writeonce:true});this.setAttributeConfig(this.OPT.TXT,{value:attr.txt||this.TXT,writeonce:true});this.setAttributeConfig(this.OPT.IMAGES,{value:attr.images||this.IMAGE,writeonce:true});this.setAttributeConfig(this.OPT.ELEMENTS,{value:{},readonly:true});_hideShowEl=function(id,on){var el=(lang.isString(id)?this.getElement(id):id);Dom.setStyle(el,"display",(on)?"":"none");};this.setAttributeConfig(this.OPT.SHOW_CONTROLS,{value:(attr.showcontrols)||true,method:function(on){var el=Dom.getElementsByClassName("bd","div",this.getElement(this.ID.CONTROLS))[0];_hideShowEl.call(this,el,on);this.getElement(this.ID.CONTROLS_LABEL).innerHTML=(on)?this.get(this.OPT.TXT).HIDE_CONTROLS:this.get(this.OPT.TXT).SHOW_CONTROLS;}});this.setAttributeConfig(this.OPT.SHOW_RGB_CONTROLS,{value:(attr.showrgbcontrols)||true,method:function(on){_hideShowEl.call(this,this.ID.RGB_CONTROLS,on);}});this.setAttributeConfig(this.OPT.SHOW_HSV_CONTROLS,{value:(attr.showhsvcontrols)||false,method:function(on){_hideShowEl.call(this,this.ID.HSV_CONTROLS,on);if(on&&this.get(this.OPT.SHOW_HEX_SUMMARY)){this.set(this.OPT.SHOW_HEX_SUMMARY,false);}}});this.setAttributeConfig(this.OPT.SHOW_HEX_CONTROLS,{value:(attr.showhexcontrols)||false,method:function(on){_hideShowEl.call(this,this.ID.HEX_CONTROLS,on);}});this.setAttributeConfig(this.OPT.SHOW_WEBSAFE,{value:(attr.showwebsafe)||true,method:function(on){_hideShowEl.call(this,this.ID.WEBSAFE_SWATCH,on);}});this.setAttributeConfig(this.OPT.SHOW_HEX_SUMMARY,{value:(attr.showhexsummary)||true,method:function(on){_hideShowEl.call(this,this.ID.HEX_SUMMARY,on);if(on&&this.get(this.OPT.SHOW_HSV_CONTROLS)){this.set(this.OPT.SHOW_HSV_CONTROLS,false);}}});this.setAttributeConfig(this.OPT.ANIMATE,{value:(attr.animate)||true,method:function(on){this.pickerSlider.animate=on;this.hueSlider.animate=on;}});this.on(this.OPT.HUE+"Change",_updateRGBFromHSV,this,true);this.on(this.OPT.SATURATION+"Change",_updateRGBFromHSV,this,true);this.on(this.OPT.VALUE+"Change",_updatePickerSlider,this,true);this.on(this.OPT.RED+"Change",_updateRGB,this,true);this.on(this.OPT.GREEN+"Change",_updateRGB,this,true);this.on(this.OPT.BLUE+"Change",_updateRGB,this,true);this.on(this.OPT.HEX+"Change",_updateHex,this,true);this.initPicker();};var _updateRGB=function(){var rgb=[this.get(this.OPT.RED),this.get(this.OPT.GREEN),this.get(this.OPT.BLUE)];this.set(this.OPT.RGB,rgb);_updateSliders.call(this);};var _updateRGBFromHSV=function(){var hsv=[this.get(this.OPT.HUE),this.get(this.OPT.SATURATION)/100,this.get(this.OPT.VALUE)/100];var rgb=Color.hsv2rgb(hsv);this.set(this.OPT.RGB,rgb);_updateSliders.call(this);};var _updateHex=function(){var hex=this.get(this.OPT.HEX),l=hex.length;if(l===3){var c=hex.split(""),i;for(i=0;i<l;i=i+1){c[i]=c[i]+c[i];}
hex=c.join("");}
if(hex.length!==6){return false;}
var rgb=Color.hex2rgb(hex);this.setValue(rgb);};var _updateSliders=function(){_updateHueSlider.call(this);_updatePickerSlider.call(this);};var _updateHueSlider=function(){var size=this.get(this.OPT.PICKER_SIZE),h=this.get(this.OPT.HUE);h=size-Math.round(h/360*size);if(h===size){h=0;}
this.hueSlider.setValue(h);};var _updatePickerSlider=function(){var size=this.get(this.OPT.PICKER_SIZE),s=this.get(this.OPT.SATURATION),v=this.get(this.OPT.VALUE);s=Math.round(s*size/100);v=Math.round(size-(v*size/100));this.pickerSlider.setRegionValue(s,v);};var _createHostElement=function(){var el=document.createElement('div');if(this.CSS.BASE){el.className=this.CSS.BASE;}
return el;};})();YAHOO.register("colorpicker",YAHOO.widget.ColorPicker,{version:"2.3.0",build:"442"});