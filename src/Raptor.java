import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.Random;

public class Raptor extends Moveable{

    Random rand = new Random();

    public Raptor(String id, Point position, List<PImage> images, int imageIndex, int resourceLimit, int resourceCount, int actionPeriod, int animationPeriod) {
        super(id, position, images, imageIndex, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }


//    public Raptor(String id,
//                  Point position,
//                  List<PImage> images,
//                  int imageIndex,
//                  int resourceLimit,
//                  int resourceCount,
//                  int actionPeriod,
//                  int animationPeriod) {
//        super(id, position, images, imageIndex, resourceLimit, resourceCount, actionPeriod, animationPeriod);
//



//    @Override
//    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
//        int x = rand.nextInt(world.getNumCols());
//        int y = rand.nextInt(world.getNumRows());
//        Point dest = new Point(x, y);
//        Point nextPos = nextPosition(world, dest);
//        System.out.println(nextPos);
//        world.moveEntity(this, nextPos);
//        scheduler.scheduleEvent(this,
//                new Activity(this, world, imageStore),
//                this.getActionPeriod());
//
//
//    }

    @Override
    public Point nextPosition(WorldModel worldModel, Point destination) {
        List<Point> path = strategy.computePath(this.getPosition(),
                destination,
                (point) -> worldModel.withinBounds(point) && !worldModel.isOccupied(point),
                (p1, p2) -> worldModel.neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        return path.size() > 0 ? path.get(0):this.getPosition();
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        int x = rand.nextInt(world.getNumCols());
        int y = rand.nextInt(world.getNumRows());
        Point dest = new Point(x, y);
        Point nextPos = nextPosition(world, dest);
        world.moveEntity(this, nextPos);
        scheduler.scheduleEvent(this, new Activity(this, world, imageStore), this.getActionPeriod());
    }
}
