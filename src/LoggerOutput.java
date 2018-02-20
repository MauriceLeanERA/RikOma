/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Maurice Bernard
 */
public class LoggerOutput {
  File file;
  private FileWriter fw;
  private BufferedWriter bw;

  LoggerOutput(String fileName) {
    file = new File("..\\" + fileName + ".txt");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException ex) {
        Logger.getLogger(LoggerOutput.class.getName()).log(Level.SEVERE, null, ex);
      }
    }
    try {
      fw = new FileWriter(file, true);
      bw = new BufferedWriter(fw);
    } catch (IOException ex) {
      Logger.getLogger(LoggerOutput.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  public void write(String text) {
    try {
//            LocalDate date = LocalDate.now();
//            text = date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + ": "+text;
      bw.append(text).append(System.lineSeparator());
      bw.flush();
    } catch (IOException ex) {
      Logger.getLogger(LoggerOutput.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
