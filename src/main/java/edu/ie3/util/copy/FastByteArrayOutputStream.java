/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.copy;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * ByteArrayOutputStream implementation that doesn't synchronize methods and doesn't copy the data
 * on toByteArray().
 *
 * @version 0.1
 * @author: hiry
 * @since 13.06.2018
 */
public class FastByteArrayOutputStream extends OutputStream {
  /** Buffer and size */
  protected byte[] buf = null;

  protected int size = 0;

  /** Constructs a stream with buffer capacity size 5K */
  public FastByteArrayOutputStream() {
    this(5 * 1024);
  }

  /**
   * Constructs a stream with the given initial size
   *
   * @param initSize Intitial size
   */
  public FastByteArrayOutputStream(int initSize) {
    this.size = 0;
    this.buf = new byte[initSize];
  }

  /** Ensures that we have a large enough buffer for the given size. */
  private void verifyBufferSize(int sz) {
    if (sz > buf.length) {
      byte[] old = buf;
      buf = new byte[Math.max(sz, 2 * buf.length)];
      System.arraycopy(old, 0, buf, 0, old.length);
      old = null;
    }
  }

  public int getSize() {
    return size;
  }

  /**
   * Returns the byte array containing the written data. Note that this array will almost always be
   * larger than the amount of data actually written.
   *
   * @return An byte Array
   */
  public byte[] getByteArray() {
    return buf;
  }

  public final void write(byte b[]) {
    verifyBufferSize(size + b.length);
    System.arraycopy(b, 0, buf, size, b.length);
    size += b.length;
  }

  public final void write(byte b[], int off, int len) {
    verifyBufferSize(size + len);
    System.arraycopy(b, off, buf, size, len);
    size += len;
  }

  public final void write(int b) {
    verifyBufferSize(size + 1);
    buf[size++] = (byte) b;
  }

  public void reset() {
    size = 0;
  }

  /**
   * Returns a ByteArrayInputStream for reading back the written data
   *
   * @return An {@link InputStream} of the current buffer
   */
  public InputStream getInputStream() {
    return new FastByteArrayInputStream(buf, size);
  }
}
