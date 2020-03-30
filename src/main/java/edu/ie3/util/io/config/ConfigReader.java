/*
 * © 2020. TU Dortmund University,
 * Institute of Energy Systems, Energy Efficiency and Energy Economics,
 * Research group Distribution grid planning and operation
*/
package edu.ie3.util.io.config;

import java.io.File;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class ConfigReader<C> {
  /*
   * http://www.mkyong.com/java/jaxb-hello-world-example/
   */

  private File file;
  private Class<C> targetClass;

  public ConfigReader(String configFilePath, Class<C> targetClass) throws IOException {
    this.file = new File(configFilePath);
    if (!file.exists()) throw new IOException("The given file does not exist!");
    else if (file.isDirectory())
      throw new IOException("The given config file path points to an directory!");
    else if (!configFilePath.toLowerCase().endsWith("xml"))
      throw new IOException("The given file is not an xml-file!");
    this.targetClass = targetClass;
  }

  public C readConfig() {
    try {
      JAXBContext jaxbCtxt = JAXBContext.newInstance(targetClass);

      Unmarshaller jaxbUnmarshaller = jaxbCtxt.createUnmarshaller();
      return (C) jaxbUnmarshaller.unmarshal(file);
    } catch (JAXBException e) {
      e.printStackTrace();
      return null;
    }
  }
}
