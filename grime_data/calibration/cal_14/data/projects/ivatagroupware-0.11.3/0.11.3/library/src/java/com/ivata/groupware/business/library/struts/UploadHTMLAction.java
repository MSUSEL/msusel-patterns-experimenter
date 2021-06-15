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
package com.ivata.groupware.business.library.struts;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.groupware.web.format.SanitizerFormat;
import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>Invoked when the user uploads a HTML file to a library
 * document.</p>
 *
 * @since 2003-07-02
 * @author Peter Illes
 * @version $Revision: 1.3 $
 *
 */
public class UploadHTMLAction extends MaskAction {
    /**
     * <p>
     * Constructor. Called by <strong>PicoContainer.</strong>.
     * </p>
     *
     * @param maskFactory This factory is needed to access the masks and groups
     * of masks.
     * @param authenticator used to confirm whether or not the
     * user should be allowed to continue, in the <code>execute</code> method.
     */
    public UploadHTMLAction(MaskFactory maskFactory, MaskAuthenticator authenticator) {
        super(maskFactory, authenticator);
    }
    /**
     * <p>Called from the other <code>execute</code> method, this can
     * be overridden by each subclass to provide the <em>ivata</em>-specific
     * processing required.</p>
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     */
    public String execute(ActionMapping mapping, ActionErrors errors,
            ActionForm form, HttpServletRequest request,
            HttpServletResponse response, HttpSession session)
            throws SystemException {
        return null;
    }

    /**
     * <p>This method is called if the ok or apply buttons are
     * pressed.</p>
     *
     * @param mapping The ActionMapping used to select this instance.
     * @param log valid logging object to write messages to.
     * @param errors valid errors object to append errors to. If there are
     * any errors, the action will return to the input.
     * @param form optional ActionForm bean for this request (if any)
     * @param request non-HTTP request we are processing
     * @param response The non-HTTP response we are creating
     * @param session  returned from the <code>request</code> parameter.
     * @param userName valid, non-null user name from session.
     * @param settings valid, non-null settings from session.
     * @param ok <code>true</code> if the ok button was pressed, otherwise
     * <code>false</code> if the apply button was pressed.
     * @exception SystemException if there is any problem which
     * prevents processing. It will result in the webapp being forwarded
     * to
     * the standard error page.
     * @return this method returns the string used to identify the correct
     * <em>Struts</em> <code>ActionForward</code> which should follow this
     * page, or <code>null</code> if it should return to the input.
     */
    public String onConfirm(ActionMapping mapping,
            ActionErrors errors, ActionForm form, HttpServletRequest request,
            HttpServletResponse response, HttpSession session,
            final String defaultForward)
            throws SystemException {
        UploadHTMLForm uploadForm = (UploadHTMLForm) form;
        String returnForward = "utilClosePopUp";

        try {
            InputStream stream = uploadForm.getFile().getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(stream));
            StringBuffer messageBuffer = new StringBuffer();
            char[] chbuf = new char[1024];
            int count;

            while ((count = in.read(chbuf)) != -1) {
                messageBuffer.append(chbuf, 0, count);
            }

            SanitizerFormat sanitizer = new SanitizerFormat();
            sanitizer.setOnlyBodyContents(true);
            sanitizer.setSourceName("user input");
// TODO            Map parseResult = sanitizer.format(messageBuffer.toString());
            Map parseResult = null;

            List pages = (List) parseResult.get("pages");

            if (!pages.isEmpty()) {
                ItemForm itemForm = (ItemForm) session.getAttribute(
                        "libraryItemForm");
//TODO                itemForm.getItem().setPages(new Vector(pages));
                session.setAttribute("libraryItemForm", itemForm);

                Set images = (Set) parseResult.get("images");
                Vector itemAttachments = null;

                // the attachmnets are either in upload (new item) or in drive
/*TODO                if (itemForm.getItem().getId() == null) {
                    itemAttachments = itemForm.getUploadingFileList();
                } else {
                    itemAttachments = itemForm.getFileList();
                }
*/
                Vector newImages = new Vector();

                // look for images not attached yet
                for (Iterator i = images.iterator(); i.hasNext();) {
                    String currentImage = (String) i.next();
                    boolean newImage = true;

/*TODO                    for (Iterator j = itemAttachments.iterator(); j.hasNext();) {
                        if (currentImage.endsWith(
                                    ((FileDO) j.next()).getFileName())) {
                            newImage = false;

                            break;
                        }
                    }
*/
                    if (newImage) {
                        newImages.add(currentImage);
                    }
                }

                // when there are ne images, go to image upload
                if (!newImages.isEmpty()) {
                    UploadImagesForm uploadImagesForm = new UploadImagesForm();
                    uploadImagesForm.setImageFileName(newImages);
                    request.setAttribute("libraryUploadImagesForm",
                        uploadImagesForm);
                    returnForward = "libraryImageUpload";
                } else {
                    request.setAttribute("openerPage", "/library/submit.action");
                }
            }
        } catch (FileNotFoundException efnf) {
            throw new SystemException(efnf);
        } catch (IOException eio) {
            throw new SystemException(eio);
        }

        return returnForward;
    }
}
