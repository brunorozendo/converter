package br.com.brunorozendo.controller;

import br.com.brunorozendo.model.Item;
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
				try {
					String[] split = {};
					if(OsValidador.isWindows()){
						split = destiny.split("\\\\");
					}else{
						split = destiny.split(System.getProperty("file.separator"));
					}

					split[split.length - 1] = "";
					String completeSentance = "";
					int a = 0;
					int size = split.length;
					for (String s : split) {
						a++;
						if (a != size){
							completeSentance += s + System.getProperty("file.separator");
						}else{
							completeSentance += s;
						}

					}
					new File(completeSentance).mkdirs();
				}
				catch (PatternSyntaxException ex) {ex.printStackTrace();}
				catch (IllegalArgumentException ex) {ex.printStackTrace();}
				catch (IndexOutOfBoundsException ex) {ex.printStackTrace();}

				inISO = new BufferedReader(new InputStreamReader(new FileInputStream(fileOrigin), Encode.ISO_8859_1));

				String strISO;
				String stringData = null;

				int linha = 0;
				while ((strISO = inISO.readLine()) != null) {
					if(linha > 0){
						stringData += System.getProperty("line.separator");
					}

					byte[] bytesISO = strISO.getBytes(Encode.ISO_8859_1);
					if(charByChar){
						for (int j = 0; j < bytesISO.length; j++) {

							byte[] caracter = { bytesISO[j] };
							String stringCaracterISO = new String(caracter, Encode.ISO_8859_1);
							String htmlCaracter = StringEscapeUtils.escapeHtml4(stringCaracterISO);
							byte[] bytesCaracter = htmlCaracter.getBytes(Encode.ISO_8859_1);
							String stringCaracterUTF = new String(bytesCaracter, Encode.UTF_8);
							stringCaracterUTF = StringEscapeUtils.unescapeHtml4(stringCaracterUTF);

							byte[] bytesU = stringCaracterUTF.getBytes(Encode.UTF_8);

							if (stringData == null) {
								stringData = new String(bytesU, Encode.UTF_8);
							} else {
								stringData += new String(bytesU, Encode.UTF_8);
							}
						}
					}else{
						//linha por linha
						String stringCaracterISO = new String(bytesISO, Encode.ISO_8859_1);
						String htmlCaracter = StringEscapeUtils.escapeHtml4(stringCaracterISO);
						byte[] bytesCaracter = htmlCaracter.getBytes(Encode.ISO_8859_1);
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
					writerDestino.write(stringData);
				} catch (Exception e) {
					e.printStackTrace();
				}finally {
					writerDestino.close();
				}
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				inISO.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}



	@Override
	protected Void call() throws Exception {
		int i = 0;
		for (Item item : this.lista){
			++i;
			try {
				this.converter(item);
				this.updateProgress(i, total);
				this.updateMessage(i+"/"+total);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void setListaArquivos(ArrayList<Item> lista ){
		this.lista = lista;
		this.total = this.lista.size();
	}

}
