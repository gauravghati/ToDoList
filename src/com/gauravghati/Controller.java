package com.gauravghati;

import com.gauravghati.dataModels.ToDoData;
import com.gauravghati.dataModels.ToDoItems;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;


public class Controller {

    @FXML
    private ListView<ToDoItems> toDoListView;
    @FXML
    private TextArea detailsTextArea;
    @FXML
    private Label dueDateText;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ContextMenu listMenuItem;
    @FXML
    private Button deleteButton;
    @FXML
    private ToggleButton toggleButton;

    private FilteredList<ToDoItems> filteredList;
    private Predicate<ToDoItems> wantaAllItems;
    private Predicate<ToDoItems> wantTodaysItem;

    public void initialize() {
//        ToDoItems item1 = new ToDoItems("Mail BirthDay Card","Buy a 30th anniversary Card of john.",
//                LocalDate.of(2016,Month.APRIL,25));
//        ToDoItems item2 = new ToDoItems("Doctors Appointment","See doctor Smith bring slip",
//                LocalDate.of(2016,Month.MAY,23));
//        ToDoItems item3 = new ToDoItems("Finish design proposal","Asap",
//                LocalDate.of(2016,Month.APRIL,22));
//        ToDoItems item4 = new ToDoItems("MPickup from train station","Sister arriving",
//                LocalDate.of(2016,Month.MARCH,25));
//        ToDoItems item5 = new ToDoItems("Dry Cleaning of cloths","Cloths Should be ready on that time.",
//                LocalDate.of(2016,Month.APRIL,25));

//        list.add(item1);
//        list.add(item2);
//        list.add(item3);
//        list.add(item4);
//        list.add(item5);
//
//        ToDoData.getInstance().setToDoItem(list);


        listMenuItem = new ContextMenu();

        MenuItem deleteMenuItem = new MenuItem("Delete");
        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ToDoItems items = toDoListView.getSelectionModel().getSelectedItem();
                deleteItem(items);
            }
        });

        listMenuItem.getItems().addAll(deleteMenuItem);
        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItems>() {
            @Override
            public void changed(ObservableValue<? extends ToDoItems> observable, ToDoItems oldValue, ToDoItems newValue) {
                if(newValue!=null) {
                    ToDoItems item = toDoListView.getSelectionModel().getSelectedItem();
                    detailsTextArea.setText(item.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                    dueDateText.setText(df.format(item.getDeadline()));
                }
            }
        });

        wantaAllItems = new Predicate<ToDoItems>() {
            @Override
            public boolean test(ToDoItems items) {
                return true;
            }
        };

        wantTodaysItem = new Predicate<ToDoItems>() {
            @Override
            public boolean test(ToDoItems items) {
                return items.getDeadline().equals(LocalDate.now());
            }
        };

        filteredList = new FilteredList<>(ToDoData.getInstance().getToDoItem(),wantaAllItems);


        SortedList<ToDoItems> sortedList = new SortedList<>(filteredList, new Comparator<ToDoItems>() {
            @Override
            public int compare(ToDoItems o1, ToDoItems o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });


//        toDoListView.setItems(ToDoData.getInstance().getToDoItem());
        toDoListView.setItems(sortedList);
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

                cell.emptyProperty().addListener(
                        (obs,wasEmpty,isNowEmpty) -> {
                            if(isNowEmpty){
                                cell.setContextMenu(null);
                            }else{
                                cell.setContextMenu(listMenuItem);
                            }
                        }
                );
                return cell;
            }
        });
    }

    @FXML
    public void showNewItemDialog(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Add new to Do Items");
        dialog.setHeaderText("Use this Dialog as a Header Text.");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("toDoItemDialogue.fxml"));
        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("Couldn't load the Dialog");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            DialogController controller = fxmlLoader.getController();
            ToDoItems newItem = controller.processResults();
            toDoListView.getSelectionModel().select(newItem);
            System.out.println("OK pressed !");
        }else{
            System.out.println("Cancel pressed !");
        }
    }

    @FXML
    public void editItem(){
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Edit To Do List");
        dialog.setHeaderText("Select Item and edit them");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editList.fxml"));

        try{
            dialog.getDialogPane().setContent(fxmlLoader.load());
        }catch(IOException e){
            System.out.println("Couldn't load the Dialog");
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if(result.isPresent() && result.get()==ButtonType.OK){
            EditList controller = fxmlLoader.getController();
            ToDoItems newItem = controller.processResults();
            toDoListView.getSelectionModel().select(newItem);
            System.out.println("OK pressed !");
        }else{
            System.out.println("Cancel pressed !");
        }
    }

    @FXML
    public void deleteItem(ToDoItems items){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Todo item");
        alert.setHeaderText(""+items.getShortDescription());
        alert.setContentText("Are you Sure? ");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get()==(ButtonType.OK)){
             ToDoData.getInstance().deleteToDoItem(items);
        }
    }

    @FXML
    public void deleteButtonHandler(ActionEvent event){
        if(event.getSource().equals(deleteButton)) {
            ToDoItems item = toDoListView.getSelectionModel().getSelectedItem();
            deleteItem(item);
        }
    }

    @FXML
    public void handleKeyPressed(KeyEvent event){

        ToDoItems item = toDoListView.getSelectionModel().getSelectedItem();

        if(event.getCode().equals(KeyCode.DELETE) && item!=null) {
            deleteItem(item);
        }
    }

    @FXML
    public void setToggleButton(){
        ToDoItems selectedItem = toDoListView.getSelectionModel().getSelectedItem();
         if(toggleButton.isSelected()){
             filteredList.setPredicate(wantTodaysItem);
             toDoListView.getSelectionModel().select(selectedItem);
         }else{
             filteredList.setPredicate(wantaAllItems);
             if(filteredList.isEmpty()){
                 detailsTextArea.clear();
                 dueDateText.setText("");
             }else if(filteredList.contains(selectedItem)){
                 toDoListView.getSelectionModel().select(selectedItem);
             }else{
                 toDoListView.getSelectionModel().selectFirst();
             }
         }
    }

    public void handlerExit(){
        Platform.exit();
    }

//    we can use Object instead of using event handler
//    @FXML
//    public void handleClickListView(){
//        ToDoItems item =  toDoListView.getSelectionModel().getSelectedItem();
//        detailsTextArea.setText(item.getDetails());
//        dueDateText.setText(item.getDeadline().toString());
//        System.out.println("The Selected item is :"+item);
//        StringBuilder s = new StringBuilder(item.getDetails());
//        s.append("\n\n\n\n");
//     s.append("Due  : ");
//        s.append(item.getDeadline());
//    }


}
