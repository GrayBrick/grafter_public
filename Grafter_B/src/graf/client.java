package graf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class client {

	public static String sendMes(String ip, int port, String message) throws Exception {
		  
	    Socket cs = new Socket(ip, port);	    
	    
	    OutputStreamWriter out = new OutputStreamWriter(cs.getOutputStream());
	    BufferedReader in = new BufferedReader(new InputStreamReader(cs.getInputStream()));

	    out.write(message + "\n");
	    out.flush();	    	    
	    
	    String ret = in.readLine();
	    
	    System.out.println(message + " " + ret);
	    
	    in.close();
	    out.close();
	    
	    cs.close();	  
	    
	    return ret;
	  }	
}
