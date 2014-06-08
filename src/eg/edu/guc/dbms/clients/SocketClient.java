package eg.edu.guc.dbms.clients;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import eg.edu.guc.dbms.components.TransactionManagerImpl;
import eg.edu.guc.dbms.factories.TransactionManagerFactory;
import eg.edu.guc.dbms.interfaces.TransactionCallbackInterface;

public class SocketClient {

	
	public static void main(String[] args) throws IOException {
		
		ServerSocket server = new ServerSocket(2698);
		while (true) {
			Socket socket = server.accept();
			TransactionManagerImpl transaction = (TransactionManagerImpl) TransactionManagerFactory.getInstance();
			final Client client = new Client(socket, transaction);
			transaction.setCallBack(new TransactionCallbackInterface() {
			
				@Override
				public void onPostExecute(List<HashMap<String, String>> results) {
				for (HashMap<String, String> h : results) {
					client.write(h.toString());
				}
			}
			});
			client.start();
		}
		

	}

}

