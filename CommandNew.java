package src.edu.kit.informatik.Commands;

import src.edu.kit.informatik.*;
import src.edu.kit.informatik.Exceptions.ShouldQuitException;

import java.util.LinkedList;

public class CommandNew extends Command {

    // Used to get the next line for the terrain input.
    private final IOHandler ioHandler;
    private final Application application;
    private int height;
    private int width;

    public CommandNew(String[] arguments, IOHandler ioHandler, Application application) {
        super(arguments);
        this.ioHandler = ioHandler;
        this.application = application;
    }

    @Override
    public boolean execute() {
        if (!isInputValid()) {
            return false;
        }

        // Height and width are swapped because the input is in lines.
        TerrainTile[][] tileArray = new TerrainTile[height][width];
        for (int h = 0; h < height; h++) {
            try {
                tileArray[h] = readTerrainLine();
            }
            catch (ShouldQuitException exception) {
                return false;
            }
            if (tileArray[h] == null) {
                return false;
            }
        }

        Terrain terrain = new Terrain(tileArray);
        if (!terrain.isValid()) {
            return false;
        }

        application.setTerrain(terrain);
        return true;
    }

    private boolean isInputValid() {
        // Check that there are exactly two arguments.
        if (arguments == null || arguments.length != 2) {
            return false;
        }
        // Check that the width and the heights are integers.
        try {
            // Sets the width and the height in case both strings can be converted to integers.
            width = Integer.parseInt(arguments[0]);
            height = Integer.parseInt(arguments[1]);
            if (width <= 0 || height <= 0) {
                return false;
            }
        }
        catch (NumberFormatException exception) {
            return false;
        }
        // If every check was successful returns true.
        return true;
    }

    private TerrainTile[] readTerrainLine() {
        String input = ioHandler.getNextLine();
        if (input == null) {
            throw new ShouldQuitException("The application should quit.");
        }
        if (input.length() != width) {
            ioHandler.error();
            System.out.println("Length error");
            return null;
        }
        LinkedList<TerrainTile> terrainTiles = new LinkedList<TerrainTile>();
        for (char tileAsChar: input.toCharArray()) {
            TerrainType terrainType = TerrainType.convertCharToTerrainType(tileAsChar);
            if (terrainType == TerrainType.ERROR) {
                System.out.println("Type error");
                return null;
            }
            terrainTiles.add(new TerrainTile(terrainType, tileAsChar));
        }
        return terrainTiles.toArray(new TerrainTile[0]);
    }

}
