import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class CreatorServer {

    Socket outsocket;
    PrintWriter printWriter;

    public CreatorServer(int portNum) throws IOException {
        outsocket = new Socket("localhost", portNum);
        printWriter = new PrintWriter(outsocket.getOutputStream());
        System.out.println("Your address: " + InetAddress.getLocalHost());
    }

    public void sendSignal(String value) {
        printWriter.println(value);
        printWriter.flush();
    }


}
