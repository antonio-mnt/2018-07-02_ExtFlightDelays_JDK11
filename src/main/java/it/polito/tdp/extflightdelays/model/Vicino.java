package it.polito.tdp.extflightdelays.model;

public class Vicino implements Comparable<Vicino>{
	
	private Airport a;
	private double peso;
	public Vicino(Airport a, double peso) {
		super();
		this.a = a;
		this.peso = peso;
	}
	public Airport getA() {
		return a;
	}
	public void setA(Airport a) {
		this.a = a;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vicino other = (Vicino) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Vicino [a=" + a + ", peso=" + peso + "]";
	}
	@Override
	public int compareTo(Vicino o) {
		return (int)-(this.peso-o.peso);
	}
	

}
