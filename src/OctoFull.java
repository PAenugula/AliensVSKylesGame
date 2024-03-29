import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class OctoFull extends Octo {

    public OctoFull(String id, int resourceLimit,
                    Point position, int actionPeriod, int animationPeriod,
                    List<PImage> images)
    {
        super(id, position, images, 0, resourceLimit, resourceLimit, actionPeriod, animationPeriod);
    }

    public void executeActivity(WorldModel world,
                                ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = world.findNearest(this.getPosition(),
                Atlantis.class);

        if (fullTarget.isPresent() &&
                moveToFull(world, fullTarget.get(), scheduler))
        {
            //at atlantis trigger animation
            ((ActiveEntity)fullTarget.get()).scheduleActions(scheduler, world, imageStore);

            //transform to unfull
            transformFull(world, scheduler, imageStore);
        }
        else
        {
            scheduler.scheduleEvent(this,
                    new Activity(this, world, imageStore),
                    this.getActionPeriod());
        }
    }

    private void transformFull(WorldModel world,
                               EventScheduler scheduler, ImageStore imageStore)
    {
        OctoNotFull octo = new OctoNotFull(this.getId(), this.getResourceLimit(),
                this.getPosition(), this.getActionPeriod(), this.getAnimationPeriod(),
                this.getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(octo);
        octo.scheduleActions(scheduler, world, imageStore);
    }

    private boolean moveToFull(WorldModel world,
                               Entity target, EventScheduler scheduler)
    {
        if (this.getPosition().adjacent(target.getPosition()))
        {
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
}
