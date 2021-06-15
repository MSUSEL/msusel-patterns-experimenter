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
package com.itextpdf.text.pdf.richmedia;

import java.io.IOException;
import java.util.HashMap;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.exceptions.IllegalPdfSyntaxException;
import com.itextpdf.text.pdf.PdfAnnotation;
import com.itextpdf.text.pdf.PdfArray;
import com.itextpdf.text.pdf.PdfDeveloperExtension;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfFileSpecification;
import com.itextpdf.text.pdf.PdfIndirectReference;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfNameTree;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Object that is able to create Rich Media Annotations as described
 * in the document "Acrobat Supplement to the ISO 32000", referenced
 * in the code as "ExtensionLevel 3". This annotation is described in
 * section 9.6 entitled "Rich Media" of this document.
 * Extension level 3 introduces rich media PDF constructs that support
 * playing a SWF file and provide enhanced rich media. With rich media
 * annotation, Flash applications, video, audio, and other multimedia
 * can be attached to a PDF with expanded functionality. It improves upon
 * the existing 3D annotation structure to support multiple multimedia
 * file assets, including Flash video and compatible variations on the
 * H.264 format. The new constructs allow a two-way scripting bridge between
 * Flash and a conforming application. There is support for generalized
 * linking of a Flash application state to a comment or view, which enables
 * video commenting. Finally, actions can be linked to video chapter points.
 * @since	5.0.0
 */
public class RichMediaAnnotation {
	/** The PdfWriter to which the annotation will be added. */
	protected PdfWriter writer;
	/** The annotation object */
	protected PdfAnnotation annot;
	/** the rich media content (can be reused for different annotations) */
	protected PdfDictionary richMediaContent = null;
	/** a reference to the RichMediaContent that can be reused. */
	protected PdfIndirectReference richMediaContentReference = null;
	/** the rich media settings (specific for this annotation) */
	protected PdfDictionary richMediaSettings = new PdfDictionary(PdfName.RICHMEDIASETTINGS);
	/** a map with the assets (will be used to construct a name tree.) */
	protected HashMap<String, PdfIndirectReference> assetsmap = null;
	/** an array with configurations (will be added to the RichMediaContent). */
	protected PdfArray configurations = null;
	/** an array of views (will be added to the RichMediaContent) */
	protected PdfArray views = null;

	/**
	 * Creates a RichMediaAnnotation.
	 * @param	writer	the PdfWriter to which the annotation will be added.
	 * @param	rect	the rectangle where the annotation will be added.
	 */
	public RichMediaAnnotation(PdfWriter writer, Rectangle rect) {
		this.writer = writer;
		annot = new PdfAnnotation(writer, rect);
        annot.put(PdfName.SUBTYPE, PdfName.RICHMEDIA);
        richMediaContent = new PdfDictionary(PdfName.RICHMEDIACONTENT);
		assetsmap = new HashMap<String, PdfIndirectReference>();
		configurations = new PdfArray();
		views = new PdfArray();
	}

	/**
	 * Creates a RichMediaAnnotation using rich media content that has already
	 * been added to the writer. Note that assets, configurations, views added
	 * to a RichMediaAnnotation created like this will be ignored.
	 * @param	writer	the PdfWriter to which the annotation will be added.
	 * @param	rect	the rectangle where the annotation will be added.
	 * @param	richMediaContentReference	reused rich media content.
	 */
	public RichMediaAnnotation(PdfWriter writer, Rectangle rect, PdfIndirectReference richMediaContentReference) {
		this.richMediaContentReference = richMediaContentReference;
		richMediaContent = null;
		this.writer = writer;
		annot = new PdfAnnotation(writer, rect);
		annot.put(PdfName.SUBTYPE, PdfName.RICHMEDIA);
	}

	/**
	 * Gets a reference to the RichMediaContent for reuse of the
	 * rich media content. Returns null if the content hasn't been
	 * added to the OutputStream yet.
	 * @return	a PdfDictionary with RichMediaContent
	 */
	public PdfIndirectReference getRichMediaContentReference() {
		return richMediaContentReference;
	}

	/**
	 * Adds an embedded file.
	 * (Part of the RichMediaContent.)
	 * @param	name	a name for the name tree
	 * @param	fs		a file specification for an embedded file.
	 */
	public PdfIndirectReference addAsset(String name, PdfFileSpecification fs)
		throws IOException {
		if (assetsmap == null)
			throw new IllegalPdfSyntaxException(
				"You can't add assets to reused RichMediaContent.");
		PdfIndirectReference ref = writer.addToBody(fs).getIndirectReference();
		assetsmap.put(name, ref);
		return ref;
	}

