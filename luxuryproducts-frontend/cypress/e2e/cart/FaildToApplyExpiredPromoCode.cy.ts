import { setupInterceptions } from '../../support/interceptions';

describe('Apply Expired Promo Code Test with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions(4); // Gebruik ID 4 voor de verlopen promo code
  });

  it('should display an error message for an expired promo code', () => {
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

    cy.get('input[name="promoCode"]').type('SUMMER2024');
    cy.get('button').contains('Apply').click();

    cy.wait('@validatePromoCode').its('response.statusCode').should('eq', 400);

    cy.get('.text-danger').should('contain', 'Invalid or expired promo code!');
  });
});
