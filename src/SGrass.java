import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class SGrass extends ActiveEntity {

    private static final String FISH_KEY = "fish";
    private static final String FISH_ID_PREFIX = "fish -- ";
    private static final int FISH_CORRUPT_MIN = 20000;
    private static final int FISH_CORRUPT_MAX = 30000;
    private static final Random rand = new Random();

    public SGrass(String id, Point position, int actionPeriod,
                  List<PImage> images)
    {
        super(id, position, images, 0, 0, 0, actionPeriod);
    }

    public void executeActivity(WorldModel world,
      ImageStore imageStore, EventScheduler scheduler)
   {
      Optional<Point> openPt = world.findOpenAround(this.getPosition());

      if (openPt.isPresent())
      {
         Fish fish = new Fish(FISH_ID_PREFIX + this.getId(),
                 openPt.get(), FISH_CORRUPT_MIN +
                         rand.nextInt(FISH_CORRUPT_MAX - FISH_CORRUPT_MIN),
                 imageStore.getImageList(FISH_KEY));
         world.addEntity(fish);
         fish.scheduleActions(scheduler, world, imageStore);
      }

      scheduler.scheduleEvent(this,
         new Activity(this, world, imageStore),
         this.getActionPeriod());
   }
}
