import java.io.*;
import java.net.Socket;

public class HttpClient {
    public static void main(String[] args) {
        try {
            // 服务器地址和端口
            String serverAddress = "127.0.0.1";
            int serverPort = 8080;

            // 创建一个Socket连接到服务器
            Socket socket = new Socket(serverAddress, serverPort);

            // 获取输出流，用于发送请求到服务器
            OutputStream output = socket.getOutputStream();
            // 获取输入流，用于读取服务器的响应
            InputStream input = socket.getInputStream();

            // 构建HTTP请求头
            String request = "GET http://hcl.baidu.com/ HTTP/1.0\r\n";
            request += "Host: hcl.baidu.com\r\n";
            request += "Connection: Keep-Alive\r\n";
            request += "\r\n";



            // 发送请求到服务器
            output.write(request.getBytes());
            output.flush();

            // 读取服务器的响应
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 关闭连接
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}