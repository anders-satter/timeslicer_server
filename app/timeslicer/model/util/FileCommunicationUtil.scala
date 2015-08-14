package timeslicer.model.util

import java.io.FileWriter
import java.io.InputStream
import java.io.FileInputStream
import java.io.BufferedWriter
import java.io.PrintWriter
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction
import scala.io.Codec.decoder2codec

/**
 * Different file reading routines, to be used
 * by other parts of the program
 */
object FileCommunicationUtil {
  /**
   * Returning lines from a text file
   */
  def readFromFileToStringArray(filename: String, encoding: String): Array[String] = {
    var source: scala.io.Source = null
    var array: Array[String] = null
    //println("readFromFileToStringArray"+filename)
    try {
      source = getSource(new FileInputStream(filename), encoding)
      array = source.getLines.toArray
    } finally {
      try {
        source.close()
      } catch {
        case e: Exception => /*nothing*/
      }
    }
    return array
  }

  def readFromFileToString(filename: String, encoding: String): String = {
    var source: scala.io.Source = null
    var fileContent: String = null
    try {
      source = getSource(new FileInputStream(filename), encoding)
      fileContent = source.getLines.mkString
    } finally {
      try {
        source.close()
      } catch {
        case e: Exception => /*do nothing*/
      }
    }
    return fileContent
  }

  /**
   * Saving a text file, if append=true the text will be appended
   * to the file, otherwise the content will overwrite everything in the file
   */
  def saveToFile(filename: String, content: String, append: Boolean) = {
    val fw = new FileWriter(filename, append)
    val out = new PrintWriter(new BufferedWriter(fw))
    if (fw != null) {
      try {
        out.println(content)
      } catch {
        case ex: Exception => {
          println("Exception " + ex.getMessage + " thrown while saving to file")
        }
      } finally {
        out.close()
      }
    }
  }

  /**
   * Check 
   */
  def createUserIdFilesIfNotExists(userId: String, usersPath: String): String = {
    val idDir = new java.io.File(usersPath + '/' + userId)
    val projFile = new java.io.File(idDir.getPath + "/" + "prj.txt")
    val logFile = new java.io.File(idDir.getPath + "/" + "log.txt")
    if (idDir.exists && idDir.isDirectory()) {
      createFiles(projFile, logFile)
      //return idDir.getPath
      return userId
    } else {
      idDir.mkdir()
      createFiles(projFile, logFile)
      //return idDir.getPath
      return userId
    }
  }

  private def createFiles(projFile: java.io.File, logFile: java.io.File) = {
    if (!projFile.exists()) {
      projFile.createNewFile()
    }
    if (!logFile.exists()) {
      logFile.createNewFile()
    }
  }

  def getSource(inputStream: InputStream, decoding: String): scala.io.BufferedSource = {
    import java.nio.charset.Charset
    import java.nio.charset.CodingErrorAction
    val decoder = Charset.forName(decoding).newDecoder()
    decoder.onMalformedInput(CodingErrorAction.IGNORE)
    scala.io.Source.fromInputStream(inputStream)(decoder)
  }
}
