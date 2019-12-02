import processing.core.PImage;

import java.nio.file.attribute.PosixFileAttributes;
import java.util.List;

public abstract class Octo extends Moveable {

    public Octo(String id,
                Point position,
                List<PImage> images,
                int imageIndex,
                int resourceLimit,
                int resourceCount,
                int actionPeriod,
                int animationPeriod) {
        super(id, position, images, imageIndex, resourceLimit, resourceCount, actionPeriod, animationPeriod);
    }
    public Point nextPosition(WorldModel worldModel,
                                  Point destPos)
    {
        List<Point> path = this.strategy.computePath(this.getPosition(), destPos,
                (point) -> worldModel.withinBounds(point) && !worldModel.isOccupied(point),
                (p1, p2) -> worldModel.neighbors(p1, p2),
                PathingStrategy.CARDINAL_NEIGHBORS);
        if (!path.isEmpty()) {
            return path.get(0);
        } else {
            return getPosition();
        }
        
//        int horiz = Integer.signum(destPos.getX() - this.getPosition().getX());
//        Point newPos = new Point(this.getPosition().getX() + horiz,
//                this.getPosition().getY());
//
//        if (horiz == 0 || worldModel.isOccupied(newPos))
//        {
//            int vert = Integer.signum(destPos.getY() - this.getPosition().getY());
//            newPos = new Point(this.getPosition().getX(),
//                    this.getPosition().getY() + vert);
//
//            if (vert == 0 || worldModel.isOccupied(newPos))
//            {
//                newPos = this.getPosition();
//            }
//        }
//
//        return newPos;
    }

}
