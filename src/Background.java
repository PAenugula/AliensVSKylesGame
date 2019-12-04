import java.util.List;
import processing.core.PImage;

final class Background
{
   private final String id;
   private final List<PImage> images;
   private int imageIndex;

   public Background(String id, List<PImage> images, int imageIndex)
   {
      System.out.println(images.size());
      this.imageIndex = imageIndex;
      this.id = id;
      this.images = images;
   }


   public static Background createSea(String id, List<PImage> images) {
      return new Background(id, images, 0);
   }

   public static Background createCave(String id, List<PImage> images) {
         return new Background(id, images, 1);
      }


   public PImage getCurrentImage() {
      return images.get(imageIndex);
   }

}
