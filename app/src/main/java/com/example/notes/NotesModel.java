package com.example.notes;

public class NotesModel {
    private String tittle;
    private String note;

    public NotesModel(String tittle, String note) {
        this.tittle = tittle;
        this.note = note;
    }

    public NotesModel() {
    }

    public String getTittle() {
        return tittle;
    }

    public void setTittle(String tittle) {
        this.tittle = tittle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
