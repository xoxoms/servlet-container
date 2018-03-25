import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by ms on 2018. 3. 25..
 */

/**
 * 헤더와 바디의 개행을 구분할 수 없는 서버
 */
public class Server_0 {
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {
        Server_0 server = new Server_0();
        server.boot();
    }

    private void boot() throws IOException {
        serverSocket = new ServerSocket(8000);
        Socket socket = serverSocket.accept();
        InputStream inputStream = socket.getInputStream();
        OutputStream out = socket.getOutputStream();
        int oneInt = -1;
        while (-1 != (oneInt = inputStream.read())) {
            System.out.println((char) oneInt);
        }
        out.close();
        inputStream.close();
        socket.close();
    }
}
