package me.sllly.buildwands.objects;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.util.RayTraceResult;

import java.util.ArrayList;
import java.util.List;

public class Wand {
    public List<Block> findBlocks(Player player, int radius, int maxDistance){
        Block centreBlock = player.getTargetBlock(null, maxDistance);
        if (centreBlock == null) {
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
        floodFillFromCenter(intArray, radius+1, radius+1);

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

    public int[][] getIntArray(Block block, BlockFace blockFace, int radius){
        int[][] result = new int[radius*2 +1][radius*2 + 1];
        World world = block.getWorld();
        int centreX = block.getX();
        int centreY = block.getY();
        int centreZ = block.getZ();
        Material material = block.getType();

        switch (blockFace){
            case UP:
                for (int dx = -radius; dx < radius; dx++) {
                    for (int dz = -radius; dz < radius ; dz++) {
                        if (world.getBlockAt(centreX+dx, centreY, centreZ+dz).getType() == material){
                            result[dx+radius][dz+radius] = 0;
                        }else {
                            result[dx+radius][dz+radius] = 1;
                        }
                    }
                }
            default:
                return null;
        }
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
        switch (blockFace){
            case UP:
                int rowCount = 0;
                for (int[] row : intArray) {
                    rowCount++;
                    int columnCount = 0;
                    for (int cell : row) {
                        columnCount++;
                        if (cell == 2){
                            Block newBlock = world.getBlockAt(centreX-radius+rowCount, centreY+1, centreZ-radius+columnCount);
                            if (newBlock != null && newBlock.getType() != Material.AIR){
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
}
