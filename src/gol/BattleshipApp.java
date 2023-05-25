package  gol;

import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;
import java.io.*;
import java.nio.file.*;
import java.awt.*;
import java.beans.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.*;
import javax.swing.filechooser.FileFilter;
/*   */
/**
* code for HW 3
* @version <b>3.0</b> rev. 0
* An app to represent Battleship. 
* System will generate the a program
*/
enum Mode { CLIENT, SERVER };
public class BattleshipApp{
	/**
	* the App java to connect all functions
	*/
	public static Connectable peer;
	public static Mode mode;
	public static final int USE_DEFAULT_PORT = -1;
	public static void main(String[] args){
		String peerName = "";
		int port = BattleshipApp.USE_DEFAULT_PORT;
		System.out.print("Do you want to Connect to the remote system or Wait for an incoming connection (enter C or W)? ");
		try (Scanner in = new Scanner(System.in);){
			String choice = in.nextLine();
			if (choice.toLowerCase().equals("c")) {
				mode = Mode.CLIENT;
					peerName = args[0];
					if (args.length > 1) {
						try {
							port = Integer.parseInt(args[1]);
						}
						catch (NumberFormatException e) {
							System.out.format("Invalid port number %s, using the default value of %d", args[1], BattleshipClient.DEFAULT_PORT);
						}
					}
			}
			else if (choice.toLowerCase().equals("w")) {
				mode = Mode.SERVER;
				if (args.length > 0) {
					try {
						port = Integer.parseInt(args[0]);
					}
					catch (NumberFormatException e) {
						System.out.format("Invalid port number %s, using the default value of %d", args[0], BattleshipServer.DEFAULT_PORT);
					}
				}
			}
			else {
				System.out.printf("Invalid mode requested: %s\n", choice);
				return;
			}
			if (mode == Mode.CLIENT) {
				if (port != USE_DEFAULT_PORT) {
					peer = new BattleshipClient(peerName, port);
				}
				else {
					peer = new BattleshipClient(peerName);
				}
			}
			else {
				try {
					if (port != USE_DEFAULT_PORT) {
						peer = new BattleshipServer(port);
					}
					else {
						peer = new BattleshipServer();
					}
				}
				catch (IOException e) {
					System.out.format("Unable to create server socket: %s", e.getMessage());
					return;
				}
			}
			try {
				peer.connect();
			}
			catch (IOException e) {
				System.out.format("Connecting to the peer failed: %s", e.getMessage());
			}
		}
     	EventQueue.invokeLater(() -> {
        BattleshipAppFrame frame = new BattleshipAppFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    	});
    }
}
class BattleshipAppFrame extends JFrame{
	private String player1;
	private String player2;
	private int countWin1;
	private int countWin2;
	private int countbeat1;
	private int countbeat2;
	public JButton leftPlayerWin;
	public JButton rightPlayerWin;
	private BattleshipBoard board;
	public boolean isMove = false;
	public int n = 30; 
	public JButton time;
	private int c_R_B,c_G_B,c_B_B,
				c_R_S,c_G_S,c_B_S,
				c_R_F,c_G_F,c_B_F,
				c_R_O,c_G_O,c_B_O;
	private void readSettings(){
		String pathName = ".." + File.separator + "src" + File.separator + "Setting.txt";
      	String line = null;
      	FileReader fr = null;
      	BufferedReader reader = null;
      	try{
        fr = new FileReader(pathName);
        reader = new BufferedReader(fr);
        line = reader.readLine();
        String[] settingArray = line.split(", ");
        c_R_B = Integer.parseInt(settingArray[0]);
        c_G_B = Integer.parseInt(settingArray[1]);
        c_B_B = Integer.parseInt(settingArray[2]);
        c_R_S = Integer.parseInt(settingArray[3]);
        c_G_S = Integer.parseInt(settingArray[4]);
        c_B_S = Integer.parseInt(settingArray[5]);
        c_R_F = Integer.parseInt(settingArray[6]);
        c_G_F = Integer.parseInt(settingArray[7]);
        c_B_F = Integer.parseInt(settingArray[8]);
        c_R_O = Integer.parseInt(settingArray[9]);
        c_G_O = Integer.parseInt(settingArray[10]);
        c_B_O = Integer.parseInt(settingArray[11]);
        line = reader.readLine();
        settingArray = line.split(", ");
        player1 = settingArray[0];
        player2 = settingArray[1];
      }
      catch(FileNotFoundException e){
        System.err.format("'%s' not found.", pathName);
        System.exit(0);
      }
      catch(IOException e2){
        System.err.format("Exception occurred when open '%s'.", pathName);
        System.exit(0);
      }
      catch(NumberFormatException a){
        System.err.format("Exception occurred when first line is not in format '%s'.", pathName);
        System.exit(0);
      }
    }
    private void changeSettings(){
    	String pathName = ".." + File.separator + "src" + File.separator + "Setting.txt";
      	FileWriter fw = null;
      	BufferedWriter bw = null;
      	String temp = "";
      	try{
        fw = new FileWriter(pathName);
        bw = new BufferedWriter(fw);
        temp =  c_R_B + ", " + c_G_B + ", " + c_B_B 
        + ", " + c_R_S + ", " + c_G_S + ", " + c_B_S
        + ", " + c_R_F + ", " + c_G_F + ", " + c_B_F
        + ", " + c_R_O + ", " + c_G_O + ", " + c_B_O;
        bw.write(temp);
        bw.newLine();
        temp = player1 + ", " + player2;
        bw.write(temp);
        bw.newLine();
        bw.close();
        fw.close();
      }
      catch(Exception e){
        e.printStackTrace();
      }
    }
    /**
	* the game frame
	*/
	public BattleshipAppFrame(){
		readSettings();
		if(BattleshipApp.mode == Mode.SERVER){
			setTitle("Battleship" + player1);
		}
		else{
			setTitle("Battleship" + player2);
		}
      	setSize(1600, 800);
      	countbeat1 = 0;
      	countbeat2 = 0;
      	countWin2 = 0;
      	countWin1 = 0;
      JMenu boardMenu = new JMenu("Battleship Game");
      JMenuItem aboutItem = boardMenu.add(new menuAction("About Battleship Game"));
      boardMenu.addSeparator();
      Action configAction = new menuAction("Configuration");
      JMenuItem configItem = boardMenu.add(configAction);
      boardMenu.addSeparator();
      boardMenu.add(new AbstractAction("Exit"){
        /**
        * actionPerformed for exit.
        * @param event - the event.
        */
        public void actionPerformed(ActionEvent event){
          //changeSettings();
          System.exit(0);
          }
        }
      	);
      	JMenuBar menuBar = new JMenuBar(); 
      	setJMenuBar(menuBar);
      	menuBar.add(boardMenu);
    	JToolBar bar = new JToolBar(JToolBar.HORIZONTAL);
    	Action button_CH = new shipAction("Horizontal Carrier");
    	Action button_CV = new shipAction("Vertical Carrier");
    	Action button_BH = new shipAction("Horizontal Battleship");
    	Action button_BV = new shipAction("Vertical Battleship");
    	Action button_SH = new shipAction("Horizontal Submarine");
    	Action button_SV = new shipAction("Vertical Submarine");
    	Action button_PH = new shipAction("Horizontal Patrol Ship");
    	Action button_PV = new shipAction("Vertical Patrol Ship");
    	bar.add(button_CH);
    	bar.addSeparator();
    	bar.add(button_BH);
    	bar.addSeparator();
    	bar.add(button_SH);
    	bar.addSeparator();
    	bar.add(button_PH);
    	bar.addSeparator();
    	bar.add(button_CV);
    	bar.addSeparator();
    	bar.add(button_BV);
    	bar.addSeparator();
    	bar.add(button_SV);
    	bar.addSeparator();
    	bar.add(button_PV);
    	add(bar, BorderLayout.NORTH);
    	Color color_B = new Color(c_R_B,c_G_B,c_B_B,225);
    	Color color_S = new Color(c_R_S,c_G_S,c_B_S,225);
    	Color color_O = new Color(c_R_O,c_G_O,c_B_O,225);
    	Color color_F = new Color(c_R_F,c_G_F,c_B_F,225);
      	board = new BattleshipBoard(BattleshipAppFrame.this,color_S, color_O, color_F, color_B, BattleshipApp.peer);
    	add(board,BorderLayout.CENTER);

    	JPanel panel = new JPanel();
      	GridLayout panel_layout = new GridLayout(1,3);
      	panel.setLayout(panel_layout);
      	leftPlayerWin = new JButton(player1 + ": " + countWin1);
      	JButton playBegin = new JButton("START");
      	time = new JButton("time: " + n);
      	playBegin.addActionListener(new startAction("START"));
      	rightPlayerWin = new JButton(player2 + ": " + countWin2);
      	leftPlayerWin.setEnabled(false);
      	rightPlayerWin.setEnabled(false);
      	time.setEnabled(false);
      	panel.add(leftPlayerWin);
      	panel.add(playBegin);
      	panel.add(time);
      	panel.add(rightPlayerWin);
      	add(panel,BorderLayout.SOUTH);
	}
	private class shipAction extends AbstractAction{
		/**
		* for the Actions when clicking starts
		* @param name the name od the actions
		*/
		public shipAction(String name){
			super(name);
		}
		/**
		* for the Actions when clicking toolbar
		* @param event the events
		*/
		public void actionPerformed(ActionEvent event){
			int i, j;
        	String inputr = JOptionPane.showInputDialog(BattleshipAppFrame.this,"Which row", "Row Number", JOptionPane.INFORMATION_MESSAGE);
        	try{
          		i = Integer.parseInt(inputr);
        	} catch(NumberFormatException e){
          	return;
        	}
        	String inputc = JOptionPane.showInputDialog(BattleshipAppFrame.this,"Which column", "column number", JOptionPane.INFORMATION_MESSAGE);
        	try{
          		j = Integer.parseInt(inputc);
        	} catch(NumberFormatException e){
          	return;
        	}
			if(getValue(Action.NAME).equals("Horizontal Carrier")){
				BattleshipBoard.Carrier carrier = board.new Carrier(i,j);
				if(carrier.addableHorizon() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
			if(getValue(Action.NAME).equals("Vertical Carrier")){
				BattleshipBoard.Carrier carrier = board.new Carrier(i,j);
				if(carrier.addableVertical() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
			if(getValue(Action.NAME).equals("Horizontal Battleship")){
				BattleshipBoard.Battleship battleship = board.new Battleship(i,j);
				if(battleship.addableHorizon() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
			if(getValue(Action.NAME).equals("Vertical Battleship")){
				BattleshipBoard.Battleship battleship = board.new Battleship(i,j);
				if(battleship.addableVertical() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
			if(getValue(Action.NAME).equals("Horizontal Submarine")){
				BattleshipBoard.Submarine submarine = board.new Submarine(i,j);
				if(submarine.addableHorizon() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
			if(getValue(Action.NAME).equals("Vertical Submarine")){
				BattleshipBoard.Submarine submarine = board.new Submarine(i,j);
				if(submarine.addableVertical() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
			if(getValue(Action.NAME).equals("Horizontal Patrol Ship")){
				BattleshipBoard.Patrol patrolBoat = board.new Patrol(i,j);
				if(patrolBoat.addableHorizon() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
			if(getValue(Action.NAME).equals("Vertical Patrol Ship")){
				BattleshipBoard.Patrol patrolBoat = board.new Patrol(i,j);
				if(patrolBoat.addableVertical() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Can't create");
				}
				board.change_Button_Board();
			}
		}
	}
	private class startAction extends AbstractAction{
		/**
		* for the Actions when clicking starts
		* @param name the name od the actions
		*/
		public startAction(String name){
			super(name);
		}
		/**
		* actions for clicking buttons/menu/toolbar
		* @param event the event
		*/
		public void actionPerformed(ActionEvent event){
			if(getValue(Action.NAME).equals("START")){
				if(BattleshipApp.mode == Mode.SERVER){
					BattleshipApp.peer.send(String.valueOf(n));
				}
				if(BattleshipApp.mode == Mode.CLIENT){
					n = Integer.parseInt(BattleshipApp.peer.receive());
				}
				if(board.getships() != 17){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "NEED MORE SHIPS");
				}
				else if(board.isFinished() == false){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "In the GAME");
				}
				else{
					BattleshipApp.peer.send("READY");
					String a = BattleshipApp.peer.receive();
				if(a.equals("READY")){
					JOptionPane.showMessageDialog(BattleshipAppFrame.this, "START GAME!");
					if(BattleshipApp.mode == Mode.SERVER){
						board.setHost(1);
					}
					else{
						board.setHost(0);
					}
					board.setFinished(1);
				}
			}
			new Thread(new Runnable() {
       			public void run() {
        			play();}
    			}).start();
			}
		}
		/**
		* using to loop between the game to send/receive messages
		*/
		public void play(){
			while(!board.isFinished()){
 				if(board.hostOrNot()){
 					isMove = false;
  					for(int i = n; i >= 0 && (!isMove); i--){
  						try{
  							Thread.sleep(1000);
  						}
  						catch(Exception e){
     					}
  						time.setText("time:" + i);
  					}
  					if(!isMove){
  						board.clearAll();
  						board.setFinished(0);
  						BattleshipApp.peer.send("lose");
  						if(BattleshipApp.mode == Mode.SERVER){
  							countWin2 ++;
  							rightPlayerWin.setText(player2 + ": " + countWin2);
  						}
  						else{
  							countWin1++;
  							leftPlayerWin.setText(player1 + ": " + countWin1);
  						}
  						break;
  					}
  					board.receiveHost(BattleshipApp.peer.receive());
  					board.change_Button_Board();
  					board.setHost(0);
 				} 
 				else{
 					isMove = false;
 					new Thread(new Runnable() {
       					public void run() {
        					for(int i = n; i >= 0 && (!isMove); i--){
  						try{
  							Thread.sleep(1000);
  						}
  						catch(Exception e){
     					}
  						time.setText("time:" + i);
  						};}
    					}).start();
 					String tmpre = BattleshipApp.peer.receive();
 					isMove = true;
 					if(tmpre.equals("lose")){
 						board.clearAll();
 						board.setFinished(0);
 						if(BattleshipApp.mode == Mode.SERVER){
 							countWin1++;
  							leftPlayerWin.setText(player1 + ": " + countWin1);
  						}
  						else{
  							countWin2 ++;
  							rightPlayerWin.setText(player2 + ": " + countWin2);
  						}
 						return;
 					}
  					String senD = board.receiveNotHost(tmpre);
  					BattleshipApp.peer.send(senD);
  					board.change_Button_Board();
  					board.setHost(1);
				}
			}
			if(board.getbeat() == 17){
				if(BattleshipApp.mode == Mode.SERVER){
					countWin1++;
  					leftPlayerWin.setText(player1 + ": " + countWin1);
				}else{
					countWin2++;
  					rightPlayerWin.setText(player2 + ": " + countWin2);
				}
				board.clearAll();
			}
		}
	}
	private class menuAction extends AbstractAction{
		/**
		* for the Actions when clicking starts
		* @param name the name od the actions
		*/
		public menuAction(String name){
			super(name);
		}
		/**
		* for the Actions when clicking the menus function
		* @param event the events
		*/
		public void actionPerformed(ActionEvent event){
			if(getValue(Action.NAME).equals("About Battleship Game")){
        		JOptionPane.showMessageDialog(BattleshipAppFrame.this, "Worked by Girrrrrro As JAVA HW3 Thank you for testing it! (:D)");
      		}
      		if(getValue(Action.NAME).equals("Configuration")){
        		JDialog dialog = new JDialog(BattleshipAppFrame.this,"A Non-Modal Dialog");
        		dialog.setVisible(true);
        		dialog.setTitle("Configuration");
        		dialog.setSize(500,500);
        		dialog.setLocation(500, 100);
        		JButton colorboardButton = new JButton("Set Board Color");
        		colorboardButton.setBackground(Color.WHITE);
        		colorboardButton.addActionListener(new menuAction("Set Board Color"));
        		colorboardButton.setVisible(true);
        		JButton colorShipButton = new JButton("Set Ship Color");
        		colorShipButton.setBackground(Color.WHITE);
        		colorShipButton.addActionListener(new menuAction("Set Ship Color"));
        		colorShipButton.setVisible(true);
        		JButton colorFalseButton = new JButton("Set False Color");
        		colorFalseButton.setBackground(Color.WHITE);
        		colorFalseButton.addActionListener(new menuAction("Set False Color"));
        		colorFalseButton.setVisible(true);
        		JButton colorShootButton = new JButton("Set Shoot Color");
        		colorShootButton.setBackground(Color.WHITE);
        		colorShootButton.addActionListener(new menuAction("Set Shoot Color"));
        		colorShootButton.setVisible(true);
        		JButton nameButton = new JButton("Change Player Name");
        		nameButton.setBackground(Color.WHITE);
        		nameButton.addActionListener(new menuAction("Change Player Name"));
        		nameButton.setVisible(true);
        		JButton timeButton = new JButton("Change Time");
        		timeButton.setBackground(Color.WHITE);
        		timeButton.addActionListener(new menuAction("Change Time"));
        		timeButton.setVisible(true);
        		GridLayout board_layout = new GridLayout(6,1);
        		dialog.setLayout(board_layout);
        		dialog.add(colorboardButton);
        		dialog.add(colorShipButton);
        		dialog.add(colorFalseButton);
        		dialog.add(colorShootButton);
        		dialog.add(nameButton);
        		dialog.add(timeButton);
      		}
      		if(getValue(Action.NAME).equals("Change Player Name")){
        		String input = JOptionPane.showInputDialog(BattleshipAppFrame.this,"Player name", "Name", JOptionPane.INFORMATION_MESSAGE);
        		if(input != null){
        			if(BattleshipApp.mode == Mode.SERVER){
          			 	player1 = input;
          			 	setTitle("Battleship " + player1);
        			}
        			else{
        				player2 = input;
        				setTitle("Battleship " + player2);
        			}
        			changeSettings();
        		}
      		}
      		if(getValue(Action.NAME).equals("Set Board Color")){
        		Color defaultColor =  new Color(c_R_B,c_G_B,c_B_B,225);
        		Color selected = JColorChooser.showDialog(BattleshipAppFrame.this, "Set Ship Color",
               	defaultColor);
         		if (selected != null){
            		board.setBoardcolor(selected);
            		c_R_B = selected.getRed();
            		c_G_B = selected.getGreen();
            		c_B_B = selected.getBlue();
            		changeSettings();
            		board.change_Button_Board();
         		}
      		}
      		if(getValue(Action.NAME).equals("Set Ship Color")){
        		Color defaultColor =  new Color(c_R_S,c_G_S,c_B_S,225);
        		Color selected = JColorChooser.showDialog(BattleshipAppFrame.this, "Set Ship Color",
               	defaultColor);
         		if (selected != null){
            		board.setshipcolor(selected);
            		c_R_S = selected.getRed();
            		c_G_S = selected.getGreen();
            		c_B_S = selected.getBlue();
            		changeSettings();
            		board.change_Button_Board();
         		}
      		}
      		if(getValue(Action.NAME).equals("Set False Color")){
        		Color defaultColor =  new Color(c_R_F,c_G_F,c_B_F,225);
        		Color selected = JColorChooser.showDialog(BattleshipAppFrame.this, "Set False Color",
               	defaultColor);
         		if (selected != null){
            		board.setfalsecolor(selected);
            		c_R_F = selected.getRed();
            		c_G_F = selected.getGreen();
            		c_B_F = selected.getBlue();
            		changeSettings();
            		board.change_Button_Board();
         		}
      		}
      		if(getValue(Action.NAME).equals("Set Shoot Color")){
        		Color defaultColor =  new Color(c_R_O,c_G_O,c_B_O,225);
        		Color selected = JColorChooser.showDialog(BattleshipAppFrame.this, "Set Shoot Color",
               	defaultColor);
         		if (selected != null){
            		board.setrightcolor(selected);
            		c_R_O = selected.getRed();
            		c_G_O = selected.getGreen();
            		c_B_O = selected.getBlue();
            		changeSettings();
            		board.change_Button_Board();
         		}
      		}
      		if(getValue(Action.NAME).equals("Change Time")){
        		String input = JOptionPane.showInputDialog(BattleshipAppFrame.this,"Change Time", "Seconds", JOptionPane.INFORMATION_MESSAGE);
        		try{
           			n = Integer.parseInt(input);
           			time.setText("time:" + n);
        		} catch(NumberFormatException e){
          			return;
        		}
      		}
		}
	}
}




