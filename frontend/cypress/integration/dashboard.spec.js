/// <reference types="cypress" />

import Chance from "chance";
const chance = new Chance();

describe("Notification-dashboard-tests", () => {
  const email = "admin";
  const password = "admin";
  const notificationTitle = chance.paragraph({ sentences: 1 });
  const notificationBody = chance.paragraph({ sentences: 5 });

  beforeEach(() => {
    cy.visit("http://localhost:4200");
  });

  it("notification-add", () => {
    cy.login(email, password);
    cy.url().should("include", "/dashboard");

    cy.contains("Obvestila administratorja sistema", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("#button-notification-add").click();

    cy.get("input[placeholder='Naslov objave']").type(notificationTitle);
    cy.get("textarea[placeholder='Telo objave']").type(notificationBody);

    cy.get("button").contains("Objavi").click();
    cy.get("button").contains("Objavi").should("not.exist");

    cy.contains(notificationTitle);
    cy.contains(notificationBody);
  });

  it("notification-delete", () => {
    cy.login(email, password);
    cy.url().should("include", "/dashboard");

    cy.contains("Obvestila administratorja sistema", { timeout: 10000 }).should(
      "be.visible"
    );

    cy.contains(notificationTitle)
      .parent()
      .children("button")
      .click()
      .then(($event) => {
        console.log($event);
      });
  });
});


describe("Notification-dashboard-tests", () => {
  const email = "admin";
  const password = "admin";
  const notificationTitle = chance.paragraph({ sentences: 1 });
  const notificationBody = chance.paragraph({ sentences: 5 });

  beforeEach(() => {
    cy.visit("http://localhost:4200");
  });

  it("notification-add", () => {
    cy.login(email, password);
    cy.url().should("include", "/dashboard");

    cy.contains("Obvestila administratorja sistema", { timeout: 10000 }).should(
      "be.visible"
    );
    cy.get("#button-notification-add").click();

    cy.get("input[placeholder='Naslov objave']").type(notificationTitle);
    cy.get("textarea[placeholder='Telo objave']").type(notificationBody);

    cy.get("button").contains("Objavi").click();
    cy.get("button").contains("Objavi").should("not.exist");

    cy.contains(notificationTitle);
    cy.contains(notificationBody);
  });

  it("notification-delete", () => {
    cy.login(email, password);
    cy.url().should("include", "/dashboard");

    cy.contains("Obvestila administratorja sistema", { timeout: 10000 }).should(
      "be.visible"
    );

    cy.contains(notificationTitle)
      .parent()
      .children("button")
      .click()
      .then(($event) => {
        console.log($event);
      });
  });
});