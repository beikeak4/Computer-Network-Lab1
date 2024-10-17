//import java.io.*;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class socketServer {
//    public static writeCache cacheWriter = new writeCache();
//    public static void main(String[] args) {
//        try {
//            int portID = 8080; //端口号设置为8080
//            ServerSocket server = new ServerSocket(portID); //对于接受连接的服务器，Java提供了一个ServerSocket类表示服务器Socket
//            System.out.println("启动服务器");
//            System.out.println("客户端:" + server.getInetAddress().getLocalHost() + "已连接到服务器");//获取主机名和IP地址
//            int indexBk = 0;
//            while (true) {
//                indexBk ++;
//                Socket s = server.accept();
//                System.out.println("第" + indexBk + "条请求:");//输出代理服务器接受的第几条请求
//                OutputStream output = null;
//                InputStream input = null;
//                output = s.getOutputStream();
//                input = s.getInputStream();
//
//
//                boolean ifBlockUser = true;//此变量为true,则屏蔽上网权限
//                if (ifBlockUser) {
//                    if (s.getInetAddress().getLocalHost().getHostAddress().equals("172.20.48.121")) {
//                        String body = "<h1>Forbidden visit web!</h1>";//禁止上网
//                        String response = "HTTP/1.1 200 ok\r\n" +
//                                "Content-Length: " + body.getBytes().length + "\r\n" +
//                                "Content-Type: textml; charset-utf-8\r\n" +
//                                "\r\n" +
//                                body + "\r\n";  //返回给浏览器的响应
//                        System.out.println("禁止该用户访问网站");
//                        output.write(response.getBytes());
//                        //按照协议,将返回请求由outputStream写入,会根据指定的decode编码返回某字符串在该编码下的byte数组表示
//                        output.flush();
//                        break;
//                    }
//                }
//
//
//                String type = null, destHost = null, URL = null, destAddr = null;
//                int destPort = 80;
//                int flag = 1;
//                StringBuilder header = new StringBuilder();
//                BufferedReader br = new BufferedReader(new InputStreamReader(input));
//                String readLine = br.readLine();
//
//                while (readLine != null && !readLine.equals("")) {
//                    if (flag == 1) { //第一行获取请求类型（connect/get）和URL
//                        type = readLine.split(" ")[0];
//                        URL = readLine.split(" ")[1];
//                        flag = 0;
//                    }
//                    String[] s1 = readLine.split(": ");
//
//                    for (int i = 0; i < s1.length; i++){
//                        if (s1[i].equals("Host")) {
//                            destAddr = s1[i + 1];
//                        }
//                    }
//                    header.append(readLine).append("\r\n");
//                    readLine = br.readLine();
//                }//解析首部完成
//
//                if (URL == null) {
//                    continue;
//                }
//                if (URL.contains("443")) {
//                    System.out.println(URL+"  是 https");
////                    continue;
//                }
//
//                if(URL.contains("www.lib.hit.edu.cn")) {
//                    System.out.println("钓鱼成功!");
//                    destAddr = "hituc.hit.edu.cn";
//                    header = new StringBuilder("GET http://hituc.hit.edu.cn/ HTTP/1.1\n"+
//                            "Host: hituc.hit.edu.cn\n" +
//                            "Proxy-Connection: keep-alive\n" +
//                            "Upgrade-Insecure-Requests: 1\n" +
//                            "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.53\n" +
//                            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\n" +
//                            "Accept-Encoding: gzip, deflate\n" +
//                            "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\n"+"\n");
//                }//钓鱼网站
//
//                if (destAddr.split(":").length > 1) {//代表包含端口号
//                    destPort = Integer.valueOf(destAddr.split(":")[1]);
//                }
//                destHost = destAddr.split(":")[0];
//                //获取目的地址和端口号
//                header.append("\r\n");
//
//                if (destAddr.contains("yzb.hit.edu.cn")) {//将jwc.hit.edu.cn作为过滤对象
//                    String body = "<h1>You can not visit this site!</h1>";//你不许访问这个网站
//                    String response = "HTTP/1.1 200 ok\r\n" +
//                            "Content-Length: " + body.getBytes().length + "\r\n" +
//                            "Content-Type: textml; charset-utf-8\r\n" +
//                            "\r\n" +
//                            body + "\r\n";
//                    System.out.println("用户访问了yzb.hit.edu.cn，已拒绝");
//                    output.write(response.getBytes());//按照协议，将返回请求由outputStream写入
//                    output.flush();
//                    s.close();
//                    continue;
//                }
//
//                System.out.println(type + "->" + URL);
//                System.out.println("-----------");
//
//                cacheWriter.writeInCache(type, destHost, URL, destPort, indexBk, output, header);
//                s.close();
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
//



