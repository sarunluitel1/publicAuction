/*
 * CS351L Project #4: PublicAuction.
 * Jacob Hurst, Jaehee Shin, Sarun Luitel, Vincent Huber.
 * 11/20/17
 *
 * Bank.java - Accepts client requests to open a new bank account
 * with a fixed initial deposit, accepts auction central requests to block/unblock
 * funds on a particular account, accepts auction central requests to move amounts to
 * auction houses when a bid is successful.
 */

package Bank;

import AuctionCentral.AuctionCentralThread;

import java.io.IOException;
import java.net.ServerSocket;

public class Bank
{
  public static void main(String[] args) throws IOException
  {
    int portNumber = 2222;
    boolean open = true;

    try (ServerSocket serverSocket = new ServerSocket(portNumber))
    {
      System.out.println("[Bank]: " + serverSocket.toString());
      while (open) new BankThread(serverSocket.accept()).start();
    }
    catch (IOException e)
    {
      System.err.println("[Bank]: Port " + portNumber + " may be busy.");
      System.exit(-1);
    }
  }

}
