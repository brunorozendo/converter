package br.com.brunorozendo.controller;

import br.com.brunorozendo.model.Item;
import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.regex.PatternSyntaxException;

public class Converter extends Task<Void> {

    private int total;
    ArrayList<Item> lista;
    private boolean charByChar = false;


    public void converter(Item item) {
        BufferedReader inISO = null;

        try {
            String arquivo = item.getOrigin();
            String origin = arquivo.replace("./", "");
            String destiny = item.getDestiny();
            File fileOrigin = new File(origin);

            if(fileOrigin.isFile()){

                setPathDestiny(destiny);
                String encode = getEncode(fileOrigin);
                inISO = new BufferedReader(new InputStreamReader(new FileInputStream(fileOrigin), encode));
                String strISO;
                String stringData = null;

                int linha = 0;
                while ((strISO = inISO.readLine()) != null) {
                    if (linha > 0) {
                        stringData += System.getProperty("line.separator");
                    }

                    byte[] bytesISO = strISO.getBytes(encode);
                    if (charByChar) {
                        for (int j = 0; j < bytesISO.length; j++) {

                            byte[] caracter = {bytesISO[j]};
                            String stringCaracterISO = new String(caracter, encode);
                            String htmlCaracter = StringEscapeUtils.escapeHtml4(stringCaracterISO);
                            byte[] bytesCaracter = htmlCaracter.getBytes(encode);
                            String stringCaracterUTF = new String(bytesCaracter, Encode.UTF_8);
                            stringCaracterUTF = StringEscapeUtils.unescapeHtml4(stringCaracterUTF);

                            byte[] bytesU = stringCaracterUTF.getBytes(Encode.UTF_8);

                            if (stringData == null) {
                                stringData = new String(bytesU, Encode.UTF_8);
                            } else {
                                stringData += new String(bytesU, Encode.UTF_8);
                            }
                        }
                    } else {
                        //linha por linha
                        String stringCaracterISO = new String(bytesISO, encode);
                        String htmlCaracter = StringEscapeUtils.escapeHtml4(stringCaracterISO);
                        byte[] bytesCaracter = htmlCaracter.getBytes(encode);
                        String stringCaracterUTF = new String(bytesCaracter, Encode.UTF_8);
                        stringCaracterUTF = StringEscapeUtils.unescapeHtml4(stringCaracterUTF);

                        byte[] bytesU = stringCaracterUTF.getBytes(Encode.UTF_8);

                        if (stringData == null) {
                            stringData = new String(bytesU, Encode.UTF_8);
                        } else {
                            stringData += new String(bytesU, Encode.UTF_8);
                        }
                    }


                    linha++;
                }
                Writer writerDestino = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destiny), Encode.UTF_8));
                try {
                    if (stringData != null) {
                        writerDestino.write(stringData);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    writerDestino.close();
                }
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if(inISO != null){
                    inISO.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPathDestiny(String destiny) {
        try {
            String[] split = {};
            if (OsValidador.isWindows()) {
                split = destiny.split("\\\\");
            } else {
                split = destiny.split(System.getProperty("file.separator"));
            }

            split[split.length - 1] = "";
            String completeSentance = "";
            int a = 0;
            int size = split.length;
            for (int i = 0 ; i < size ; i++ ){
                if (i != size) {
                    completeSentance += split[i] + System.getProperty("file.separator");
                } else {
                    completeSentance += split[i] ;
                }
            }
            new File(completeSentance).mkdirs();
        } catch (PatternSyntaxException ex) {
            ex.printStackTrace();
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    private String getEncode(File fileOrigin){
        String encode = null;
        BufferedInputStream buffer = null;
        try {
            InputStream input = new FileInputStream(fileOrigin);
            buffer = new BufferedInputStream(input);

            CharsetDetector detector = new CharsetDetector();
            detector.setDeclaredEncoding(Encode.ISO_8859_1);
            detector.setText(buffer);
            CharsetMatch[] matches = detector.detectAll() ;

            if(matches == null || (matches != null && matches.length == 0) ){
                throw new Exception("não  foi  possivel identificar a codificaçãop do arquivo");
            }
            /*for (int i = 0; i < matches.length ; i++) {
                System.out.println(matches[i].getName() );
            }*/
            encode = matches[0].getName();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                buffer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return encode;
    }


    @Override
    protected Void call() throws Exception {
        int i = 0;
        for (Item item : this.lista) {
            ++i;
            try {
                this.converter(item);
                this.updateProgress(i, total);
                this.updateMessage(i + "/" + total);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void setListaArquivos(ArrayList<Item> lista) {
        this.lista = lista;
        this.total = this.lista.size();
    }

}
