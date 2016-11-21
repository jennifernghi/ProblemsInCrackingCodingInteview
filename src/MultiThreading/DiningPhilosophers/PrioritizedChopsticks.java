package MultiThreading.DiningPhilosophers;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
/**
 * we can label chopstick with a number from 0 to n-1
 * each philosopher attempts to pick up the lower numbered chopstick first
 * this essentially means that each philosopher goes for the left chopstick
 * before the right one, except for the last philosopher who does this in reverse. it will break the cycle 
 * @author jennifernghinguyen
 *
 */
public class PrioritizedChopsticks {
	//3 phiosophers
			public static int size = 3;
			
			public static int leftOf(int i) {
				return i;
			}
			
			public static int rightOf(int i) {
				return (i + 1) % size;
			}
			
			public static void main(String[] args) {		
				ChopStick3[] chopsticks = new ChopStick3[size + 1];
				for (int i = 0; i < size + 1; i++) {
					chopsticks[i] = new ChopStick3(i);
				}
				
				Philosopher3[] philosophers = new Philosopher3[size];
				for (int i = 0; i < size; i++) {
					ChopStick3 left = chopsticks[leftOf(i)];
					ChopStick3 right = chopsticks[rightOf(i)];
					philosophers[i] = new Philosopher3(i, left, right);
				}
				
				for (int i = 0; i < size; i++) {
					philosophers[i].start();
				}		
			}
}

class Philosopher3 extends Thread{
	private int bites = 3;
	private ChopStick3 lower, higher;
	private int index;
	
	public Philosopher3(int i, ChopStick3 left, ChopStick3 right) {
		index=i;
		if(left.getNUmber()<right.getNUmber()){
			this.lower=left;
			this.higher=right;
		}else{
			this.lower=right;
			this.higher=left;
		}
	}
	
	public void eat() {
		System.out.println("Philosopher " + index + ": start eating");
		pickUp();
		chew();
		putDown();
		System.out.println("Philosopher " + index + ": done eating");
	}

	
	public void pickUp() {
		lower.pickUp();
		System.out.println("philosopher " + index +" pick up " + lower.getNUmber() +" chopstick");
		higher.pickUp();
		System.out.println("philosopher " + index +" pick up " + higher.getNUmber() +" chopstick");
	}
	public void chew() {
		System.out.println("philosopher " + index +" chew");
	}
	public void putDown() {
		higher.putDown();
		System.out.println("philosopher " + index +" put down " + higher.getNUmber() +" chopstick");
		lower.putDown();
		System.out.println("philosopher " + index +" put down " + lower.getNUmber() +" chopstick");
	}
	@Override
	public void run() {
		for(int i=0; i< bites; i++){
			eat();
		}
	}
}

class ChopStick3{
	private Lock lock;
	private int number;
	
	public ChopStick3(int n) {
		lock = new ReentrantLock();
		this.number = n;
	}
	
	public void pickUp() {
		lock.lock();
	}
	
	public void putDown() {
		lock.unlock();
	}
	
	public int getNUmber() {
		return number;
	}
}