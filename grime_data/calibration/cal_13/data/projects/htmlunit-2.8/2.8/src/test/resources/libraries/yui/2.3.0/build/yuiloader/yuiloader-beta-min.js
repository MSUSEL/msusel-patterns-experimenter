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

(function(){if(typeof YAHOO_config==="undefined"){YAHOO_config={};}
var YUI={info:{'base':'http://yui.yahooapis.com/2.3.0/build/','skin':{'defaultSkin':'sam','base':'assets/skins/','path':'skin.css','rollup':3},'moduleInfo':{'animation':{'type':'js','path':'animation/animation-min.js','requires':['dom','event']},'autocomplete':{'type':'js','path':'autocomplete/autocomplete-min.js','requires':['dom','event'],'optional':['connection','animation'],'skinnable':true},'button':{'type':'js','path':'button/button-beta-min.js','requires':['element'],'optional':['menu'],'skinnable':true},'calendar':{'type':'js','path':'calendar/calendar-min.js','requires':['event','dom'],'skinnable':true},'colorpicker':{'type':'js','path':'colorpicker/colorpicker-beta-min.js','requires':['slider','element'],'optional':['animation'],'skinnable':true},'connection':{'type':'js','path':'connection/connection-min.js','requires':['event']},'container':{'type':'js','path':'container/container-min.js','requires':['dom','event'],'optional':['dragdrop','animation'],'supersedes':['containercore'],'skinnable':true},'containercore':{'type':'js','path':'container/container_core-min.js','requires':['dom','event']},'datasource':{'type':'js','path':'datasource/datasource-beta-min.js','requires':['event'],'optional':['connection']},'datatable':{'type':'js','path':'datatable/datatable-beta-min.js','requires':['element','datasource'],'optional':['calendar','dragdrop'],'skinnable':true},'dom':{'type':'js','path':'dom/dom-min.js','requires':['yahoo']},'dragdrop':{'type':'js','path':'dragdrop/dragdrop-min.js','requires':['dom','event']},'editor':{'type':'js','path':'editor/editor-beta-min.js','requires':['menu','container','element','button'],'optional':['animation','dragdrop'],'skinnable':true},'element':{'type':'js','path':'element/element-beta-min.js','requires':['dom','event']},'event':{'type':'js','path':'event/event-min.js','requires':['yahoo']},'fonts':{'type':'css','path':'fonts/fonts-min.css'},'grids':{'type':'css','path':'grids/grids-min.css','requires':['fonts'],'optional':['reset']},'history':{'type':'js','path':'history/history-beta-min.js','requires':['event']},'imageloader':{'type':'js','path':'imageloader/imageloader-experimental-min.js','requires':['event','dom']},'logger':{'type':'js','path':'logger/logger-min.js','requires':['event','dom'],'optional':['dragdrop'],'skinnable':true},'menu':{'type':'js','path':'menu/menu-min.js','requires':['containercore'],'skinnable':true},'reset':{'type':'css','path':'reset/reset-min.css'},'reset-fonts-grids':{'type':'css','path':'reset-fonts-grids/reset-fonts-grids.css','supersedes':['reset','fonts','grids']},'slider':{'type':'js','path':'slider/slider-min.js','requires':['dragdrop'],'optional':['animation']},'tabview':{'type':'js','path':'tabview/tabview-min.js','requires':['element'],'optional':['connection'],'skinnable':true},'treeview':{'type':'js','path':'treeview/treeview-min.js','requires':['event'],'skinnable':true},'utilities':{'type':'js','path':'utilities/utilities.js','supersedes':['yahoo','event','dragdrop','animation','dom','connection','element','yahoo-dom-event'],'rollup':6},'yahoo':{'type':'js','path':'yahoo/yahoo-min.js'},'yahoo-dom-event':{'type':'js','path':'yahoo-dom-event/yahoo-dom-event.js','supersedes':['yahoo','event','dom'],'rollup':3},'yuiloader':{'type':'js','path':'yuiloader/yuiloader-beta-min.js'},'yuitest':{'type':'js','path':'yuitest/yuitest-beta-min.js','requires':['logger'],'skinnable':true}}},ObjectUtil:{appendArray:function(o,a){if(a){for(var i=0;i<a.length;i=i+1){o[a[i]]=true;}}},clone:function(o){var c={};for(var i in o){c[i]=o[i];}
return c;},merge:function(){var o={},a=arguments,i,j;for(i=0;i<a.length;i=i+1){for(j in a[i]){o[j]=a[i][j];}}
return o;},keys:function(o,ordered){var a=[],i;for(i in o){a.push(i);}
return a;}},ArrayUtil:{appendArray:function(a1,a2){Array.prototype.push.apply(a1,a2);},indexOf:function(a,val){for(var i=0;i<a.length;i=i+1){if(a[i]===val){return i;}}
return-1;},toObject:function(a){var o={};for(var i=0;i<a.length;i=i+1){o[a[i]]=true;}
return o;},uniq:function(a){return YUI.ObjectUtil.keys(YUI.ArrayUtil.toObject(a));}},loaders:[],finishInit:function(yahooref){yahooref=yahooref||YAHOO;yahooref.env.YUIInfo=YUI.info;yahooref.util.YUILoader=YUI.YUILoader;},onModuleLoaded:function(minfo){var mname=minfo.name,m;for(var i=0;i<YUI.loaders.length;i=i+1){YUI.loaders[i].loadNext(mname);}},init:function(){var c=YAHOO_config,o=c.load,y_loaded=(typeof YAHOO!=="undefined"&&YAHOO.env);if(y_loaded){YAHOO.env.listeners.push(YUI.onModuleLoaded);}else{if(c.listener){YUI.cachedCallback=c.listener;}
c.listener=function(minfo){YUI.onModuleLoaded(minfo);if(YUI.cachedCallback){YUI.cachedCallback(minfo);}};}
if(o||!y_loaded){o=o||{};var loader=new YUI.YUILoader(o);loader.onLoadComplete=function(){YUI.finishInit();if(o.onLoadComplete){loader._pushEvents();o.onLoadComplete(loader);}};if(!y_loaded){loader.require("yahoo");}
loader.insert(null,o);}else{YUI.finishInit();}}};YUI.YUILoader=function(o){YAHOO_config.injecting=true;o=o||{};this._internalCallback=null;this.onLoadComplete=null;this.base=("base"in o)?o.base:YUI.info.base;this.allowRollup=("allowRollup"in o)?o.allowRollup:true;this.filter=o.filter;this.sandbox=o.sandbox;this.required={};this.moduleInfo=o.moduleInfo||YUI.info.moduleInfo;this.rollups=null;this.loadOptional=o.loadOptional||false;this.sorted=[];this.loaded={};this.dirty=true;this.inserted={};this.skin=o.skin||YUI.ObjectUtil.clone(YUI.info.skin);if(o.require){this.require(o.require);}
YUI.loaders.push(this);};YUI.YUILoader.prototype={FILTERS:{RAW:{'searchExp':"-min\\.js",'replaceStr':".js"},DEBUG:{'searchExp':"-min\\.js",'replaceStr':"-debug.js"}},SKIN_PREFIX:"skin-",addModule:function(o){if(!o||!o.name||!o.type||(!o.path&&!o.fullpath)){return false;}
this.moduleInfo[o.name]=o;this.dirty=true;return true;},require:function(what){var a=(typeof what==="string")?arguments:what;this.dirty=true;for(var i=0;i<a.length;i=i+1){this.required[a[i]]=true;var s=this.parseSkin(a[i]);if(s){this._addSkin(s.skin,s.module);}}
YUI.ObjectUtil.appendArray(this.required,a);},_addSkin:function(skin,mod){var name=this.formatSkin(skin);if(!this.moduleInfo[name]){this.addModule({'name':name,'type':'css','path':this.skin.base+skin+"/"+this.skin.path,'rollup':this.skin.rollup});}
if(mod){name=this.formatSkin(skin,mod);if(!this.moduleInfo[name]){this.addModule({'name':name,'type':'css','path':mod+'/'+this.skin.base+skin+"/"+mod+".css"});}}},getRequires:function(mod){if(!this.dirty&&mod.expanded){return mod.expanded;}
mod.requires=mod.requires||[];var i,d=[],r=mod.requires,o=mod.optional,s=mod.supersedes,info=this.moduleInfo;for(i=0;i<r.length;i=i+1){d.push(r[i]);YUI.ArrayUtil.appendArray(d,this.getRequires(info[r[i]]));}
if(o&&this.loadOptional){for(i=0;i<o.length;i=i+1){d.push(o[i]);YUI.ArrayUtil.appendArray(d,this.getRequires(info[o[i]]));}}
mod.expanded=YUI.ArrayUtil.uniq(d);return mod.expanded;},getProvides:function(name){var mod=this.moduleInfo[name];var o={};o[name]=true;s=mod&&mod.supersedes;YUI.ObjectUtil.appendArray(o,s);return o;},calculate:function(o){if(this.dirty){this._setup(o);this._explode();this._skin();if(this.allowRollup){this._rollup();}
this._reduce();this._sort();this.dirty=false;}},_setup:function(o){o=o||{};this.loaded=YUI.ObjectUtil.clone(this.inserted);if(!this.sandbox&&typeof YAHOO!=="undefined"&&YAHOO.env){this.loaded=YUI.ObjectUtil.merge(this.loaded,YAHOO.env.modules);}
if(o.ignore){YUI.ObjectUtil.appendArray(this.loaded,o.ignore);}
if(o.force){for(var i=0;i<o.force.length;i=i+1){if(o.force[i]in this.loaded){delete this.loaded[o.force[i]];}}}},_explode:function(){var r=this.required,i,mod;for(i in r){mod=this.moduleInfo[i];if(mod){var req=this.getRequires(mod);if(req){YUI.ObjectUtil.appendArray(r,req);}}}},_skin:function(){var r=this.required,i,mod;for(i in r){mod=this.moduleInfo[i];if(mod&&mod.skinnable){var o=this.skin.override,j;if(o&&o[i]){for(j=0;j<o[i].length;j=j+1){this.require(this.formatSkin(o[i][j],i));}}else{this.require(this.formatSkin(this.skin.defaultSkin,i));}}}},formatSkin:function(skin,mod){var s=this.SKIN_PREFIX+skin;if(mod){s=s+"-"+mod;}
return s;},parseSkin:function(mod){if(mod.indexOf(this.SKIN_PREFIX)===0){var a=mod.split("-");return{skin:a[1],module:a[2]};}
return null;},_rollup:function(){var i,j,m,s,rollups={},r=this.required,roll;if(this.dirty||!this.rollups){for(i in this.moduleInfo){m=this.moduleInfo[i];if(m&&m.rollup){rollups[i]=m;}}
this.rollups=rollups;}
for(;;){var rolled=false;for(i in rollups){if(!r[i]&&!this.loaded[i]){m=this.moduleInfo[i];s=m.supersedes;roll=true;if(!m.rollup){continue;}
var skin=this.parseSkin(i),c=0;if(skin){for(j in r){if(i!==j&&this.parseSkin(j)){c++;roll=(c>=m.rollup);if(roll){break;}}}}else{for(j=0;j<s.length;j=j+1){if(this.loaded[s[j]]){roll=false;break;}else if(r[s[j]]){c++;roll=(c>=m.rollup);if(roll){break;}}}}
if(roll){r[i]=true;rolled=true;this.getRequires(m);}}}
if(!rolled){break;}}},_reduce:function(){var i,j,s,m,r=this.required;for(i in r){if(i in this.loaded){delete r[i];}else{var skinDef=this.parseSkin(i);if(skinDef){if(!skinDef.module){var skin_pre=this.SKIN_PREFIX+skinDef.skin;for(j in r){if(j!==i&&j.indexOf(skin_pre)>-1){delete r[j];}}}}else{m=this.moduleInfo[i];s=m&&m.supersedes;if(s){for(j=0;j<s.length;j=j+1){if(s[j]in r){delete r[s[j]];}}}}}}},_sort:function(){var s=[],info=this.moduleInfo,loaded=this.loaded;var requires=function(aa,bb){if(loaded[bb]){return false;}
var ii,mm=info[aa],rr=mm&&mm.expanded;if(rr&&YUI.ArrayUtil.indexOf(rr,bb)>-1){return true;}
var ss=info[bb]&&info[bb].supersedes;if(ss){for(ii=0;ii<ss.length;ii=i+1){if(requires(aa,ss[ii])){return true;}}}
return false;};for(var i in this.required){s.push(i);}
var p=0;for(;;){var l=s.length,a,b,j,k,moved=false;for(j=p;j<l;j=j+1){a=s[j];for(k=j+1;k<l;k=k+1){if(requires(a,s[k])){b=s.splice(k,1);s.splice(j,0,b[0]);moved=true;break;}}
if(moved){break;}else{p=p+1;}}
if(!moved){break;}}
this.sorted=s;},insert:function(callback,o,type){if(!type){var self=this;this._internalCallback=function(){self._internalCallback=null;self.insert(callback,o,"js");};this.insert(null,o,"css");return;}
o=o||{};this.onLoadComplete=callback||this.onLoadComplete;var f=o&&o.filter||null;if(typeof f==="string"){f=f.toUpperCase();if(f==="DEBUG"){this.require("logger");}}
this.filter=this.FILTERS[f]||f||this.FILTERS[this.filter]||this.filter;this.insertOptions=o;this.calculate(o);this.loading=true;this.loadType=type;this.loadNext();},loadNext:function(mname){if(mname){this.inserted[mname]=true;}
if(!this.loading){return;}
if(mname&&mname!==this.loading){return;}
var s=this.sorted,len=s.length,i,m,url;for(i=0;i<len;i=i+1){if(s[i]in this.inserted){continue;}
if(s[i]===this.loading){return;}
m=this.moduleInfo[s[i]];if(!this.loadType||this.loadType===m.type){this.loading=s[i];if(m.type==="css"){url=m.fullpath||this._url(m.path);this.insertCss(url);this.inserted[s[i]]=true;}else{url=m.fullpath||this._url(m.path);this.insertScript(url);if(m.verifier){var self=this,name=s[i];m.verifier(name,function(){self.loadNext(name);});}
return;}}}
this.loading=null;if(this._internalCallback){var f=this._internalCallback;this._internalCallback=null;f(this);}else if(this.onLoadComplete){this._pushEvents();this.onLoadComplete(this);}},_pushEvents:function(){if(typeof YAHOO!=="undefined"&&YAHOO.util&&YAHOO.util.Event){YAHOO.util.Event._load();}},_url:function(path){var u=this.base||"",f=this.filter;u=u+path;if(f){u=u.replace(new RegExp(f.searchExp),f.replaceStr);}
return u;},insertScript:function(url,win){var w=win||window,d=w.document,n=d.createElement("script"),h=d.getElementsByTagName("head")[0];n.src=url;n.type="text/javascript";h.appendChild(n);},insertCss:function(url,win){var w=win||window,d=w.document,n=d.createElement("link"),h=d.getElementsByTagName("head")[0];n.href=url;n.type="text/css";n.rel="stylesheet";h.appendChild(n);},sandbox:function(callback){}};YUI.init();})();