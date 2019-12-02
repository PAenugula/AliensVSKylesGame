import processing.core.PImage;

import java.util.List;

public abstract class ActiveEntity extends Entity{

    private final int actionPeriod;


    public ActiveEntity(String id,
                        Point position,
                        List<PImage> images,
                        int imageIndex,
                        int resourceLimit,
                        int resourceCount,
                        int actionPeriod) {
        super(id, position, images, imageIndex, resourceLimit, resourceCount);
        this.actionPeriod = actionPeriod;

    }


    protected int getActionPeriod() {
        return this.actionPeriod;
    }

    public abstract void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler);

    public void scheduleActions(EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
    }

}
