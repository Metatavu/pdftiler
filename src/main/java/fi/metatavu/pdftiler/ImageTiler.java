package fi.metatavu.pdftiler;

import java.io.File;
import java.io.IOException;

/**
 * Creates map tiles from image files
 * 
 * @author Antti Leppä
 * @author Ville Koivukangas
 */
public class ImageTiler {
  
  private Settings settings;
  
  public ImageTiler(Settings settings) {
    this.settings = settings;
  }
  
  /**
   * Creates tiles from an image file
   * 
   * @param imageFile image file
   * @return whether the operation was a success
   * @throws IOException If an I/O error occurs
   * @throws InterruptedException when operation terminates unexpectedly
   */
  public boolean createTiles(File imageFile) throws IOException, InterruptedException {
    return runGdalToTiles(imageFile);
  }
  
  /**
   * Creates tiles by running gdal2tiles.py
   * 
   * @param imageFile input image file
   * @return whether the operation was a success
   * @throws IOException If an I/O error occurs
   * @throws InterruptedException when application terminates unexpectedly
   */
  private boolean runGdalToTiles(File imageFile) throws IOException, InterruptedException {
    String[] command = {
      settings.getGdalToTilesExecutable(),
      "-p",
      "raster",
      "-z",
      String.format("%d-%d", settings.getFromZoomLevel(), settings.getToZoomLevel()),
      imageFile.getAbsolutePath(),
      settings.getOutputDirectory()
    };

    Runtime runtime = Runtime.getRuntime();
    Process process = runtime.exec(command);
  
    return process.waitFor() == 0;
  }
  
}
