package src.edu.kit.informatik;

import src.edu.kit.informatik.Util.Vector2Int;

public class Terrain {
    // First dimension is the height (y), second dimension is the width (x).
    public final TerrainTile[][] terrain;
    public final int height;
    public final int width;

    public Terrain(TerrainTile[][] terrain) {
        this.terrain = terrain;
        height = terrain.length;
        width = terrain[0].length;
    }

    public void setTerrainTile (Vector2Int position, char typeAsChar) {
        terrain[position.y][position.x] = new TerrainTile(TerrainType.convertCharToTerrainType(typeAsChar), typeAsChar);
    }

    // Knows that there is exactly one rover because validate is always called beforehand.
    public Vector2Int getRoverPosition() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (getTerrainType(x, y) == TerrainType.ROVER || getTerrainType(x, y) == TerrainType.ROVER_AND_GOAL) {
                    return new Vector2Int(x, y);
                }
            }
        }
        // should NEVER happen
        System.out.println("Did not find the rover position!");
        return null;
    }


    public TerrainType getTerrainType(Vector2Int position) {
        return getTerrainType(position.x, position.y);
    }

    public TerrainType getTerrainType(int x, int y) {
        return terrain[y][x].terrainType;
    }

    /**
     * Checks if a rover and a goal exist.
     * (does NOT check if obstacles are closed)
     * @return true if valid, false if not
     */
    public boolean isValid() {
        boolean hasRover = false;
        boolean hasGoal = false;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                TerrainTile terrainTile = terrain[y][x];
                if (terrainTile.terrainType == TerrainType.ROVER) {
                    if (hasRover) {
                        // Because terrain has more than one rover.
                        return false;
                    }
                    hasRover = true;
                }
                if (terrainTile.terrainType == TerrainType.GOAL) {
                    hasGoal = true;
                }
            }
        }
        return hasRover && hasGoal;
    }

    public boolean isObstacle(Vector2Int position) {
        TerrainType terrainType = getTerrainType(position);
        switch (terrainType) {
            case GOAL:
            case EMPTY:
                return false;
        }
        // The case rover, rover and goal and error should never appear.
        return true;
    }
}
