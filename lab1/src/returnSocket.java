import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class returnSocket {

    public final static int FILESIZE = 1048576;

    public void returnBlockSocket (OutputStream output) throws IOException {
        String type = "<!DOCTYPE html>\n"
            + "<html lang=\"zh-CN\">\n"
            + "<head>\n"
            + "    <meta charset=\"UTF-8\">\n"
            + "    <title>禁止访问</title>\n"
            + "    <style>\n"
            + "        body, html {\n"
            + "            height: 100%;\n"
            + "            margin: 0;\n"
            + "            font-family: 'Arial', sans-serif;\n"
            + "            background: linear-gradient(135deg, #74b9ff, #ff7777);\n"
            + "            display: flex;\n"
            + "            justify-content: center;\n"
            + "            align-items: center;\n"
            + "        }\n"
            + "        .container {\n"
            + "            text-align: center;\n"
            + "            background-color: rgba(255, 255, 255, 0.9);\n"
            + "            padding: 40px;\n"
            + "            border-radius: 10px;\n"
            + "            box-shadow: 0 4px 8px rgba(0,0,0,0.1);\n"
            + "            color: #333;\n"
            + "        }\n"
            + "        h1 {\n"
            + "            font-size: 36px;\n"
            + "            color: #ff7777;\n"
            + "            margin-bottom: 20px;\n"
            + "        }\n"
            + "        p {\n"
            + "            font-size: 18px;\n"
            + "            margin-bottom: 30px;\n"
            + "        }\n"
            + "        .logo {\n"
            + "            width: 100px;\n"
            + "            margin-bottom: 20px;\n"
            + "        }\n"
            + "        .button {\n"
            + "            display: inline-block;\n"
            + "            background-color: #74b9ff;\n"
            + "            color: white;\n"
            + "            padding: 10px 20px;\n"
            + "            border-radius: 5px;\n"
            + "            text-decoration: none;\n"
            + "            font-size: 16px;\n"
            + "            transition: background-color 0.3s ease;\n"
            + "        }\n"
            + "        .button:hover {\n"
            + "            background-color: #5a97ff;\n"
            + "        }\n"
            + "        @keyframes spin {\n"
            + "            0% { transform: rotate(0deg); }\n"
            + "            100% { transform: rotate(360deg); }\n"
            + "        }\n"
            + "        .spinner {\n"
            + "            animation: spin 2s linear infinite;\n"
            + "        }\n"
            + "    </style>\n"
            + "</head>\n"
            + "<body>\n"
            + "    <div class=\"container\">\n"
            + "        <h1>禁止访问</h1>\n"
            + "        <p>朱妹妹VPN把你阻止了。</p>\n"
            + "        <p>您尝试访问的页面已被限制。</p>\n"
            + "        <a href=\"#\" class=\"button\">返回首页</a>\n"
            + "    </div>\n"
            + "</body>\n"
            + "</html>";
        int lengthType = type.getBytes().length;
        String response = "HTTP/1.1 200 ok\r\nContent-Length: " + lengthType + "\r\nContent-Type: textml; charset-utf-8\r\n\r\n" + type + "\r\n";
        System.out.println("禁止该用户访问网站");
        output.write(response.getBytes());
        output.flush();
    }

    public void returnHTTPSSocket(OutputStream output) {
    }

    public void returnRefuseSocket(OutputStream output) throws IOException {
        String body = "<h1>403 Forbidden</h1>";//你不许访问这个网站
        String response = "HTTP/1.1 403 ok\r\n" +
            "Content-Length: " + body.getBytes().length + "\r\n" +
            "Content-Type: textml; charset-utf-8\r\n" +
//                            "\r\n" +
//                            body +
            "\r\n";
        output.write(response.getBytes());//按照协议，将返回请求由outputStream写入
        output.flush();
    }

    public void returnUnmodifiedSocket(OutputStream output, File file, String URL) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(file));
        String pad = URL + "\r\n";
        inputStream.read(pad.getBytes());

        byte[] bytes = new byte[FILESIZE];
        int length;
        while((length = inputStream.read(bytes)) != -1) {
            output.write(bytes, 0, length);
        }

        output.flush();
    }

    public void returnModifiedSocket(OutputStream output, File file, String URL, InputStream inputReturn, byte[] tempBytes,int len) throws IOException {

        file.delete();
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        String pad = URL + "\r\n";
        outputStream.write(pad.getBytes("utf-8"));
        outputStream.write(tempBytes, 0, len);
        output.write(tempBytes, 0, len);
        BufferedInputStream inputStream = new BufferedInputStream(inputReturn);
        byte[] bytes = new byte[FILESIZE];

        int length;
        while ((length = inputStream.read(bytes)) != -1) {
            output.write(bytes, 0, length);
            outputStream.write(bytes, 0, length);
        }

        output.flush();
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }


    public void returnNoCacheSocket(OutputStream output, File file, String URL, InputStream inputReturn)
        throws IOException {
        file.createNewFile();
        FileOutputStream outputStream = new FileOutputStream(file);
        String line = URL + "\r\n";
        outputStream.write(line.getBytes("utf-8"));
        BufferedInputStream inputStream = new BufferedInputStream(inputReturn);
        byte[] buf = new byte[FILESIZE];
        int length;
        while((length = inputStream.read(buf)) != -1) {
            output.write(buf, 0, length);
            outputStream.write(buf, 0, length);
        }

        output.flush();
        outputStream.flush();
        inputStream.close();
        outputStream.close();
    }

    public void returnNoDestHostSocket(OutputStream output, InputStream inputReturn) throws IOException {
        BufferedInputStream inputStream = new BufferedInputStream(inputReturn);
        byte[] buf = new byte[FILESIZE];
        int length;
        while((length = inputStream.read(buf)) != -1) {
            output.write(buf, 0, length);
        }

        output.flush();
        inputStream.close();
    }
}
