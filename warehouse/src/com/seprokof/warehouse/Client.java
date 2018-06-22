package com.seprokof.warehouse;

/**
 * Represents client which is buyer. Equality based on <code>id</code>.
 * 
 * @author seprokof
 *
 */
public class Client extends AbstractBaseStorage {
	private static final long serialVersionUID = -5587878277343187576L;

	private String name;
	private String surname;
	private String id;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Client other = (Client) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "Client [name=" + name + ", surname=" + surname + ", id=" + id + ", goods=" + goods + "]";
	}

}
