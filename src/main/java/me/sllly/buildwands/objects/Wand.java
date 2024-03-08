package me.sllly.buildwands.objects;

import me.sllly.buildwands.Util.NbtApiUtils;
import me.sllly.buildwands.Util.Util;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.RayTraceResult;

import java.util.*;

public class Wand {

    public static Map<String, Wand> definedWands = new HashMap<>();

    private String wandId;
    private final String wandGroupId;
    private final int radius;
    private final int maxDurability;
    private final int maxUniqueMaterials;
    private final ItemStack originalItem;
    private ItemStack wandItem;
    private int durability;
    private Map<Material, Integer> materialAmounts;

    private final String nbtKey = "buildwand";

    public Wand(String wandGroupId, int radius, int maxDurability, int maxUniqueMaterials, ItemStack originalItem) {
        this.wandGroupId = wandGroupId;
        this.radius = radius;
        this.maxDurability = maxDurability;
        this.maxUniqueMaterials = maxUniqueMaterials;
        this.originalItem = originalItem;
        wandItem = originalItem;
        durability = maxDurability;
        materialAmounts = new HashMap<>();

        updateWandItem();
    }

    public Wand(ItemStack itemStack){
        wandId = NbtApiUtils.getNBTString(itemStack, nbtKey+"-wandid");
        wandGroupId = NbtApiUtils.getNBTString(itemStack, nbtKey+"-group");
        radius = definedWands.get(wandGroupId).getRadius();
        maxDurability = definedWands.get(wandGroupId).getMaxDurability();
        maxUniqueMaterials = definedWands.get(wandGroupId).getMaxUniqueMaterials();
        originalItem = definedWands.get(wandGroupId).getOriginalItem();
        wandItem = itemStack;
        durability = Integer.parseInt(NbtApiUtils.getNBTString(itemStack, nbtKey+"-durability"));

        materialAmounts = new HashMap<>();
        List<String> nbtKeys = NbtApiUtils.getNBTKeys(wandItem);
        String nbtMaterialKey = nbtKey+"-material-";
        for (String key : nbtKeys) {
            if (key.startsWith(nbtMaterialKey)) {
                String materialString = key.substring(nbtMaterialKey.length());
                Material material = Material.getMaterial(materialString.toUpperCase());
                String intString = NbtApiUtils.getNBTString(wandItem, key);
                int amount = Integer.parseInt(intString);
                materialAmounts.put(material, amount);
            }
        }
    }

    public void updateWandItem(){
        wandItem = originalItem.clone();
        wandItem = Util.replaceTextInItem(wandItem, "%durability%", durability+"");
        wandItem = Util.replaceTextInItem(wandItem, "%radius%", radius+"");

        wandItem = NbtApiUtils.applyNBTString(wandItem, nbtKey+"-group", wandGroupId);
        wandItem = NbtApiUtils.applyNBTString(wandItem, nbtKey+"-durability", durability+"");
        if (wandId == null) {
            wandId = UUID.randomUUID().toString();
            wandItem = NbtApiUtils.applyNBTString(wandItem, nbtKey+"-wandid", wandId);
        }


        for (Map.Entry<Material, Integer> materialIntegerEntry : materialAmounts.entrySet()) {
            wandItem = NbtApiUtils.applyNBTString(wandItem, nbtKey+"-material-"+materialIntegerEntry.getKey().toString().toLowerCase(),
                    materialIntegerEntry.getValue()+"");
        }
    }

    public List<Block> findBlocks(Player player, int radius, int maxDistance){
        Block centreBlock = player.getTargetBlock(null, maxDistance);
        if (centreBlock.getType() == Material.AIR) {
            return null;
        }
        BlockFace blockFace = getLookedAtBlockFace(player, maxDistance);
        if (blockFace == null) {
            return null;
        }
        int[][] intArray = getIntArray(centreBlock, blockFace, radius);
        if (intArray == null) {
            return null;
        }
        floodFillFromCenter(intArray, radius, radius);

        return blockToChange(intArray, centreBlock, blockFace, radius);
    }

    //ChatGPT Method
    public BlockFace getLookedAtBlockFace(Player player, int maxDistance) {
        // Perform a ray trace from the player's eye location in the direction they're looking
        // The ray trace will check for blocks up to the specified maxDistance
        RayTraceResult rayTraceResult = player.getWorld().rayTraceBlocks(player.getEyeLocation(), player.getEyeLocation().getDirection(), maxDistance);

        // Check if the ray trace hit a block
        if (rayTraceResult != null && rayTraceResult.getHitBlock() != null) {
            // Return the face of the block that was hit
            return rayTraceResult.getHitBlockFace();
        } else {
            // No block was hit, or the block does not have a specific "face" that was hit
            return null;
        }
    }

