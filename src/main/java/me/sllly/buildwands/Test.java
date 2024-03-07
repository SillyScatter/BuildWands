package me.sllly.buildwands;

public class Test {
    public static void main(String[] args) {
        int[][] grid = {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1},
                {1, 1, 1, 0, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 0, 0, 0, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 0, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 0, 1, 1, 1, 1, 1, 1, 1, 1},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };

        floodFillFromCenter(grid, 5, 5);

        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    private static void floodFillFromCenter(int[][] grid, int row, int col) {
        // Base condition to check if we are within the bounds of the grid and current cell is 0.
        if (row < 0 || row >= grid.length || col < 0 || col >= grid[0].length || grid[row][col] != 0) {
            return;
        }

        // Change the current cell to 2 as it's connected to the center or another 0 that's connected to the center.
        grid[row][col] = 2;

        // Recursively apply the same for all adjacent cells (up, down, left, right)
        floodFillFromCenter(grid, row + 1, col); // down
        floodFillFromCenter(grid, row - 1, col); // up
        floodFillFromCenter(grid, row, col + 1); // right
        floodFillFromCenter(grid, row, col - 1); // left
    }
}