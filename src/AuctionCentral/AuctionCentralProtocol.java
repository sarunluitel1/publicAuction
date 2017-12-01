/*
 * CS351L Project #4: PublicAuction.
 * Jacob Hurst, Jaehee Shin, Sarun Luitel, Vincent Huber.
 * 11/20/17
 *
 * AuctionCentralProtocol.java - Protocol for auction central to follow.
 */

package AuctionCentral;

import Agent.Agent;
import AuctionHouse.AuctionHouse;
import Message.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

class AuctionCentralProtocol {
  private static Map<String, AuctionHouse> auctionRepository = Collections.synchronizedMap(new HashMap<String, AuctionHouse>());
  
  private Socket bankSocket = null;
  private DataInputStream bankI;
  private DataOutputStream bankO;
  
  private Socket socket;
  private Object object;
  
  private Agent agent;
  private static int agentCount;
  
  /**
   * Default constructor.
   *
   * Takes a socket and an object to identify who it is speaking with.
   * @param socket
   * @param message
   * @throws IOException
   */
  AuctionCentralProtocol(Socket socket, Message message) throws IOException
  {
    this.socket = socket;
    System.out.println(message.getMessage() + message.getItem());
    if(message.getSender() instanceof Agent)
    {
      agent = ((Agent)message.getSender());
      agentCount++;
  
      System.out.println(agent.getAgentName() + ": Connected to AuctionCentral.");
      System.out.println("[AuctionCentral]: " + agentCount + " agent(s) are connected!");
    }
    else this.object = message.getSender();
    
    /* for now, registering auction houses within auction central. */
    for(int i = 0; i < 5; i++) registerAuctionHouse();
    
    if(bankSocket == null)
    {
      System.out.println("[AuctionCentral]: Connected to bank.");
      /* update this to take an address for the bank server - diff. from LocalHost. */
      bankSocket = new Socket(InetAddress.getByName(message.getItem()),2222);
      bankI = new DataInputStream(bankSocket.getInputStream());
      bankO = new DataOutputStream(bankSocket.getOutputStream());
    }
  }
  
  /**
   * Handles requests as they are received from socket.
   *
   * @param request
   * @return response to request.
   */
  public Message handleRequest(Message request)
  {
//    Message response = new Message(this, "", "", 0, 0);
    switch(request.getMessage())
    {
      case "START":
        System.out.println(1);
        break;
      case "register":
        System.out.println(2);
        break;
      case "de-register":
        System.out.println(3);
        break;
      case "repository":
        System.out.println(4);
        break;
      case "transaction":
        System.out.println(5);
//        handleTransaction(message.get)
        break;
      case "EXIT":
        System.out.println(6);
        break;
      default:
        System.out.println(-1);
        break;
    }
    return null;//response;
  }
  
  /* tell bank to find agent account with ID & perform action if possible
     then respond according to bank confirmation to de-register auction houses,
     get public ID and de-register there.                                       */
  /**
   * Mitigates transaction requests between agents and houses.
   *
   * @param agentBid
   * @param agentID
   * @param houseID
   * @return response to transaction request.
   * @throws IOException
   */
  private String handleTransaction(String agentBid, String agentID, String houseID) throws IOException
  {
    //don't allow bid if it has not yet been accepted by bank
    bankO.writeUTF("[AuctionCentral]: block:"+agentBid+":"+agentID);
    bankO.writeUTF("[AuctionCentral]: unblock:"+agentBid+":"+agentID);
    bankO.writeUTF("[AuctionCentral]: move:"+agentBid+":"+agentID+":"+houseID);
    //if item is sold check if house is empty de-register house if so.
    bankO.flush();

    return bankI.readUTF();
  }
  
  /**
   * Registers auction houses and adds them to repository.
   */
  private void registerAuctionHouse()
  {
//    AuctionHouse auctionHouse = new AuctionHouse();
//    auctionRepository.put(auctionHouse.getName(), auctionHouse);
  }
  
  /**
   * De-registers auction houses from repository & closes their socket.
   *
   * @param publicID
   */
  private void deregisterAuctionHouse(int publicID)
  {
    //not sure if anything extra should be done on auction house - could just be left as remove
    //AuctionHouse auctionHouse = auctionRepository.remove("[House-" + publicID + "]");
    //auctionHouse.exit();
  }
}
