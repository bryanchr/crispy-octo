package com.example.lenovo.healthmax;

public class noteModel {
    public String noteTitle;
    public String noteTime;
    public String id;

    public noteModel(){

    }

    public noteModel(String noteTitle, String noteTime){
        this.noteTitle = noteTitle;
        this.noteTime = noteTime;
    }

    public String getNoteTitle(){
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteTime(){
        return noteTime;
    }

    public void setNoteTime(String noteTime) {
        this.noteTime = noteTime;
    }
}
