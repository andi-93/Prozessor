
public class Laufumgebung {

	public static void main(String[] args) {
		
		Scheduler scheduler = new Scheduler();
		Prozessor pr = new Prozessor();
		pr.setMaxIntZeitpunkt(5000);
		pr.setMaxIntDauer(4000);
		scheduler.addPr(pr);
		
		// new Prozess (PID, Prio, Zeitscheibe, Restzeit)	Prio & Zeitscheibe nur für FS relevant
		scheduler.addPs(new Prozess(0, 2, 5000, 1000));
		scheduler.addPs(new Prozess(1, 4, 3000, 400));
		scheduler.addPs(new Prozess(2, 5, 1000, 200));
		
		scheduler.setModusRR();	//FCFS | RR | FS
		scheduler.setZeitqauantum_RR(2000);	//Nur für Round robin relevant
		
		scheduler.schedule();
	}
}
