
/*
 * Besitzt PCB (Process Control Block)
 * Implementiert Prozess-Zustandsmodell
 */

public class Prozess {
	
	private Zustand zustand;
	public PCB pcb = new PCB();

	private enum Zustand {
		RECHNEND,
		BEREIT,
		BLOCKIERT
	}
	
	public Prozess(int pid, int prio, int zeitscheibe, int restzeit) {
		pcb.PID = pid;
		pcb.prioritaet = prio;
		pcb.zeitscheibe = zeitscheibe;
		pcb.restzeit = restzeit;
		hinzufuegen();
	}
	
	public void hinzufuegen() {
		if(zustand == null)
			zustand = Zustand.BEREIT;
	}
	
	public void zuweisen() {
		if (zustand == Zustand.BEREIT)
			zustand = Zustand.RECHNEND;
	}
	
	public void abgeben() {
		if (zustand == Zustand.RECHNEND)
			zustand = Zustand.BEREIT;
	}
	
	public void verzichten() {
		if (zustand == Zustand.RECHNEND)
			zustand = Zustand.BLOCKIERT;
	}
	
	public void bewerben() {
		if (zustand == Zustand.BLOCKIERT)
			zustand = Zustand.BEREIT;
	}
	
	public void entfernen() {
		if (zustand == Zustand.RECHNEND)
			zustand = null;
	}
	
	public Zustand getZustand() {
		return zustand;
	}
	
	public Zustand getZustandBereit() {
		return Zustand.BEREIT;
	}
}
