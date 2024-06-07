import {setupInterceptions} from "../../support/interceptions";

describe('Promo Code Test in Cart Page with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions();
  });
  it('should not login with invalid credentials', () => {

    cy.contains('label', 'Email').siblings('input').type('invalid@mail.com');
    cy.contains('label', 'Password').siblings('input').type('InvalidPassword!');
    cy.get('button').contains('Login').click();

    cy.wait('@login').its('response.statusCode').should('eq', 403);

    cy.url().should('not.include', '/products');
  });

});
