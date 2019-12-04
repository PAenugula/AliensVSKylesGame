import processing.core.PImage;

import java.util.List;

public class Player extends Entity {

    public Player(String id,
                  Point position,
                  List<PImage> images,
                  int imageIndex){
        super(id, position, images, imageIndex, 0,0);
    }


}
