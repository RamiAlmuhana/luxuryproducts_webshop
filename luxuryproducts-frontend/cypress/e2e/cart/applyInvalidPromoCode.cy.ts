import { setupInterceptions } from '../../support/interceptions';

describe('Apply Invalid Promo Code Test with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions(2, true); // Gebruik de parameter om een ongeldige promo code te simuleren
  });

  it('should display an error message for an invalid promo code', () => {
    cy.contains('label', 'Email').siblings('input').type('test@mail.com');
    cy.contains('label', 'Password').siblings('input').type('Test123!');
    cy.get('button').contains('Login').click();

    cy.wait('@login').its('response.statusCode').should('eq', 200);
    cy.wait('@getUser').its('response.statusCode').should('eq', 200);

    cy.url().should('eq', 'http://localhost:4200/');

    cy.contains('a', 'Product').click();
    cy.url().should('include', '/products');
    cy.wait('@getProducts').its('response.statusCode').should('eq', 200);

    cy.contains('button', 'Buy').click();

    cy.contains('a', 'Cart').click();
    cy.url().should('eq', 'http://localhost:4200/cart');

    cy.get('input[name="promoCode"]').type('RAMI2024');
    cy.get('button').contains('Apply').click();

    cy.get('.text-danger').should('contain', 'Invalid or expired promo code!');
  });
});
