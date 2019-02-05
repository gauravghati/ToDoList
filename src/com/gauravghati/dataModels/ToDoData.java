package com.gauravghati.dataModels;

import javafx.collections.FXCollections;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

public class ToDoData {

    private static ToDoData instance = new ToDoData();
    private static String fileName = "ToDoList.txt";

    private List<ToDoItems> toDoItem;
    private DateTimeFormatter formatter;

    public static ToDoData getInstance(){
        return instance;
    }

    private ToDoData(){
        formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    }

    public List<ToDoItems> getToDoItem() {
        return this.toDoItem;
    }

//    public void setToDoItem(List<ToDoItems> toDoItem) {
//        this.toDoItem = toDoItem;
//    }


    public void loadToDOItems() throws IOException{
        toDoItem = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while((input = br.readLine())!=null){
                String[] itemsPieces = input.split("\t");
                String shortDescription = itemsPieces[0];
                String details = itemsPieces[1];
                String dateString = itemsPieces[2];

                LocalDate date = LocalDate.parse(dateString,formatter);
                ToDoItems toDoItems = new ToDoItems(shortDescription, details, date);
                toDoItem.add(toDoItems);
            }
        }finally {
            if(br != null){
                br.close();
            }
        }
    }

    public void storeToDoItems() throws IOException{
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<ToDoItems> iter = toDoItem.iterator();
            while(iter.hasNext()) {
                ToDoItems item = iter.next();
                bw.write(String.format("%s\t%s\t%s",
                        item.getShortDescription(),
                        item.getDetails(),
                        item.getDeadline().format(formatter)));
                bw.newLine();
            }

        } finally {
            if(bw != null) {
                bw.close();
            }
        }
    }










}
