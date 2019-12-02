import java.util.List;

import processing.core.PImage;

/*
Entity ideally would includes functions for how all the entities in our virtual world might act...
 */


public abstract class Entity {

   private final String id;
   private Point position;
   private final List<PImage> images;
   private int imageIndex;
   private final int resourceLimit;
   private int resourceCount;

   public Entity(String id,
                      Point position,
                      List<PImage> images,
                      int imageIndex,
                      int resourceLimit,
                      int resourceCount)
   {
      this.id = id;
      this.position = position;
      this.images = images;
      this.imageIndex = imageIndex;
      this.resourceLimit = resourceLimit;
      this.resourceCount = resourceCount;
   }

   protected Point getPosition() {
      return position;
   }

   protected void setPosition(Point position) {
      this.position = position;
   }

   protected PImage getCurrentImage() {
      return images.get(imageIndex);
   }

   protected void nextImage() {
      imageIndex = (imageIndex + 1) % images.size();
   }

   protected int getResourceCount() {
      return resourceCount;
   }

   protected int getResourceLimit() {
      return resourceLimit;
   }

   protected void setResourceCount(int resourceCount) {
      this.resourceCount = resourceCount;
   }

   protected String getId() {
      return id;
   }

   protected List<PImage> getImages() {
      return images;
   }

   protected int getImageIndex() {
      return imageIndex;
   }

   protected void setImageIndex(int imageIndex) {
      this.imageIndex = imageIndex;
   }

}
