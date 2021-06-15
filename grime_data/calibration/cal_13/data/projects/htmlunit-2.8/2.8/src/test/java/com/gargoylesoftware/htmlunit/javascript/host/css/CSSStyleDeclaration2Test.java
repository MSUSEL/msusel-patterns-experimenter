/**
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
package com.gargoylesoftware.htmlunit.javascript.host.css;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;

/**
 * Tests for {@link CSSStyleDeclaration}.
 *
 * @version $Revision: 5301 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class CSSStyleDeclaration2Test extends WebDriverTestCase {

    /*
     Below is a page to see the different elements behavior
<html>
  <head>
    <script>
      function test() {
        //all properties of CSSStyleDeclaration in JavaScriptConfiguration.xml
        var properties = ['azimuth','background','backgroundAttachment','backgroundColor','backgroundImage',
        'backgroundPosition','backgroundPositionX','backgroundPositionY','backgroundRepeat','behavior','border',
        'borderBottom','borderBottomColor','borderBottomStyle','borderBottomWidth','borderCollapse','borderColor',
        'borderLeft','borderLeftColor','borderLeftStyle','borderLeftWidth','borderRight','borderRightColor',
        'borderRightStyle','borderRightWidth','borderSpacing','borderStyle','borderTop','borderTopColor',
        'borderTopStyle','borderTopWidth','borderWidth','bottom','captionSide','clear','clip','color','content',
        'counterIncrement','counterReset','cssFloat','cssText','cue','cueAfter','cueBefore','cursor','direction',
        'display','elevation','emptyCells','filter','font','fontFamily','fontSize','fontSizeAdjust','fontStretch',
        'fontStyle','fontVariant','fontWeight','height','imeMode','layoutFlow','layoutGrid','layoutGridChar',
        'layoutGridLine','layoutGridMode','layoutGridType','left','letterSpacing','lineBreak','lineHeight',
        'listStyle','listStyleImage','listStylePosition','listStyleType','margin','marginBottom','marginLeft',
        'marginRight','marginTop','markerOffset','marks','maxHeight','maxWidth','minHeight','minWidth',
        'MozAppearance','MozBackgroundClip','MozBackgroundInlinePolicy','MozBackgroundOrigin','MozBinding',
        'MozBorderBottomColors','MozBorderLeftColors','MozBorderRadius','MozBorderRadiusBottomleft',
        'MozBorderRadiusBottomright','MozBorderRadiusTopleft','MozBorderRadiusTopright','MozBorderRightColors',
        'MozBorderTopColors','MozBoxAlign','MozBoxDirection','MozBoxFlex','MozBoxOrdinalGroup','MozBoxOrient',
        'MozBoxPack','MozBoxSizing','MozColumnCount','MozColumnGap','MozColumnWidth','MozFloatEdge',
        'MozForceBrokenImageIcon','MozImageRegion','MozMarginEnd','MozMarginStart','MozOpacity','MozOutline',
        'MozOutlineColor','MozOutlineOffset','MozOutlineRadius','MozOutlineRadiusBottomleft',
        'MozOutlineRadiusBottomright','MozOutlineRadiusTopleft','MozOutlineRadiusTopright','MozOutlineStyle',
        'MozOutlineWidth','MozPaddingEnd','MozPaddingStart','MozUserFocus','MozUserInput','MozUserModify',
        'MozUserSelect','msInterpolationMode','opacity','orphans','outline','outlineColor','outlineOffset',
        'outlineStyle','outlineWidth','overflow','overflowX','overflowY','padding','paddingBottom','paddingLeft',
        'paddingRight','paddingTop','page','pageBreakAfter','pageBreakBefore','pageBreakInside','pause',
        'pauseAfter','pauseBefore','pitch','pitchRange','pixelBottom','pixelLeft','pixelRight','pixelTop',
        'posBottom','posHeight','position','posLeft','posRight','posTop','posWidth','quotes','richness',
        'right','rubyAlign','rubyOverhang','rubyPosition','scrollbar3dLightColor','scrollbarArrowColor',
        'scrollbarBaseColor','scrollbarDarkShadowColor','scrollbarFaceColor','scrollbarHighlightColor',
        'scrollbarShadowColor','scrollbarTrackColor','size','speak','speakHeader','speakNumeral',
        'speakPunctuation','speechRate','stress','styleFloat','tableLayout','textAlign','textAlignLast',
        'textAutospace','textDecoration','textDecorationBlink','textDecorationLineThrough','textDecorationNone',
        'textDecorationOverline','textDecorationUnderline','textIndent','textJustify','textJustifyTrim',
        'textKashida','textKashidaSpace','textOverflow','textShadow','textTransform','textUnderlinePosition',
        'top','unicodeBidi','verticalAlign','visibility','voiceFamily','volume','whiteSpace','widows','width',
        'wordBreak','wordSpacing','wordWrap','writingMode','zIndex','zoom'];

    var ta = document.getElementById('myTextarea');
    for (var prop in properties) {
      prop = properties[prop];
      var node = document.createElement('div');
      var buffer = prop + ':';
      try {
        buffer += node.style[prop];
        node.style[prop] = '42.0';
        buffer += ',' + node.style[prop];
        node.style[prop] = '42.7';
        buffer += ',' + node.style[prop];
        node.style[prop] = '42';
        buffer += ',' + node.style[prop];
      } catch (e) {
          buffer += ',' + 'error';
      }
      ta.value += buffer + '\n';
    }
}
</script></head>
<body onload='test()'>
  <textarea id='myTextarea' cols='120' rows='40'></textarea>
</body></html>
     */

    /**
     * @throws Exception if the test fails
     */
    @Test
    @NotYetImplemented(Browser.IE6)
    //TODO: This test fails with WebDriver with real FF3, but succeed if test is done outside WebDriver
    @Alerts(FF = "success", IE = "success",
            IE7 = "error: outlineWidth-error: outlineWidth-error: outlineWidth-error: outlineWidth-",
            IE6 = "error: maxHeight-error: maxHeight-error: maxHeight-error: maxHeight-error: maxWidth-error: "
                + "maxWidth-error: maxWidth-error: maxWidth-error: minWidth-error: minWidth-error: minWidth-error: "
                + "minWidth-error: outlineWidth-error: outlineWidth-error: outlineWidth-error: outlineWidth-")
    public void width_like_properties() throws Exception {
        final String html
            = "<html><head><script>\n"
            + "function test() {\n"
            + "  var properties = ['borderBottomWidth','borderLeftWidth','borderRightWidth','borderTopWidth',\n"
            + "      'bottom', 'fontSize','height','left','letterSpacing','marginBottom','marginLeft',\n"
            + "      'marginRight','marginTop','maxHeight','maxWidth','minHeight','minWidth',\n"
            + "      'outlineWidth','paddingBottom','paddingLeft','paddingRight','paddingTop','right',\n"
            + "      'textIndent','top','verticalAlign','width','wordSpacing'];\n"
            + "\n"
            + "  var result = '';\n"
            + "  for (var prop in properties) {\n"
            + "    prop = properties[prop];\n"
            + "    var node = document.createElement('div');\n"
            + "    if (node.style[prop] != '')\n"
            + "      result += 'error: ' + prop + '-';\n"
            + "    node.style[prop] = '42.0';\n"
            + "    if (node.style[prop] != '42px')\n"
            + "      result += 'error: ' + prop + '-';\n"
            + "    node.style[prop] = '42.7';\n"
            + "    var expected = document.all ? '42px' : '42.7px';\n"
            + "    if (node.style[prop] != expected)\n"
            + "      result += 'error: ' + prop + '-';\n"
            + "    node.style[prop] = '42';\n"
            + "    if (node.style[prop] != '42px')\n"
            + "      result += 'error: ' + prop + '-';\n"
            + "  }\n"
            + "  alert(result == '' ? 'success' : result);\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'></body></html>";

        loadPageWithAlerts2(html);
    }
}
