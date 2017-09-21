package minesweeper;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.ImageIcon.*;

class Game extends JFrame implements MouseListener{
	private int width=400,height=450,mapRow=9,mapCol=9; 
	private JButton button[][]=new JButton[width][height];
	private int bombCount=10; 
	private JLabel bombnumber=new JLabel("Mines left: "+bombCount);
	private ImageIcon hex = new ImageIcon("hex.png");
	private ImageIcon bomb = new ImageIcon("bomb.png");
	private ImageIcon flag = new ImageIcon("flag.png");
	private int map[][]=new int[mapRow][mapCol]; 
	private boolean buttonIsPress[][]=new boolean[mapRow][mapCol]; 
	private int mapAroundBomb[][]=new int[mapRow][mapCol]; 
	private int direct[][]={{0,0},{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,-1},{-1,1},{1,-1}};
	
	private int timeCount=0; 
	private int timeContinue=1; 
	
	Game(){
	
		setSize(width, height); 
		setResizable(false); 
		setDefaultCloseOperation(EXIT_ON_CLOSE); 
		setTitle("Minesweeper"); 
		setLocationRelativeTo(this); 
		
		
		JPanel topPanel=new JPanel();
		
		bombnumber.setText("Mines left:"+bombCount); 
		topPanel.add(bombnumber);
		
		JButton restart=new JButton("New game"); 
		restart.setActionCommand("r"); 
		restart.addMouseListener(this); 
		topPanel.add(restart);

		JLabel time=new JLabel("Time passed: "); 
		TimerTask timertask=new TimerTask(){
			public void run(){
				if(timeContinue==1)time.setText("Time passed:"+(timeCount++));
			}
		};
		new Timer().scheduleAtFixedRate(timertask,0,1000);
		topPanel.add(time);
		
		add(topPanel,BorderLayout.NORTH);

		
		JPanel centerButtonPanel = new JPanel();
        centerButtonPanel.setLayout(new GridLayout(mapRow,mapCol));
        for(int i=0;i<mapRow;i++){
        	for(int j=0;j<mapCol;j++){
        		button[i][j]=new JButton();
        		

        		button[i][j].setActionCommand(i+" "+j); 
        		button[i][j].addMouseListener(this); 
        		centerButtonPanel.add(button[i][j]);
        	}
        }
        add(centerButtonPanel,BorderLayout.CENTER);
        
        
        setMap();
        aroundBomb();
        
        setVisible(true);
	}
	

	private void setMap(){
		int count=0;
		while(count!=10){
			int x=(int)(Math.random()*9),y=(int)(Math.random()*9);
			if(map[x][y]==0){
				map[x][y]=1;
				count++;
			}
		}
	}
	

	private void aroundBomb(){
		for(int i=0;i<mapRow;i++){
			for(int j=0;j<mapCol;j++){
				if(map[i][j]==1){
					mapAroundBomb[i][j]=-1; 
				}else{
					for(int k=0;k<direct.length;k++){
						int row=i+direct[k][0],col=j+direct[k][1];
						if((row>=0 && row<mapCol && col>=0 && col<mapCol) && map[row][col]==1) mapAroundBomb[i][j]++;
					}
				}
			}
		}
	}
	private void restart(){
		timeCount=1;
		timeContinue=1;
		for(int i=0;i<mapRow;i++) Arrays.fill(map[i],0); //initial map array
		for(int i=0;i<mapRow;i++) Arrays.fill(buttonIsPress[i],false); //initial buttonIsPress
		for(int i=0;i<mapRow;i++) Arrays.fill(mapAroundBomb[i],0); //initial mapAroundBomb
		
		for(int i=0;i<mapRow;i++){
        	for(int j=0;j<mapCol;j++){
        		button[i][j].setIcon(null);
        		button[i][j].setBackground(Color.WHITE);
        		button[i][j].setText("");
        	}
        }
		setMap();
		aroundBomb();
        bombCount=10;
        bombnumber.setText("Mines left: "+bombCount);
        
	}

	

