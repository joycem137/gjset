package gjset.tools;

import java.util.Timer;
import java.util.TimerTask;

/* 
 *  LEGAL STUFF
 * 
 *  This file is part of Combo Cards.
 *  
 *  Combo Cards is Copyright 2008-2010 Artless Entertainment
 *  
 *  Set¨ is a registered trademark of Set Enterprises. 
 *  
 *  This project is in no way affiliated with Set Enterprises, 
 *  but the authors of Combo Cards are very grateful for
 *  them creating such an excellent card game.
 *  
 *  Combo Cards is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *   
 *  Combo Cards is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details
 *   
 *  You should have received a copy of the GNU General Public License
 *  along with Combo Cards.  If not, see <http://www.gnu.org/licenses/>.
 */

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
		cancel();
		isRunning = true;
		
		// Now schedule running this.
		timer = new Timer("Countdown Timer");
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
