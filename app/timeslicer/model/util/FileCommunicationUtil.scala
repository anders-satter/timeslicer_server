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
  def readFromFile(filename: String, encoding: String): Array[String] = {
		  toSource(new FileInputStream(filename), encoding).getLines.toArray
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
  
  def toSource(inputStream: InputStream, decoding: String): scala.io.BufferedSource = {
    import java.nio.charset.Charset
    import java.nio.charset.CodingErrorAction
    val decoder = Charset.forName(decoding).newDecoder()
    decoder.onMalformedInput(CodingErrorAction.IGNORE)
    scala.io.Source.fromInputStream(inputStream)(decoder)
  }
  
  def test:Int = 1
  
}
