/**
 * DiningServer.java
 *
 * This class contains the methods called by the  philosophers.
 *
 */

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DiningServerImpl  implements DiningServer
{  
	/* Variables */
	enum State { // Possible states for the Philosophers 
		THINKING,
		HUNGRY,
		EATING;
	}
	State[] state = new State[5]; // Array for the Philosopher's states 
	Lock lock = new ReentrantLock();
	Condition[] self = new Condition[5]; // For the Thread behavior: await, signal
	Thread[] philth = new Thread[5];

	/* Constructor */
	public DiningServerImpl(){
		// Initialize all Philosophers at Thinking
		for(int i = 0; i < 5; i++){
			state[i] = State.THINKING; 
			self[i] = lock.newCondition();
		}
		// Initialize Philosophers and thread start
		for(int i = 0; i < 5; i++){
			Philosopher phil = new Philosopher(i, this); 
			philth[i] = new Thread(phil);
			philth[i].start(); // This thread will now execute the run() in Philosopher
		}
	}

	/* called by a philosopher when they wish to eat */
	public void takeForks(int philNumber){
		lock.lock(); // The thread / philosopher blocks until it is unlocked with unlock() 
	try{
		state[philNumber] = State.HUNGRY;
		System.out.println("PHILOSOPHER [" + (philNumber+1) + "]: HUNGRY");
		test(philNumber);
		if(state[philNumber].toString() != "EATING"){
			self[philNumber].await();
		}
		System.out.println("-- PHILOSOPHER [" + (philNumber+1) + "]: *Picked up forks* --");
	}catch(InterruptedException ie){
		System.out.println("intru-error in takeforks");
		Thread.currentThread().interrupt(); 
	}
		lock.unlock();
	
	}
	
	/* Called by a philosopher when they are finished eating 
	* IN: int philNumber - the seat number of the philosopher 
	* OUT: void
	*/
	public void returnForks(int philNumber){
		lock.lock();
		state[philNumber] = State.THINKING; 
		eating(philNumber);
		System.out.println("-- PHILOSOPHER [" + (philNumber+1) + "]: *Returns Forks* --");
		// Test the left and right neighbors 
		test((philNumber + 4) % 5);
		test((philNumber + 1) % 5); 
		lock.unlock();
	}

	/* Tests if neighbors are both NOT eating and current philosopher is hungry 
	* IN: int philNumber - the seat number of the philosopher 
	* OUT: void
	*/
	public void test(int philNumber){
		/* Convert state with toString() and compare with "EATING" so they are of comparable types */
		if((state[(philNumber + 4) % 5].toString() != "EATING")
		&& (state[philNumber].toString() == "HUNGRY")
		&& state[(philNumber + 1) % 5].toString() != "EATING"){
			state[philNumber] = State.EATING;
			System.out.println("PHILOSOPHER [" + (philNumber+1) + "]: EATING");
		}
		self[philNumber].signal();	 
	}

	/* Puts the thread eating to sleep for a random number 1000 - 4000 milliseconds 
	 * IN: int philNumber - the seat number of the philosopher 
	 * OUT: void
	 */
	public void eating(int philNumber){
		try{
			// Time spent eating (Between 1000 - 4000 ms)
			int chowTime = (int)(Math.random() * (4000 - 1000) + 1000); 

			// Will put thread to sleep for the value in chowTime
			philth[philNumber].sleep(chowTime);
			System.out.println("-- Philosopher [" + (philNumber+1) + "] has been eating for " + chowTime + "ms --");
		}catch(Exception e){
			System.out.println(e);
		}
	}
}
