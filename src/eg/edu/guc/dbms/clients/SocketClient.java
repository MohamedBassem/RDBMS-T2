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
			TransactionManagerImpl transaction = null;
			final Client client = new Client(socket, transaction);
			TransactionManagerImpl.setCallBack(new TransactionCallbackInterface() {
				@Override
				public void onPostExecute(List<HashMap<String, String>> results) {
				if (results == null) {
					client.write("OK");
					return;
				}
				for (HashMap<String, String> h : results) {
					client.write(h.toString());
				}
			}
			});
			client.start();
		}
		

	}

}

