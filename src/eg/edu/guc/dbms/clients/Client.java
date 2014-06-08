package eg.edu.guc.dbms.clients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import eg.edu.guc.dbms.components.TransactionManagerImpl;
import eg.edu.guc.dbms.interfaces.TransactionManager;

public class Client extends Thread {
	
	private Socket socket;
	private BufferedReader br;
	private PrintWriter pw;
	private TransactionManagerImpl transaction;
	
	public Client(Socket socket, TransactionManagerImpl transaction) {
		this.socket = socket;
		this.transaction = transaction;
		try {
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		read();
	}
	
	
	public void write(String msg) {
		pw.println(msg);
		pw.flush();
	}
	
	public String read() {
		String result = "";
		String line = "";
		try {
			while (true) {
				line = br.readLine();
				while (!line.toLowerCase().equals("end")) {
				result += line;
				line = br.readLine();
				}
				transaction.runConcurrently(result);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
	
}
