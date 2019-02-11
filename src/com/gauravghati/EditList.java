package com.gauravghati;

import com.gauravghati.dataModels.ToDoData;
import com.gauravghati.dataModels.ToDoItems;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class EditList {
    @FXML
    private TextField shortDescription;
    @FXML
    private DatePicker deadlinePicker;
    @FXML
    private TextArea detailsArea;
    @FXML
    private ListView<ToDoItems> toDoListView;
    @FXML
    private Label prevDate;

    public void initialize(){
        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItems>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItems> observable, ToDoItems oldValue, ToDoItems newValue) {
                if(newValue!=null) {
                    ToDoItems item = toDoListView.getSelectionModel().getSelectedItem();
                    shortDescription.setText(item.getShortDescription());
                    detailsArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    prevDate.setText(df.format(item.getDeadline()));
                }
            }
        });

        toDoListView.setItems(ToDoData.getInstance().getToDoItem());
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        toDoListView.getSelectionModel().selectFirst();
        toDoListView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<ToDoItems> call(ListView<ToDoItems> param) {
                ListCell<ToDoItems> cell = new ListCell<>(){
                    @Override
                    protected void updateItem(ToDoItems item, boolean empty) {
                        super.updateItem(item, empty);
                        if(empty){
                            setText(null);
                        }else{
                            setText(item.getShortDescription());
                            if(item.getDeadline().isBefore(LocalDate.now().plusDays(1))){
                                setTextFill(Color.RED);
                            }else if(item.getDeadline().equals(LocalDate.now().plusDays(1))){
                                setTextFill(Color.GREEN);
                            }
                        }
                    }
                };
                return cell;
            }
        });

    }

    public ToDoItems processResults(){
        String shortDescriptionText = shortDescription.getText().trim();
        String detailAreaText = detailsArea.getText().trim();
        LocalDate deadlineDate = deadlinePicker.getValue();

        toDoListView.getSelectionModel().getSelectedItem().setShortDescription(shortDescriptionText);
        toDoListView.getSelectionModel().getSelectedItem().setDetails(detailAreaText);
        if(!deadlineDate.toString().trim().isEmpty())
            toDoListView.getSelectionModel().getSelectedItem().setDeadline(deadlineDate);
        return toDoListView.getSelectionModel().getSelectedItem();
    }

}
