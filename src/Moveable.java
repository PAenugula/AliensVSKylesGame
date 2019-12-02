import processing.core.PImage;

import java.util.List;

public abstract class Moveable extends AnimatedEntity {

    protected PathingStrategy strategy;

    public Moveable(String id,
                    Point position,
                    List<PImage> images,
                    int imageIndex,
                    int resourceLimit,
                    int resourceCount,
                    int actionPeriod,
                    int animationPeriod) {
        super(id, position, images, imageIndex, resourceLimit, resourceCount, actionPeriod, animationPeriod);
        this.strategy = new DijkstraPathingStrategy();
    }

    abstract public Point nextPosition(WorldModel worldModel, Point destination);


}
