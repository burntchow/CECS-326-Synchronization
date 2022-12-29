/**
 * Philosopher.java
 *
 * This class represents each philosopher thread.
 * Philosophers alternate between eating and thinking.
 *
 */


public class Philosopher implements Runnable
{
    /* Variables */
    int seat; // seat number of the Philosopher 
    DiningServerImpl dinPhil; // Access to returnForks() and takeForks() functions
    
    /* Philosopher will have seat number and DiningServerImpl */
    public Philosopher(int index, DiningServerImpl din){
        seat = index; 
        dinPhil = din; 
    }

    public void run(){
        // How many servings of rice a Philosopher can eat before getting full - otherwise infinte looping
        int serving = 2;
        while(serving > 0){
            // Thinking 
            System.out.println("PHILOSOPHER [" + (seat+1) + "]: THINKING...");  
            dinPhil.takeForks(seat);
                
            // Eating 

            dinPhil.returnForks(seat);
            // Thinking again
            serving--;
        }
    }
}
