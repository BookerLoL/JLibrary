package main.networking.learn;

import java.io.IOException;

public class QuoteServer {
	  public static void main(String[] args) throws IOException {
	        new QuoteServerThread().start();
	    }
}
