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
(function(b){var c,a=[];function e(g,f,i){var h;h=b.fn[f];b.fn[f]=function(){var j;if(g!=="after"){j=i.apply(this,arguments);if(j!==undefined){return j}}j=h.apply(this,arguments);if(g!=="before"){i.apply(this,arguments)}return j}}b.fn.tinymce=function(i){var h=this,g,j="",f;if(!h.length){return}if(!i){return tinyMCE.get(this[0].id)}function k(){if(d){d();d=null}h.each(function(m,p){var l,o=p.id||tinymce.DOM.uniqueId();p.id=o;l=new tinymce.Editor(o,i);l.render()})}if(!window.tinymce&&!c&&(g=i.script_url)){c=1;if(/_(src|dev)\.js/g.test(g)){j="_src"}window.tinyMCEPreInit={base:g.substring(0,g.lastIndexOf("/")),suffix:j,query:""};b.getScript(g,function(){tinymce.dom.Event.domLoaded=1;c=2;k();b.each(a,function(l,m){m()})})}else{if(c===1){a.push(k)}else{k()}}};b.extend(b.expr[":"],{tinymce:function(f){return f.id&&!!tinyMCE.get(f.id)}});function d(){function f(){this.find("span.mceEditor,div.mceEditor").each(function(j,k){var h;if(h=tinyMCE.get(k.id.replace(/_parent$/,""))){h.remove()}})}function g(i){var h;if(i!==undefined){f.call(this);this.each(function(k,l){var j;if(j=tinyMCE.get(l.id)){j.setContent(i)}})}else{if(this.length>0){if(h=tinyMCE.get(this[0].id)){return h.getContent()}}}}e("both","text",function(h){if(h!==undefined){return g.call(this,h)}if(this.length>0){if(ed=tinyMCE.get(this[0].id)){return ed.getContent().replace(/<[^>]+>/g,"")}}});b.each(["val","html"],function(j,h){e("both",h,g)});b.each(["append","prepend"],function(j,h){e("before",h,function(i){if(i!==undefined){this.each(function(l,m){var k;if(k=tinyMCE.get(m.id)){if(h==="append"){k.setContent(k.getContent()+i)}else{k.setContent(i+k.getContent())}}})}})});e("both","attr",function(h,i){if(h&&h==="value"){return g.call(this,i)}});b.each(["remove","replaceWith","replaceAll","empty"],function(j,h){e("before",h,f)})}})(jQuery);