package com.bounce.balloongame;

import java.util.HashSet;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.LifecycleListener;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.badlogic.gdx.backends.android.AndroidFiles;
import com.badlogic.gdx.backends.android.AndroidGraphics;
import com.badlogic.gdx.backends.android.AndroidInput;
import com.badlogic.gdx.backends.android.AndroidNet;
import com.badlogic.gdx.backends.android.surfaceview.FillResolutionStrategy;
import com.balloongame.listeners.ISensorUpdateListener;
import com.balloongame.main.BalloonGame;
import com.balloongame.misc.Config;
import com.balloongame.misc.Config.HistoricalSamplingState;

public class AndroidGame extends AndroidApplication implements SensorEventListener
{
	public static AndroidApplication activityInstance;
	
	BalloonGame game = null;
	
	private float mLastX, mLastY, mLastZ;
	private boolean mInitialized;
	private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float deltaX = 0;
    private float deltaY = 0;
    private float deltaZ = 0;
    private float gravity[] = new float[]{0,0,0};
    private float linear_acceleration[] = new float[]{0,0,0};

    protected ISensorUpdateListener iSensorUpdateListener = null;
    
    private int totalSamplesCount = 0;

    private final float NOISE = (float) 0.0;
    private  float historicalValueSum = 0f;
    
    private HistoricalSamplingState historicalSampleState = HistoricalSamplingState.UNDER_VALUE;
	
	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		activityInstance = this;
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useCompass = false;
		config.useAccelerometer = false;
		config.useWakelock = true;
		config.useGL20 = true;
		
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(Config.TEST_MODE)
        {
           mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
        }
		
		game = new BalloonGame();
		iSensorUpdateListener = game;
		
