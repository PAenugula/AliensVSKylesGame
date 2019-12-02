import java.nio.file.Path;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DijkstraPathingStrategy implements PathingStrategy {

    @Override
    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors) {


        List<Point> path = new LinkedList<>();
        Point currentPoint = new Point(start.getX(), start.getY());
        currentPoint.setDistanceFromStart(0);
        HashMap<Point, String> visited = new HashMap<>();
        List<Point> nonVisited = new ArrayList<>();

        while(!withinReach.test(currentPoint, end)){
            List<Point> unvisitedNeighbors = potentialNeighbors.apply(currentPoint).filter(canPassThrough).collect(Collectors.toList());
            for(Point point: unvisitedNeighbors){
                if(!visited.containsKey(point)) {
                    if(!nonVisited.contains(point)){
                        nonVisited.add(point);
                    }
                    double distanceFromStart = Math.sqrt(Math.pow(point.getX() - currentPoint.getX(), 2) +
                            Math.pow(point.getY() - currentPoint.getY(), 2)) + currentPoint.getDistanceFromStart();
                    if (distanceFromStart < point.getDistanceFromStart()) {
                        point.setDistanceFromStart(distanceFromStart);
                        point.setPointBefore(currentPoint);
                    }
                }
            }
            visited.put(currentPoint, "1");
            nonVisited.remove(currentPoint);
            nonVisited.sort(new Comparator<Point>() {
                @Override
                public int compare(Point o1, Point o2) {
                    return ((int)(o1.getDistanceFromStart() - o2.getDistanceFromStart()));
                }
            });

            if(nonVisited.size() == 0){
                return path;
            }
            currentPoint = nonVisited.get(0);
            Point pointBefore = currentPoint.getPointBefore();
            currentPoint.setDistanceFromStart(pointBefore.getDistanceFromStart() +
                    Math.sqrt(Math.pow(pointBefore.getX() - currentPoint.getX(), 2) +
                            Math.pow(pointBefore.getY() - currentPoint.getY(), 2)));

        }

        end.setPointBefore(currentPoint);
        while(currentPoint.getPointBefore() != null){
            path.add(0, currentPoint);
            currentPoint = currentPoint.getPointBefore();
        }
        return path;

    }

}
