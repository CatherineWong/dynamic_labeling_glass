

/*
 * QRScanAuthentication: Uses the ZXing library to scan and read a QR code encoding the username and a 64-character 
 * hash that can function as an authentication key to log in
 */
package com.dynamic_labeling;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.glass.app.Card;

public class QR_Scan extends Activity{
	public static final int ZXING_INTENT_CODE = 0;
	
	Card instructionsCard;
	boolean instructionsCardDisplayed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		initializeInstructionsCard();
		super.onCreate(savedInstanceState);
		setContentView(instructionsCard.toView());
	}
	

	//Set up the initial instructions card
	public void initializeInstructionsCard(){
		instructionsCard = new Card(this);
		instructionsCard.setText("Tap to scan QR code!");
		instructionsCard.setFootnote("Swipe to return to main menu");
		instructionsCardDisplayed = true;
	}
	
	
	
	//Allow user to tap to start, or swipe back to return to the main menu
	@Override
    public boolean onKeyDown(int keycode, KeyEvent event) {
        if (keycode == KeyEvent.KEYCODE_DPAD_CENTER) {
        	if(!instructionsCardDisplayed){
        		initializeInstructionsCard();
        		setContentView(instructionsCard.toView());
        	} else {
        		instructionsCardDisplayed = false;
        		launchQRCodeReader();
        	}
        	launchQRCodeReader();
        	
            return true;
        } else if(keycode == KeyEvent.KEYCODE_BACK){
        	Intent returnToMain = new Intent("com.dynamic_labeling.MAIN_MENU");
			startActivity(returnToMain);
        }
        return false;
    }
    
	//Launch the ZXing intent to scan the code
	public void launchQRCodeReader(){
		try{
			Intent zxingIntent = new Intent("com.google.zxing.client.android.SCAN");
			zxingIntent.putExtra("SCAN_MODE", "QR_CODE_MODE, PRODUCT_MODE");
			startActivityForResult(zxingIntent, ZXING_INTENT_CODE);
			
		} catch (Exception e){
			e.printStackTrace();
			Intent returnToMain = new Intent("com.dynamic_labeling.MAIN_MENU");
			startActivity(returnToMain);
		}
		
	}

	//Retrieve the string data from the QR code reader
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		String result;
		if(requestCode == ZXING_INTENT_CODE){
			if(resultCode==RESULT_OK){
				result = data.getStringExtra("SCAN_RESULT");
				instructionsCard.setText(result);
			} else if(resultCode == RESULT_CANCELED){
				instructionsCard.setText("Unable to read QR code!");
				instructionsCard.setFootnote("Tap to try again");
			}
		}
		setContentView(instructionsCard.toView());
	}
	
	

	
	 
	
}

