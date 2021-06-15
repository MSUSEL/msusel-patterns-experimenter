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

if(typeof(YAHOO.util.ImageLoader)=='undefined'){YAHOO.util.ImageLoader={};}
YAHOO.util.ImageLoader.group=function(trigEl,trigAct,timeout){this.name='unnamed';this._imgObjs={};this.timeoutLen=timeout;this._timeout=null;this._triggers=[];this.foldConditional=false;this.className=null;this._classImageEls=null;YAHOO.util.Event.addListener(window,'load',this._onloadTasks,this,true);this.addTrigger(trigEl,trigAct);};YAHOO.util.ImageLoader.group.prototype.addTrigger=function(trigEl,trigAct){if(!trigEl||!trigAct){return;}
var wrappedFetch=function(){this.fetch();};this._triggers.push([trigEl,trigAct,wrappedFetch]);YAHOO.util.Event.addListener(trigEl,trigAct,wrappedFetch,this,true);};YAHOO.util.ImageLoader.group.prototype._onloadTasks=function(){if(this.timeoutLen&&typeof(this.timeoutLen)=='number'&&this.timeoutLen>0){this._timeout=setTimeout(this._getFetchTimeout(),this.timeoutLen*1000);}
if(this.foldConditional){this._foldCheck();}};YAHOO.util.ImageLoader.group.prototype._getFetchTimeout=function(){var self=this;return function(){self.fetch();};};YAHOO.util.ImageLoader.group.prototype.registerBgImage=function(domId,url){this._imgObjs[domId]=new YAHOO.util.ImageLoader.bgImgObj(domId,url);return this._imgObjs[domId];};YAHOO.util.ImageLoader.group.prototype.registerSrcImage=function(domId,url,width,height){this._imgObjs[domId]=new YAHOO.util.ImageLoader.srcImgObj(domId,url,width,height);return this._imgObjs[domId];};YAHOO.util.ImageLoader.group.prototype.registerPngBgImage=function(domId,url){this._imgObjs[domId]=new YAHOO.util.ImageLoader.pngBgImgObj(domId,url);return this._imgObjs[domId];};YAHOO.util.ImageLoader.group.prototype.fetch=function(){YAHOO.log('Fetching images in group: "'+this.name+'".','info','imageloader');clearTimeout(this._timeout);for(var i=0;i<this._triggers.length;i++){YAHOO.util.Event.removeListener(this._triggers[i][0],this._triggers[i][1],this._triggers[i][2]);}
this._fetchByClass();for(var id in this._imgObjs){if(YAHOO.lang.hasOwnProperty(this._imgObjs,id)){this._imgObjs[id].fetch();}}};YAHOO.util.ImageLoader.group.prototype._foldCheck=function(){YAHOO.log('Checking for images above the fold in group: "'+this.name+'"','info','imageloader');var scrollTop=(document.compatMode!='CSS1Compat')?document.body.scrollTop:document.documentElement.scrollTop;var viewHeight=YAHOO.util.Dom.getViewportHeight();var hLimit=scrollTop+viewHeight;var scrollLeft=(document.compatMode!='CSS1Compat')?document.body.scrollLeft:document.documentElement.scrollLeft;var viewWidth=YAHOO.util.Dom.getViewportWidth();var wLimit=scrollLeft+viewWidth;for(var id in this._imgObjs){if(YAHOO.lang.hasOwnProperty(this._imgObjs,id)){var elPos=YAHOO.util.Dom.getXY(this._imgObjs[id].domId);if(elPos[1]<hLimit&&elPos[0]<wLimit){YAHOO.log('Image with id "'+this._imgObjs[id].domId+'" is above the fold. Fetching image.','info','imageloader');this._imgObjs[id].fetch();}}}
if(this.className){this._classImageEls=YAHOO.util.Dom.getElementsByClassName(this.className);for(var i=0;i<this._classImageEls.length;i++){var elPos=YAHOO.util.Dom.getXY(this._classImageEls[i]);if(elPos[1]<hLimit&&elPos[0]<wLimit){YAHOO.log('Image with id "'+this._classImageEls[i].id+'" is above the fold. Fetching image. (Image registered by class name with the group - may not have an id.)','info','imageloader');YAHOO.util.Dom.removeClass(this._classImageEls[i],this.className);}}}};YAHOO.util.ImageLoader.group.prototype._fetchByClass=function(){if(!this.className){return;}
YAHOO.log('Fetching all images with class "'+this.className+'" in group "'+this.name+'".','info','imageloader');if(this._classImageEls===null){this._classImageEls=YAHOO.util.Dom.getElementsByClassName(this.className);}
YAHOO.util.Dom.removeClass(this._classImageEls,this.className);};YAHOO.util.ImageLoader.imgObj=function(domId,url){this.domId=domId;this.url=url;this.width=null;this.height=null;this.setVisible=false;this._fetched=false;};YAHOO.util.ImageLoader.imgObj.prototype.fetch=function(){if(this._fetched){return;}
var el=document.getElementById(this.domId);if(!el){return;}
YAHOO.log('Fetching image with id "'+this.domId+'".','info','imageloader');this._applyUrl(el);if(this.setVisible){el.style.visibility='visible';}
if(this.width){el.width=this.width;}
if(this.height){el.height=this.height;}
this._fetched=true;};YAHOO.util.ImageLoader.imgObj.prototype._applyUrl=function(el){};YAHOO.util.ImageLoader.bgImgObj=function(domId,url){YAHOO.util.ImageLoader.bgImgObj.superclass.constructor.call(this,domId,url);};YAHOO.lang.extend(YAHOO.util.ImageLoader.bgImgObj,YAHOO.util.ImageLoader.imgObj);YAHOO.util.ImageLoader.bgImgObj.prototype._applyUrl=function(el){el.style.backgroundImage="url('"+this.url+"')";};YAHOO.util.ImageLoader.srcImgObj=function(domId,url,width,height){YAHOO.util.ImageLoader.srcImgObj.superclass.constructor.call(this,domId,url);this.width=width;this.height=height;};YAHOO.lang.extend(YAHOO.util.ImageLoader.srcImgObj,YAHOO.util.ImageLoader.imgObj);YAHOO.util.ImageLoader.srcImgObj.prototype._applyUrl=function(el){el.src=this.url;};YAHOO.util.ImageLoader.pngBgImgObj=function(domId,url){YAHOO.util.ImageLoader.pngBgImgObj.superclass.constructor.call(this,domId,url);};YAHOO.lang.extend(YAHOO.util.ImageLoader.pngBgImgObj,YAHOO.util.ImageLoader.imgObj);YAHOO.util.ImageLoader.pngBgImgObj.prototype._applyUrl=function(el){if(YAHOO.env.ua.ie&&YAHOO.env.ua.ie<=6){el.style.filter='progid:DXImageTransform.Microsoft.AlphaImageLoader(src="'+this.url+'", sizingMethod="scale")';}
else{el.style.backgroundImage="url('"+this.url+"')";}};YAHOO.register("imageloader",YAHOO.util.ImageLoader,{version:"2.3.0",build:"442"});