package com.example1.lathinh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import com.aviy.memory.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class MainActivity extends Activity {
    private static int ROW_COUNT = -1;
	private static int COL_COUNT = -1;
	private Context context;
	private Drawable backImage;
	private int [] [] cards;
	private List<Drawable> images;
	private Card firstCard;
	private Card seconedCard;
	private ButtonListener buttonListener;
	
	private static Object lock = new Object();
	
	int turns;
	private TableLayout mainTable;
	private UpdateCardsHandler handler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
      
        
        handler = new UpdateCardsHandler();
        loadImages();
        setContentView(R.layout.main);

       backImage =  getResources().getDrawable(R.drawable.icon);      
       buttonListener = new ButtonListener();
        
        mainTable = (TableLayout)findViewById(R.id.TableLayout03);
        
        
        context  = mainTable.getContext();
        // tao spinner
       	 Spinner s = (Spinner) findViewById(R.id.Spinner01);
       	 //tao adapter cho spinner
	        ArrayAdapter adapter = ArrayAdapter.createFromResource(
	                this, R.array.type, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        //add adapter vap spinner
	        s.setAdapter(adapter);
	        
	        //bat su kien click vao item trong spinner
	        s.setOnItemSelectedListener(new OnItemSelectedListener(){
	        	
	    	  @Override
	    	  public void onItemSelected(
	    			  android.widget.AdapterView<?> arg0, 
	    			  View arg1, int pos, long arg3){
	    		  
	    		  ((Spinner) findViewById(R.id.Spinner01)).setSelection(0);
	    		 //x la cot,y la hang 
	  			int x,y;
	  			
	  			switch (pos) {
				case 1:
					x=4;y=4;
					break;
				case 2:
					x=4;y=5;
					break;
				case 3:
					x=4;y=6;
					break;
				case 4:
					x=5;y=6;
					break;
				case 5:
					x=6;y=6;
					break;
				default:
					return;
				}
	  			newGame(x,y);
	  			
	  		}


			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}

	  	});
    }
    
    private void newGame(int x, int y) {
    	ROW_COUNT = y;
    	COL_COUNT = x;
    	// khoi tao ma tran voi y hang va x cot
    	cards = new int [ROW_COUNT] [COL_COUNT];
    	TableRow tr = ((TableRow)findViewById(R.id.TableRow03));
    	tr.removeAllViews();
    	
    	mainTable = new TableLayout(context);
    	tr.addView(mainTable);
    	
    	 for (int i = 0; i < ROW_COUNT; i++) {
    		 mainTable.addView(createRow(i));
          }
    	 
    	 firstCard=null;
    	 loadCards();
    	 
    	 turns=0;
    	 ((TextView)findViewById(R.id.tv1)).setText("Tries: "+turns);
    	 
			
	}
    //load anh tu drawable vap mang
    private void loadImages() {
    	images = new ArrayList<Drawable>();
    	
    	images.add(getResources().getDrawable(R.drawable.card1));
    	images.add(getResources().getDrawable(R.drawable.card2));
    	images.add(getResources().getDrawable(R.drawable.card3));
    	images.add(getResources().getDrawable(R.drawable.card4));
    	images.add(getResources().getDrawable(R.drawable.card5));
    	images.add(getResources().getDrawable(R.drawable.card6));
    	images.add(getResources().getDrawable(R.drawable.card7));
    	images.add(getResources().getDrawable(R.drawable.card8));
    	images.add(getResources().getDrawable(R.drawable.card9));
    	images.add(getResources().getDrawable(R.drawable.card10));
    	images.add(getResources().getDrawable(R.drawable.card11));
    	images.add(getResources().getDrawable(R.drawable.card12));
    	images.add(getResources().getDrawable(R.drawable.card13));
    	images.add(getResources().getDrawable(R.drawable.card14));
    	images.add(getResources().getDrawable(R.drawable.card15));
    	images.add(getResources().getDrawable(R.drawable.card16));
    	images.add(getResources().getDrawable(R.drawable.card17));
    	images.add(getResources().getDrawable(R.drawable.card18));
    	images.add(getResources().getDrawable(R.drawable.card19));
    	images.add(getResources().getDrawable(R.drawable.card20));
    	images.add(getResources().getDrawable(R.drawable.card21));
		
	}

	private void loadCards(){
		try{
	    	int size = ROW_COUNT*COL_COUNT/2;
	    	
	    	Log.i("loadCards()","size=" + size);
	    	// luu vi tri diem tu[0][0]->[row-1][cot-1]
	    	ArrayList<Point> listpos=new ArrayList<Point>();
	    		for(int i=0;i<ROW_COUNT;i++){
	    			for(int j=0;j<COL_COUNT;j++){
	    				Point p=new Point(i, j);
	    				listpos.add(p);
	    			}
	    		}
	    	
	    	ArrayList<Integer> list = new ArrayList<Integer>();
	    	Random r = new Random();
	    	// tao danh sach cac anh random de choi
	    	for(int i=0;i<size;i++){
	    		list.add(r.nextInt(images.size()));
	    	}
	    	//1 anh thi dc gan vao 2 vi tri bat ky trong mang
	    	for(int i=0;i<size;i++){
	    		int t1=r.nextInt(listpos.size());
	    		cards[listpos.get(t1).x][listpos.get(t1).y]=list.get(i);
	    		//Log.i("cardvalue","card["+listpos.get(t1).x+"]["+listpos.get(t1).y+"]"+list.get(i));
	    		listpos.remove(t1);
	    		
	    		int t2=r.nextInt(listpos.size());
	    		cards[listpos.get(t2).x][listpos.get(t2).y]=list.get(i);
	    		//Log.i("cardvalue","card["+listpos.get(t2).x+"]["+listpos.get(t2).y+"]"+list.get(i));
	    		listpos.remove(t2);
	    		
	    		
	    	}
	    	
	    }

		catch (Exception e) {
			Log.e("loadCards()", e+"");
		}
		
    }
    
    private TableRow createRow(int i){
    	 TableRow row = new TableRow(context);
    	 row.setHorizontalGravity(Gravity.CENTER);
         //tao va in cac button tren 1 hang
         for (int j = 0; j < COL_COUNT; j++) {
		         row.addView(createImageButton(i,j));
         }
         return row;
    }
    
    private View createImageButton(int i, int j){
    	Button button = new Button(context);
    	button.setBackgroundDrawable(backImage);
    	button.setId(100*i+j);
    	button.setOnClickListener(buttonListener);
    	return button;
    }
    
    class ButtonListener implements OnClickListener {
    	
		@Override
		public void onClick(View v) {
			
			synchronized (lock) {
				if(firstCard!=null && seconedCard != null){
					return;
				}
				int id = v.getId();
				int i = id/100;
				int j = id%100;
				turnCard((Button)v,i,j);
			}
			
		}

		private void turnCard(Button button,int i, int j) {
			//Log.i("sothu tu anh", (String.valueOf(cards[i][j]))+"   i="+i+",j="+j);
			// hien anh khi click
			button.setBackgroundDrawable(images.get(cards[i][j]));
			
			if(firstCard==null){
				firstCard = new Card(button,i,j);
			}
			else{ 
				
				if(firstCard.x == i && firstCard.y == j){
					return; //the user pressed the same card
				}
					
				seconedCard = new Card(button,i,j);
				
				turns++;
				((TextView)findViewById(R.id.tv1)).setText("Tries: "+turns);
				
			
				TimerTask tt = new TimerTask() {
					
					@Override
					public void run() {
						try{
							synchronized (lock) {
							  handler.sendEmptyMessage(0);
							}
						}
						catch (Exception e) {
							Log.e("E1", e.getMessage());
						}
					}
				};
				
				  Timer t = new Timer(false);
			        t.schedule(tt, 1300);
			}
			
				
		   }
			
		}
    
    class UpdateCardsHandler extends Handler{
    	
    	@Override
    	public void handleMessage(Message msg) {
    		synchronized (lock) {
    			checkCards();
    		}
    	}
    	//kiem tra 2 anh trong ma tran cards co = nhua hay ko, neu = thi cho 2 button ko hien nua
    	// nguoc lai thi cho no hien ve anh mac dinh
    	 public void checkCards(){
    	    	if(cards[seconedCard.x][seconedCard.y] == cards[firstCard.x][firstCard.y]){
    				firstCard.button.setVisibility(View.INVISIBLE);
    				seconedCard.button.setVisibility(View.INVISIBLE);
    			}
    			else {
    				seconedCard.button.setBackgroundDrawable(backImage);
    				firstCard.button.setBackgroundDrawable(backImage);
    			}
    	    	
    	    	firstCard=null;
    			seconedCard=null;
    	    }
    }
    
   
    
    
}