package br.com.brunorozendo.controller;

import br.com.brunorozendo.model.Item;
import javafx.concurrent.Task;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.*;
import java.util.regex.PatternSyntaxException;

public class Converter extends Task<Boolean> {

	private Item item;
	private int total, contador;
	private boolean charByChar = false;


	public Boolean converter(Item item, int i) {
		Boolean status = Boolean.FALSE;
		BufferedReader inISO = null;

		try {
				String arquivo = item.getOrigin();

				String origin = arquivo.replace("./", "");

				String destiny = item.getDestiny();

				File fileOrigin = new File(origin);

				try {
					String[] split = destiny.split("/");
					split[split.length - 1] = "";
					String completeSentance = "";
					for (String s : split) {
						completeSentance += s + "/";
					}
					new File(completeSentance).mkdirs();
				}
				catch (PatternSyntaxException ex) {}
				catch (IllegalArgumentException ex) {}
				catch (IndexOutOfBoundsException ex) {}

				inISO = new BufferedReader(new InputStreamReader(new FileInputStream(fileOrigin), Encode.ISO_8859_1));

				String strISO;
				String stringdata = null;

				int linha = 0;
				while ((strISO = inISO.readLine()) != null) {
					if(linha > 0){
						stringdata += "\n";
					}

					byte[] bytesISO = strISO.getBytes(Encode.ISO_8859_1);
					if(charByChar){
						// caracter by caracter
						for (int j = 0; j < bytesISO.length; j++) {

							byte[] caracter = { bytesISO[j] };
							String stringCaracterISO = new String(caracter, Encode.ISO_8859_1);
							String htmlCaracter = StringEscapeUtils.escapeHtml4(stringCaracterISO);
							byte[] bytesCaracter = htmlCaracter.getBytes(Encode.ISO_8859_1);
							String stringCaracterUTF = new String(bytesCaracter, Encode.UTF_8);
							stringCaracterUTF = StringEscapeUtils.unescapeHtml4(stringCaracterUTF);

							byte[] bytesU = stringCaracterUTF.getBytes(Encode.UTF_8);

							if (stringdata == null) {
								stringdata = new String(bytesU, Encode.UTF_8);
							} else {
								stringdata += new String(bytesU, Encode.UTF_8);
							}
						}
					}else{
						//line by line
						String stringCaracterISO = new String(bytesISO, Encode.ISO_8859_1);
						String htmlCaracter = StringEscapeUtils.escapeHtml4(stringCaracterISO);
						byte[] bytesCaracter = htmlCaracter.getBytes(Encode.ISO_8859_1);
						String stringCaracterUTF = new String(bytesCaracter, Encode.UTF_8);
						stringCaracterUTF = StringEscapeUtils.unescapeHtml4(stringCaracterUTF);

						byte[] bytesU = stringCaracterUTF.getBytes(Encode.UTF_8);

						if (stringdata == null) {
							stringdata = new String(bytesU, Encode.UTF_8);
						} else {
							stringdata += new String(bytesU, Encode.UTF_8);
						}
					}


					linha++;
				}
				Writer myWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destiny), Encode.UTF_8));
				try {
					myWriter.write(stringdata);
				} catch (Exception e) {
					e.getCause();
				}finally {
					myWriter.close();
				}
				this.updateProgress(i, total);
				this.updateMessage(item.getDestiny());
				status = Boolean.TRUE;
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
		return status;
	}

	public void setItem(Item item, int i){
		this.item = item;
		this.contador = i;
	}

	@Override
	protected Boolean call() throws Exception {
		return this.converter(this.item, this.contador);
	}

	public void setTotal(int t){
		this.total = t;
	}
}
