package com.example.owlmusicplayer;

public class musicinf {
    public String filename;
    public String filepath;

    musicinf(){

    }

    musicinf(String filename,String filepath){
        this.filename=filename;
        this.filepath=filepath;

    }

    public String getFilename() {
        return filename;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }
}
