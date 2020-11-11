/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.copy;

import java.io.InputStream;

/**
 * ByteArrayInputStream implementation that does not synchronize methods.
 *
 * @version 0.1
 * @author hiry
 * @since 13.06.2018
 */
public class FastByteArrayInputStream extends InputStream {
  /** Our byte buffer */
  protected byte[] buf = null;

  /** Number of bytes that we can read from the buffer */
  protected int count = 0;

  /** Number of bytes that have been read from the buffer */
  protected int pos = 0;

  public FastByteArrayInputStream(byte[] buf, int count) {
    this.buf = buf;
    this.count = count;
  }

  @Override
  public final int available() {
    return count - pos;
  }

  @Override
  public final int read() {
    return (pos < count) ? (buf[pos++] & 0xff) : -1;
  }

  @Override
  public final int read(byte[] b, int off, int len) {
    if (pos >= count) return -1;

    if ((pos + len) > count) len = (count - pos);

    System.arraycopy(buf, pos, b, off, len);
    pos += len;
    return len;
  }

  @Override
  public final long skip(long n) {
    if ((pos + n) > count) n = (long) count - pos;
    if (n < 0) return 0;
    pos += n;
    return n;
  }
}
