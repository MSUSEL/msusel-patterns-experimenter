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
package org.geotools.arcsde.gce.producer;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferFloat;

import com.esri.sde.sdk.client.SeRasterAttr;
import com.esri.sde.sdk.client.SeRasterConsumer;
import com.esri.sde.sdk.client.SeRasterRenderedImage;
import com.esri.sde.sdk.client.SeRasterScanLineGenerator;

/**
 * 
 *
 * @source $URL$
 */
public class ArcSDERasterFloatProducerImpl extends ArcSDERasterProducer {

    public ArcSDERasterFloatProducerImpl() {
        super(null, null, SeRasterScanLineGenerator.MASK_ALL_ON);
    }

    public ArcSDERasterFloatProducerImpl(SeRasterAttr attr, BufferedImage sourceImage, int maskType) {
        super(attr, sourceImage, maskType);
    }

    @Override
    public void setSourceImage(BufferedImage sourceImage) {
        // TODO: check that the image is compatible
        this.sourceImage = sourceImage;
    }

    public void startProduction(final SeRasterConsumer consumer) {
        Thread runme;
        if (consumer instanceof SeRasterRenderedImage) {

            runme = new Thread() {
                @Override
                public void run() {
                    try {
                        final int imageHeight = sourceImage.getHeight();
                        float min = 0, max = 0;
                        // for each band...
                        for (int i = 0; i < sourceImage.getData().getNumBands(); i++) {
                            // get the data as floats, then convert to four-byte segments
                            byte[] sdeRasterData = new byte[sourceImage.getWidth() * 4
                                    * sourceImage.getHeight()];
                            float[] srcImgData = ((DataBufferFloat) sourceImage.getData()
                                    .getDataBuffer()).getData();
                            for (int y = 0; y < sourceImage.getHeight(); y++) {
                                // byte[] curRow = new byte[sourceImage.getWidth() * 4];
                                for (int x = 0; x < sourceImage.getWidth(); x++) {
                                    // final float sample = sourceImage.getData().getSampleFloat(x,
                                    // y, i);
                                    final float sample = srcImgData[y * sourceImage.getWidth() + x];
                                    min = Math.min(min, sample);
                                    max = Math.max(max, sample);
                                    // convert float to bytes
                                    int bits = Float.floatToIntBits(sample);
                                    sdeRasterData[y * sourceImage.getWidth() * 4 + x * 4] = (byte) ((bits & 0xff000000) >> 24);
                                    sdeRasterData[y * sourceImage.getWidth() * 4 + x * 4 + 1] = (byte) ((bits & 0x00ff0000) >> 16);
                                    sdeRasterData[y * sourceImage.getWidth() * 4 + x * 4 + 2] = (byte) ((bits & 0x0000ff00) >> 8);
                                    sdeRasterData[y * sourceImage.getWidth() * 4 + x * 4 + 3] = (byte) (bits & 0x000000ff);
                                }
                            }

                            consumer.setScanLines(imageHeight, sdeRasterData, null);
                            consumer.rasterComplete(SeRasterConsumer.SINGLEFRAMEDONE);
                        }
                        System.out.println("min value: " + min + ", max value: " + max);
                        consumer.rasterComplete(SeRasterConsumer.STATICIMAGEDONE);
                    } catch (Exception se) {
                        se.printStackTrace();
                        consumer.rasterComplete(SeRasterConsumer.IMAGEERROR);
                    }
                }
            };
        } else {
            throw new IllegalArgumentException("You must set SeRasterAttr.setImportMode(false) to "
                    + "load data using this SeProducer implementation.");
        }

        runme.start();

    }
}
