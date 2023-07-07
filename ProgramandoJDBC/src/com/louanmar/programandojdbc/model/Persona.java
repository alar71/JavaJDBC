package com.louanmar.programandojdbc.model;

import java.time.LocalDate;

public class Persona {
	private int idPersona;
	private String nombre;
	private LocalDate fechaNacimiento;
	private double salario;
	private boolean deBaja;
	
	public Persona(int idPersona, String nombre, LocalDate fechaNacimiento, double salario, boolean deBaja) {
		this.idPersona = idPersona;
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
		this.salario = salario;
		this.deBaja = deBaja;
	}

	public Persona(String nombre, LocalDate fechaNacimiento, double salario, boolean deBaja) {
		this.nombre = nombre;
		this.fechaNacimiento = fechaNacimiento;
		this.salario = salario;
		this.deBaja = deBaja;
	}

	public int getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(int idPersona) {
		this.idPersona = idPersona;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}

	public boolean isDeBaja() {
		return deBaja;
	}

	public void setDeBaja(boolean deBaja) {
		this.deBaja = deBaja;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean iguales = false;
		if (obj != null && obj instanceof Persona) {
			Persona otra = (Persona)obj;
			iguales = this.idPersona == otra.idPersona;
		}
		return iguales;
	}

	@Override
	public int hashCode() {
		return Integer.hashCode(this.idPersona);
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Persona [").append(idPersona).append("] ").append(nombre).append(", nacido el ").append(fechaNacimiento).append(", salario: ").append(salario)
				.append(", deBaja: ").append(deBaja);
		return builder.toString();
	}
		
}
