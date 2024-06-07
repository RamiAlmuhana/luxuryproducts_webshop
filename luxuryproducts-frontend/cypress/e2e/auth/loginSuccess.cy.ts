import {setupInterceptions} from "../../support/interceptions";

describe('Promo Code Test in Cart Page with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions();
  });

  it('should login, navigate to products page, add a product to the cart and apply a promo code', () => {
    cy.contains('label', 'Email').siblings('input').type('test@mail.com');
    cy.contains('label', 'Password').siblings('input').type('Test123!');
    cy.get('button').contains('Login').click();

    cy.wait('@login').its('response.statusCode').should('eq', 200);
    cy.wait('@getUser').its('response.statusCode').should('eq', 200);

    cy.url().should('eq', 'http://localhost:4200/');
  });
});
