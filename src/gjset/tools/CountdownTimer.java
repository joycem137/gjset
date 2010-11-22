package gjset.tools;

import java.util.Timer;
import java.util.TimerTask;

/**
 * This class implements a basic timer that accepts a Runnable object to
 * call upon timer completion.
 * 
 * It will also output the remaining time for others to use if desired.
 */
public class CountdownTimer
{
	private Timer timer;
	
	private boolean isRunning;
	
	private long time;
	private Runnable runnable;

	/**
	 * Create a timer with the indicated parameters.
	 *
	 * @param setTime
	 * @param runnable
	 */
	public CountdownTimer(long time, Runnable runnable)
	{
		this.time = time;
		this.runnable = runnable;
		
		isRunning = false;
	}

	/**
	 * Start the timer running.
	 *
	 */
	public void start()
	{
		// Cancel any existing timer events.
		if(isRunning)
		{
			timer.cancel();
		}
		
		// Now schedule running this.
		timer = new Timer();
		timer.schedule(new TimerTask()
		{
			public void run()
			{
				onTimerFinished();
			}
		}, time);
	}
	
	/**
	 * 
	 * Handle the timer reaching its end.
	 *
	 */
	private void onTimerFinished()
	{
		isRunning = false;
		
		runnable.run();
	}

	/**
	 * Cancel this timer, preventing further events from being fired.
	 *
	 */
	public void cancel()
	{
		if(isRunning)
		{
			timer.cancel();
			isRunning = false;
		}
	}

}
