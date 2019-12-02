import processing.core.PImage;

import java.util.List;

public abstract class AnimatedEntity extends ActiveEntity {

    private final int animationPeriod;

    public AnimatedEntity(String id,
                          Point position,
                          List<PImage> images,
                          int imageIndex,
                          int resourceLimit,
                          int resourceCount,
                          int actionPeriod,
                          int animationPeriod
    )
    {
        super(id, position, images, imageIndex, resourceLimit, resourceCount, actionPeriod);
        this.animationPeriod = animationPeriod;
    }

    protected int getAnimationPeriod() {
        return animationPeriod;
    }

    public void nextImage() {
        setImageIndex((getImageIndex()+ 1) % getImages().size());
    }

    public void scheduleActions(EventScheduler eventScheduler, WorldModel world, ImageStore imageStore) {
        eventScheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());
        eventScheduler.scheduleEvent(this,
                new Animation(this, 0),
                this.getAnimationPeriod());
    }
}
