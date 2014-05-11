package controllers;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import models.User;
import play.*;
import play.Logger.ALogger;
import play.data.*;
import play.mvc.*;
import views.html.*;
import static play.data.Form.*;
import play.db.*;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

public class Application extends Controller {

	public static ALogger log = Logger.of(Application.class);

	public static Result authentication() {
		return ok(authentication.render(Form.form(User.class)));
	}

	@Transactional
	public static Result authenticate() {
		Form<User> loginForm = Form.form(User.class).bindFromRequest();
		if (loginForm.hasErrors()) {
			return badRequest(authentication.render(loginForm));
		} else {
			User u = loginForm.get();
			if (isAuthentic(u)) {
				session("name", u.getName());
				return redirect(routes.Application.index());
			} else {
				return badRequest(authentication.render(loginForm));
			}
		}
	}

	public static Result logout() {
		//session("name", null);
		flash("success", "You've been logged out.");
		return redirect(routes.Application.authentication());
	}

	public static Result registration() {
		return ok(registration.render(Form.form(User.class)));
	}

	@Transactional
	public static Result register() {
		EntityManager em = JPA.em();
		Form<User> registerForm = Form.form(User.class).bindFromRequest();
		if (registerForm.hasErrors()) {
			return badRequest(registration.render(registerForm));
		} else {
			User u = registerForm.get();
			if (userExists(u)) {
				return badRequest(registration.render(registerForm));
			} else {
				em.persist(u);
				return redirect(routes.Application.authentication());
			}
		}
	}

	@Security.Authenticated(Bouncer.class)
	public static Result index() {
		return ok(index.render());
	}

	private static boolean userExists(User u) {
		EntityManager em = JPA.em();
		TypedQuery<User> query = em.createQuery(
				"SELECT u from User u WHERE u.name = :name", User.class);
		try {
			query.setParameter("name", u.getName()).getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}

	public static boolean isAuthentic(User u) {
		EntityManager em = JPA.em();
		TypedQuery<User> query = em
				.createQuery(
						"SELECT u from User u WHERE u.name = :name and u.password = :password",
						User.class);
		try {
			query.setParameter("name", u.getName())
					.setParameter("password", u.getPassword())
					.getSingleResult();
			return true;
		} catch (NoResultException e) {
			return false;
		}
	}
}