		initialize(game, config);
	}

	@Override
    protected void onResume() {
        super.onResume();
        if(Config.TEST_MODE)
        {
        	mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	        if(iSensorUpdateListener != null){
	           iSensorUpdateListener.onSensorStatusUpdate(ISensorUpdateListener.DEVICE_CONNECTED, 
	                 "BalloonGame's Input Device", "Device connected");
	        }
		}
    }

	@Override
    protected void onPause() {
        super.onPause();
        if(Config.TEST_MODE)
        {
	        mSensorManager.unregisterListener(this);
	        if(iSensorUpdateListener != null){
	           iSensorUpdateListener.onSensorStatusUpdate(ISensorUpdateListener.DEVICE_DISCONNECTED, 
	                 "BalloonGame's Input Device", "Device disconnected");
	        }
        }
    }
	
	@Override
	protected void onDestroy()
	{		
		super.onDestroy();
		game.dispose();
	}
	
	/*
	 * Overridded because getFilesDir was coming out to be Null for 
	 * android 2.1 and 2.2
	 * 
	 * *(non-Javadoc)
	 * @see com.badlogic.gdx.backends.android.AndroidApplication#initialize(com.badlogic.gdx.ApplicationListener, com.badlogic.gdx.backends.android.AndroidApplicationConfiguration)
	 */
	@Override
	public void initialize(ApplicationListener listener, AndroidApplicationConfiguration config) {
		
		graphics = new AndroidGraphics(this, config, config.resolutionStrategy == null ? new FillResolutionStrategy()
			: config.resolutionStrategy);
		input = new AndroidInput(this, this, graphics.getView(), config);
		audio = new AndroidAudio(this, config);
		net = new AndroidNet(this);
		
		if(this.getFilesDir() != null)
			files = new AndroidFiles(this.getAssets(), this.getFilesDir().getAbsolutePath());
		else
			files = new AndroidFiles(this.getAssets());
		
		this.listener = listener;
		this.handler = new Handler();
		
		Gdx.net = this.getNet();
		Gdx.app = this;
		Gdx.input = this.getInput();
		Gdx.audio = this.getAudio();
		Gdx.files = this.getFiles();
		Gdx.graphics = this.getGraphics();
		Gdx.net = this.getNet();

		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		} catch (Exception ex) {
			log("AndroidApplication", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		setContentView(graphics.getView(), createLayoutParams());
		createWakeLock(config);
	}

	@Override
	public void addLifecycleListener(LifecycleListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeLifecycleListener(LifecycleListener arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event)
	{	
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
		if (!mInitialized) {
			mLastX = x;
			mLastY = y;
			mLastZ = z;
			mInitialized = true;
		} else {
			/**
			 * It should be apparent that in order to measure the real acceleration of the device, 
			 * the contribution of the force of gravity must be eliminated.
			 * This can be achieved by applying a high-pass filter.  
			 * 
			 * alpha is calculated as t / (t + dT)
			 * with t, the low-pass filter's time-constant
			 * and dT, the event delivery rate
			 */

			final float alpha = 0.8f;

	        gravity[0] = alpha * gravity[0] + (1 - alpha) * event.values[0];
	        gravity[1] = alpha * gravity[1] + (1 - alpha) * event.values[1];
	        gravity[2] = alpha * gravity[2] + (1 - alpha) * event.values[2];
	
	        linear_acceleration[0] = event.values[0] - gravity[0];
	        linear_acceleration[1] = event.values[1] - gravity[1];
	        linear_acceleration[2] = event.values[2] - gravity[2];
          
			deltaX = Math.abs(linear_acceleration[0]);
			deltaY = Math.abs(linear_acceleration[1]);
			deltaZ = Math.abs(linear_acceleration[2]);
			 
			if (deltaX < NOISE) { deltaX = linear_acceleration[0] = (float)0.0; }
			if (deltaY < NOISE) { deltaY = linear_acceleration[1] = (float)0.0; }
			if (deltaZ < NOISE) { deltaZ = linear_acceleration[2] = (float)0.0; }
			mLastX = x;
			mLastY = y;
			mLastZ = z;

			if(iSensorUpdateListener != null){
				if(deltaX > 0 || deltaY > 0)
				{
					if(deltaX > Config.THRESHOLD_INDICATOR)
						linear_acceleration[1] = 0;
					if(deltaY > Config.THRESHOLD_INDICATOR)
						linear_acceleration[0] = 0;
					iSensorUpdateListener.onSensorDataUpdate(linear_acceleration[0],
							linear_acceleration[1]);
				}
			}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}
	
	protected void onSensorChanged(float x, float y, float z)
	{
		/**
		 * It should be apparent that in order to measure the real acceleration of the device, 
		 * the contribution of the force of gravity must be eliminated.
		 * This can be achieved by applying a high-pass filter.  
		 * 
		 * alpha is calculated as t / (t + dT)
		 * with t, the low-pass filter's time-constant
		 * and dT, the event delivery rate
		 */

		final float alpha = 0.8f;

        gravity[0] = 0; //alpha * gravity[0] + (1 - alpha) * x;
        gravity[1] = 0; //alpha * gravity[1] + (1 - alpha) * y;
        gravity[2] = 0; //alpha * gravity[2] + (1 - alpha) * z;

        linear_acceleration[0] = x - gravity[0];
        linear_acceleration[1] = y - gravity[1];
        linear_acceleration[2] = z - gravity[2];
        
        deltaX = Math.abs(linear_acceleration[0]);
		deltaY = Math.abs(linear_acceleration[1]);
		deltaZ = Math.abs(linear_acceleration[2]);
		 
		if (deltaX < NOISE) { deltaX = linear_acceleration[0] = (float)0.0; }
		if (deltaY < NOISE) { deltaY = linear_acceleration[1] = (float)0.0; }
		if (deltaZ < NOISE) { deltaZ = linear_acceleration[2] = (float)0.0; }

		if(iSensorUpdateListener != null){
			if((deltaY > Config.THRESHOLD_INDICATOR || deltaX > Config.THRESHOLD_INDICATOR)
					)
			{
			   Log.i("test3", "SAMPLING_STARTED:"+deltaY);
			      
				//historicalSampleState = HistoricalSamplingState.SAMPLING_STARTED;
				historicalValueSum += deltaY;
				totalSamplesCount++;
			}
			
			else //if(historicalSampleState == HistoricalSamplingState.SAMPLING_STARTED)
			{
			   Log.i("test3", "SAMPLING_ENDED:"+deltaY);
				//historicalSampleState = HistoricalSamplingState.SAMPLING_ENDED;
			}
			
			if( (totalSamplesCount >= Config.MIN_NUM_SAMPLES))
			{	
				final float  fValue= historicalValueSum/totalSamplesCount;
				iSensorUpdateListener.onSensorDataUpdate(linear_acceleration[0],
				      fValue);
				Log.i("test3", "onSensorDataUpdate:"+fValue);
				//reset values
				totalSamplesCount = 0;
				historicalValueSum = 0;
				
			}
		}
	}
}
