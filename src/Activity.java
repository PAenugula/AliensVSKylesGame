public class Activity implements Action {
    private final ActiveEntity entity;
    private final WorldModel world;
    private final ImageStore imageStore;
    private final int repeatCount;

    public Activity(ActiveEntity entity, WorldModel world,
                                             ImageStore imageStore)
    {
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = 0;
    }

    public void executeAction(EventScheduler eventScheduler) {
        entity.executeActivity(world, imageStore, eventScheduler);
    }

}
