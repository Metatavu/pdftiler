package fi.metatavu.pdftiler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 * Creates images from PDF files
 * 
 * @author Antti Leppä
 * @author Ville Koivukangas
 */
public class PdfReader {
  
  private static final Logger logger = Logger.getLogger(PdfReader.class.getName());
   
  private Settings settings;
  private PDDocument pdDocument;
  
  /**
   * Constructor
   * 
   * @param settings settings
   */
  public PdfReader(Settings settings) {
    this.settings = settings;
  }

  /**
   * Creates images from PDF file
   * 
   * @return list of created image files
   * @throws IOException If an I/O error occurs
   */
  public List<File> createImages() throws IOException {
    pdDocument = PDDocument.load(new File(settings.getPdfFile()));
    return writeImages();
  }
  
  /**
   * Reads PDF file and writes it into images
   * 
   * @return list of created image files
   * @throws IOException If an I/O error occurs
   */
  private List<File> writeImages() throws IOException {
    List<File> result = new ArrayList<>();
    
    int startPage = settings.getPdfStartPage() != null ? settings.getPdfStartPage() : 0;
    int endPage = settings.getPdfEndPage() != null ? settings.getPdfEndPage() : pdDocument.getNumberOfPages();
    PDFRenderer pdfRenderer = new PDFRenderer(pdDocument);
    
    for (int i = startPage; i < endPage; i++) {
      BufferedImage image = pdfRenderer.renderImageWithDPI(i, settings.getResolution(), ImageType.RGB);
      result.add(writeImage(image));
    }
    
    return result;
  }
  
  /**
   * Writes buffered image into file
   * 
   * @param image buffered image
   * @return written file
   * @throws IOException If an I/O error occurs
   */
  private File writeImage(BufferedImage image) throws IOException {
    String format = settings.getImageFormat();
    
    File file = File.createTempFile("pdftiler", String.format(".%s", format));
    
    ImageWriter imageWriter = getImageWriter(format);
    if (imageWriter == null) {
      return null;
    }
    
    try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
      try (ImageOutputStream outputStream = ImageIO.createImageOutputStream(fileOutputStream)) {
        ImageWriteParam param = imageWriter.getDefaultWriteParam();
        
        try {
          param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
          param.setCompressionQuality(1.0f); 
        } catch (UnsupportedOperationException e) {
          logger.warning("Using compressed images");
        }
        
        imageWriter.setOutput(outputStream);
        imageWriter.write(null, new IIOImage(image, null, null), param);
      }
    }
    
    return file;
  }
  
  /**
   * Finds most suitable image writer for given format
   * 
   * @param format format
   * @return most suitable image writer for given format or null if not found
   */
  private ImageWriter getImageWriter(String format) {
    List<ImageWriter> writers = getImageWriters(format);

    if (writers.isEmpty()) {
      return null;
    }
    
    for (ImageWriter writer : writers) {
      if (isUncompressedSupported(writer)) {
        return writer;
      }
    }
    
    return writers.get(0);
  }
  
  /**
   * Lists suitable image writers for given format
   * 
   * @param format format
   * @return suitable image writers for given format
   */
  private List<ImageWriter> getImageWriters(String format) {
    Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName(format);
    List<ImageWriter> result = new ArrayList<>();
    writers.forEachRemaining(result::add);
    return result;
  }
  
  /**
   * Returns whether image writer supports writing uncompressed files
   * 
   * @param writer writer
   * @return whether image writer supports writing uncompressed files
   */
  private boolean isUncompressedSupported(ImageWriter writer) {
    ImageWriteParam param = writer.getDefaultWriteParam();
    return param.canWriteCompressed();
  }

}
