import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel {
    public int numRows;
    public int numCols;
    public Background[][] background;
    public Entity[][] occupancy;
    public Set<Entity> entities;

    public WorldModel() {

    }

    /**
     * Helper method for testing. Don't move or modify this method.
     */
    public List<String> log(){
        List<String> list = new ArrayList<>();
        for (Entity entity : entities) {
            String log = entity.log();
            if(log != null) list.add(log);
        }
        return list;
    }

    //these parse methods should not be static because they need an instance of world model
    public void parseSapling(String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.SAPLING_NUM_PROPERTIES) {
            int health = Integer.parseInt(properties[Functions.SAPLING_HEALTH]);
            Entity entity = Functions.createSapling(id, pt, Functions.getImageList(imageStore, Functions.SAPLING_KEY), health);
            Functions.tryAddEntity(this.world, entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.SAPLING_KEY, Functions.SAPLING_NUM_PROPERTIES));
        }
    }

    public static void parseDude(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.DUDE_NUM_PROPERTIES) {
            Entity entity = Functions.createDudeNotFull(id, pt, Double.parseDouble(properties[Functions.DUDE_ACTION_PERIOD]), Double.parseDouble(properties[Functions.DUDE_ANIMATION_PERIOD]), Integer.parseInt(properties[Functions.DUDE_LIMIT]), Functions.getImageList(imageStore, Functions.DUDE_KEY));
            Functions.tryAddEntity(world, entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.DUDE_KEY, Functions.DUDE_NUM_PROPERTIES));
        }
    }

    public static void parseFairy(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.FAIRY_NUM_PROPERTIES) {
            Entity entity = Functions.createFairy(id, pt, Double.parseDouble(properties[Functions.FAIRY_ACTION_PERIOD]), Double.parseDouble(properties[Functions.FAIRY_ANIMATION_PERIOD]), Functions.getImageList(imageStore, Functions.FAIRY_KEY));
            Functions.tryAddEntity(world, entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.FAIRY_KEY, Functions.FAIRY_NUM_PROPERTIES));
        }
    }

    public static void parseTree(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.TREE_NUM_PROPERTIES) {
            Entity entity = Functions.createTree(id, pt, Double.parseDouble(properties[Functions.TREE_ACTION_PERIOD]), Double.parseDouble(properties[Functions.TREE_ANIMATION_PERIOD]), Integer.parseInt(properties[Functions.TREE_HEALTH]), Functions.getImageList(imageStore, Functions.TREE_KEY));
            Functions.tryAddEntity(world, entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.TREE_KEY, Functions.TREE_NUM_PROPERTIES));
        }
    }

    public static void parseObstacle(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.OBSTACLE_NUM_PROPERTIES) {
            Entity entity = Functions.createObstacle(id, pt, Double.parseDouble(properties[Functions.OBSTACLE_ANIMATION_PERIOD]), Functions.getImageList(imageStore, Functions.OBSTACLE_KEY));
            Functions.tryAddEntity(world, entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.OBSTACLE_KEY, Functions.OBSTACLE_NUM_PROPERTIES));
        }
    }

    public static void parseHouse(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.HOUSE_NUM_PROPERTIES) {
            Entity entity = Functions.createHouse(id, pt, Functions.getImageList(imageStore, Functions.HOUSE_KEY));
            Functions.tryAddEntity(world, entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.HOUSE_KEY, Functions.HOUSE_NUM_PROPERTIES));
        }
    }
    public static void parseStump(WorldModel world, String[] properties, Point pt, String id, ImageStore imageStore) {
        if (properties.length == Functions.STUMP_NUM_PROPERTIES) {
            Entity entity = Functions.createStump(id, pt, Functions.getImageList(imageStore, Functions.STUMP_KEY));
            Functions.tryAddEntity(world, entity);
        }else{
            throw new IllegalArgumentException(String.format("%s requires %d properties when parsing", Functions.STUMP_KEY, Functions.STUMP_NUM_PROPERTIES));
        }
    }

}
