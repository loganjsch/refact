/**
 * An action that can be taken by an entity
 */
public final class Action {
    public ActionKind kind;
    public Entity entity;
    public WorldModel world;
    public ImageStore imageStore;
    public int repeatCount;

    public Action(ActionKind kind, Entity entity, WorldModel world, ImageStore imageStore, int repeatCount) {
        this.kind = kind;
        this.entity = entity;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    // I think these transform functions remain static because they transform the object by removing and creating a whole new object
    // so modifying the existing objects properties isn't really applicable
    public static boolean transformNotFull(Entity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (entity.resourceCount >= entity.resourceLimit) {
            Entity dude = Functions.createDudeFull(entity.id, entity.position, entity.actionPeriod, entity.animationPeriod, entity.resourceLimit, entity.images);

            Functions.removeEntity(world, scheduler, entity);
            Functions.unscheduleAllEvents(scheduler, entity);

            Functions.addEntity(world, dude);
            Functions.scheduleActions(dude, scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public static void transformFull(Entity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        Entity dude = Functions.createDudeNotFull(entity.id, entity.position, entity.actionPeriod, entity.animationPeriod, entity.resourceLimit, entity.images);

        Functions.removeEntity(world, scheduler, entity);

        Functions.addEntity(world, dude);
        Functions.scheduleActions(dude, scheduler, world, imageStore);
    }


    public static boolean transformPlant(Entity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (entity.kind == EntityKind.TREE) {
            return transformTree(entity, world, scheduler, imageStore);
        } else if (entity.kind == EntityKind.SAPLING) {
            return transformSapling(entity, world, scheduler, imageStore);
        } else {
            throw new UnsupportedOperationException(String.format("transformPlant not supported for %s", entity));
        }
    }

    public static boolean transformTree(Entity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (entity.health <= 0) {
            Entity stump = Functions.createStump(Functions.STUMP_KEY + "_" + entity.id, entity.position, Functions.getImageList(imageStore, Functions.STUMP_KEY));

            Functions.removeEntity(world, scheduler, entity);

            Functions.addEntity(world, stump);

            return true;
        }

        return false;
    }

    public static boolean transformSapling(Entity entity, WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        if (entity.health <= 0) {
            Entity stump = Functions.createStump(Functions.STUMP_KEY + "_" + entity.id, entity.position, Functions.getImageList(imageStore, Functions.STUMP_KEY));

            Functions.removeEntity(world, scheduler, entity);

            Functions.addEntity(world, stump);

            return true;
        } else if (entity.health >= entity.healthLimit) {
            Entity tree = Functions.createTree(Functions.TREE_KEY + "_" + entity.id, entity.position, Functions.getNumFromRange(Functions.TREE_ACTION_MAX, Functions.TREE_ACTION_MIN), Functions.getNumFromRange(Functions.TREE_ANIMATION_MAX, Functions.TREE_ANIMATION_MIN), Functions.getIntFromRange(Functions.TREE_HEALTH_MAX, Functions.TREE_HEALTH_MIN), Functions.getImageList(imageStore, Functions.TREE_KEY));

            Functions.removeEntity(world, scheduler, entity);

            Functions.addEntity(world, tree);
            Functions.scheduleActions(tree, scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    //these move functions
    public static boolean moveToFairy(Entity fairy, WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(fairy.position, target.position)) {
            Functions.removeEntity(world, scheduler, target);
            return true;
        } else {
            Point nextPos = Functions.nextPositionFairy(fairy, world, target.position);

            if (!fairy.position.equals(nextPos)) {
                Functions.moveEntity(world, scheduler, fairy, nextPos);
            }
            return false;
        }
    }

    public static boolean moveToNotFull(Entity dude, WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(dude.position, target.position)) {
            dude.resourceCount += 1;
            target.health--;
            return true;
        } else {
            Point nextPos = Functions.nextPositionDude(dude, world, target.position);

            if (!dude.position.equals(nextPos)) {
                Functions.moveEntity(world, scheduler, dude, nextPos);
            }
            return false;
        }
    }

    public static boolean moveToFull(Entity dude, WorldModel world, Entity target, EventScheduler scheduler) {
        if (Functions.adjacent(dude.position, target.position)) {
            return true;
        } else {
            Point nextPos = Functions.nextPositionDude(dude, world, target.position);

            if (!dude.position.equals(nextPos)) {
                Functions.moveEntity(world, scheduler, dude, nextPos);
            }
            return false;
        }
    }


}
