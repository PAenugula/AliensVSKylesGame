import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Crab extends Moveable {

    private static final String QUAKE_KEY = "quake";

    public Crab(String id, Point position,
                int actionPeriod, int animationPeriod, List<PImage> images)
    {
        super(id, position, images, 0, 0,0, actionPeriod, animationPeriod);
    }

    private boolean moveToCrab(WorldModel world,
                               Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
            world.removeEntity(target);
            scheduler.unscheduleAllEvents(target);
            return true;
        }
        else
        {
            Point nextPos = nextPosition(world, target.getPosition());

            if (!this.getPosition().equals(nextPos))
            {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent())
                {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public void executeActivity(WorldModel world,
                                    ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> crabTarget = world.findNearest(
                this.getPosition(), SGrass.class);
        long nextPeriod = this.getActionPeriod();

        if (crabTarget.isPresent())
        {
            Point tgtPos = crabTarget.get().getPosition();

            if (moveToCrab(world, crabTarget.get(), scheduler))
            {
                ActiveEntity quake = new Quake(tgtPos,
                        imageStore.getImageList(QUAKE_KEY));

                world.addEntity(quake);
                nextPeriod += this.getActionPeriod();
                quake.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                nextPeriod);
    }

    @Override
    public Point nextPosition(WorldModel worldModel, Point destination) {

        Predicate<Point> fishFind = (point -> {
            Optional<Entity> occupant = worldModel.getOccupant(point);
            return !occupant.isPresent() || occupant.get().getClass().equals(Fish.class);
        });
        List<Point> path = this.strategy.computePath(this.getPosition(), destination,
                fishFind,
                (p1, p2) -> worldModel.neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (!path.isEmpty()) {
            return path.get(0);
        } else {
            return getPosition();
        }
//        int horiz = Integer.signum(destination.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz,
//                this.getPosition().getY());
//
//        Optional<Entity> occupant = worldModel.getOccupant(newPos);
//
//        if (horiz == 0 || (occupant.isPresent() && !(occupant.get().getClass().equals(Fish.class)))) {
//            int vert = Integer.signum(destination.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(), this.getPosition().getY() + vert);
//            occupant = worldModel.getOccupant(newPos);
//
//            if (vert == 0 || (occupant.isPresent() && !(occupant.get().getClass().equals(Fish.class)))) {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
    }
}