	@Override
	public void mouseClicked(MouseEvent e){
		String command[]=((JButton)e.getSource()).getActionCommand().split(" ");
		if(command[0].equals("r")){
	
			
			restart();
		}else{
			int row=Integer.parseInt(command[0]),col=Integer.parseInt(command[1]);
			if(e.getButton()==MouseEvent.BUTTON1){
				if(map[row][col]==1 && !buttonIsPress[row][col]){
					
					
					button[row][col].setBackground(Color.RED); 
					for(int i=0;i<mapRow;i++)for(int j=0;j<mapCol;j++) if(map[i][j]==1) button[i][j].setIcon(bomb); 
					timeContinue=0; 
					JOptionPane.showMessageDialog(null, "You found the mine."); 
					restart(); 
				}else{
					if(mapAroundBomb[row][col]==0 && !buttonIsPress[row][col]){
						
						
						Vector<postion> vector=new Vector<postion>(); 
						vector.add(new postion(row,col));
						
						for(int i=0;i<vector.size();i++){
							for(int j=0;j<direct.length;j++){
								int tempRow=direct[j][0]+vector.get(i).getRow(),tempCol=direct[j][1]+vector.get(i).getCol();
								if((tempRow>=0 && tempRow<mapRow) && (tempCol>=0 && tempCol<mapCol) && mapAroundBomb[tempRow][tempCol]==0){
									boolean flag=false;
								
									for(int k=0;k<vector.size();k++){
										if(tempRow==vector.get(k).getRow() && tempCol==vector.get(k).getCol()){
											flag=true;
											break;
										}
									}
									if(!flag) vector.add(new postion(tempRow,tempCol)); 
								}
							}
						}
				
						for(int i=0;i<vector.size();i++){
							for(int j=0;j<direct.length;j++){
								int tempRow=direct[j][0]+vector.get(i).getRow(),tempCol=direct[j][1]+vector.get(i).getCol();
								if((tempRow>=0 && tempRow<mapRow) && (tempCol>=0 && tempCol<mapCol)){
								
									if(mapAroundBomb[tempRow][tempCol]!=0) 
										button[tempRow][tempCol].setText(Integer.toString(mapAroundBomb[tempRow][tempCol]));
									button[tempRow][tempCol].setBackground(Color.GRAY); 
									buttonIsPress[tempRow][tempCol]=true; 
								}
							}
						}
					}else if(!buttonIsPress[row][col]){
						
						
						button[row][col].setText(Integer.toString(mapAroundBomb[row][col])); 
						button[row][col].setBackground(Color.GRAY); 
						buttonIsPress[row][col]=true; 
					}
				}
			}else if(buttonIsPress[row][col] && e.getButton()==MouseEvent.BUTTON2){
				
				
				buttonIsPress[row][col]=false;
				button[row][col].setBackground(Color.WHITE); 
				bombCount++; 
				bombnumber.setText("Mines left: "+bombCount); 
			}else if(e.getButton()==MouseEvent.BUTTON3 && !buttonIsPress[row][col]){
				
				
				((JButton)e.getSource()).setIcon(flag); 
				buttonIsPress[row][col]=true; 
				bombCount--; 
				bombnumber.setText("Mines left: "+bombCount);

				
				if(bombCount==0){
					boolean endGame=true;
					
					for(int i=0;i<mapRow;i++){
						for(int j=0;j<mapCol;j++){
							if(map[i][j]==1)if(buttonIsPress[i][j]!=true) endGame=false;
						}
					}
					if(endGame){
						timeContinue=0; 
						JOptionPane.showMessageDialog(null, "You found the bomb"); 
						restart(); 
					}
				}
			}
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub	
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}
}

class postion{
	private int row,col;
	postion(int row,int col){
		this.row=row;
		this.col=col;
	}
	public int getRow(){
		return row;
	}
	public int getCol(){
		return col;
	}
}
class minesweeper{
	public static void main(String args[]){
		Game g=new Game();
	}
}