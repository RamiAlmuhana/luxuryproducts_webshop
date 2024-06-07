import { setupInterceptions } from '../../support/interceptions';

describe('Apply Fixed Amount Promo Code Test with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions(2); // Gebruik ID 1 voor de vaste korting promo code
  });

  it('should login, navigate to products page, add a product to the cart and apply a fixed amount promo code', () => {
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

    cy.wait('@validatePromoCode').its('response.statusCode').should('eq', 200);

    cy.get('.text-danger').should('contain', 'Minimum spend amount for this promo code is 50');
  });
});
