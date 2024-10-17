import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
public class readSocket {

    public final static int TIMEOUT = 100000; // 设置超时时间为100秒
    public Socket socketAccept;
    public OutputStream output;
    public InputStream input;
    public InputStreamReader inputReader;
    public BufferedReader br;
    public String URL;
    public String type;
    public String destAddr;
    public String destHost;
    public int destPort = 80;
    public StringBuilder header = new StringBuilder();
    public readSocket(ServerSocket server) throws IOException {
        this.socketAccept = server.accept();
        this.socketAccept.setSoTimeout(TIMEOUT); // 设置超时时间为100秒
        this.output = socketAccept.getOutputStream();
        this.input = socketAccept.getInputStream();
        this.inputReader = new InputStreamReader(input);
        this.br = new BufferedReader(inputReader);
        readSocketInfo();
//        PrintAll();
    }
    private void readSocketInfo() throws IOException {
        int flag = 1;
        String readLine;
        this.header = new StringBuilder();

        while ((readLine = br.readLine()) != null) {
            if (readLine.isEmpty()) {
                break; // 如果读到空行，结束循环
            }
            if (flag == 1) {
                type = readLine.split(" ")[0];
                URL = readLine.split(" ")[1];
                flag = 0;
            }
            String[] s1 = readLine.split(": ");

            for (int i = 0; i < s1.length; i++) {
                if ("Host".equals(s1[i])) {
                    destAddr = s1[i + 1];
                }
            }
            header.append(readLine).append("\r\n");
        }

        if (destAddr == null) {
            return;
        }

        try {
            if (destAddr.split(":").length > 1) { // 代表包含端口号
                destPort = Integer.valueOf(destAddr.split(":")[1]);
            }
            destHost = destAddr.split(":")[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            // 处理异常，可能是由于格式不正确导致的
        }
    }

    private void PrintAll() {
        System.out.println("type: " + type);
        System.out.println("URL: " + URL);
        System.out.println("destAddr: " + destAddr);
        System.out.println("destHost: " + destHost);
        System.out.println("destPort: " + destPort);
        System.out.println("header: " + header);
    }



}
