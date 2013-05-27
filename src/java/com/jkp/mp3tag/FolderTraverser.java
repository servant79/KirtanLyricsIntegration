package com.jkp.mp3tag;

import java.io.File;

import com.jkp.mp3tag.command.Command;

public class FolderTraverser {

    private File fileObject;

    private static int depth = 1000;
    public FolderTraverser(File fileObject)
    {
        this.fileObject = fileObject;
    }

    public void traverse(Command command)
    {
        recursiveTraversal(fileObject,command);
    }


    public void recursiveTraversal(File fileObject,Command command){		
        if (fileObject.isDirectory()){

            File allFiles[] = fileObject.listFiles();
            for(File aFile : allFiles){
                recursiveTraversal(aFile,command);
            }
        }else if (fileObject.isFile()){
        	command.execute(fileObject);
        }		
    }

}