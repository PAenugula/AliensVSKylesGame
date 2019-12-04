import processing.core.PImage;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Zenomorph extends Moveable{

    public Zenomorph(String id,
                     Point position, int actionPeriod, int animationPeriod,
                     List<PImage> images) {
        super(id, position, images, 0, 0, 0, actionPeriod, animationPeriod);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> player = world.findNearest(this.getPosition(), Player.class);
        if (player.isPresent()) {
            Point nextPos = nextPosition(world, player.get().getPosition());
            if (getImageIndex() > 2) {
                world.moveEntity(this, nextPos);
            }
        }

        scheduler.scheduleEvent(this,
                new Activity(this, world, imageStore),
                this.getActionPeriod());

    }

    @Override
    public void nextImage() {
        if (getImageIndex() < 3) {
            setImageIndex(getImageIndex() + 1);
        } else {
            setImageIndex(getImageIndex() == 3 ? 4:3);
        }
    }

    @Override
    public Point nextPosition(WorldModel worldModel, Point destination) {
        List<Point> path = this.strategy.computePath(
                    this.getPosition(),
                    destination,
                    (point) -> worldModel.withinBounds(point) && (!worldModel.isOccupied(point) || worldModel.getOccupant(point).getClass().equals(this.getClass())),
                    (p1, p2) -> worldModel.neighbors(p1, p2),
                    PathingStrategy.CARDINAL_NEIGHBORS);
        return path.size() > 0? path.get(0): this.getPosition();
    }
}
