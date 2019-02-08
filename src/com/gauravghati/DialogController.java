package com.gauravghati;

import com.gauravghati.dataModels.ToDoData;
import com.gauravghati.dataModels.ToDoItems;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextField shortDescription;
    @FXML
    private TextArea detailsArea;
    @FXML
    private DatePicker deadlinePicker;

    public ToDoItems processResults(){
        String shortDescriptionText = shortDescription.getText().trim();
        String detailAreaText = detailsArea.getText().trim();
        LocalDate deadlineDate = deadlinePicker.getValue();
        ToDoItems newtoDoItems = new ToDoItems(shortDescriptionText,detailAreaText,deadlineDate);
        ToDoData.getInstance().addToDoItems(newtoDoItems);
        return newtoDoItems;
    }
}