import processing.core.PImage;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
WorldModel ideally keeps track of the actual size of our grid world and what is in that world
in terms of entities and background elements
 */

final class WorldModel
{
   private int numRows;
   private int numCols;
   private Background background[][];
   private Entity occupancy[][];
   private Set<Entity> entities;
   private Player gamePlayer1;

   private static final String OCTO_KEY = "octo";
   private static final int OCTO_NUM_PROPERTIES = 7;
   private static final int OCTO_ID = 1;
   private static final int OCTO_COL = 2;
   private static final int OCTO_ROW = 3;
   private static final int OCTO_LIMIT = 4;
   private static final int OCTO_ACTION_PERIOD = 5;
   private static final int OCTO_ANIMATION_PERIOD = 6;

   private static final String OBSTACLE_KEY = "obstacle";
   private static final int OBSTACLE_NUM_PROPERTIES = 4;
   private static final int OBSTACLE_ID = 1;
   private static final int OBSTACLE_COL = 2;
   private static final int OBSTACLE_ROW = 3;

   private static final String FISH_KEY = "fish";
   private static final int FISH_NUM_PROPERTIES = 5;
   private static final int FISH_ID = 1;
   private static final int FISH_COL = 2;
   private static final int FISH_ROW = 3;
   private static final int FISH_ACTION_PERIOD = 4;

   private static final String ATLANTIS_KEY = "atlantis";
   private static final int ATLANTIS_NUM_PROPERTIES = 4;
   private static final int ATLANTIS_ID = 1;
   private static final int ATLANTIS_COL = 2;
   private static final int ATLANTIS_ROW = 3;

   private static final String SGRASS_KEY = "seaGrass";
   private static final int SGRASS_NUM_PROPERTIES = 5;
   private static final int SGRASS_ID = 1;
   private static final int SGRASS_COL = 2;
   private static final int SGRASS_ROW = 3;
   private static final int SGRASS_ACTION_PERIOD = 4;

   private static final int FISH_REACH = 1;

   private static final String BGND_KEY = "background";
   private static final int BGND_NUM_PROPERTIES = 4;
   private static final int BGND_ID = 1;
   private static final int BGND_COL = 2;
   private static final int BGND_ROW = 3;

   private static final int PROPERTY_KEY = 0;



   //OUR NEW STATIC VARIABLES:
   private static final String PLAYER_KEY = "player";
   private static final int PLAYER_ID = 1;
   private static final int PLAYER_COL = 2;
   private static final int PLAYER_ROW = 3;
   private static final String ZENOMORPH_KEY = "zenomorph";
   private static final String RAPTOR_KEY = "raptor";



   public WorldModel(int numRows, int numCols, Background defaultBackground)
   {
      this.numRows = numRows;
      this.numCols = numCols;
      this.background = new Background[numRows][numCols];
      this.occupancy = new Entity[numRows][numCols];
      this.entities = new HashSet<>();

      for (int row = 0; row < numRows; row++)
      {
         Arrays.fill(this.background[row], defaultBackground);
      }
   }

   public int getNumRows() {
      return numRows;
   }

   public int getNumCols() {
      return numCols;
   }

   public Set<Entity> getEntities() {
      return entities;
   }

   public void movePlayer(int dx, int dy) {
      Point newPos = new Point(gamePlayer1.getPosition().getX() + dx, gamePlayer1.getPosition().getY() + dy);
      if (withinBounds(newPos) && !isOccupied(newPos)) {
         gamePlayer1.setPosition(newPos);
      }
   }

