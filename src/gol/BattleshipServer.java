package gol;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.nio.charset.*;
/**
 * From Lecture Notes
 *
 */
public class BattleshipServer implements Connectable {
	public BattleshipServer() throws IOException {
		this(DEFAULT_PORT);
	}
	
	public BattleshipServer(int port) throws IOException {
		this.port = port;
		this.servSocket = new ServerSocket(this.port);
	}
	
	@Override
	public void connect() throws IOException {
		this.socket = this.servSocket.accept();
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

	public static final int DEFAULT_PORT = 8189;
	
	private int port;
	private Socket socket;
	private ServerSocket servSocket;
	private InputStream inStream;
	private OutputStream outStream;
	Scanner in;
	PrintWriter out;
}