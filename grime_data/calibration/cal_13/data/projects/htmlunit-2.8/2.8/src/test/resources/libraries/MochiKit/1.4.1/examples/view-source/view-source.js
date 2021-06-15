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

Do syntax highlighting on every textarea inside of a "codeview" element

The content of textareas are URLs, not code!

*/
var viewSource = function () {
    var filename = location.hash;
    if (!filename) {
        filename = location.hash = "view-source/view-source.js";
    }
    filename = lstrip(filename, "#");
    var href = "../" + filename;
    replaceChildNodes("filename", "loading ", A({"href": href}, filename));
    replaceChildNodes("code", href);
    ext = filename.split(".").pop();
    var classes = {
        "html": "xml",
        "js": "javascript",
        "json": "javascript",
        "xml": "xml"
    };
    updateNodeAttributes("code", {"class": classes[ext]});
    syntaxHighlight(filename);
};

var syntaxHighlight = function (filename) {
    var swapContents = function (dest, req) {
        replaceChildNodes(dest, req.responseText);
    };

    var showParsing = function () {
        replaceChildNodes("filename",
            "parsing ", 
            A({"href": "../" + filename}, filename)
        );
        return wait(0);
    };

    var finishSyntaxHighlight = function () {
        dp.sh.HighlightAll("code", true, true, false);
        replaceChildNodes("filename", A({"href": "../" + filename}, filename));
        removeElementClass("codeview", "invisible");
    };

    var elems = getElementsByTagAndClassName("textarea", null, "codeview");
    var dl = new Deferred();
    var deferredCount = 0;
    var checkDeferredList = function () {
        deferredCount -= 1;
        if (!deferredCount) {
            dl.callback();
        }
    };
    for (var i = 0; i < elems.length; i++) {
        var elem = elems[i];
        if (elem.name != "code") {
            continue;
        }
        var url = strip(scrapeText(elem))
        var d = doXHR(url).addCallback(
            partial(swapContents, elem)
        );
        deferredCount += 1;
        d.addCallback(checkDeferredList);
    }
    dl.addCallback(showParsing);
    dl.addCallback(finishSyntaxHighlight);
};
