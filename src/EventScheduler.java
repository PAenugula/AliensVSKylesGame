import java.util.*;

/*
EventScheduler: ideally our way of controlling what happens in our virtual world
 */

final class EventScheduler
{


   private static final int ATLANTIS_ANIMATION_REPEAT_COUNT = 7;

   private static final int QUAKE_ANIMATION_REPEAT_COUNT = 10;
   private final PriorityQueue<Event> eventQueue;
   private final Map<Entity, List<Event>> pendingEvents;
   private final double timeScale;

   public EventScheduler(double timeScale)
   {
      this.eventQueue = new PriorityQueue<>(new EventComparator());
      this.pendingEvents = new HashMap<>();
      this.timeScale = timeScale;
   }

   public void scheduleEvent(Entity entity, Action action, long afterPeriod)
   {
      long time = System.currentTimeMillis() +
              (long)(afterPeriod * timeScale);
      Event event = new Event(action, time, entity);

      eventQueue.add(event);

      // update list of pending events for the given entity
      List<Event> pending = pendingEvents.getOrDefault(entity,
              new LinkedList<>());
      pending.add(event);
      pendingEvents.put(entity, pending);
   }

   public void unscheduleAllEvents(Entity entity)
   {
      List<Event> pending = pendingEvents.remove(entity);

      if (pending != null)
      {
         for (Event event : pending)
         {
            eventQueue.remove(event);
         }
      }
   }
   private void removePendingEvent(Event event)
   {
      List<Event> pending = pendingEvents.get(event.getEntity());

      if (pending != null)
      {
         pending.remove(event);
      }
   }

   public void updateOnTime(long time)
   {
      while (!eventQueue.isEmpty() &&
              eventQueue.peek().getTime() < time)
      {
         Event next = eventQueue.poll();

         removePendingEvent(next);

         next.getAction().executeAction(this);
      }
   }

//   public void scheduleActions(Entity entity, WorldModel world, ImageStore imageStore)
//   {
//      switch (entity.getClass().getName())
//      {
//         case "OctoFull":
//            scheduleEvent(entity,
//                    new Activity(entity, world, imageStore),
//                    entity.getActionPeriod());
//            scheduleEvent(entity, new Animation(entity, 0),
//                    entity.getAnimationPeriod());
//            break;
//
//         case "OctoNotFull":
//            scheduleEvent(entity,
//                    new Activity(entity, world, imageStore),
//                    entity.getActionPeriod());
//            scheduleEvent(entity, new Animation(entity, 0),
//                    entity.getAnimationPeriod());
//            break;
//
//         case "Fish":
//            scheduleEvent(entity,
//                    new Activity(entity, world, imageStore),
//                    entity.getActionPeriod());
//            break;
//
//         case "Crab":
//            scheduleEvent(entity,
//                    new Activity(entity, world, imageStore),
//                    entity.getActionPeriod());
//            scheduleEvent(entity,
//                    new Animation(entity, 0), entity.getAnimationPeriod());
//            break;
//
//         case "Quake":
//            scheduleEvent(entity,
//                    new Activity(entity, world, imageStore),
//                    entity.getActionPeriod());
//            scheduleEvent(entity,
//                    new Animation(entity, QUAKE_ANIMATION_REPEAT_COUNT),
//                    entity.getAnimationPeriod());
//            break;
//
//         case "SGrass":
//            scheduleEvent(entity,
//                    new Activity(entity, world, imageStore),
//                    entity.getActionPeriod());
//            break;
//         case "Atlantis":
//            scheduleEvent(entity,
//                    new Animation(entity, ATLANTIS_ANIMATION_REPEAT_COUNT),
//                    entity.getAnimationPeriod());
//            break;
//
//         default:
//      }
//   }




}
