/// <reference types="cypress" />

import Chance from "chance";
import { clear } from "console";
const chance = new Chance();

describe("Login-tests", () => {
  beforeEach(() => {
    cy.visit("http://localhost:4200");
  });

  it("login-admin-valid", () => {
    const email = "admin";
    const password = "admin";

    cy.login(email, password);
    cy.url().should("include", "/dashboard");
    cy.contains("Dodaj predmet");

    cy.contains("Obvestila administratorja sistema", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("#button-notification-add");
  });

  it("login-professor-valid", () => {
    const email = "prof";
    const password = "prof";

    cy.login(email, password);
    cy.url().should("include", "/dashboard");
    cy.contains("Dodaj predmet");
    cy.contains("Obvestila administratorja sistema", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("#button-notification-add").should("not.exist");
  });

  it("login-student-valid", () => {
    const email = "student";
    const password = "student";

    cy.login(email, password);
    cy.url().should("include", "/dashboard");
    cy.contains("Dodaj predmet").should("not.exist");
    cy.contains("Obvestila administratorja sistema", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("#button-notification-add").should("not.exist");
  });

  it("login-invalid", () => {
    const email = chance.email();
    const password = chance.word();

    cy.login(email, password);
    cy.contains("Neveljaven epoštni naslov ali geslo");
  });
});

describe("Register-tests", () => {
  const name = "Ime";
  const surname = "Surname";
  const email = "ime.surname";
  const pass = "pass123";

  beforeEach(() => {
    cy.visit("http://localhost:4200");
  });

  it("register-professor", () => {
    cy.get(".has-text-centered > :nth-child(1) > .button")
      .contains("Pedagog")
      .click(); //register pedagog button

    cy.get("button").contains("Registracija").click();
    cy.contains("Ime mora biti izpolnjeno");
    cy.get("input[placeholder=Ime]").clear().type(name);

    cy.get("button").contains("Registracija").click();
    cy.contains("Priimek mora biti izpolnjen");
    cy.get("input[placeholder=Priimek]").clear().type(surname);

    cy.get("button").contains("Registracija").click();
    cy.contains("Epošta mora biti izpolnjena");
    cy.get("input[placeholder=Epošta]").clear().type(email);

    cy.get("button").contains("Registracija").click();
    cy.contains("Geslo ne sme biti prazno");
    cy.get("input[placeholder=Geslo]").clear().type(pass);

    cy.get("button").contains("Registracija").click();
    cy.contains("Geslo ne sme biti prazno");
    cy.get("input[placeholder='Ponovi geslo']")
      .clear()
      .type(pass + "123");

    cy.get("button").contains("Registracija").click();
    cy.contains("Gesli se ne ujemata");
    cy.get("input[placeholder='Ponovi geslo']").clear().type(pass);

    cy.get("button").contains("Registracija").click();

    cy.contains("Prijava obstoječi uporabniki");
  });

  it("register-professor-invalid", () => {
    cy.get(".has-text-centered > :nth-child(1) > .button")
      .contains("Pedagog")
      .click(); //register pedagog button

    cy.get("input[placeholder=Ime]").clear().type(name);
    cy.get("input[placeholder=Priimek]").clear().type(surname);
    cy.get("input[placeholder=Epošta]").clear().type(email);
    cy.get("input[placeholder=Geslo]").clear().type(pass);
    cy.get("input[placeholder='Ponovi geslo']").clear().type(pass);

    cy.get("button").contains("Registracija").click();
    cy.contains("Uporabnik s tem epoštnim naslovom že obstaja");
  });
});
