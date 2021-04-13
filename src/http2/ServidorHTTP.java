package http2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ServidorHTTP {

    public static void main(String[] args) throws IOException {

        ServerSocket servidor = new ServerSocket(8000);
        Socket socket = servidor.accept();
        if (socket.isConnected()) {
            System.out.println(socket.getInetAddress());
            BufferedReader buffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Requisição: ");

            String linha = buffer.readLine();
            String[] dadosReq = linha.split(" ");
            String metodo = dadosReq[0];
            String caminhoArquivo = dadosReq[1];
            String protocolo = dadosReq[2];
            while (!linha.isEmpty()) {
                System.out.println(linha);
                linha = buffer.readLine();
            }
            if (caminhoArquivo.equals("/")) {
                caminhoArquivo = "index.html";
            }
            File arquivo = new File(caminhoArquivo.replaceFirst("/", ""));

            String status = protocolo + " 200 OK\r\n";
            if (!arquivo.exists()) {
                status = protocolo + " 404 Not Found\r\n";
                arquivo = new File("404.html");
            }

            byte[] conteudo = Files.readAllBytes(arquivo.toPath());
            SimpleDateFormat formatador = new SimpleDateFormat("E, dd MMM yyyy hh:mm:ss", Locale.ENGLISH);
            formatador.setTimeZone(TimeZone.getTimeZone("GMT"));
            Date data = new Date();
            String dataFormatada = formatador.format(data) + " GMT";
            String header = status + "Location: http://localhost:8000/\r\n"
                    + "Date: " + dataFormatada + "\r\n"
                    + "Server: MeuServidor/1.0\r\n"
                    + "Content-Type: text/html\r\n"
                    + "Content-Length: " + conteudo.length + "\r\n"
                    + "Connection: close\r\n"
                    + "\r\n";
            OutputStream resposta = socket.getOutputStream();
            resposta.write(header.getBytes());
            resposta.write(conteudo);
            resposta.flush();
        }
    }
}
