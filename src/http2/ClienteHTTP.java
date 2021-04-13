package http2;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteHTTP {

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("google.com.br", 80);
        if (socket.isConnected()) {
            System.out.println("Conectado a " + socket.getInetAddress());
        }
        String requisicao = ""
                + "GET / HTTP/1.1\r\n"
                + "Host: www.google.com.br\r\n"
                + "\r\n";
        OutputStream envioServ = socket.getOutputStream();
        byte[] b = requisicao.getBytes();
        envioServ.write(b);
        envioServ.flush();
        Scanner sc = new Scanner(socket.getInputStream());
        while (sc.hasNext()) {
            System.out.println(sc.nextLine());
        }
        sc.close();
        envioServ.close();
        socket.close();
        System.out.println("Conexao Encerrada!");
    }
}