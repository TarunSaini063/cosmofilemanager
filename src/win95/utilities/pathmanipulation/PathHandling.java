package win95.utilities.pathmanipulation;

import win95.constants.FileType;
import win95.constants.LogicConstants;
import win95.constants.Messages;
import win95.debug.LogsPrinter;
import win95.utilities.exceptions.InvalidPathException;
import win95.utilities.exceptions.UnknownFileMovement;

public class PathHandling {
    private  FileType type;
    private String path;
    public PathHandling(String path) {
        this.path = path;
    }

    public String getFixedPath(){
        String []pathNodes = path.split(FileSeparator.fileSeparator());
        StringBuilder res = new StringBuilder();
        int count = 0;
        for(String node : pathNodes){
            count++;
            if(node.contains(" ")){
                res.append("\"").append(node).append("\"");
            }else {
                res.append(node);
            }
            if(count != pathNodes.length) {
                res.append(FileSeparator.fileSeparator());
            }
        }
        return res.toString();
    }

    String cd(String path, LogicConstants movementType) throws InvalidPathException, UnknownFileMovement {
        PathValidity pathValidity = new PathValidity(path);
        if(!pathValidity.isValid()) {
            throw new InvalidPathException(Messages.INVALID_PATH + path);
        }

        if(movementType != LogicConstants.CD_BACK){
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

    String cd(String path,LogicConstants movementType,String nextNode) throws InvalidPathException, UnknownFileMovement {
        PathValidity pathValidity = new PathValidity(path);
        if(!pathValidity.isValid()) {
//            ExceptionPrinter.print("PathHandling",40,Messages.INVALID_PATH + path);
            throw new InvalidPathException(Messages.INVALID_PATH + path);
        }

        if(movementType != LogicConstants.CD_NEXT){
//            ExceptionPrinter.print("PathHandling",50,Messages.INVALID_PATH_MOVEMENT);
            throw new UnknownFileMovement(Messages.INVALID_PATH_MOVEMENT);
        }
        String newPath = path + FileSeparator.fileSeparator()+nextNode;

        LogsPrinter.printLogic("PathHandling",47,"new Path : "+newPath);
        pathValidity = new PathValidity(newPath);
        if(!pathValidity.isValid()) {
//            ExceptionPrinter.print("PathHandling",50,Messages.INVALID_PATH + newPath);
            throw new InvalidPathException(Messages.INVALID_PATH + newPath);
        }
        return newPath;
    }
}