   public void load(Scanner in, ImageStore imageStore)
   {



      int lineNumber = 0;
      while (in.hasNextLine())
      {
         try
         {
            if (!processLine(in.nextLine(), imageStore))
            {
               System.err.println(String.format("invalid entry on line %d",
                       lineNumber));
            }
         }
         catch (NumberFormatException e)
         {
            System.err.println(String.format("invalid entry on line %d",
                    lineNumber));
         }
         catch (IllegalArgumentException e)
         {
            System.err.println(String.format("issue on line %d: %s",
                    lineNumber, e.getMessage()));
         }
         lineNumber++;
      }

//      Function<Point, Stream<Point>> CARDINAL_NEIGHBORS =
//              point ->
//                      Stream.<Point>builder()
//                              .add(new Point(point.getX(), point.getY() - 1))
//                              .add(new Point(point.getX(), point.getY() + 1))
//                              .add(new Point(point.getX() - 1, point.getY()))
//                              .add(new Point(point.getX() + 1, point.getY()))
//                              .build();
//      Random rand = new Random();
//      List<Point> walls = new ArrayList<>();
//      List<Point> maze = new ArrayList<>();
//      Map<Point, Boolean> placeToPassage = new HashMap<>();
//      for (int x = 0; x < getNumCols(); x ++) {
//         for(int y =0; y < getNumRows(); y++) {
//            placeToPassage.put(new Point(x, y), false);
//         }
//      }
//      Point start = new Point(rand.nextInt(getNumCols()), rand.nextInt(getNumRows()));
//      placeToPassage.put(start, true);
//      start.visited = true;
//      walls.addAll(CARDINAL_NEIGHBORS.apply(start).filter((s) -> (withinBounds(s))).collect(Collectors.toList()));
//      while (!walls.isEmpty()) {
//         int index = rand.nextInt(walls.size());
//         Point curr = walls.get(index);
//         List<Point> neighbors = CARDINAL_NEIGHBORS.apply(curr).filter((s) -> withinBounds(s)).filter((s) -> (placeToPassage.get(s))).collect(Collectors.toList());
//         boolean isPassage = neighbors.size() <= 1;
//         if (isPassage) {
//            placeToPassage.put(curr, true);
//            walls.addAll(CARDINAL_NEIGHBORS.apply(curr).filter((s) -> withinBounds(s)).filter(s -> (!placeToPassage.get(s))).collect(Collectors.toList()));
//         }
//         walls.remove(index);
//         curr.visited = true;
//      }
//      for (int x = 0; x < getNumCols(); x ++) {
//         for(int y =0; y < getNumRows(); y++) {
//            if (!placeToPassage.get(new Point(x, y)) && getOccupant(new Point(x, y)).getClass().equals(Background.class)) {
//               Entity e = new Obstacle("rand", new Point(x,y), imageStore.getImageList(OBSTACLE_KEY));
//               tryAddEntity(e);
//            }
//         }
//      }

   }

   private boolean processLine(String line,
                               ImageStore imageStore)
   {
      String[] properties = line.split("\\s");
      if (properties.length > 0)
      {
         switch (properties[PROPERTY_KEY])
         {
            case ZENOMORPH_KEY:
               return parseZeno(properties, imageStore);
            case BGND_KEY:
               return parseBackground(properties, imageStore);
            case PLAYER_KEY:
               return parsePlayer(properties, imageStore);
            case OCTO_KEY:
               return parseOcto(properties, imageStore);
            case OBSTACLE_KEY:
               return parseObstacle(properties, imageStore);
            case FISH_KEY:
               return parseFish(properties, imageStore);
            case ATLANTIS_KEY:
               return parseAtlantis(properties, imageStore);
            case SGRASS_KEY:
               return parseSgrass(properties, imageStore);
         }
      }
      return false;
   }

   private boolean parseBackground(String [] properties, ImageStore imageStore)
   {
      if (properties.length == BGND_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                 Integer.parseInt(properties[BGND_ROW]));
         String id = properties[BGND_ID];
         setBackground(pt,
                 new Background(id, imageStore.getImageList(id)));
      }

