package win95.utilities.pathmanipulation;

import win95.constants.DirectoryMovement;
import win95.constants.FileType;
import win95.constants.Messages;
import win95.utilities.exceptions.InvalidPathException;
import win95.utilities.exceptions.UnknownFileMovement;
import win95.debug.LogsPrinter;

public class PathHandling {
    private static FileType type;

    public PathHandling(String path) {

    }

    String cd(String path, DirectoryMovement movementType) throws InvalidPathException, UnknownFileMovement {
        PathValidity pathValidity = new PathValidity(path);
        if(!pathValidity.isValid()) {
            throw new InvalidPathException(Messages.INVALID_PATH + path);
        }

        if(movementType != DirectoryMovement.CD_BACK){
            throw new UnknownFileMovement(Messages.INVALID_PATH_MOVEMENT);
        }
        String []nodes = path.split(FileSeparator.fileSeparator());
        if(nodes.length == 1){
            throw new UnknownFileMovement(Messages.INVALID_PATH_MOVEMENT);
        }
        StringBuilder newPathStringBuilder = new StringBuilder() ;
        for(int i = 0; i < nodes.length-1; i++){
            newPathStringBuilder.append(nodes[i]);
        }
        return newPathStringBuilder.toString();
    }

    String cd(String path,DirectoryMovement movementType,String nextNode) throws InvalidPathException, UnknownFileMovement {
        PathValidity pathValidity = new PathValidity(path);
        if(!pathValidity.isValid()) {
            throw new InvalidPathException(Messages.INVALID_PATH + path);
        }

        if(movementType != DirectoryMovement.CD_NEXT){
            throw new UnknownFileMovement(Messages.INVALID_PATH_MOVEMENT);
        }
        String newPath = path + FileSeparator.fileSeparator()+nextNode;

        LogsPrinter.printLogic("PathHandling",47,"new Path : "+newPath);

        return newPath;
    }
}
