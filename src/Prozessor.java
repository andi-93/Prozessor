import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;


/*
 * Zustandsmodell implementieren
 * Besitzt UV, UR und NR (Unterbrechungsvektor, Unterbrechungs-/ Normalmodusregister)
 * Unterbrechungen annehmen k�nnen
 * 
 * Methode: simuliert das Rechnen, l�st zuf�llig Unterbrechung aus
 * 			�ndert dabei Zustand und Registersatz
 * 			endet nach zuf�lliger Zeit
 */

public class Prozessor {
	
	private int UV[] = new int[64];	//Enth�lt Startadresse eines Programms zur Unterbrechungsbehandlung
	private int UR[] = new int[64];
	private int NR[] = new int[64];
	private Zustand zustand = Zustand.ENABLED_RESTRICTED;
	private Random rd = new Random();
	private int max_int_Zeitpunkt;	//Interrupt nach max. x ms
	private int max_int_Dauer;		//Interruptdauer max. x ms
	private double durchsatz;
	private String ausgabe = "";
	
	private enum Zustand {
		ENABLED_RESTRICTED,		//Normalmodus, arbeitet auf NR
		ENABLED_PRIVILEGED,		//Nimmt Unterbrechungen an
		DISABLED_PRIVILEGED		//Gesperrt gegen weitere Unterbrechungen
	}
	
	public void rechne(Prozess p, int t) {
		p.zuweisen();
		long t_end = System.currentTimeMillis() + t;
		System.out.println("Prozessor rechnet Prozess " + p.pcb.PID + " " + t + "ms (Prio " + p.pcb.prioritaet + " Restzeit " + p.pcb.restzeit + "ms)");
		ausgabe += "Prozessor rechnet Prozess " + p.pcb.PID + " " + t + "ms (Prio " + p.pcb.prioritaet + " Restzeit " + p.pcb.restzeit + "ms) \n";
		long t_int = System.currentTimeMillis() + rd.nextInt(max_int_Zeitpunkt)+1;
		boolean interrupt_done = false;
		while (System.currentTimeMillis() < t_end) {
			System.out.print(". ");
			ausgabe += ". ";
			if (!interrupt_done && System.currentTimeMillis() >= t_int) {
				t_end += interrupt();										//Endzeitpunkt um Dauer des Interrupts verschoben
				interrupt_done = true;
			}
			try {Thread.sleep(500);}
			catch (InterruptedException e) {e.printStackTrace();};
		}
		p.pcb.restzeit -= t;
		if (p.pcb.restzeit <= 0) {
			p.entfernen();
			System.out.println("Prozess " + p.pcb.PID + " fertig.");
			ausgabe += "Prozess " + p.pcb.PID + " fertig. \n";
		}
		else {
			System.out.println("Prozess " + p.pcb.PID + " gestoppt (Restzeit " + p.pcb.restzeit + "ms)");
			ausgabe += "Prozess " + p.pcb.PID + " gestoppt (Restzeit " + p.pcb.restzeit + "ms) \n";
			p.abgeben();
		}
	}
	
	private int interrupt() {
		zustand = Zustand.DISABLED_PRIVILEGED;
		int dauer = rd.nextInt(max_int_Dauer)+1;
		System.out.print("Interrupt (" + dauer + " ms) ");
		ausgabe += "Interrupt (" + dauer + " ms) ";
		try {Thread.sleep(dauer);}
		catch (InterruptedException e) {e.printStackTrace();};
		zustand = Zustand.ENABLED_RESTRICTED;
		return dauer;
	}
	
	public void schreibeDatei() {
		ausgabe += "\nDurchsatz: " + durchsatz*1000 + " Jobs/s \nAuslastung des Prozessors 100%, da keine Peripheriegeraete und somit auch keine Wartezeiten vorhanden sind.";
		PrintWriter pWriter = null;
		try {
            pWriter = new PrintWriter(new FileWriter("Daten.txt"));
            pWriter.println(ausgabe);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            if (pWriter != null)
                pWriter.flush();
        }
	}
	
	public void setMaxIntZeitpunkt(int t) {
		max_int_Zeitpunkt = t;
	}
	
	public void setMaxIntDauer(int dt) {
		max_int_Dauer = dt;
	}
	
	public void setDurchsatz(double d) {
		durchsatz = d;
	}
}
