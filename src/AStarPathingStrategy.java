import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AStarPathingStrategy implements PathingStrategy {

    @Override
    public List<Point> computePath(Point start, Point end, Predicate<Point> canPassThrough, BiPredicate<Point, Point> withinReach, Function<Point, Stream<Point>> potentialNeighbors) {
        List<Point> path = new ArrayList<>();
        List<Point> openList = new ArrayList<>();
        openList.add(start);
        Function<Point, Integer> getHeuristic = (p) -> (Math.abs(p.getX()-end.getX()) + Math.abs(p.getY()-end.getY()));
        Function<Point, Integer> getFValue = (p) -> (getHeuristic.apply(p) + p.getG());
        Comparator<Point> t1 = Comparator.comparingInt(p -> getFValue.apply(p));
        Map<Point, List<Point>> closedList = new HashMap<>();
        while(!withinReach.test(openList.get(0), end)) {
            List<Point> goodNeighbors = potentialNeighbors.apply(openList.get(0))
                    .filter(p -> (canPassThrough.test(p)))
                    .filter(p -> ((!closedList.containsKey(p)) || p.getG() == 0 || (p.getG() > openList.get(0).getG()+1)))
                    .collect(Collectors.toList());
            goodNeighbors.forEach(point -> {point.setG(openList.get(0).getG()+1); point.setPointBefore(openList.get(0));});
            closedList.put(openList.get(0), goodNeighbors);
            if (goodNeighbors.contains(end)) break;
            if (goodNeighbors.isEmpty()) {
                openList.remove(0);
            } else {
                openList.remove(0);
                goodNeighbors.stream().filter((p)->(!closedList.containsKey(p) && !openList.contains(p))).forEach(p -> openList.add(0,p));
            }
            openList.sort(t1);
            if (openList.isEmpty()) return new ArrayList<>();
        }
        path.add(openList.get(0));
        while(path.get(0).getPointBefore()!=start) {
            path.add(0, path.get(0).getPointBefore());
        }
        return path;
    }
}
