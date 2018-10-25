package fi.metatavu.pdftiler;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Creates map tiles from PDF file
 * 
 * @author Antti Leppä
 * @author Ville Koivukangas
 */
public class PdfTiler {

  private static final Logger logger = Logger.getLogger(PdfTiler.class.getName());
  
  private Settings settings;
  
  /**
   * Constructor
   * 
   * @param settings settings
   */
  public PdfTiler(Settings settings) {
    this.settings = settings;
  }

  /**
   * Creates tiles from PDF
   * 
   * @param settings
   * @return whether the operation was a success
   * @throws IOException If an I/O error occurs
   * @throws InterruptedException when operation terminates unexpectedly
   */
  public boolean createTiles() throws IOException, InterruptedException {
    boolean result = true;
    
    ImageTiler imageTiler = new ImageTiler(settings);
    PdfReader pdfReader = new PdfReader(settings);
    
    List<File> imageFiles = pdfReader.createImages();
    for (File imageFile : imageFiles) {
      if (!imageTiler.createTiles(imageFile)) {
        logger.severe(String.format("Failed to create files from image %s", imageFile.getAbsolutePath()));
        result = false;
      }
    }
    
    return result;
  }
  
}
