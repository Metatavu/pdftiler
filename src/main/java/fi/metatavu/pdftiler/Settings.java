package fi.metatavu.pdftiler;

import java.io.File;

/**
 * Settings object
 * 
 * @author Antti Leppä
 */
public class Settings {

  private static final String[] GDAL_TO_TILES_PATHS = new String[] {
    "/usr/bin/gdal2tiles.py",
    "/Library/Frameworks/GDAL.framework/Programs/gdal2tiles.py"
  };
 
  private String pdfFile;
  private String outputDirectory;
  private String gdalToTilesExecutable;
  private int fromZoomLevel = 1;
  private int toZoomLevel = 8;
  private Integer pdfStartPage;
  private Integer pdfEndPage;
  private String imageFormat = "jpg";
  private int resolution = 600;
  
  /**
   * Constructor
   * 
   * @param pdfFile path to input PDF file
   * @param outputDirectory path to tiles output directory
   */
  public Settings(String pdfFile, String outputDirectory) {
    this.pdfFile = pdfFile;
    this.outputDirectory = outputDirectory;
    this.gdalToTilesExecutable = findGdalToTilesPath();
  }

  /**
   * 
   * Constructor
   * 
   * @param pdfFile path to input PDF file
   * @param outputDirectory path to tiles output directory
   * @param gdalToTilesExecutable path to gdal2tiles.py executable
   * @param fromZoomLevel from zoom level to render
   * @param toZoomLevel to zoom level to render
   * @param pdfStartPage start from PDF page index. Defaults to 0
   * @param pdfEndPage end to PDF page index. Defaults to last page
   */
  public Settings(String pdfFile, String outputDirectory, String gdalToTilesExecutable, int fromZoomLevel, int toZoomLevel, Integer pdfStartPage, Integer pdfEndPage) {
    this.pdfFile = pdfFile;
    this.outputDirectory = outputDirectory;
    this.gdalToTilesExecutable = gdalToTilesExecutable;
    this.fromZoomLevel = fromZoomLevel;
    this.toZoomLevel = toZoomLevel;
    this.pdfStartPage = pdfStartPage;
    this.pdfEndPage = pdfEndPage;
  }

  public String getGdalToTilesExecutable() {
    return gdalToTilesExecutable;
  }
  
  public void setGdalToTilesExecutable(String gdalToTilesExecutable) {
    this.gdalToTilesExecutable = gdalToTilesExecutable;
  }
  
  public int getFromZoomLevel() {
    return fromZoomLevel;
  }
  
  public void setFromZoomLevel(int fromZoomLevel) {
    this.fromZoomLevel = fromZoomLevel;
  }
  
  public int getToZoomLevel() {
    return toZoomLevel;
  }
  
  public void setToZoomLevel(int toZoomLevel) {
    this.toZoomLevel = toZoomLevel;
  }
  
  public String getImageFormat() {
    return imageFormat;
  }
  
  public void setImageFormat(String imageFormat) {
    this.imageFormat = imageFormat;
  }
  
  public String getOutputDirectory() {
    return outputDirectory;
  }
  
  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
  }
  
  public String getPdfFile() {
    return pdfFile;
  }
  
  public void setPdfFile(String pdfFile) {
    this.pdfFile = pdfFile;
  }
  
  public int getResolution() {
    return resolution;
  }
  
  public void setResolution(int resolution) {
    this.resolution = resolution;
  }
  
  public Integer getPdfEndPage() {
    return pdfEndPage;
  }
  
  public void setPdfEndPage(Integer pdfEndPage) {
    this.pdfEndPage = pdfEndPage;
  }
  
  public Integer getPdfStartPage() {
    return pdfStartPage;
  }
  
  public void setPdfStartPage(Integer pdfStartPage) {
    this.pdfStartPage = pdfStartPage;
  }

  /**
   * Attempts to find gdal2tiles.py executable
   * 
   * @return found gdal2tiles.py executable or null if not found
   */
  private String findGdalToTilesPath() {
    for (String location : GDAL_TO_TILES_PATHS) {
      File file = new File(location);
      if (file.exists()) {
        return location;
      }
    }
    
    return null;
  }
  
}
