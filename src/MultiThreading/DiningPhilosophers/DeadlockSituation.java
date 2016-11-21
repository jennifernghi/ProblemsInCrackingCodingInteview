package MultiThreading.DiningPhilosophers;
/**
 * deadlock situation
 */
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockSituation {
	//3 phiosophers
	public static int size = 3;
	
	public static int leftOf(int i) {
		return i;
	}
	
	public static int rightOf(int i) {
		return (i + 1) % size;
	}
	
	public static void main(String[] args) {		
		Chopstick[] chopsticks = new Chopstick[size + 1];
		for (int i = 0; i < size + 1; i++) {
			chopsticks[i] = new Chopstick();
		}
		
		Philosopher[] philosophers = new Philosopher[size];
		for (int i = 0; i < size; i++) {
			Chopstick left = chopsticks[leftOf(i)];
			Chopstick right = chopsticks[rightOf(i)];
			philosophers[i] = new Philosopher(i, left, right);
		}
		
		for (int i = 0; i < size; i++) {
			philosophers[i].start();
		}		
	}

}

class Chopstick{
	private Lock lock;
	
	public Chopstick(){
		lock = new ReentrantLock();
	}
	
	public void pickUp(){
		 lock.lock();
	}
	
	public void putDown() {
		lock.unlock();
	}
}

class Philosopher extends Thread{
	private int index;
	private int bites =10;
	private Chopstick left,right;
	
	public Philosopher(int i,Chopstick left, Chopstick right) {
		this.index=i;
		this.left=left;
		this.right=right;
	}
	
	public void eat(){
		pickUp();
		chew();
		putDown();
	}
	
	public void pickUp() {
		left.pickUp();
		System.out.println("philosopher" + index +" picked up left chopstick");
		right.pickUp();
		System.out.println("philosopher" + index +" picked up right chopstick");
	}
	public void chew() {
		System.out.println("chew");
	}
	public void putDown() {
		right.putDown();
		System.out.println("philosopher" + index +" put down right chopstick");
		left.putDown();
		System.out.println("philosopher" + index +" put down left chopstick");
	}
	
	@Override
	public void run() {
		for(int i=0; i<bites; i++){
			eat();
		}
	}
}