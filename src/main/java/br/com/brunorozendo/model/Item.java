package br.com.brunorozendo.model;


/**
 * Created by bruno on 02/05/17.
 */
public class Item {

    private String origin;
    private String destiny;

    public Item(String origin){
        this.origin = origin;
    }
    public Item(String origin, String destiny){
        this.origin = origin;
        this.destiny = destiny;
    }


    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestiny() {
        return destiny;
    }

    public void setDestiny(String destiny) {
        this.destiny = destiny;
    }
}
