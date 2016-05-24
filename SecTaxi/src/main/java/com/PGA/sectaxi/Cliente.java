package com.PGA.sectaxi;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

import android.os.Environment;
import android.util.Log;

public class Cliente {


	private static double lat;
	private static double lon;
	static String temp;
	static int temp1 = 0;
	private static String tipo;
	private static String ende;
	private static String conteudo;
	static String temp2;

	public Cliente(double lat, double lon, String tipo, String ende) {
		Log.i("info","classe criada");
		Cliente.lat = lat;
		Cliente.lon = lon;
		Cliente.tipo = tipo;
		Cliente.ende = ende;
		Client();
	}
	public Cliente(String tipo, String ende) {
		Log.i("info","classe criada");
		Cliente.tipo = tipo;
		Cliente.ende = ende;
		Client();
	}
	public Cliente(String tipo, String ende, String conteudo) {
		Log.i("info","classe criada");
		Cliente.tipo = tipo;
		Cliente.ende = ende;
		Cliente.conteudo = conteudo;
		Client();
	}
	/**
	 * @param args
	 * @throws IOException 
	 */
	public void Client() {
		Socket socket;
		Socket socket1;
		try
		{
			socket1 = new Socket("10.0.2.2", 9998);  
			if(!socket1.isConnected())
				Log.i("infoserver", "Socket Connection Not established");
			else
				Log.i("infoserver", "Socket Connection established : "+socket1.getInetAddress());

			DataOutputStream outToServer = null;
			try {
				outToServer = new DataOutputStream(socket1.getOutputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			try {
				outToServer.write(tipo.getBytes());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			outToServer.close();
			Log.i("info","tipo enviado");

			temp = (String.valueOf(lat) + "," + String.valueOf(lon));
			temp2 = conteudo;
			generateNoteOnSD("gpscoor.txt", temp);
			generateNoteOnSD("trouble.txt", temp2);

			File myfile = new File(Environment.getExternalStorageDirectory() + ende);
			//"/Pictures/foto.jpg"
			//"/Notes/gpscoor.txt"
			//"/Notes/trouble.txt"
			Log.i("info","arquivo criado");
			socket = new Socket("10.0.2.2", 9999);
			if(!socket1.isConnected())
				Log.i("infoserver", "Socket Connection Not established");
			else
				Log.i("infoserver", "Socket Connection established : "+socket1.getInetAddress());

			byte[] byteArray = new byte[1024];
			FileInputStream fis = new FileInputStream(myfile);
			BufferedInputStream bis = new BufferedInputStream(fis);
			OutputStream os = socket.getOutputStream();
			int trxBytes =0;
			while((trxBytes = bis.read(byteArray, 0, byteArray.length)) !=-1)
			{           
				os.write(byteArray, 0, byteArray.length);
				Log.i("infoserver","Transfering bytes : "+trxBytes );
				temp1++;
			}
			os.flush();
			bis.close();
			socket.close();
			Log.i("info","arquivo enviado");
			Log.i("infoserver","filesize: "+temp1 );
			temp1 = 0;

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static void generateNoteOnSD(String sFileName, String sBody){
		try
		{
			File root = new File(Environment.getExternalStorageDirectory(), "Notes");
			if (!root.exists()) {
				root.mkdirs();
			}
			File gpxfile = new File(root, sFileName);
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(sBody);
			writer.flush();
			writer.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	} 
}
