import java.util.Vector;


public class Scheduler {

	private Vector<Prozess> prozesse = new Vector<Prozess>();
	private Vector<Prozessor> prozessoren = new Vector<Prozessor>();
	private Modus modus;
	private int zeitquantum_RR;		//Zeitscheibe f�r Round Robin
	
	private enum Modus {
		FCFS,	//First come first served
		RR,		//Round robin
		FS		//Feedback Scheduling
	}
	
	public void schedule() {
		double durchsatz = 0;
		for (int i=0; i<prozesse.size(); i++) {
			durchsatz += prozesse.elementAt(i).pcb.restzeit;
		}
		durchsatz = prozesse.size() / durchsatz;
		prozessoren.elementAt(0).setDurchsatz(durchsatz);
		
		if (modus == Modus.FCFS) {
			for (int i=0; i<prozesse.size(); i++) {
				prozessoren.elementAt(0).rechne(prozesse.elementAt(i), prozesse.elementAt(i).pcb.restzeit);
			}
		}
		
		
		else if(modus == Modus.RR){
			while (prozesse.size() > 0) {
				for (int i=0; i<prozesse.size(); i++) {
					if (prozesse.elementAt(i).getZustand() == prozesse.elementAt(i).getZustandBereit()) {
						int dauer = zeitquantum_RR;
						if (prozesse.elementAt(i).pcb.restzeit < zeitquantum_RR || prozesse.size() == 1) {
							dauer = prozesse.elementAt(i).pcb.restzeit;		//Restzeit kleiner Quantum ODER nur noch ein Prozess zu rechnen
						}
						prozessoren.elementAt(0).rechne(prozesse.elementAt(i), dauer);
						if (prozesse.elementAt(i).getZustand() == null) {	//Prozess fertig
							prozesse.remove(i);
						}
					}
				}
			}
		}
		
		
		else if(modus == Modus.FS){
			while (prozesse.size() > 0) {
				for (int i=prozesse.size()-1; i>=0; i--) {
					//Maximale Priorit�t bestimmen
					int maxPrio = prozesse.elementAt(i).pcb.prioritaet;
					for (int k=1; k<prozesse.size(); k++) {
						if (prozesse.elementAt(k).pcb.prioritaet > maxPrio) {
							maxPrio = prozesse.elementAt(k).pcb.prioritaet;
						}
					}
					//Prozess mit max. Prio rechnen
					if (prozesse.elementAt(i).pcb.prioritaet == maxPrio) {
						if (prozesse.elementAt(i).getZustand() == prozesse.elementAt(i).getZustandBereit()) {
							int dauer = prozesse.elementAt(i).pcb.zeitscheibe;
							if (prozesse.elementAt(i).pcb.restzeit < prozesse.elementAt(i).pcb.zeitscheibe || prozesse.size() == 1) {
								dauer = prozesse.elementAt(i).pcb.restzeit;		//Restzeit kleiner Quantum ODER nur noch ein Prozess zu rechnen
							}
							prozessoren.elementAt(0).rechne(prozesse.elementAt(i), dauer);
							if (prozesse.elementAt(i).getZustand() == null) {	//Prozess fertig
								prozesse.remove(i);
							}
							else {
								prozesse.elementAt(i).pcb.zeitscheibe = 2 * prozesse.elementAt(i).pcb.zeitscheibe;
								prozesse.elementAt(i).pcb.prioritaet--;
							}							
						}
					}
				}
			}
		}
		
		System.out.println("\nDurchsatz: " + durchsatz*1000 + " Jobs/s");
		System.out.println("Auslastung des Prozessors 100%, da keine Peripheriegeraete und somit auch keine Wartezeiten vorhanden sind.");
		prozessoren.elementAt(0).schreibeDatei();
	}
	
	public void addPr(Prozessor pr) {
		prozessoren.add(pr);
	}
	
	public void addPs(Prozess ps) {
		prozesse.add(ps);
	}
	
	public void setModusFCFS() {
		modus = Modus.FCFS;
	}
	
	public void setModusRR() {
		modus = Modus.RR;
	}
	
	public void setModusFS() {
		modus = Modus.FS;
	}
	
	public void setZeitqauantum_RR(int q) {
		zeitquantum_RR = q;
	}
}
