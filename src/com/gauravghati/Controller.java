package com.gauravghati;

import com.gauravghati.dataModels.ToDoData;
import com.gauravghati.dataModels.ToDoItems;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


public class Controller {

    private List<ToDoItems> list = new ArrayList<>();
    @FXML
    private ListView<ToDoItems> toDoListView;
    @FXML
    private TextArea detailsTextArea;
    @FXML
    private Label dueDateText;

    public void initialize(){
//        ToDoItems item1 = new ToDoItems("Mail BirthDay Card","Buy a 30th anniversary Card of john.",
//                LocalDate.of(2016,Month.APRIL,25));
//        ToDoItems item2 = new ToDoItems("Doctors Appoinment","See doctor Smith bring slip",
//                LocalDate.of(2016,Month.MAY,23));
//        ToDoItems item3 = new ToDoItems("Finish desing proposal","Asap",
//                LocalDate.of(2016,Month.APRIL,22));
//        ToDoItems item4 = new ToDoItems("MPickup from train station","Sister arriving",
//                LocalDate.of(2016,Month.MARCH,25));
//        ToDoItems item5 = new ToDoItems("Dry Cleaning of cloths","Cloths Should be ready on that time.",
//                LocalDate.of(2016,Month.APRIL,25));
//
//        list.add(item1);
//        list.add(item2);
//        list.add(item3);
//        list.add(item4);
//        list.add(item5);
//        ToDoData.getInstance().setToDoItem(list);



        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItems>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItems> observable, ToDoItems oldValue, ToDoItems newValue) {
                ToDoItems item = toDoListView.getSelectionModel().getSelectedItem();
                detailsTextArea.setText(item.getDetails());
                DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                dueDateText.setText(df.format(item.getDeadline()));
            }
        });

        toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItem());
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();
    }

//    @FXML
//    public void handleClickListView(){
//        ToDoItems item =  toDoListView.getSelectionModel().getSelectedItem();
//        detailsTextArea.setText(item.getDetails());
//        dueDateText.setText(item.getDeaaline().toString());
//        System.out.println("The Selected item is :"+item);
//        StringBuilder s = new StringBuilder(item.getDetails());
//        s.append("\n\n\n\n");
//     s.append("Due  : ");
//        s.append(item.getDeaaline());
//    }


}
