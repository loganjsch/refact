import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An event is made up of an Entity that is taking an
 * Action a specified time.
 */
public final class Event {
    public Action action;
    public double time;
    public Entity entity;

    public Event(Action action, double time, Entity entity) {
        this.action = action;
        this.time = time;
        this.entity = entity;
    }

    public void executeAction(EventScheduler scheduler) {
        switch (this.action.kind) {
            case ACTIVITY:
                executeActivityAction(scheduler);
                break;

            case ANIMATION:
                executeAnimationAction(scheduler);
                break;
        }
    }

    public void executeAnimationAction(EventScheduler scheduler) {
        Functions.nextImage(this.action.entity);

        if (this.action.repeatCount != 1) {
            Functions.scheduleEvent(scheduler, this.action.entity, Functions.createAnimationAction(this.action.entity, Math.max(this.action.repeatCount - 1, 0)), Functions.getAnimationPeriod(this.action.entity));
        }
    }

    public void executeActivityAction(EventScheduler scheduler) {
        switch (this.action.entity.kind) {
            case SAPLING:
                executeSaplingActivity(this.action.world, this.action.imageStore, scheduler);
                break;
            case TREE:
                executeTreeActivity(this.action.world, this.action.imageStore, scheduler);
                break;
            case FAIRY:
                executeFairyActivity(this.action.world, this.action.imageStore, scheduler);
                break;
            case DUDE_NOT_FULL:
                executeDudeNotFullActivity(this.action.world, this.action.imageStore, scheduler);
                break;
            case DUDE_FULL:
                executeDudeFullActivity(this.action.world, this.action.imageStore, scheduler);
                break;
            default:
                throw new UnsupportedOperationException(String.format("executeActivityAction not supported for %s", this.action.entity.kind));
        }
    }

    public void executeSaplingActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        this.entity.health++;
        if (!Action.transformPlant(this.entity, world, scheduler, imageStore)) {
            Functions.scheduleEvent(scheduler, this.entity, Functions.createActivityAction(this.entity, world, imageStore), this.entity.actionPeriod);
        }
    }

    public void executeTreeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

        if (!Action.transformPlant(this.entity, world, scheduler, imageStore)) {

            Functions.scheduleEvent(scheduler, this.entity, Functions.createActivityAction(this.entity, world, imageStore), this.entity.actionPeriod);
        }
    }

    public void executeFairyActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fairyTarget = Functions.findNearest(world, this.entity.position, new ArrayList<>(List.of(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (Action.moveToFairy(this.entity, world, fairyTarget.get(), scheduler)) {

                Entity sapling = Functions.createSapling(Functions.SAPLING_KEY + "_" + fairyTarget.get().id, tgtPos, Functions.getImageList(imageStore, Functions.SAPLING_KEY), 0);

                Functions.addEntity(world, sapling);
                Functions.scheduleActions(sapling, scheduler, world, imageStore);
            }
        }

        Functions.scheduleEvent(scheduler, this.entity, Functions.createActivityAction(this.entity, world, imageStore), this.entity.actionPeriod);
    }

    public void executeDudeNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> target = Functions.findNearest(world, this.entity.position, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (target.isEmpty() || !Action.moveToNotFull(this.entity, world, target.get(), scheduler) || !Action.transformNotFull(this.entity, world, scheduler, imageStore)) {
            Functions.scheduleEvent(scheduler, this.entity, Functions.createActivityAction(entity, world, imageStore), this.entity.actionPeriod);
        }
    }

    public void executeDudeFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        Optional<Entity> fullTarget = Functions.findNearest(world, this.entity.position, new ArrayList<>(List.of(EntityKind.HOUSE)));

        if (fullTarget.isPresent() && Action.moveToFull(this.entity, world, fullTarget.get(), scheduler)) {
            Action.transformFull(this.entity, world, scheduler, imageStore);
        } else {
            Functions.scheduleEvent(scheduler, this.entity, Functions.createActivityAction(entity, world, imageStore), this.entity.actionPeriod);
        }
    }

}
