package com.projeto.max.domain;

import jakarta.persistence.*;
import java.io.Serializable;

@Embeddable
public class UserDairyKey implements Serializable {
	@ManyToOne
	@JoinColumn(referencedColumnName = "iddairy", nullable = false)
	private Dairys dairy;

	@ManyToOne
	@JoinColumn(referencedColumnName = "iduser", nullable = false)
	private Users user;


    public UserDairyKey() { }
    public UserDairyKey(Dairys dairy, Users user) {
		this.dairy = dairy;
		this.user = user;
    
    }

	public Dairys getDairy() { return dairy; }
	public void setDairy(Dairys dairy) { this.dairy = dairy; }
	public Users getUser() { return user; }
	public void setUser(Users user) { this.user = user; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDairyKey)) return false;
        UserDairyKey that = (UserDairyKey) o;
        return dairy.equals(that.dairy) && user.equals(that.user);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(dairy, user);
    }
}
