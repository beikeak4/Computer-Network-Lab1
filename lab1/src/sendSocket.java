import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class sendSocket {
    public Socket socketReturn;
    public OutputStream outputReturn;
    public InputStream inputReturn;

    public void sendSocket(String destHost, int destPort) throws IOException {
        this.socketReturn = new Socket(destHost, destPort);
        this.outputReturn = socketReturn.getOutputStream();
        this.inputReturn = socketReturn.getInputStream();
    }


    public void sendCacheSocket(String destHost, int destPort, String URL, File file)
        throws IOException {
        Scanner sc = new Scanner(new FileReader(file));
        String date = null;
        String line;
        while(sc.hasNextLine()) {
            line = sc.nextLine();
            if (line.contains("Date")) {
                date = line.substring(6);
            }
        };

        StringBuffer requestBuffer = new StringBuffer();
        requestBuffer.append("GET " + URL + " HTTP/1.1\r\n");
        requestBuffer.append("Host: " + destHost + ":" + destPort + "\r\n");
        requestBuffer.append("If-modified-since: " + date + "\r\n");
        requestBuffer.append("\r\n");
        String request = requestBuffer.toString();
        this.outputReturn.write(request.getBytes());
        this.outputReturn.flush();
        this.socketReturn.shutdownOutput();

    }

    public void sendNoCacheSocket(StringBuilder header)
        throws IOException {
        this.outputReturn.write(header.toString().getBytes("utf-8"));
        this.outputReturn.flush();
        this.socketReturn.shutdownOutput();
    }

    public void sendNoDestHonstSocket(StringBuilder header) throws IOException {
        this.outputReturn.write(header.toString().getBytes());
        this.outputReturn.flush();
        this.socketReturn.shutdownOutput();

    }
}
