package gol;
import java.io.IOException;
/**
 * From Lecture Notes
 *
 */
public interface Connectable {
	public void connect() throws IOException;
	public void send(String message);	
	public String receive();
	public int getPort();
}
