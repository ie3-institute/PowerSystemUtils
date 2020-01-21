/*
 * © 2019. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
 */

package edu.ie3.util.copy;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Utility for making deep copies (vs. clone()'s shallow copies) of objects. Objects are first
 * serialized and then deserialized. Error checking is fairly minimal in this implementation. If an
 * object is encountered that cannot be serialized (or that references an object that cannot be
 * serialized) an error is printed to System.err and null is returned. Depending on your specific
 * application, it might make more sense to have copy(...) re-throw the exception.
 *
 * @version 0.1
 * @author hiry
 * @since 13.06.2018
 */
public class DeepCopy {

  /**
   * Returns a copy of the object, or null if the object cannot be serialized.
   *
   * @param orig Original {@link Object} to be copied
   * @return The duplicate
   */
  public static <C> C copy(C orig) {
    C obj = null;
    try {
      // Write the object out to a byte array
      FastByteArrayOutputStream fbos = new FastByteArrayOutputStream();
      ObjectOutputStream out = new ObjectOutputStream(fbos);
      out.writeObject(orig);
      out.flush();
      out.close();

      // Retrieve an input stream from the byte array and read
      // a copy of the object back in.
      ObjectInputStream in = new ObjectInputStream(fbos.getInputStream());
      obj = (C) in.readObject();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException cnfe) {
      cnfe.printStackTrace();
    }
    return obj;
  }
}
