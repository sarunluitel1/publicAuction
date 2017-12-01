/*
 * CS351L Project #4: PublicAuction.
 * Jacob Hurst, Jaehee Shin, Sarun Luitel, Vincent Huber.
 * 11/20/17
 *
 * BankThread.java - Threading to handle multiple client requests.
 */

package Bank;

import Message.Message;

import java.io.*;
import java.net.Socket;

class BankThread extends Thread
{
  private Socket socket = null;
  
  /**
   * Default constructor.
   *
   * @param socket
   */
  public BankThread(Socket socket)
  {
    super("[BankThread]");
    this.socket = socket;

    System.out.println("[Bank]: " + socket.toString() + " connected!");
  }
  
  /**
   * Run method for bank thread.
   */
  public void run()
  {
    try (ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
         ObjectInputStream in = new ObjectInputStream(socket.getInputStream()))
    {
      try
      {
        Message input, output;
        input = ((Message)in.readObject());
      
        BankProtocol bankProtocol = new BankProtocol(socket, input);

        while (true)
        {
          if(input != null)
          {
            System.out.println(input.getMessage());
    
            input = ((Message)in.readObject());
            output = bankProtocol.handleRequest(input);
    
            out.writeObject(output);
    
            input = null;
          }
        }
      } catch(ClassNotFoundException e)
      {
        System.err.println(e.getMessage());
      }
      in.close();
      out.close();
      socket.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }


}