import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class socketServerTemp {
    public static void main(String[] args) {
        try {
            int port = 8080;
            ServerSocket server = new ServerSocket(port);
            System.out.println("客户端:" + server.getInetAddress().getLocalHost());

            int indexBk = 0;
            while(true) {
                ++indexBk;
                Socket socketAccept = server.accept();
                socketAccept.setSoTimeout(100000);
                OutputStream output = socketAccept.getOutputStream();
                InputStream input = socketAccept.getInputStream();
                InputStreamReader inputReader = new InputStreamReader(input);
                BufferedReader br = new BufferedReader(inputReader);

//                PrintWriter(br);
                //打印出所有outputStream的内容
//                System.out.println("Received data from client:");
//                String line;
//                while ((line = br.readLine()) != null) {
//                    System.out.println(line);
//                }

                int lengthOfType;
                Boolean ifBlockUser = true;
                String type;
                String destHost;
                if (ifBlockUser) {
                    socketAccept.getInetAddress();
                    if (InetAddress.getLocalHost().getHostAddress().equals("172.20.48.121")) {
                        type = "<h1>Forbidden visit web!</h1>";
                        lengthOfType = type.getBytes().length;
                        destHost = "HTTP/1.1 200 ok\r\nContent-Length: " + lengthOfType + "\r\nContent-Type: textml; charset-utf-8\r\n\r\n" + type + "\r\n";
                        System.out.println("禁止该用户访问网站");
                        output.write(destHost.getBytes());
                        output.flush();
                        break;
                    }
                }


                int flag = 1;
                String readLine = br.readLine();
                String URL = null;
                type = null;
                String destAddr = null;
                destHost = null;
                int destPort = 80;

                StringBuilder header = new StringBuilder();

                while(readLine != null && !readLine.equals("")) {
                    if(flag == 1) {
                        type = readLine.split(" ")[0];
                        URL = readLine.split(" ")[1];
                        flag = 0;
                    }
                    String[] s1 = readLine.split(": ");

                    for (int i = 0; i < s1.length; i++){
                        if (s1[i].equals("Host")) {
                            destAddr = s1[i + 1];
                        }
                    }
                    header.append(readLine).append("\r\n");
                    readLine = br.readLine();
                }

                if (URL == null) {
                    continue;
                }

                if (URL.contains("443")) {
                    System.out.println(URL+"  是 https");
                    continue;
                }

                if (URL.contains("www.lib.hit.edu.cn")) {
                    System.out.println("钓鱼成功!");
                    destAddr = "hituc.hit.edu.cn";
                    header = new StringBuilder("GET http://hituc.hit.edu.cn/ HTTP/1.1\nHost: hituc.hit.edu.cn\nProxy-Connection: keep-alive\nUpgrade-Insecure-Requests: 1\nUser-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36 Edg/105.0.1343.53\nAccept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9\nAccept-Encoding: gzip, deflate\nAccept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\n\n");
                }

                if (destAddr.split(":").length > 1) {//代表包含端口号
                    destPort = Integer.valueOf(destAddr.split(":")[1]);
                }
                destHost = destAddr.split(":")[0];

                if (destAddr.contains("yzb.hit.edu.cn")) {//将jwc.hit.edu.cn作为过滤对象
                    String body = "<h1>You can not visit this site!</h1>";//你不许访问这个网站
                    String response = "HTTP/1.1 200 ok\r\n" +
                            "Content-Length: " + body.getBytes().length + "\r\n" +
                            "Content-Type: textml; charset-utf-8\r\n" +
                            "\r\n" +
                            body + "\r\n";
                    System.out.println("用户访问了yzb.hit.edu.cn，已拒绝");
                    output.write(response.getBytes());//按照协议，将返回请求由outputStream写入
                    output.flush();
                    socketAccept.close();
                    continue;
                }

//                // 打印请求头
//                System.out.println("Request Header:");
//                System.out.println(header.toString());
//
//                // 读取请求体（如果有）
//                if (type != null && type.equalsIgnoreCase("POST")) {
//                    StringBuilder requestBody = new StringBuilder();
//                    String line;
//                    while ((line = br.readLine()) != null) {
//                        requestBody.append(line).append("\r\n");
//                    }
//                    System.out.println("Request Body:");
//                    System.out.println(requestBody.toString());
//                }

                System.out.println(type + "->" + URL);
                System.out.println("-----------");

                ///////

                try {
                    Socket socketReturn;
                    OutputStream outputReturn;
                    InputStream inputReturn;
                    if (destHost != null && !destHost.equals("")) {
                        socketReturn = new Socket(destHost, destPort);
                        outputReturn = socketReturn.getOutputStream();
                        inputReturn = socketReturn.getInputStream();
                        if (type.equals("GET")) {
                            boolean ifHasFile = false;
                            System.out.println(destHost);
                            String dir = "F:\\git-repo\\lab1\\lab1\\src\\cache\\" + destHost + indexBk + ".txt";
                            File file = new File(dir);
                            File folder = new File("F:/git-repo/lab1/lab1/src/cache");
                            File[] files = folder.listFiles();
                            int fileLength = files.length;

                            for(int fileIndex = 0; fileIndex < fileLength; ++ fileIndex) {
                                File fileExist = files[fileIndex];
                                BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileExist)));
                                if (fileReader.readLine().equals(URL)) {
                                    System.out.println(URL + "-->\t存在缓存");
                                    ifHasFile = true;
                                    file = fileExist;
                                    break;
                                }
                            }

                            String line;

                            if (ifHasFile) {
                                Scanner sc = new Scanner(new FileReader(file));
                                String date = null;

                                while(sc.hasNextLine()) {
                                    line = sc.nextLine();
                                    if (line.contains("Date")) {
                                        date = line.substring(6);
                                    }
                                }

                                StringBuffer ifGetReqBuffer = new StringBuffer();
                                ifGetReqBuffer.append("GET " + URL + " HTTP/1.1\r\n");
                                ifGetReqBuffer.append("Host: " + destHost + ":" + destPort + "\r\n");
                                ifGetReqBuffer.append("If-modified-since: " + date + "\r\n");
                                ifGetReqBuffer.append("\r\n");
                                String ifGetReq = ifGetReqBuffer.toString();
                                outputReturn.write(ifGetReq.getBytes());
                                outputReturn.flush();
                                byte[] tempBytes = new byte[30];
                                int len = inputReturn.read(tempBytes);
                                String res = new String(tempBytes, 0, len);
                                String pad;
                                byte[] bytes;
                                if (res.contains("304")) {
                                    System.out.println("缓存内容未更新，直接使用");
                                    socketReturn.shutdownOutput();
                                    BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
                                    pad = URL + "\r\n";
                                    inputStream.read(pad.getBytes());
                                    bytes = new byte[1024];


                                    int length;
                                    while((length = inputStream.read(bytes)) != -1) {
                                        output.write(bytes, 0, length);
                                    }

                                    output.flush();
                                } else {
                                    System.out.println("缓存内容更新，重新缓存并使用");
                                    file.delete();
                                    file.createNewFile();
                                    FileOutputStream outputStream = new FileOutputStream(file);
                                    pad = URL + "\r\n";
                                    outputStream.write(pad.getBytes("utf-8"));
                                    outputStream.write(tempBytes, 0, len);
                                    output.write(tempBytes, 0, len);
                                    socketReturn.shutdownOutput();
                                    BufferedInputStream inputStream = new BufferedInputStream(inputReturn);
                                    bytes = new byte[1024];

                                    int size;
                                    while((size = inputStream.read(bytes)) != -1) {
                                        output.write(bytes, 0, size);
                                        outputStream.write(bytes, 0, size);
                                    }

                                    output.flush();
                                    outputStream.flush();
                                }
                            } else {
                                outputReturn.write(header.toString().getBytes("utf-8"));
                                outputReturn.flush();
                                socketReturn.shutdownOutput();
                                System.out.println(URL + "-->\t缓存不存在");
                                file.createNewFile();
                                FileOutputStream outputStream = new FileOutputStream(file);
                                line = URL + "\r\n";
                                outputStream.write(line.getBytes("utf-8"));
                                BufferedInputStream inputStream = new BufferedInputStream(inputReturn);
                                byte[] buf = new byte[1];////
                                int size;
                                while((size = inputStream.read(buf)) != -1) {
                                    output.write(buf, 0, size);
                                    outputStream.write(buf, 0, size);
                                }

                                output.flush();
                                outputStream.flush();
                            }

                        }
                    } else {
                        socketReturn = new Socket(destHost, destPort);
                        outputReturn = socketReturn.getOutputStream();
                        inputReturn = socketReturn.getInputStream();
                        outputReturn.write(header.toString().getBytes());
                        outputReturn.flush();
                        socketReturn.shutdownOutput();
                        BufferedInputStream inputStream = new BufferedInputStream(inputReturn);
                        byte[] buf = new byte[1];
                        int size;
                        while((size = inputStream.read(buf)) != -1) {
                            output.write(buf, 0, size);
                        }

                        output.flush();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }


                ///////


                socketAccept.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void PrintWriter(BufferedReader br) {
        String line;
        try {
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

