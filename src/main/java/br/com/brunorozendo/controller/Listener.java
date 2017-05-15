package br.com.brunorozendo.controller;

import br.com.brunorozendo.model.Item;
import br.com.brunorozendo.view.Layout;
import javafx.concurrent.WorkerStateEvent;
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

    private Layout layout;
    private ArrayList<Item> listaArquivos;
    private boolean destino = false;

    public Listener(Layout layout){
        this.layout = layout;
    }


    @Override
    public void handle(ActionEvent event) {
        String value = null;
        if(event.getSource() instanceof Button){
            value = ((Button)event.getSource()).getId();
        }
        if("inputListaArquivos".equals(value)){
            inputLista();
        }
        if("inputDestino".equals(value)){
            if(this.listaArquivos == null ){
                popUpLista();
                return;
            }
            inputDestino();
        }
        if("inputConverter".equals(value)){
            if(this.listaArquivos == null ){
                popUpLista();
                return;
            }
            if(!this.destino){
                popUpDestino();
                return;
            }
            layout.disableButtons();
            actionConverter();
        }
        if("inputLimpar".equals(value)){
            inputLimpar();
        }
    }

    private void inputLimpar() {
        this.listaArquivos = null;
        layout.getStatus().progressProperty().unbind();
        layout.setStatus(0.0);
        layout.getLabelStatus().textProperty().unbind();
        layout.setLabelStatus("");
    }

    private void actionConverter() {
        Converter converter = new Converter();
        converter.setListaArquivos(this.listaArquivos);

        converter.setOnSucceeded((WorkerStateEvent event2) -> {
            layout.getLabelStatus().textProperty().unbind();
            layout.setLabelStatus("Concluido");
            layout.enableButtons();
        });

        layout.getStatus().progressProperty().unbind();
        layout.getStatus().progressProperty().bind(converter.progressProperty());
        layout.getLabelStatus().textProperty().unbind();
        layout.getLabelStatus().textProperty().bind(converter.messageProperty());

        Thread th = new Thread(converter);
        th.start();
    }

    private void inputDestino() {
        final FileChooser fileChooserDestiny = new FileChooser();
        File file = fileChooserDestiny.showSaveDialog(this.layout.getStage());
        if (file != null) {
            this.listaArquivos.forEach(item -> {
                String d = file.toString();
                if(OsValidador.isWindows()){
                    d = d+item.getOrigin().replaceAll("^[A-Z0-9]*?:", "");
                }else{
                    d = d+"/"+item.getOrigin().replace("./", "");
                }
                item.setDestiny(d);
            });
        }
        this.destino = true;
    }

    private void popUpLista() {
        this.popUp("carregue primeiro a lista de arquivos.");
    }

    private void popUpDestino() {
        this.popUp("carregue a pasta de destino primeiro.");
    }

    private void popUp(String msg) {
        FlowPane popupFlowpane = new FlowPane();
        popupFlowpane.getChildren().addAll(new Text((msg)));
        Scene popupScene = new Scene(popupFlowpane, 400, 100);
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.APPLICATION_MODAL);
        popupStage.setScene(popupScene);
        popupStage.showAndWait();
    }

    private void inputLista() {
        final FileChooser fileChooser = new FileChooser();

        File file = fileChooser.showOpenDialog(layout.getStage());
        if (file != null) {
            setlistaArquivos(file);
            layout.setLabelStatus("0/"+this.listaArquivos.size());
        }
    }


    public void setlistaArquivos(File input) {
        String arquivo;
        BufferedReader listaBuffer = null;
        this.listaArquivos = new ArrayList<Item>();
        try {
            File lista = input;
            listaBuffer = new BufferedReader(new InputStreamReader(new FileInputStream(lista), Encode.ISO_8859_1));
            while ((arquivo = listaBuffer.readLine()) != null) {
                this.listaArquivos.add(new Item(arquivo));
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