	/**
	 * Adds a reference to an embedded file.
	 * (Part of the RichMediaContent.)
	 * @param	ref	a reference to a PdfFileSpecification
	 */
	public PdfIndirectReference addAsset(String name, PdfIndirectReference ref) throws IOException {
		if (views == null)
			throw new IllegalPdfSyntaxException(
				"You can't add assets to reused RichMediaContent.");
		assetsmap.put(name, ref);
		return ref;
	}

	/**
	 * Adds a RichMediaConfiguration.
	 * (Part of the RichMediaContent.)
	 * @param	configuration	a configuration dictionary
	 */
	public PdfIndirectReference addConfiguration(RichMediaConfiguration configuration) throws IOException {
		if (configurations == null)
			throw new IllegalPdfSyntaxException(
				"You can't add configurations to reused RichMediaContent.");
		PdfIndirectReference ref = writer.addToBody(configuration).getIndirectReference();
		configurations.add(ref);
		return ref;
	}

	/**
	 * Adds a reference to a RichMediaConfiguration.
	 * (Part of the RichMediaContent.)
	 * @param	ref		a reference to a RichMediaConfiguration
	 */
	public PdfIndirectReference addConfiguration(PdfIndirectReference ref) throws IOException {
		if (configurations == null)
			throw new IllegalPdfSyntaxException(
				"You can't add configurations to reused RichMediaContent.");
		configurations.add(ref);
		return ref;
	}

	/**
	 * Adds a view dictionary.
	 * (Part of the RichMediaContent.)
	 * @param	view	a view dictionary
	 */
	public PdfIndirectReference addView(PdfDictionary view) throws IOException {
		if (views == null)
			throw new IllegalPdfSyntaxException(
				"You can't add views to reused RichMediaContent.");
		PdfIndirectReference ref = writer.addToBody(view).getIndirectReference();
		views.add(ref);
		return ref;
	}

	/**
	 * Adds a reference to a view dictionary.
	 * (Part of the RichMediaContent.)
	 * @param	ref	a reference to a view dictionary
	 */
	public PdfIndirectReference addView(PdfIndirectReference ref) throws IOException {
		if (views == null)
			throw new IllegalPdfSyntaxException(
				"You can't add views to reused RichMediaContent.");
		views.add(ref);
		return ref;
	}

	/**
	 * Sets the RichMediaActivation dictionary specifying the style of
	 * presentation, default script behavior, default view information,
	 * and animation style when the annotation is activated.
	 * (Part of the RichMediaSettings.)
	 * @param	richMediaActivation
	 */
	public void setActivation(RichMediaActivation richMediaActivation) {
		richMediaSettings.put(PdfName.ACTIVATION, richMediaActivation);
	}

	/**
	 * Sets the RichMediaDeactivation dictionary specifying the condition
	 * that causes deactivation of the annotation.
	 * (Part of the RichMediaSettings.)
	 * @param	richMediaDeactivation
	 */
	public void setDeactivation(RichMediaDeactivation richMediaDeactivation) {
		richMediaSettings.put(PdfName.DEACTIVATION, richMediaDeactivation);
	}

	/**
	 * Creates the actual annotation and adds different elements to the
	 * PdfWriter while doing so.
	 * @return	a PdfAnnotation
	 */
	public PdfAnnotation createAnnotation() throws IOException {
		if (richMediaContent != null) {
			if (!assetsmap.isEmpty()) {
				PdfDictionary assets = PdfNameTree.writeTree(assetsmap, writer);
				richMediaContent.put(PdfName.ASSETS, writer.addToBody(assets).getIndirectReference());
			}
			if (configurations.size() > 0) {
				richMediaContent.put(PdfName.CONFIGURATION, writer.addToBody(configurations).getIndirectReference());
			}
			if (views.size() > 0) {
				richMediaContent.put(PdfName.VIEWS, writer.addToBody(views).getIndirectReference());
			}
			richMediaContentReference = writer.addToBody(richMediaContent).getIndirectReference();
		}
		writer.addDeveloperExtension(PdfDeveloperExtension.ADOBE_1_7_EXTENSIONLEVEL3);
        annot.put(PdfName.RICHMEDIACONTENT, richMediaContentReference);
        annot.put(PdfName.RICHMEDIASETTINGS, writer.addToBody(richMediaSettings).getIndirectReference());
		return annot;
	}
}