      return properties.length == BGND_NUM_PROPERTIES;
   }

   private boolean parsePlayer(String [] properties, ImageStore imageStore)
   {
      if (properties.length == 4)
      {
         Point pt = new Point(Integer.parseInt(properties[PLAYER_COL]),
                 Integer.parseInt(properties[PLAYER_ROW]));
         String id = properties[BGND_ID];
         this.gamePlayer1 = new Player(properties[OCTO_ID],
                 pt,
                 imageStore.getImageList(PLAYER_KEY),
                 0);
         tryAddEntity(gamePlayer1);
      }

      return properties.length == 4;
   }

   private void setBackground(Point pos,
      Background background)
   {
      if (withinBounds(pos))
      {
         setBackgroundCell(pos, background);
      }
   }

   private boolean parseOcto(String [] properties,
                                   ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         Entity entity = new OctoNotFull(properties[OCTO_ID],
                 Integer.parseInt(properties[OCTO_LIMIT]),
                 pt,
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(OCTO_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   private boolean parseZeno(String [] properties,
                                   ImageStore imageStore)
   {
      if (properties.length == OCTO_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[OCTO_COL]),
                 Integer.parseInt(properties[OCTO_ROW]));
         Entity entity = new Zenomorph(properties[OCTO_ID],
                 pt,
                 Integer.parseInt(properties[OCTO_ACTION_PERIOD]),
                 Integer.parseInt(properties[OCTO_ANIMATION_PERIOD]),
                 imageStore.getImageList(ZENOMORPH_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OCTO_NUM_PROPERTIES;
   }

   private boolean parseObstacle(String [] properties,
                                       ImageStore imageStore)
   {
      if (properties.length == OBSTACLE_NUM_PROPERTIES)
      {
         Point pt = new Point(
                 Integer.parseInt(properties[OBSTACLE_COL]),
                 Integer.parseInt(properties[OBSTACLE_ROW]));
         Entity entity = new Obstacle(properties[OBSTACLE_ID],
                 pt, imageStore.getImageList(OBSTACLE_KEY));
         tryAddEntity(entity);
      }

      return properties.length == OBSTACLE_NUM_PROPERTIES;
   }

   private boolean parseFish(String [] properties,
                                   ImageStore imageStore)
   {
      if (properties.length == FISH_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[FISH_COL]),
                 Integer.parseInt(properties[FISH_ROW]));
         Entity entity = new Fish(properties[FISH_ID],
                 pt, Integer.parseInt(properties[FISH_ACTION_PERIOD]),
                 imageStore.getImageList(FISH_KEY));
         tryAddEntity(entity);
      }

      return properties.length == FISH_NUM_PROPERTIES;
   }

   private boolean parseAtlantis(String [] properties,
                                       ImageStore imageStore)
   {
      if (properties.length == ATLANTIS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[ATLANTIS_COL]),
                 Integer.parseInt(properties[ATLANTIS_ROW]));
         Entity entity = new Atlantis(properties[ATLANTIS_ID],
                 pt, imageStore.getImageList(ATLANTIS_KEY));
         tryAddEntity(entity);
      }

      return properties.length == ATLANTIS_NUM_PROPERTIES;
   }

   private boolean parseSgrass(String [] properties,
                                     ImageStore imageStore)
   {
      if (properties.length == SGRASS_NUM_PROPERTIES)
      {
         Point pt = new Point(Integer.parseInt(properties[SGRASS_COL]),
                 Integer.parseInt(properties[SGRASS_ROW]));
         Entity entity = new SGrass(properties[SGRASS_ID],
                 pt,
                 Integer.parseInt(properties[SGRASS_ACTION_PERIOD]),
                 imageStore.getImageList(SGRASS_KEY));
         tryAddEntity(entity);
      }

      return properties.length == SGRASS_NUM_PROPERTIES;
   }

   private void tryAddEntity(Entity entity)
   {
      if (isOccupied(entity.getPosition()))
      {
         // arguably the wrong type of exception, but we are not
         // defining our own exceptions yet
         throw new IllegalArgumentException("position occupied");
      }

      addEntity(entity);
   }

   public boolean withinBounds(Point pos)
   {
      return pos.getY() >= 0 && pos.getY() < numRows &&
              pos.getX() >= 0 && pos.getX() < numCols;
   }

   public boolean isOccupied(Point pos)
   {
      return withinBounds(pos) &&
              getOccupancyCell(pos) != null;
   }

   private Optional<Entity> nearestEntity(List<Entity> entities,
                                                Point pos)
   {
      if (entities.isEmpty())
      {
         return Optional.empty();
      }
      else
      {
         Entity nearest = entities.get(0);
         int nearestDistance = nearest.getPosition().distanceSquared(pos);

         for (Entity other : entities)
         {
            int otherDistance = other.getPosition().distanceSquared(pos);

            if (otherDistance < nearestDistance)
            {
               nearest = other;
               nearestDistance = otherDistance;
            }
         }

         return Optional.of(nearest);
      }
   }

   public Optional<Entity> findNearest(Point pos,
                                              Class kind)
   {
      List<Entity> ofType = new LinkedList<>();
      for (Entity entity : entities)
      {
         if (entity.getClass().equals(kind))
         {
            ofType.add(entity);
         }
      }

      return nearestEntity(ofType, pos);
   }

   public void addEntity(Entity entity)
   {
      if (withinBounds(entity.getPosition()))
      {
         setOccupancyCell(entity.getPosition(), entity);
         entities.add(entity);
      }
   }

   public void moveEntity(Entity entity, Point pos)
   {
      Point oldPos = entity.getPosition();
      if (withinBounds(pos) && !pos.equals(oldPos))
      {
         setOccupancyCell(oldPos, null);
         removeEntityAt(pos);
         setOccupancyCell(pos, entity);
         entity.setPosition(pos);
      }
   }

   public void removeEntity(Entity entity)
   {
      removeEntityAt(entity.getPosition());
   }


   private void removeEntityAt(Point pos)
   {
      if (withinBounds(pos)
              && getOccupancyCell(pos) != null)
      {
         Entity entity = getOccupancyCell(pos);

         /* this moves the entity just outside of the grid for
            debugging purposes */
         entity.setPosition(new Point(-1, -1));
         entities.remove(entity);
         setOccupancyCell(pos, null);
      }
   }

   public Optional<PImage> getBackgroundImage(Point pos)
   {
      if (withinBounds(pos))
      {
         return Optional.of(getBackgroundCell(pos).getCurrentImage());
      }
      else
      {
         return Optional.empty();
      }
   }

   public Optional<Entity> getOccupant(Point pos)
   {
      if (isOccupied(pos))
      {
         return Optional.of(getOccupancyCell(pos));
      }
      else
      {
         return Optional.empty();
      }
   }

   private Entity getOccupancyCell(Point pos)
   {
      return this.occupancy[pos.getY()][pos.getX()];
   }

   private void setOccupancyCell(Point pos,
                                       Entity entity)
   {
      this.occupancy[pos.getY()][pos.getX()] = entity;
   }

   private Background getBackgroundCell(Point pos)
   {
      return this.background[pos.getY()][pos.getX()];
   }

   private void setBackgroundCell(Point pos,
                                        Background background)
   {
      this.background[pos.getY()][pos.getX()] = background;
   }

   public Optional<Point> findOpenAround(Point pos)
   {
      for (int dy = -FISH_REACH; dy <= FISH_REACH; dy++)
      {
         for (int dx = -FISH_REACH; dx <= FISH_REACH; dx++)
         {
            Point newPt = new Point(pos.getX() + dx, pos.getY() + dy);
            if (withinBounds(newPt) &&
                    !isOccupied(newPt))
            {
               return Optional.of(newPt);
            }
         }
      }

      return Optional.empty();
   }

   public boolean neighbors(Point p1, Point p2) {
      return p1.getX()+1 == p2.getX() && p1.getY() == p2.getY() ||
              p1.getX()-1 == p2.getX() && p1.getY() == p2.getY() ||
              p1.getX() == p2.getX() && p1.getY()+1 == p2.getY() ||
              p1.getX() == p2.getX() && p1.getY()-1 == p2.getY();
   }



}
