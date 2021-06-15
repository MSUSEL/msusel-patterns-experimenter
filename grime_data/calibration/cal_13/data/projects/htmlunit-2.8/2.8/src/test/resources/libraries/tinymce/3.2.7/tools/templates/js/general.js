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
(function($){
	var currentPage, currentHash;

	function resizeUI() {
		$('#doc3').css('height', (window.innerHeight || document.documentElement.clientHeight) - $('#hd').height() - 12);
	}

	function scrollToHash(hash) {
		if (hash) {
			$(hash).each(function() {
				$(this)[0].scrollIntoView();
			});
		}
	}

	function loadURL(url) {
		var parts, hash;

		// Trim away everything but the file name
		url = /([^\/]+)$/.exec(url)[0];

		// Parse out parts
		parts = /^([^#]+)(#.+)?$/.exec(url);
		hash = parts[2];

		// In page link, no need to load anything
		if (parts[1] == currentPage) {
			if (hash)
				scrollToHash(hash);
			else
				 $('#detailsView')[0].scrollTop = 0;

			return;
		}

		currentPage = parts[1];

		$("#classView a.selected").removeClass('selected');
		$("#classView a[href='" + currentPage.replace(/^.*\/([^\/]+)$/, '$1') + "']").addClass('selected').focus().parents("li.expandable").each(function() {
			var li = $(this).removeClass("expandable").addClass("collapsable");

			li.find("> div.expandable-hitarea").removeClass("expandable-hitarea").addClass("collapsable-hitarea");
			li.find("> ul").show();
		});

		$('#detailsView').find("div.page").hide();

		// Check if the page has been loaded before
		if ($("#detailsView div[url='" + currentPage + "']").show().length == 0) {
			$('#detailsView').addClass("loading");

			// Load page and cache it in a div
			$.get(currentPage, "", function(data) {
				data = /<body[^>]*>([\s\S]+)<\/body>/.exec(data);

				if (data) {
					$('#detailsView').removeClass("loading").append('<div url="' + currentPage + '" class="page">' + data[1] + '</div>')[0].scrollTop = 0;

					SyntaxHighlighter.config.clipboardSwf = 'js/clipboard.swf';
					SyntaxHighlighter.highlight({gutter : false});

					scrollToHash(hash);
				}
			});
		} else
			scrollToHash(hash);
	}

	$().ready(function(){
		$("#browser").treeview();
		$(window).resize(resizeUI).trigger('resize');

		window.setInterval(function() {
			var hash = document.location.hash;

			if (hash != currentHash && hash) {
				loadURL(hash.replace(/\-/g, '#').substring(1));
				currentHash = hash;
			}
		}, 100);

		$("a").live("click", function(e) {
			var url = e.target.href;

			if (e.button == 0) {
				if (url.indexOf('class_') != -1 || url.indexOf('alias_') != -1 || url.indexOf('member_') != -1) {
					document.location.hash = e.target.href.replace(/^.*\/([^\/]+)/, '$1').replace(/#/g, '-');

					loadURL(url);
				}

				e.preventDefault();
			}
		});
	});
})(jQuery);
