final class Point
{
   private final int x;
   private final int y;
   private int g;
   private int h;
   private int f;
   private Point pointBefore;
   private double distanceFromStart;

   public Point(int x, int y)
   {
      this.x = x;
      this.y = y;
      this.distanceFromStart = Integer.MAX_VALUE;
   }

   public int getX() {
      return x;
   }

   public int getY() {
      return y;
   }

   public int getG() {
      return g;
   }

   public void setG(int g) {
      this.g = g;
   }

   public int getH() {
      return h;
   }

   public void setH(int h) {
      this.h = h;
   }

   public int getF() {
      return f;
   }

   public void setF(int f) {
      this.f = f;
   }

   public Point getPointBefore() {
      return pointBefore;
   }

   public void setPointBefore(Point pointBefore) {
      this.pointBefore = pointBefore;
   }

   public double getDistanceFromStart() {
      return distanceFromStart;
   }

   public void setDistanceFromStart(double distanceFromStart) {
      this.distanceFromStart = distanceFromStart;
   }

   public String toString()
   {
      return "(" + x + "," + y + ")";
   }

   public boolean equals(Object other)
   {
      return other instanceof Point &&
              ((Point)other).x == this.x &&
              ((Point)other).y == this.y;
   }

   public int hashCode()
   {
      int result = 17;
      result = result * 31 + x;
      result = result * 31 + y;
      return result;
   }

   public boolean adjacent(Point p2)
   {
      return (this.x == p2.x && Math.abs(this.y - p2.y) == 1) ||
              (this.y == p2.y && Math.abs(this.x - p2.x) == 1);
   }

   public int distanceSquared(Point p2)
   {
      int deltaX = this.x - p2.x;
      int deltaY = this.y - p2.y;

      return deltaX * deltaX + deltaY * deltaY;
   }

}