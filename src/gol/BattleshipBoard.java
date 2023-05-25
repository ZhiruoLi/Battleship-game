package gol;
import java.util.*;
import java.io.*;
import java.nio.file.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.Color;
/*   */

/**
* code for HW3
* @version <b>1.0</b> rev. 0
* A class to represent the board of BattleShip Game for 2 people base on a 2-d array of int or buttons.
*/
public class BattleshipBoard extends JPanel{
	/**
	* board is the base of Board, contains 0s after initialization.
	*/
	private int SIDE;
	private int STATUS;
	public	Connectable peer; 
	private BattleshipAppFrame f;
	private int ifHost;
	private int countStatus;
	private Color shipColor;
	private Color rightColor;
	private Color falseColor;
	private Color boardColor;
	private int[][] boardSelf;
	private int[][] boardOther;
	private JButton[][] buttonBoardSelf;
	private JButton[][] buttonBoardOther;
	private JPanel panelboardLeft = new JPanel();
	private JPanel panelboardRight = new JPanel();
	private JPanel panelboard = new JPanel();
	private GridLayout board_layout_west;
	private GridLayout board_layout_east;
	private boolean a;
	private int countbeat;
	private int count2, count3, count4, count5, countships;

	/**
	* Carrier is the base 5-length boat, contains 1s after initialization.
	*/
    public class Carrier{
    	int i,j;
    	/**
		* Function to initialize Carrier by fields.
    	* @param ii int, row number.
    	* @param jj int, column number.
    	*/
    	public Carrier(int ii, int jj){
    		i = ii;
    		j = jj;
    	}
    	/**
		* Function to add and estimate is the carrier addable
    	* @return boolean.
    	*/
    	public boolean addableHorizon(){
    		if(i < 1 || j < 1 || j > 6 || count5 > 0 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i][j+1] + boardSelf[i][j+2]
    		 + boardSelf[i][j+3] + boardSelf[i][j+4])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i][j+1]=1;
    		boardSelf[i][j+2] =1;
    		boardSelf[i][j+3] =1;
    		boardSelf[i][j+4] = 1;
    		count5 ++;
    		return true;
    	}
    	/**
		* Function to add and estimate is the carrier addable
    	* @return boolean.
    	*/
    	public boolean addableVertical(){
    		if(i < 1 || j < 1 || i > 6 || count5 > 0 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i+1][j] + boardSelf[i+2][j]
    		 + boardSelf[i+3][j] + boardSelf[i+4][j])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i+1][j] = 1;
    		boardSelf[i+2][j] = 1;
    		boardSelf[i+3][j] = 1;
    		boardSelf[i+4][j] = 1;
    		count5 ++;
    		return true;
    	}
    }
    public class Battleship{
    	int i,j;
    	/**
		* Function to initialize Battleship by fields.
    	* @param ii int, row number.
    	* @param jj int, column number.
    	*/
    	public Battleship(int ii, int jj){
    		i = ii;
    		j = jj;
    	}
    	/**
		* Function to add and estimate is the Battleship addable
    	* @return boolean.
    	*/
    	public boolean addableHorizon(){
    		if(i < 1 || j < 1 || j > 7 || count4 > 0 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i][j+1] + boardSelf[i][j+2] + boardSelf[i][j+3])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i][j+1] = 1;
    		boardSelf[i][j+2] = 1;
    		boardSelf[i][j+3] = 1;
    		count4 ++;
    		return true;
    	}
    	/**
		* Function to add and estimate is the Battleship addable
    	* @return boolean.
    	*/
    	public boolean addableVertical(){
    		if(i < 1 || j < 1 || i > 7 || count4>0 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i+1][j] + boardSelf[i+2][j] + boardSelf[i+3][j])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i+1][j] = 1;
    		boardSelf[i+2][j] = 1;
    		boardSelf[i+3][j] = 1;
    		count4++;
    		return true;
    	}
    }
    public class Submarine{
    	int i,j;
    	/**
		* Function to initialize Submarine by fields.
    	* @param ii int, row number.
    	* @param jj int, column number.
    	*/
    	public Submarine(int ii, int jj){
    		i = ii;
    		j = jj;
    	}
    	/**
		* Function to add and estimate is the submarine addable
    	* @return boolean.
    	*/
    	public boolean addableHorizon(){
    		if(count3 > 1|| i < 1 || j < 1 || j > 8 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i][j+1] + boardSelf[i][j+2])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i][j+1] = 1;
    		boardSelf[i][j+2] = 1;
			count3++;
    		return true;
    	}
    	/**
		* Function to add and estimate is the submarine addable
    	* @return boolean.
    	*/
    	public boolean addableVertical(){
    		if(count3 > 1||i < 1 || j < 1 || i > 8 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i+1][j] + boardSelf[i+2][j])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i+1][j] = 1;
    		boardSelf[i+2][j] = 1;
    		count3++;
    		return true;
    	}
    }
    public class Patrol{
    	int i,j;
    	/**
		* Function to initialize Patrol by fields.
    	* @param ii int, row number.
    	* @param jj int, column number.
    	*/
    	public Patrol(int ii, int jj){
    		i = ii;
    		j = jj;
    	}
    	/**
		* Function to add and estimate is the Partol addable
    	* @return boolean.
    	*/
    	public boolean addableHorizon(){
    		if(i < 1 || j < 1 || j > 9 || count2 > 0 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i][j+1])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i][j+1] = 1;
    		count2++;
    		return true;
    	}
    	/**
		* Function to add and estimate is the Patrol addable
    	* @return boolean.
    	*/
    	public boolean addableVertical(){
    		if(i < 1 || j < 1 || i > 9|| count2 > 0 || STATUS == 1){
    			return false;
    		}
    		if((boardSelf[i][j] + boardSelf[i+1][j])!= 0){
    			return false;
    		}
    		boardSelf[i][j] = 1;
    		boardSelf[i+1][j] = 1;
    		count2++;
    		return true;
    	}
    }
    /**
		* Function to initialize Carrier by fields.
    	* @param f_ frame.
    	* @param boardSHIP color of the ship
    	* @param boardRIGHT color of the shooted ship
    	* @param boardFALSE color of the wrong shooted
    	* @param boardCOLOR color of the board
    	* @param c for connect
    	*/
    public BattleshipBoard(BattleshipAppFrame f_, Color boardSHIP, 
    	Color boardRIGHT, Color boardFALSE, Color boardCOLOR, Connectable c){
    	SIDE = 11;
    	STATUS = 0;
    	count2 = 0;
    	f = f_;
    	peer = c;
    	count3 = 0;
    	count4 = 0;
    	count5 = 0;
    	countbeat = 0;
    	ifHost = 0;
    	shipColor = boardSHIP;
		rightColor = boardRIGHT;
		falseColor = boardFALSE;
		boardColor = boardColor;
		//this.balea = balEA;
		boardSelf = new int[SIDE][SIDE];
    	boardOther = new int[SIDE][SIDE];
    	buttonBoardSelf = new JButton[SIDE][SIDE];
    	buttonBoardOther = new JButton[SIDE][SIDE];
    	GridLayout board_layout_Big = new GridLayout(1,2);
    	board_layout_Big.setHgap(20);
    	setLayout(board_layout_Big);
    	board_layout_east = new GridLayout(SIDE,SIDE);
    	board_layout_west = new GridLayout(SIDE,SIDE);
    	panelboardLeft.setLayout(board_layout_west);
    	panelboardRight.setLayout(board_layout_east);

        for(int i = 0; i < SIDE; i++){
			for(int j = 0; j <SIDE;j++){
				if(i == 0){
					JButton button = new JButton(String.valueOf(j));
    				button.setBackground(boardColor);
					button.setOpaque(true);
					button.setEnabled(false);
					panelboardLeft.add(button);
					buttonBoardSelf[i][j] = button;

					button = new JButton(String.valueOf(j));
					button.setBackground(boardColor);
					button.setOpaque(true);
					button.setEnabled(false);
					panelboardRight.add(button);
					buttonBoardOther[i][j] = button;
				}
				else if(j == 0){
					JButton button = new JButton(String.valueOf(i));
    				button.setBackground(boardColor);
					button.setOpaque(true);
					button.setEnabled(false);
					panelboardLeft.add(button);
					buttonBoardSelf[i][j] = button;

					button = new JButton(String.valueOf(i));
					button.setBackground(boardColor);
					button.setOpaque(true);
					button.setEnabled(false);
					panelboardRight.add(button);
					buttonBoardOther[i][j] = button;
				}
				else{
					boardSelf[i][j] = 0;
					boardOther[i][j] = 0;
					buttonBoardSelf[i][j] = addButtonWithoutListener(i, j, panelboardLeft);
					buttonBoardOther[i][j] = addButton(i, j, panelboardRight);
				}
			}
		}
    	add(panelboardLeft);
    	add(panelboardRight);
    	change_Button_Board();
    }

    private JButton addButton(int row, int col, JPanel p){
    	JButton button = new JButton();
    	button.setBackground(boardColor);
		button.setOpaque(true);
		button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		button.addActionListener(new board_button(row,col));
		p.add(button);
		return button;
    }
    private JButton addButtonWithoutListener(int row, int col, JPanel p){
    	JButton button = new JButton();
    	button.setBackground(boardColor);
		button.setOpaque(true);
		button.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		p.add(button);
		return button;
    }
      /**
		* Function to set the identity.
    	* @param a set the identity
    	*/
    public void setHost(int a){
    	ifHost = a;
    }
    /**
		* Function to determine if it is a host.
    	* @return boolean of host or not
    	*/
    public boolean hostOrNot(){
    	if(this.ifHost == 0){
    		return false;
    	}
    	return true;
    }
    private String sendIfHost(int row, int col){
    	String command = row + "," + col;
    	return command;
    }
    /**
		* Function use with peer.
    	* @param s the messege it get
    	* @return s is a String to be send
    	*/
    public String receiveNotHost(String s){
    	String[] temp = s.split(",");
    	int i, j;
    	i = Integer.parseInt(temp[0]);
    	j = Integer.parseInt(temp[1]);
    	if(boardSelf[i][j] == 1){
    		boardSelf[i][j] = 3;
    		s=s+",3";
    		return s;
    	}
    	else{
    		boardSelf[i][j] = 2;
    		s=s+",2";
    		return s;
    	}
    }
    /**
		* Function use with peer and rearrange the board.
    	* @param s the messege it get.
    	*/
    public void receiveHost(String s){
    	String[] temp = s.split(",");
    	int i, j, v;
    	i = Integer.parseInt(temp[0]);
    	j = Integer.parseInt(temp[1]);
    	v = Integer.parseInt(temp[2]);
    	if(v == 3){
    		boardOther[i][j] = 3;
    	}
    	else{
    		boardOther[i][j] = 2;
    	}
    }

	/**
	* Function to change Button Board after change.
	* change button_board to times step.
    */
	public void change_Button_Board(){
		countbeat = 0;
		countships = 0;
		for(int i = 1; i < SIDE; i++){
			for(int j = 1; j < SIDE;j++){
				if(boardSelf[i][j] == 0){
					buttonBoardSelf[i][j].setBackground(boardColor);
				}
				if(boardOther[i][j] == 0){
					buttonBoardOther[i][j].setBackground(boardColor);
				}
				if(boardSelf[i][j] == 1){
					countships ++;
					buttonBoardSelf[i][j].setBackground(shipColor);
				}
				if(boardOther[i][j] == 1){
					buttonBoardOther[i][j].setBackground(shipColor);
				}
				if(boardSelf[i][j] == 2){
					buttonBoardSelf[i][j].setBackground(falseColor);
				}
				if(boardOther[i][j] == 2){
					buttonBoardOther[i][j].setBackground(falseColor);
				}
				if(boardSelf[i][j] == 3){
					buttonBoardSelf[i][j].setBackground(rightColor);
				}
				if(boardOther[i][j] == 3){
					countbeat += 1;
					buttonBoardOther[i][j].setBackground(rightColor);
				}
			}
		}
	}
	/**
		* Function use to check the game status.
    	* @return boolean 
    	*/
	public boolean isFinished(){
		if(STATUS == 0){
			return true;
		}
		else{
			if(countbeat == 17){
				STATUS = 0;
				return true;
			}
		}
		return false;
	}
	/**
		* Function to get beat number of ship grids
    	* @return int of the grid bumbers
    	*/
	public int getbeat(){
		return countbeat;
	}
	/**
		* Function to get number of ship grids
    	* @return int of the grid bumbers
    	*/
	public int getships(){
		return countships;
	}
	/**
		* Function to set the status of the Game
    	* @param f the messege it get
    	*/
	public void setFinished(int f){
		STATUS = f;
	}
	private class board_button implements ActionListener{
		private int col;
		private int row;
		public board_button(int r, int c){
			col = c;
			row = r;
		}
		@Override
		public void actionPerformed(ActionEvent event){
			if(STATUS == 1 && boardOther[row][col] == 0 && f.isMove == false){
				if(hostOrNot() == true){
					f.isMove = true;
					peer.send(sendIfHost(row,col));
				}
			}
      	}
	}
	/**
	* Funciton clean all data
    */
	public void clearAll(){
		for(int i = 1; i < SIDE; i++){
			for(int j = 1; j < SIDE;j++){
				boardSelf[i][j] = 0;
				boardOther[i][j] = 0;
			}
		}
		count2 = 0;
		count3 = 0;
		count4 = 0;
		count5 = 0;
		change_Button_Board();
	}
	/**
		* Function use to set color
    	* @param c color
    	*/
	public void setBoardcolor(Color c){
		boardColor = c;
	}
	/**
		* Function use to set color
    	* @param c color
    	*/
	public void setshipcolor(Color c){
		shipColor = c;
	}
	/**
		* Function use to set color
    	* @param c color
    	*/
	public void setrightcolor(Color c){
		rightColor = c;
	}
	/**
		* Function use to set color
    	* @param c color
    	*/
	public void setfalsecolor(Color c){
		falseColor = c;
	}
}