    public int[][] getIntArray(Block block, BlockFace blockFace, int radius) {
        int[][] result = new int[radius * 2 + 1][radius * 2 + 1];
        World world = block.getWorld();
        int centreX = block.getX();
        int centreY = block.getY();
        int centreZ = block.getZ();
        Material material = block.getType();

        switch (blockFace) {
            case UP:
            case DOWN:
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        if (world.getBlockAt(centreX + dx, centreY, centreZ + dz).getType() == material) {
                            result[dx + radius][dz + radius] = 0;
                        } else {
                            result[dx + radius][dz + radius] = 1;
                        }
                    }
                }
                break;
            case WEST:
            case EAST:
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dz = -radius; dz <= radius; dz++) {
                        if (world.getBlockAt(centreX, centreY+dy, centreZ + dz).getType() == material) {
                            result[dy + radius][dz + radius] = 0;
                        } else {
                            result[dy + radius][dz + radius] = 1;
                        }
                    }
                }
                break;
            case SOUTH:
            case NORTH:
                for (int dx = -radius; dx <= radius; dx++) {
                    for (int dy = -radius; dy <= radius; dy++) {
                        if (world.getBlockAt(centreX + dx, centreY +dy, centreZ).getType() == material) {
                            result[dx + radius][dy + radius] = 0;
                        } else {
                            result[dx + radius][dy + radius] = 1;
                        }
                    }
                }
                break;
            default:
                return null;
        }
        return result;
    }

    //ChatGPT Method
    private void floodFillFromCenter(int[][] grid, int startingRow, int startingColumn) {
        // Base condition to check if we are within the bounds of the grid and current cell is 0.
        if (startingRow < 0 || startingRow >= grid.length || startingColumn < 0 || startingColumn >= grid[0].length || grid[startingRow][startingColumn] != 0) {
            return;
        }

        // Change the current cell to 2 as it's connected to the center or another 0 that's connected to the center.
        grid[startingRow][startingColumn] = 2;

        // Recursively apply the same for all adjacent cells (up, down, left, right)
        floodFillFromCenter(grid, startingRow + 1, startingColumn); // down
        floodFillFromCenter(grid, startingRow - 1, startingColumn); // up
        floodFillFromCenter(grid, startingRow, startingColumn + 1); // right
        floodFillFromCenter(grid, startingRow, startingColumn - 1); // left
    }

    public List<Block> blockToChange(int[][] intArray, Block centreBlock, BlockFace blockFace, int radius){
        List<Block> blocks = new ArrayList<>();
        World world = centreBlock.getWorld();
        int centreX = centreBlock.getX();
        int centreY = centreBlock.getY();
        int centreZ = centreBlock.getZ();

        int rowCount;

        switch (blockFace){
            case UP:
                rowCount = 0;
                for (int[] row : intArray) {
                    rowCount++;
                    int columnCount = 0;
                    for (int cell : row) {
                        columnCount++;
                        if (cell == 2){
                            Block newBlock = world.getBlockAt(centreX-radius+rowCount-1, centreY+1, centreZ-radius+columnCount-1);
                            if (newBlock.getType() == Material.AIR){
                                blocks.add(newBlock);
                            }
                        }
                    }
                }
                return blocks;
            case DOWN:
                rowCount = 0;
                for (int[] row : intArray) {
                    rowCount++;
                    int columnCount = 0;
                    for (int cell : row) {
                        columnCount++;
                        if (cell == 2){
                            Block newBlock = world.getBlockAt(centreX-radius+rowCount-1, centreY-1, centreZ-radius+columnCount-1);
                            if (newBlock.getType() == Material.AIR){
                                blocks.add(newBlock);
                            }
                        }
                    }
                }
                return blocks;
            case WEST:
                rowCount = 0;
                for (int[] row : intArray) {
                    rowCount++;
                    int columnCount = 0;
                    for (int cell : row) {
                        columnCount++;
                        if (cell == 2){
                            Block newBlock = world.getBlockAt(centreX-1, centreY-radius+rowCount-1, centreZ-radius+columnCount-1);
                            if (newBlock.getType() == Material.AIR){
                                blocks.add(newBlock);
                            }
                        }
                    }
                }
                return blocks;
            case EAST:
                rowCount = 0;
                for (int[] row : intArray) {
                    rowCount++;
                    int columnCount = 0;
                    for (int cell : row) {
                        columnCount++;
                        if (cell == 2){
                            Block newBlock = world.getBlockAt(centreX+1, centreY-radius+rowCount-1, centreZ-radius+columnCount-1);
                            if (newBlock.getType() == Material.AIR){
                                blocks.add(newBlock);
                            }
                        }
                    }
                }
                return blocks;
            case NORTH:
                rowCount = 0;
                for (int[] row : intArray) {
                    rowCount++;
                    int columnCount = 0;
                    for (int cell : row) {
                        columnCount++;
                        if (cell == 2){
                            Block newBlock = world.getBlockAt(centreX-radius+rowCount-1, centreY-radius+columnCount-1, centreZ-1);
                            if (newBlock.getType() == Material.AIR){
                                blocks.add(newBlock);
                            }
                        }
                    }
                }
                return blocks;
            case SOUTH:
                rowCount = 0;
                for (int[] row : intArray) {
                    rowCount++;
                    int columnCount = 0;
                    for (int cell : row) {
                        columnCount++;
                        if (cell == 2){
                            Block newBlock = world.getBlockAt(centreX-radius+rowCount-1, centreY-radius+columnCount-1, centreZ+1);
                            if (newBlock.getType() == Material.AIR){
                                blocks.add(newBlock);
                            }
                        }
                    }
                }
                return blocks;
            default:
                return null;
        }
    }

    public static ItemStack getWand(String wandGroupId){
        Wand wand = definedWands.get(wandGroupId);
        if (wand == null) {
            return null;
        }
        return wand.getWandItem();
    }

    public String getWandGroupId() {
        return wandGroupId;
    }

    public int getRadius() {
        return radius;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public int getMaxUniqueMaterials() {
        return maxUniqueMaterials;
    }

    public ItemStack getOriginalItem() {
        return originalItem;
    }

    public ItemStack getWandItem() {
        return wandItem;
    }

    public int getDurability() {
        return durability;
    }

    public Map<Material, Integer> getMaterialAmounts() {
        return materialAmounts;
    }

    public void setWandItem(ItemStack wandItem) {
        this.wandItem = wandItem;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public void setMaterialAmounts(Map<Material, Integer> materialAmounts) {
        this.materialAmounts = materialAmounts;
    }

    public String getWandId() {
        return wandId;
    }
}
