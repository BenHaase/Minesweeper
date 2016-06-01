package p5MineSweeper;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

public class mineFrame extends JFrame{ //main class for game
	private static mineFrame ms; //instance of the jframe declared outside of main for access purposes
	//main method to create the and initialize the board and jframe
	public static void main(String[] args){
		Random rand = new Random(); //initialize random
		int mines = 5; //set number of mines to be placed
		int ro,rt; //hold random numbers for mine placement
		char[][] board = new char[8][8]; //create board
		for(int i=0; i<8; i++) for(int k=0; k<8; k++) board[i][k] = ' '; //initialize board
		//set mines on board
		for(int i=0; i<mines; i++){
			ro = rand.nextInt(8);
			rt = rand.nextInt(8);
			if(board[ro][rt]!='m') board[ro][rt] = 'm';
			else i--;
		}
		//set mine adjacent board values
		for(int i=0; i<8; i++) for(int k=0; k<8; k++){
			int m = 0;
			for(int x=-1; x<2; x++) for(int y=-1; y<2; y++) if(x+i>-1 && y+k>-1 && x+i<8 && y+k<8){if(board[i+x][k+y]=='m') m++;}
			if(board[i][k]!='m' && m!=0){board[i][k] = (char)(m+48);}
		}
		//initialize frame
		ms = new mineFrame();
		ms.setTitle("MineSweeper");
		ms.setSize(450, 400);
		ms.setLocationRelativeTo(null);
		ms.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ms.setVisible(true);
		//assosiate button value (v) with parrallel board value
		for(int i=0; i<8; i++)for(int k=0; k<8; k++)ms.gb[i][k].v = board[i][k];
	}
	
	private gbut[][] gb = new gbut[8][8]; //create custom button array, declared here for access
	//button class
	public class gbut extends JButton{
		char v = '?'; //value associated with board, initialed to ? but set to board value
		int x; //x position in array
		int y; //y position in array
		boolean cheating = false; //determine if show is clicked
		boolean clicked = false; //determine if button has been clicked
		gbut(String n, int x, int y){ //button constructor, adds action listener
			this.setText(n);
			this.x = x;
			this.y = y;
			this.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					showval();}
				});
		}
		public void showval(){ //method invoked by action listener
			this.setEnabled(false);
			if(this.v=='m')this.setText("B");
			else this.setText(String.valueOf(v));
			this.clicked=true;
			this.expand();
			win();
		}
		public void expand(){ //expansion method invoked from showval(), recursive expansion
			if(!this.clicked && this.v!='m'){ //show value and set clicked for this button
				this.setEnabled(false);
				this.setText(String.valueOf(v));
				this.clicked=true;
			}
			if(this.v==' '){ //expand empty value
				for(int i=-1; i<2; i++){
					for(int k=-1; k<2; k++){
						if(i+this.x>-1 && this.y+k>-1 && this.x+i<8 && this.y+k<8 && !gb[this.x+i][this.y+k].clicked) gb[this.x+i][this.y+k].expand();
					}
				}
			}
		}
		public void cheater(){ //method to toggle cheating (show is clicked)
			if(this.v=='m' && !this.clicked && !this.cheating){
				this.setText("m");
				cheating = true;
			}
			else if(this.v=='m' && !this.clicked && this.cheating){
				this.setText("?");
				cheating = false;
			}
		}
	}
	
	public mineFrame(){ //frame constructor
		JPanel mf = new JPanel(); //panel to contain button array
		mf.setLayout(new GridLayout(9,9));
		for(int i=0; i<8; i++)for(int k=0; k<8; k++){ //initialize button array
			gb[i][k] = new gbut("?", i, k);
			mf.add(gb[i][k]);
		}
		JPanel ss = new JPanel(); //panel to contain start and show buttons
		ss.setLayout(new GridLayout(1,2));
		JButton start = new JButton("Start"); //create start button
		start.addActionListener(new ActionListener(){ //associate action listener
			public void actionPerformed(ActionEvent e){
				reset();}
			});
		ss.add(start);
		JButton show = new JButton("Show"); //create show button
		show.addActionListener(new ActionListener(){ //associate action listener
			public void actionPerformed(ActionEvent e){
				showM();}
			});
		ss.add(show);
		JPanel gs = new JPanel(new BorderLayout()); //panel to contain button array panel, and start/show panel
		gs.add(mf, BorderLayout.CENTER);
		gs.add(ss, BorderLayout.NORTH);
		this.add(gs);
	}
	public void reset(){ //method associated with start button action listener
		ms.dispose(); //dispose of old frame
		String[] args = {};
		main(args); //this is probably frowned upon at best but it worked well and kept me from writing a long reset method
	}
	public void showM(){ //method associated with show button action listener
		for(int i=0; i<8; i++) for(int k=0; k<8; k++) gb[i][k].cheater(); //calls cheater method for each button (if mine&&notclicked show m)
	}
	public void win(){ //method to show winner if you win or lose
		boolean loser = false;
		for(int i=0; i<8; i++) for(int k=0; k<8; k++){
			if(gb[i][k].clicked && gb[i][k].v=='m') loser = true;
			if(!gb[i][k].clicked && gb[i][k].v!='m') return;
		}
		if(loser) JOptionPane.showMessageDialog( ms, "Loser!");
		else JOptionPane.showMessageDialog( ms, "Winner!");
	}
}
