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

public class socketServer {
    public final static String BLOCK_IP = "172.20.226.166";
    public final static String FORIBBEN_URL = "qinghua.edu.cn";
    public final static String CASH_DIR = "F:/git-repo/lab1/lab1/src/cache";
    public final static Boolean IF_BLOCK_USER = true;
    public final static int PORT = 8080;
    public final static String FISHING_SOURCE = "www.lib.hit.edu.cn";
    public final static String FISHING_TARGET = "hcl.baidu.com";
    public final static String FISHING_TARGET_HEADER = "GET http://hcl.baidu.com/ HTTP/1.1\n"
        + "Host: hcl.baidu.com\n"
        + "Proxy-Connection: keep-alive\n"
        + "Cache-Control: max-age=0\n"
        + "Upgrade-Insecure-Requests: 1\n"
        + "User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Safari/537.36 Edg/129.0.0.0\n"
        + "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7\n"
        + "Accept-Encoding: gzip, deflate\n"
        + "Accept-Language: zh-CN,zh;q=0.9,en;q=0.8,en-GB;q=0.7,en-US;q=0.6\n\n";


    public static void main(String[] args) {
        try {
            returnSocket rs = new returnSocket();
            ServerSocket server = new ServerSocket(PORT);
            System.out.println("客户端:" + server.getInetAddress().getLocalHost());

            int indexBk = 0;
            while(true) {
                ++indexBk;

                // 接受连接并读取请求信息
                readSocket rds = new readSocket(server);

                System.out.println("-----------");
                System.out.println(rds.type + "->" + rds.URL);

                // 屏蔽用户
                if (IF_BLOCK_USER && InetAddress.getLocalHost().getHostAddress().equals(BLOCK_IP)) {
                    System.out.println("禁止该用户访问网站");
                    rs.returnBlockSocket(rds.output);
                    rds.socketAccept.close();
                    continue;
                }

                // 空请求
                if (rds.URL == null) {
                    continue;
                }

                // HTTPS请求
                if (rds.URL.contains("443")) {
                    System.out.println(rds.URL + " 是 https");
                    rs.returnHTTPSSocket(rds.output);
                    rds.socketAccept.close();
                    continue;
                }

                // 拒绝访问的URL
                if (rds.destAddr.contains(FORIBBEN_URL)) {
                    System.out.println("用户访问了" + FORIBBEN_URL + "，已拒绝");
                    rs.returnRefuseSocket(rds.output);
                    rds.socketAccept.close();
                    continue;
                }

                // 钓鱼网站
                if (rds.URL.contains(FISHING_SOURCE)) {
                    System.out.println("钓鱼成功!");
                    rds.destAddr = FISHING_TARGET;
                    rds.URL = "http://" + FISHING_TARGET + "/";
                    rds.destHost = FISHING_TARGET;
                    rds.header = new StringBuilder(FISHING_TARGET_HEADER);
                }

                try {
                    sendSocket ss = new sendSocket();
                    ss.sendSocket(rds.destHost, rds.destPort);
                    if (rds.destHost != null && !rds.destHost.equals("")) {
                        if (rds.type.equals("GET")) {
                            boolean ifHasFile = false;
                            String dir = CASH_DIR + "/" + rds.destHost + indexBk + ".txt";
                            File file = new File(dir);
                            File folder = new File(CASH_DIR);
                            File[] files = folder.listFiles();
                            int fileLength = files.length;

                            for(int fileIndex = 0; fileIndex < fileLength; ++ fileIndex) {
                                File fileExist = files[fileIndex];
                                BufferedReader fileReader = new BufferedReader(new InputStreamReader(new FileInputStream(fileExist)));
                                if (fileReader.readLine().equals(rds.URL)) {
                                    System.out.println(rds.URL + "-->\t存在缓存");
                                    ifHasFile = true;
                                    file = fileExist;
                                    break;
                                }
                            }

                            if (ifHasFile) {
                                System.out.println("缓存中存在该文件，发送请求到目标服务器，以检测是否要更新缓存");
                                ss.sendCacheSocket(rds.destHost, rds.destPort, rds.URL, file);

                                byte[] tempBytes = new byte[30];
                                int len = ss.inputReturn.read(tempBytes);
                                if (len == -1) {
                                    System.out.println("目标服务器返回为空");
                                    continue;
                                }
                                String response = new String(tempBytes, 0, len);

                                if (response.contains("304")) {
                                    System.out.println("缓存内容未更新，直接使用");
                                    rs.returnUnmodifiedSocket(rds.output, file, rds.URL);


                                } else {
                                    System.out.println("缓存内容更新，直接使用");
                                    rs.returnModifiedSocket(rds.output, file, rds.URL, ss.inputReturn, tempBytes, len);

                                }
                            } else {

                                System.out.println("，缓存中不存在该文件，发送请求到目标服务器，以获取文件并缓存");
                                System.out.println(rds.header.toString());
                                ss.sendNoCacheSocket(rds.header);
                                System.out.println(rds.URL + "-->\t缓存不存在");
                                rs.returnNoCacheSocket(rds.output, file, rds.URL, ss.inputReturn);

                            }

                        }
                    } else {
                        System.out.println("目标服务器地址为空");
                        ss.sendNoDestHonstSocket(rds.header);

                        System.out.println(rds.URL + "-->\t目标服务器为空");
                        rs.returnNoDestHostSocket(rds.output, ss.inputReturn);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.exit(-1);
                }
                rds.socketAccept.close();
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

