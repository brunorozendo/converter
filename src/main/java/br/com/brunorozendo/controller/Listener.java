package br.com.brunorozendo.controller;

import br.com.brunorozendo.model.Item;
import br.com.brunorozendo.view.Layout;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class Listener implements EventHandler<ActionEvent> {

    private Layout pai;
    private ArrayList<Item> listFiles;

    public Listener(Layout pai){
        this.pai = pai;
    }


    @Override
    public void handle(ActionEvent event) {

        String value = null;
        if(event.getSource() instanceof Button){
            value = ((Button)event.getSource()).getId();

        }

        if("inputList".equals(value)){
            final FileChooser fileChooser = new FileChooser();

            File file = fileChooser.showOpenDialog(pai.getStage());
            if (file != null) {
                setListFiles(file);
                pai.setLabelStatus("0/"+this.listFiles.size());
            }
        }



        if("inputDestiny".equals(value)){
            final FileChooser fileChooserDestiny = new FileChooser();
            if(this.listFiles == null ){
                FlowPane popupFlowpane = new FlowPane();
                popupFlowpane.getChildren().addAll(new Text(("carregue primeiro a lista de arquivos.")));
                Scene popupScene = new Scene(popupFlowpane, 400, 100);
                Stage popupStage = new Stage();
                popupStage.initModality(Modality.APPLICATION_MODAL);
                popupStage.setScene(popupScene);
                popupStage.showAndWait();
                return;
            }
            File file = fileChooserDestiny.showOpenDialog(this.pai.getStage());
            if (file != null) {
                this.listFiles.forEach(item -> item.setDestiny(file.toString()+item.getOrigin().replace("./", "")));
            }
        }


        if("inputConverter".equals(value)){
            int total = listFiles.size();
            Converter converter = new Converter();
            pai.getStatus().progressProperty().unbind();
            pai.getStatus().progressProperty().bind(converter.progressProperty());
            //pai.getLabelStatus().textProperty().unbind();
            //pai.getLabelStatus().textProperty().bind(converter.messageProperty());
            int i = 0;
            for (Item item : listFiles){
                ++i;
                converter.setTotal(total);
                converter.setItem(item, i);
                try {
                    converter.call();
                    pai.setLabelStatus(i+"/"+total);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if("inputClear".equals(value)){
            pai.getStatus().progressProperty().unbind();
            pai.setStatus(0.0);
            //pai.getLabelStatus().textProperty().unbind();
            pai.setLabelStatus("");
        }
    }


    public void setListFiles(File input) {
        String arquivo;
        BufferedReader listaBuffer = null;
        this.listFiles = new ArrayList<Item>();
        try {
            File lista = input;
            listaBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(lista), Encode.ISO_8859_1));
            while ((arquivo = listaBuffer.readLine()) != null) {
                this.listFiles.add(new Item(arquivo));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                listaBuffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
