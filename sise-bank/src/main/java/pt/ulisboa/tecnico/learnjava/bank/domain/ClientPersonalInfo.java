package pt.ulisboa.tecnico.learnjava.bank.domain;

import pt.ulisboa.tecnico.learnjava.bank.exceptions.ClientException;

/// Refactor for Keep Unit Interfaces Small - all class.
public class ClientPersonalInfo {
	private final String firstName;
	private final String lastName;
	private final String nif;
	private int age;

	public ClientPersonalInfo(String firstName, String lastName, String nif, int age) throws ClientException {
		this.firstName = firstName;
		this.lastName = lastName;
		this.nif = nif;
		this.age = age;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public String getLastName() {
		return this.lastName;
	}

	public String getNif() {
		return this.nif;
	}

	public int getAge() {
		return this.age;
	}

	public void setAge(int age) {
		this.age = age;
	}
}
