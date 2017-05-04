package br.com.brunorozendo.view;

import br.com.brunorozendo.controller.Listener;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by bruno on 02/05/17.
 */
public class Layout extends Application {

    private Stage stage;
    private Scene scene;
    private Listener listener;
    private Label labelStatus;
    private ProgressBar statusbar;

    public void build(String ... args){
        launch(args);
    }

    @Override
    public void init() throws Exception {
        super.init();
        this.scene = new Scene(new Group());
        this.listener = new Listener(this);
        setLayout();
    }

    private void setWindows(){
        this.stage.setTitle("Converter  ISO-8859-1 to UTF-8 ");
        this.stage.setWidth(450);
        this.stage.setHeight(250);
        this.stage.setScene(this.scene);
        this.stage.setResizable(false);
        this.stage.show();
    }

    public void setLayout(){
        BorderPane border = new BorderPane();
        border.setTop(buildTop());
        border.setCenter(buildCenter());
        border.setBottom(buildBotton());
        ((Group) this.scene.getRoot()).getChildren().addAll(border);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        this.setWindows();
    }


    private VBox buildTop(){
        Label labelInputList = this.setLabelInputList();
        Button buttonInputList = this.setButtonInputList();
        Label labelInputDestiny = this.setLabelInputDestiny();
        Button buttonInputDestiny = this.setButtonInputDestiny();

        final GridPane inputGridPane = new GridPane();
        inputGridPane.setHgap(10);
        inputGridPane.setVgap(10);
        inputGridPane.setConstraints(labelInputList, 0, 0);
        inputGridPane.setConstraints(buttonInputList, 1, 0);
        inputGridPane.setConstraints(labelInputDestiny, 0, 2);
        inputGridPane.setConstraints(buttonInputDestiny, 1, 2);
        inputGridPane.getChildren().addAll(labelInputList, buttonInputList, labelInputDestiny, buttonInputDestiny);

        final VBox vbox = new VBox(12);
        vbox.getChildren().addAll(inputGridPane);
        vbox.setPadding(new Insets(12, 12, 12, 12));
        return vbox;
    }

    private VBox buildCenter(){
        Label labelTable = this.setLabelTable();
        ProgressIndicator status = this.setStatusBar();
        Label  statusConverter = this.setStatusConverter();

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(labelTable,status,statusConverter);

        return  vbox;
    }


    private VBox buildBotton(){
        Button buttonConverter = this.setButtonConverter();
        Button buttonClear = this.setButtonClear();


        final GridPane inputGridPane = new GridPane();
        inputGridPane.setHgap(10);
        inputGridPane.setVgap(10);
        inputGridPane.setConstraints(buttonConverter, 0, 0);
        inputGridPane.setConstraints(buttonClear, 1, 0);
        inputGridPane.getChildren().addAll(buttonConverter, buttonClear);

        final VBox vbox = new VBox(12);
        vbox.getChildren().addAll(inputGridPane);
        vbox.setPadding(new Insets(12, 12, 12, 12));

        return  vbox;
    }


    private Label setLabelInputList(){
        final Label label = new Label("Carregar arquivos:");
        label.setFont(new Font("Arial", 14));
        return label;
    }

    private Button setButtonInputList(){
        final Button openButton = new Button("Lista de arquivos (.txt)");
        openButton.setId("inputList");
        openButton.setOnAction(this.listener);
        return openButton;
    }

    private Label setLabelInputDestiny(){
        final Label labelDestiny = new Label("Destino:");
        labelDestiny.setFont(new Font("Arial", 14));
        return labelDestiny;
    }

    private Button setButtonInputDestiny(){
        final Button buttonDesntino = new Button("pasta de destino");
        buttonDesntino.setId("inputDestiny");
        buttonDesntino.setOnAction(this.listener);
        return buttonDesntino;
    }

    private Button setButtonConverter(){
        final Button buttonConverter = new Button("converter");
        buttonConverter.setId("inputConverter");
        buttonConverter.setOnAction(this.listener);
        return buttonConverter;
    }

    private Button setButtonClear(){
        final Button buttonConverter = new Button("Limpar");
        buttonConverter.setId("inputClear");
        buttonConverter.setOnAction(this.listener);
        return buttonConverter;
    }

    private Label setLabelTable(){
        final Label label = new Label("Status");
        label.setFont(new Font("Arial", 14));
        return label;
    }

    private Label setStatusConverter(){
        this.labelStatus = new Label("");
        labelStatus.setFont(new Font("Arial", 14));
        return labelStatus;
    }

    public ProgressBar setStatusBar(){
        this.statusbar = new ProgressBar();
        this.statusbar.setProgress(0.0);
        this.statusbar.setMinWidth(430);
        return this.statusbar;
    }

    public Stage getStage(){
        return  this.stage;
    }

    public void setLabelStatus(String value){
        this.labelStatus.setText(value);
    }

    public Label getLabelStatus(){
        return this.labelStatus;
    }

    public void setStatus(double value){
        this.statusbar.setProgress(value);
    }

    public ProgressBar getStatus(){
        return this.statusbar;
    }

}

