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
package org.apache.hadoop.io.compress;

import java.io.*;
import java.util.zip.GZIPOutputStream;
import java.util.zip.GZIPInputStream;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.io.compress.zlib.*;
import org.apache.hadoop.io.compress.zlib.ZlibCompressor.CompressionLevel;
import org.apache.hadoop.io.compress.zlib.ZlibCompressor.CompressionStrategy;

/**
 * This class creates gzip compressors/decompressors. 
 */
public class GzipCodec extends DefaultCodec {
  /**
   * A bridge that wraps around a DeflaterOutputStream to make it 
   * a CompressionOutputStream.
   */
  protected static class GzipOutputStream extends CompressorStream {

    private static class ResetableGZIPOutputStream extends GZIPOutputStream {
      private static final int TRAILER_SIZE = 8;
      public static final String JVMVendor= System.getProperty("java.vendor");
      public static final String JVMVersion= System.getProperty("java.version");
      private static final boolean HAS_BROKEN_FINISH =
          (JVMVendor.contains("IBM") && JVMVersion.contains("1.6.0"));

      public ResetableGZIPOutputStream(OutputStream out) throws IOException {
        super(out);
      }

      public void resetState() throws IOException {
        def.reset();
      }

      /**
       * Override this method for HADOOP-8419.
       * Override because IBM implementation calls def.end() which
       * causes problem when reseting the stream for reuse.
       *
       */
      @Override
      public void finish() throws IOException {
        if (HAS_BROKEN_FINISH) {
          if (!def.finished()) {
            def.finish();
            while (!def.finished()) {
              int i = def.deflate(this.buf, 0, this.buf.length);
              if ((def.finished()) && (i <= this.buf.length - TRAILER_SIZE)) {
                writeTrailer(this.buf, i);
                i += TRAILER_SIZE;
                out.write(this.buf, 0, i);

                return;
              }
              if (i > 0) {
                out.write(this.buf, 0, i);
              }
            }

            byte[] arrayOfByte = new byte[TRAILER_SIZE];
            writeTrailer(arrayOfByte, 0);
            out.write(arrayOfByte);
          }
        } else {
          super.finish();
        }
      }

      /** re-implement for HADOOP-8419 because the relative method in jdk is invisible */
      private void writeTrailer(byte[] paramArrayOfByte, int paramInt)
        throws IOException {
        writeInt((int)this.crc.getValue(), paramArrayOfByte, paramInt);
        writeInt(this.def.getTotalIn(), paramArrayOfByte, paramInt + 4);
      }

      /** re-implement for HADOOP-8419 because the relative method in jdk is invisible */
      private void writeInt(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
        throws IOException {
        writeShort(paramInt1 & 0xFFFF, paramArrayOfByte, paramInt2);
        writeShort(paramInt1 >> 16 & 0xFFFF, paramArrayOfByte, paramInt2 + 2);
      }

      /** re-implement for HADOOP-8419 because the relative method in jdk is invisible */
      private void writeShort(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
        throws IOException {
        paramArrayOfByte[paramInt2] = (byte)(paramInt1 & 0xFF);
        paramArrayOfByte[(paramInt2 + 1)] = (byte)(paramInt1 >> 8 & 0xFF);
      }
    }

    public GzipOutputStream(OutputStream out) throws IOException {
      super(new ResetableGZIPOutputStream(out));
    }
    
    /**
     * Allow children types to put a different type in here.
     * @param out the Deflater stream to use
     */
    protected GzipOutputStream(CompressorStream out) {
      super(out);
    }
    
    public void close() throws IOException {
      out.close();
    }
    
    public void flush() throws IOException {
      out.flush();
    }
    
    public void write(int b) throws IOException {
      out.write(b);
    }
    
    public void write(byte[] data, int offset, int length) 
      throws IOException {
      out.write(data, offset, length);
    }
    
    public void finish() throws IOException {
      ((ResetableGZIPOutputStream) out).finish();
    }

    public void resetState() throws IOException {
      ((ResetableGZIPOutputStream) out).resetState();
    }
  }

  public CompressionOutputStream createOutputStream(OutputStream out) 
    throws IOException {
    return (ZlibFactory.isNativeZlibLoaded(conf)) ?
               new CompressorStream(out, createCompressor(),
                                    conf.getInt("io.file.buffer.size", 4*1024)) :
               new GzipOutputStream(out);
  }
  
  public CompressionOutputStream createOutputStream(OutputStream out, 
                                                    Compressor compressor) 
  throws IOException {
    return (compressor != null) ?
               new CompressorStream(out, compressor,
                                    conf.getInt("io.file.buffer.size", 
                                                4*1024)) :
               createOutputStream(out);
  }

  public Compressor createCompressor() {
    return (ZlibFactory.isNativeZlibLoaded(conf))
      ? new GzipZlibCompressor(conf)
      : null;
  }

  public Class<? extends Compressor> getCompressorType() {
    return ZlibFactory.isNativeZlibLoaded(conf)
      ? GzipZlibCompressor.class
      : null;
  }

  public CompressionInputStream createInputStream(InputStream in)
  throws IOException {
    return createInputStream(in, null);
  }

  public CompressionInputStream createInputStream(InputStream in,
                                                  Decompressor decompressor)
  throws IOException {
    if (decompressor == null) {
      decompressor = createDecompressor();  // always succeeds (or throws)
    }
    return new DecompressorStream(in, decompressor,
                                  conf.getInt("io.file.buffer.size", 4*1024));
  }

  public Decompressor createDecompressor() {
    return (ZlibFactory.isNativeZlibLoaded(conf))
      ? new GzipZlibDecompressor()
      : new BuiltInGzipDecompressor();
  }

  public Class<? extends Decompressor> getDecompressorType() {
    return ZlibFactory.isNativeZlibLoaded(conf)
      ? GzipZlibDecompressor.class
      : BuiltInGzipDecompressor.class;
  }

  public String getDefaultExtension() {
    return ".gz";
  }

  static final class GzipZlibCompressor extends ZlibCompressor {
    public GzipZlibCompressor() {
      super(ZlibCompressor.CompressionLevel.DEFAULT_COMPRESSION,
          ZlibCompressor.CompressionStrategy.DEFAULT_STRATEGY,
          ZlibCompressor.CompressionHeader.GZIP_FORMAT, 64*1024);
    }
    
    public GzipZlibCompressor(Configuration conf) {
      super(ZlibFactory.getCompressionLevel(conf),
           ZlibFactory.getCompressionStrategy(conf),
           ZlibCompressor.CompressionHeader.GZIP_FORMAT,
           64 * 1024);
    }
  }

  static final class GzipZlibDecompressor extends ZlibDecompressor {
    public GzipZlibDecompressor() {
      super(ZlibDecompressor.CompressionHeader.AUTODETECT_GZIP_ZLIB, 64*1024);
    }
  }

}
