package com.seprokof.warehouse;

/**
 * Represents product supplier. Equality based on <code>id</code>.
 * 
 * @author seprokof
 *
 */
public class Provider extends AbstractBaseStorage {
	private static final long serialVersionUID = -3382675453246401529L;

	private String id;
	private String orgName;
	private String contactWith;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getContactWith() {
		return contactWith;
	}

	public void setContactWith(String contactWith) {
		this.contactWith = contactWith;
	}

	public Good getGoodWithName(String productName) {
		return goods.keySet().stream().filter(g -> g.getName().equals(productName)).findFirst().get();
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
		Provider other = (Provider) obj;
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
		return "Provider [id=" + id + ", orgName=" + orgName + ", contactWith=" + contactWith + "]";
	}

}
