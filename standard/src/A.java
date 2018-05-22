
import java.util.concurrent.locks.ReentrantLock;

public class A {
	static ReentrantLock first = new ReentrantLock();
	static ReentrantLock second = new ReentrantLock();
	static ReentrantLock third = new ReentrantLock();
	static ReentrantLock global = new ReentrantLock();

	static public void main(String[] args) {
		Philosoph sokrates = new Philosoph("Ich weiß, dass ich nichts weiß.", first, second);
		Philosoph hobbes = new Philosoph("Homo homini lupus est.", second, third);
		Philosoph adorno = new Philosoph("Es gibt kein richtiges Leben im falschen.", third, first);// TODO Swap first
																									// and third for
																									// global order
		sokrates.start();
		hobbes.start();
		adorno.start();
	}

	static class Philosoph extends Thread {
		String message;
		ReentrantLock forkleft, forkright;

		Philosoph(String message, ReentrantLock forkleft, ReentrantLock forkright) {
			this.message = message;
			this.forkleft = forkleft;
			this.forkright = forkright;
		}

		public void run() {
			while (true) {
				// Implementation um einen Entzug darzustellen
				do { // solange wir nicht das rechte Lock bekommen unlocken wir das linke Lock und versuchen es nochmal
					if (forkleft.isHeldByCurrentThread())forkleft.unlock();
					forkleft.lock();
				} while (!forkright.tryLock());
				forkleft.unlock();
				forkright.unlock();
				
				// Atomares Locking
				synchronized (Object.class) {//Nur ein Thread kann locken
					forkleft.lock();

					forkright.lock();
				}
				System.out.println(message);
				forkright.unlock();
				forkleft.unlock();
				
				//anderes Atomares Locking
				global.lock();//globales Lock verhindert gleichzeitiges locken analog zu synchronized(Object.class)
				forkleft.lock();
				forkright.lock();
				global.unlock();
				System.out.println(message);
				forkright.unlock();
				forkleft.unlock();
				
				// Nur locken wenn das zweite Lock verfügbar ist
				forkleft.lock();//erstes Lock holen
				if (forkright.tryLock()) {//versuchen zweites Lock zu holen
					System.out.println(message);//nur ausgeben wenn wir beide Locks haben
					forkright.unlock();//nur unlocken wenn wir das rechte Lock haben
				}
				forkleft.unlock();//immer unlocken

			}
		}
	}
}
