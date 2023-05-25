package gol;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
/**
 *From Lecture Notes
 */
public class BattleshipClient implements Connectable{
 	public BattleshipClient(String server){
 		this(server, DEFAULT_PORT);
 	}
 	public BattleshipClient(String server, int port) {
		this.server = server;
		this.port = port;
	}
	@Override
	public void connect() throws IOException {
		this.socket = new Socket(this.server, this.port);
		this.inStream =  this.socket.getInputStream();
		this.outStream = this.socket.getOutputStream();
		this.in = new Scanner(this.inStream);
		this.out = new PrintWriter(new OutputStreamWriter(this.outStream, StandardCharsets.UTF_8), true /*autoFlush */);		
	}
	@Override
	public void send(String message) {
		this.out.println(message);
	}
	
	@Override
	public String receive() {
		String message = this.in.nextLine();
		return message;
	}
	
	public int getPort() {
		return this.port;
	}
	
	public String getServer() {
		return this.server;
	}
	
	public boolean isConnectionClosed() {
		return this.socket.isClosed();
	}
	
	public static final int DEFAULT_PORT = 8189;
	
	private Socket socket;
	private String server;
	private int port;
	private InputStream inStream;
	private OutputStream outStream;
	Scanner in;
	PrintWriter out;
}

