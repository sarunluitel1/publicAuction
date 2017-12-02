/*
 * CS351L Project #4: PublicAuction.
 * Jacob Hurst, Jaehee Shin, Sarun Luitel, Vincent Huber.
 * 11/29/17
 *
 * AgentGUIController.java - asks a prompt to connect to the Bank and Auction
 * central. secures connections and provides an GUI for Agents to place bids
 * and see the status.
 */

package Agent;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AgentGUIController extends Application
{
  @FXML
  private TextField bankIP, auctionIP, input;
  @FXML
  private Button connect;
  @FXML
  private TextArea textArea;
  @FXML
  private Text txtTotalBidPlaced, txtBankBalance;
  @FXML
  private ComboBox<String> itemsComboBox;

  private Agent agent;
  private String history = "";

  @FXML
  public void initialize()
  {
    textArea.setVisible(false);
    input.setVisible(false);
    textArea.setEditable(false);
    txtTotalBidPlaced.setVisible(false);
    txtBankBalance.setVisible(false);
    itemsComboBox.setVisible(false);

  }

  /**
   * secureConnection() is invoked when btnConnect is clicked. secures connections.
   * hides old GUIElements and shows interface for agent to work on. starts the4 Agent Thread
   *
   * @return void.
   */
  @FXML
  private void secureConnection()
  {
    agent = new Agent();

    try
    {
      agent.setAuctionAddress(InetAddress.getByName(bankIP.getText()));
      agent.setBankAddress(InetAddress.getByName(auctionIP.getText()));
      auctionIP.setVisible(false);
      bankIP.setVisible(false);
      connect.setVisible(false);

      textArea.setVisible(true);
      input.setVisible(true);
      txtTotalBidPlaced.setVisible(true);
      txtBankBalance.setVisible(true);
      itemsComboBox.setVisible(true);

      agent.setMessageText("");
      synchronized (agent)
      {
        agent.notify();
      }

      //The Event input Comes from FXML we do not need Lambda expressions.
      /*input.setOnKeyPressed(e ->
      {
        if(e.getCode() == KeyCode.ENTER)
        {
          placeBid();
        }
      });*/
    } catch (UnknownHostException e)
    {
      e.printStackTrace();
      System.exit(-1);
    }

    // run code to setup connections after we get addresses to bank and Auction Central
    try
    {
      agent.start();
    } catch (Exception e)
    {
      e.printStackTrace();
      System.exit(-1);
    }

  }

  public static void main(String args[])
  {
    launch(args);
  }

  @Override
  public void start(Stage primaryStage) throws Exception
  {
    Parent root = FXMLLoader.load(getClass().getResource("AgentGUI.fxml"));
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
  }


  @FXML
  private void placeBid()
  {
    //Just hit enter to place Bid!
    String request = input.getText();
    history = history + request + "\n";
    textArea.setText(history);
    agent.setMessageText(request);
    input.setText("");

    synchronized (agent)
    {
      agent.notify();
    }
  }
}
