/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Scanner;

/**
 * @author Maurice Bernard
 */
public class InputParser {
  public static int timeBank = 0;
  private static int timePerMove = 0;
  public static int raises = 0;
  private Scanner scan = new Scanner(System.in);
  private CardParser cardParser = new CardParser();
  private Table table;

  InputParser() {
    table = new Table();
  }

  public void input() {
    while (scan.hasNextLine()) {
      String line = scan.nextLine();

      if (line.length() == 0) {
        continue;
      }

      String[] parts = line.split(" ");

      switch (parts[0]) {
        case "Action":
          performAction(parts);
          break;

        case "Settings":
          evalSettings(parts);
          break;

        case "Match":
          evalMatch(parts);
          break;


        default:
          if (parts[0].equals("player1") || parts[0].equals("player2")) {
            evalPlayer(parts);
          }
          break;
      }

//                if( parts.length == 3 && parts[0].equals("Action") ) 
//                { 
//                    // A move is requested
//                    performAction(parts);
//                }
//                else if(parts.length == 3 && parts[0].equals("Settings")) 
//                {
//                    evalSettings(parts);
//                }
    }
  }

  private void evalPlayer(String[] args) {
    if (args.length != 3) {
      return;
    }
    Player target;
    if (args[0].equals(RikOma.botID)) {
      //self
      target = table.getSelf();
    } else {
      //opponent
      target = table.getOpponent();
    }

    switch (args[1]) {
      case "stack":
        target.setStack(Integer.parseInt(args[2]));
        break;

      case "post": //pay blind
        target.removeFromStack(Integer.parseInt(args[2]));
        break;

      case "call":
        target.removeFromStack(Integer.parseInt(args[2]));
        break;

      case "raise":
        target.removeFromStack(Integer.parseInt(args[2]));
        break;

      case "wins":
        target.addToStack(Integer.parseInt(args[2]));
        break;

      case "hand":
        cardParser.parseHand(target, args[2]);
        raises = 0;
        break;

      default:
        //check or fold
        break;
    }
  }

  private void evalMatch(String[] args) {
    if (args.length != 3) {
      return;
    }
    switch (args[1]) {
      case "round":
        //number of current round
        break;

      case "smallBlind":
        table.setSmallBlind(Integer.parseInt(args[2]));
        break;

      case "bigBlind":
        table.setBigBlind(Integer.parseInt(args[2]));
        break;

      case "onButton":
        //player args[2] is dealer
        break;

      case "table":
        //communityCards
        cardParser.parseCommunityCards(table, args[2]);
        break;

      case "maxWinPot":
        table.setPot(Integer.parseInt(args[2]));
        break;

      case "amountToCall":
        table.setAmountToCall(Integer.parseInt(args[2]));
        break;

      default:
        break;

    }
  }

  private void evalSettings(String[] args) {
    if (args.length != 3) {
      return;
    }
    switch (args[1]) {
      case "timebank":
        timeBank = Integer.parseInt(args[2]);
        break;

      case "timePerMove":
        timePerMove = Integer.parseInt(args[2]);
        break;

      case "handPerLevel":
        break;

      case "startingStack":
        table.getSelf().setStack(Integer.parseInt(args[2]));
        table.getOpponent().setStack(Integer.parseInt(args[2]));
        break;

      case "yourBot":
        RikOma.botID = args[2];
        break;

      default:
        break;
    }
  }

  private void performAction(String[] args) {
    if (args[1].equals(RikOma.botID)) {
      timeBank += timePerMove;
      TacticalProcessor proc = new TacticalProcessor(table);
      proc.start();
      System.out.println(proc.returnAction());
      System.out.flush();
      //Take action
//            System.out.println("check 0");
//            System.out.flush();
    }
  }
}
