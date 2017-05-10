package com.simcity.simcityexpert;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@SuppressLint("SdCardPath")
public class MainActivity extends Activity {

	private Button buttonShip;
	private ImageView viewShip;

	private Button buttonParis;
	private ImageView viewParis;

	private Button buttonTokyo;
	private ImageView viewTokyo;
	
	private Bitmap bitMapShip;
	private Bitmap bitMapParis;
	private Bitmap bitMapTokyo;

	private Bitmap bitMapShipResized;
	private Bitmap bitMapParisResized;
	private Bitmap bitMapTokyoResized;
	
	public static String root_folder = "/sdcard/SimCityExpert/";

	private int picType;
	
	private int imgWidth = 640 * 6;//130;//640;//130
	private int imgHeight = 320 * 4;//175;//480;//175

	private float scaleWidth = 0.25f;//3120 ((float) newWidth) / width; 
	private float scaleHeight = 0.25f;//4208

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// shipping
		buttonShip = (Button) findViewById(R.id.button1);
		viewShip = (ImageView) findViewById(R.id.imageView1);
		buttonShip.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				picType = 1;
				 
				doOnClick(v);
			}
		});

		// Button01 Paris
		buttonParis = (Button) findViewById(R.id.Button01);
		viewParis = (ImageView) findViewById(R.id.ImageView01);
		buttonParis.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picType = 2;
				doOnClick(v);
			}
		});

		// Button01 Paris
		buttonTokyo = (Button) findViewById(R.id.button2);
		viewTokyo = (ImageView) findViewById(R.id.ImageView05);
		buttonTokyo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				picType = 3;
				doOnClick(v);
			}
		});
		String folder = root_folder + "SimCity_1.jpg";
		
		setImageView(folder, viewShip, bitMapShip, bitMapShipResized);
		
		folder = root_folder + "SimCity_2.jpg";
		setImageView(folder, viewParis, bitMapParis, bitMapParisResized);
		folder = root_folder + "SimCity_3.jpg";
		setImageView(folder, viewTokyo, bitMapTokyo, bitMapTokyoResized);
	}

	private void setImageView(String folder, ImageView viewShip, Bitmap bitmap, Bitmap bitMapResized) {
		File file = new File(folder);
		 
		FileInputStream inStream = null;
		if (file.exists()){
			
			try {
				inStream = new FileInputStream(folder);
				
				byte[] buffer = new byte[1024];
				int len = -1;
				ByteArrayOutputStream outStream = new ByteArrayOutputStream();
				while ((len = inStream.read(buffer)) != -1) {
				    outStream.write(buffer, 0, len);
				}
				byte[] data = outStream.toByteArray();
				outStream.close();
				inStream.close();
				Matrix matrix = new Matrix();  
		        matrix.postScale(scaleWidth, scaleHeight); 
		        if (bitmap != null) bitmap.recycle();
		        if (bitMapResized != null) bitMapResized.recycle();
		        
		        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
				        null);
				
				int tmpWidth = imgWidth > bitmap.getWidth() ? bitmap.getWidth() : imgWidth;
				int tmpHeight = imgHeight > bitmap.getHeight() ? bitmap.getHeight() : imgHeight; 
				bitMapResized = Bitmap.createBitmap(bitmap, 0, 0, tmpWidth,  
						tmpHeight, matrix, true);
				 
				viewShip.setImageBitmap(bitMapResized);//
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SimCityLog.rec("setImageView() -- FileNotFoundException " + folder); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				SimCityLog.rec("setImageView() -- IOException " + folder); 
			}
		}
	}

	protected void doOnClick(View v) {
 
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		String mUri = root_folder + "SimCity_" + String.valueOf(this.picType) + ".jpg";
		File file = new File(root_folder);// ("/sdcard/Image/");

		if (!file.exists()) {
			file.mkdirs();//
		}
		
		Uri url = Uri.fromFile(new File(mUri));
        cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, url);
 
        try {
            cameraIntent.putExtra("return-data", true); 
        } catch (Exception e) {
            e.printStackTrace();
        }
        
		startActivityForResult(cameraIntent, 1);
	}
 

	@SuppressLint("SdCardPath")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { //
				Log.i("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
		 
			String name = "SimCity_" + String.valueOf(this.picType) + ".jpg";

			Toast.makeText(this, name, Toast.LENGTH_LONG).show();
 		 
			String fileName = root_folder + name;// "/sdcard/Image/"+name;
/*  
			ContentResolver cr = this.getContentResolver();
			Uri url = Uri.fromFile(new File(fileName));
			Bitmap bmp_selectedPhoto = null;
            try { 
                bmp_selectedPhoto = BitmapFactory.decodeStream(cr
                        .openInputStream(url));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Matrix matrix = new Matrix();  
	        matrix.postScale(scaleWidth, scaleHeight); 

			int tmpWidth = imgWidth > bmp_selectedPhoto.getWidth() ? bmp_selectedPhoto.getWidth() : imgWidth;
			int tmpHeight = imgHeight > bmp_selectedPhoto.getHeight() ? bmp_selectedPhoto.getHeight() : imgHeight; 
            Bitmap resizedBitmap = Bitmap.createBitmap(bmp_selectedPhoto, 0, 0, tmpWidth,  
            		tmpHeight, matrix, true); 
*/             
			try {
				switch (this.picType) {
				case 1:
//					viewShip.setImageBitmap(resizedBitmap);

					setImageView(fileName, viewShip, bitMapShip, bitMapShipResized);
					
					break;
				case 2:
//					viewParis.setImageBitmap(resizedBitmap);//
					setImageView(fileName, viewParis, bitMapParis, bitMapParisResized);
					break;
				case 3:
//					viewTokyo.setImageBitmap(resizedBitmap);//
					setImageView(fileName, viewTokyo, bitMapTokyo, bitMapTokyoResized);
					break;
				}
			} catch (Exception e) {
				Log.e("error", e.getMessage());
			}

		}
	}
	
//	protected void onDestroy() {
//		 TODO Auto-generated method stub
//		super.onDestroy();
//		if(bitMapShip != null)
//			bitMapShip.recycle();
//		if(bitMapParis != null)
//			bitMapParis.recycle();
//		if(bitMapTokyo != null)
//			bitMapTokyo.recycle();
//		
//		if(bitMapShipResized != null)
//			bitMapShipResized.recycle();
//		if(bitMapParisResized != null)
//			bitMapParisResized.recycle();
//		if(bitMapTokyoResized != null)
//			bitMapTokyoResized.recycle();
//		SimCityLog.rec("onDestroy()");
//	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
