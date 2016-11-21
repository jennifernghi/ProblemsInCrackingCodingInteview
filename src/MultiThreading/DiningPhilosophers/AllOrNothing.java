package MultiThreading.DiningPhilosophers;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * to prevent deadloack, we can implement a strategy where a philosopher will put down his left chpopstick if he unable to obtain
 * the right one
 * @author jennifernghinguyen
 * ISSUSE: if all the philosophers were perfectly synchornized, they could simultaneously release their left chopstick, 
 * be unable to pick up the right one, the put back down the left one  - only to have the process repeated again
 */
public class AllOrNothing {
	//3 phiosophers
		public static int size = 3;
		
		public static int leftOf(int i) {
			return i;
		}
		
		public static int rightOf(int i) {
			return (i + 1) % size;
		}
		
		public static void main(String[] args) {		
			Chopstick1[] chopsticks = new Chopstick1[size + 1];
			for (int i = 0; i < size + 1; i++) {
				chopsticks[i] = new Chopstick1();
			}
			
			Philosopher1[] philosophers = new Philosopher1[size];
			for (int i = 0; i < size; i++) {
				Chopstick1 left = chopsticks[leftOf(i)];
				Chopstick1 right = chopsticks[rightOf(i)];
				philosophers[i] = new Philosopher1(i, left, right);
			}
			
			for (int i = 0; i < size; i++) {
				philosophers[i].start();
				
			}		
		}

	}

class Chopstick1{
	private Lock lock;
	
	public Chopstick1(){
		lock = new ReentrantLock();
	}
	

	public void putDown() {
		lock.unlock();
	}
	/**
	 * acquire the lock if it is available
	 * true: if it is available, then acquire the lock
	 * @return
	 */
	public boolean pickUp(){
		return lock.tryLock();
	}
}

class Philosopher1 extends Thread{
	private final int maxPause = 50;
	private int index;
	private int bites =2;
	private Chopstick1 left,right;
	
	public Philosopher1(int i,Chopstick1 left, Chopstick1 right) {
		this.index=i;
		this.left=left;
		this.right=right;
	}
	
	public void eat(){
		if(pickUp()){
			chew();
			putDown();
			System.out.println("Philosopher " + index + ": done eating");
		}else{
			System.out.println("Philosopher " + index + ": gave up on eating");
		}
	}
	/**
	 * release the left chopstick if we can't pick up the right one
	 * to not call putDown() on the chopsticks if we had them in the first place
	 * @return
	 */
	public boolean pickUp() {
		pause();
		/* attempt to pick up*/
		if(!left.pickUp()){
			return false;
		}
		pause();
		if(!right.pickUp()){
			left.putDown();
			System.out.println("Philosopher " + index + ": put down left chopstick");
			return false;
		}
		pause();
		System.out.println("Philosopher " + index + ": pickup");
		return true;
	}
	public void chew() {
		System.out.println("philosopher " + index +" chew");
		pause();
	}
	public void putDown() {
		right.putDown();
		System.out.println("philosopher " + index +" put down right chopstick");
		left.putDown();
		System.out.println("philosopher " + index +" put down left chopstick");
	}
	public void pause() {
		try {
			
			Thread.sleep(maxPause);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void run() {
		for(int i=0; i<bites; i++){
			eat();
		}
	}
}