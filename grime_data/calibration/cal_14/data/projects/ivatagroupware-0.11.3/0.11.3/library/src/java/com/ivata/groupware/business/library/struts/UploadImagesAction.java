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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.ivata.mask.MaskFactory;
import com.ivata.mask.util.SystemException;
import com.ivata.mask.web.struts.MaskAction;
import com.ivata.mask.web.struts.MaskAuthenticator;


/**
 * <p>Library image upload action. It's used when there are some local
 * images
 * in the uploaded HTML for library document.</p>
 *
 * @since 2003-07-04
 * @author Peter Illes
 * @version $Revision: 1.3 $
 */
public class UploadImagesAction extends MaskAction {
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
    public UploadImagesAction(MaskFactory maskFactory, MaskAuthenticator authenticator) {
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
    public String execute(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session) throws SystemException {
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
    public String onConfirm(final ActionMapping mapping,
            final ActionErrors errors,
            final ActionForm form,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final HttpSession session,
            final String defaultForward) throws SystemException {
        UploadImagesForm uploadForm = (UploadImagesForm) form;

/* TODO        DriveRemote drive = getDriveFromSession(session);
        UsageRemote usage = getUsageFromSession(session);

        Map images = uploadForm.getImages();

        // only worth to do something when there's at least one file uploaded
        if (!images.isEmpty()) {
            Vector fileNameList = new Vector();

            try {
                int finalSize = 0;

                // count fileSize together and check if we can upload
                for (Iterator i = images.keySet().iterator(); i.hasNext();) {
                    String currentKey = (String) i.next();

                    FormFile formFile = (FormFile) images.get(currentKey);

                    // ignore empty files and files already uploaded
                    if ((formFile != null) && (formFile.getFileSize() > 0) &&
                            !fileNameList.contains(formFile.getFileName())) {
                        finalSize += formFile.getFileSize();
                    }
                }

                Integer canUpload = null;

                try {
                    canUpload = usage.canUpload(new Integer(finalSize / 1024),
                            (String) session.getAttribute("userName"));
                } catch (java.rmi.RemoteException e) {
                    throw new RuntimeException(e);
                }

                ResourceBundle adminBundle = ResourceBundle.getBundle("com.ivata.groupware.business.ApplicationResources",
                        (Locale) request.getSession().getAttribute(Globals.LOCALE_KEY));

                if (canUpload.equals(
                            com.ivata.groupware.admin.usage.UsageConstants.CAN_UPLOAD_NEARLY_RUN_OUT_OF_QUOTA) ||
                        canUpload.equals(
                            com.ivata.groupware.admin.usage.UsageConstants.OK)) {
                    for (Iterator i = images.keySet().iterator(); i.hasNext();) {
                        String currentKey = (String) i.next();

                        FormFile formFile = (FormFile) images.get(currentKey);

                        // ignore empty files and files already uploaded
                        if ((formFile != null) && (formFile.getFileSize() > 0) &&
                                !fileNameList.contains(formFile.getFileName())) {
                            InputStream inStream = formFile.getInputStream();

                            // reading the content, writing it to an output stream
                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            int oneByte = 0;

                            while ((oneByte = inStream.read()) != -1) {
                                outStream.write(oneByte);
                            }

                            // storing the content to the fileContentDO
                            SerializedByteArray fileContent = new SerializedByteArray(outStream.toByteArray());
                            FileContentDO fileContentDO = new FileContentDO(fileContent,
                                    formFile.getContentType());

                            // store the file server-side, get the updated list
                            drive.uploadFile(fileContentDO,
                                formFile.getFileName(),
                                uploadForm.getComment(currentKey), userName);

                            fileNameList.add(formFile.getFileName());
                        }

                        if (canUpload.equals(
                                    com.ivata.groupware.admin.usage.UsageConstants.CAN_UPLOAD_NEARLY_RUN_OUT_OF_QUOTA)) {
                            request.setAttribute("javaScript",
                                "alert(\"" +
                                adminBundle.getString(
                                    "errors.upload.quota.runOut.nearly") +
                                "\");");
                        }
                    }
                } else if (canUpload.equals(
                            com.ivata.groupware.admin.usage.UsageConstants.NOT_FREE_SPACE_FOR_UPLOAD)) {
                    request.setAttribute("javaScript",
                        "alert(\"" +
                        adminBundle.getString("errors.upload.quota.runOut") +
                        "\");");
                }

                // if there were some uploads, move them to drive upload directory
                if (!fileNameList.isEmpty()) {
                    drive.moveUploads(fileNameList, "drive", userName);
                }
            } catch (java.rmi.RemoteException e) {
                throw new SystemException(e);
            } catch (FileNotFoundException e) {
                throw new SystemException(e);
            } catch (IOException e) {
                throw new SystemException(e);
            }
        }

        request.setAttribute("openerPage", "/library/submit.action");
*/
        return "utilClosePopUp";
    }
}
