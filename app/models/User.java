package models;

import javax.persistence.Entity;
import javax.persistence.Id;

import controllers.Application;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

@Entity
public class User implements at.ac.tuwien.big.we14.lab2.api.User {

	@Id
	@Required
	@MinLength(4)
	@MaxLength(8)
	private String name;

	@Required
	@MinLength(4)
	@MaxLength(8)
	private String password;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}