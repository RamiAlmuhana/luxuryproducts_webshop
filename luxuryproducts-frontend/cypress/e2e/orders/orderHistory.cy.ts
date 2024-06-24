// cypress/e2e/orderHistory.spec.ts
import { setupInterceptions } from '../../support/interceptions';

describe('Order History Test with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions();
  });

  it('should display the order and the ordered products in order history', () => {
    cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
    cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
    cy.get('button').contains('Login').click();

    cy.wait('@login').its('response.statusCode').should('eq', 200);
    cy.wait('@getUser').its('response.statusCode').should('eq', 200);

    cy.url().should('eq', 'http://localhost:4200/');
    cy.contains("a", "Order History").click();
    cy.url().should('eq', 'http://localhost:4200/order-history');

    cy.get('.order').should('contain', 'John van');
    cy.get('.order').should('contain', '1234AB');
    cy.get('.order').should('contain', '10');
    cy.get('.order').should('contain', '15.99');
    cy.get('.order').should('contain', '5.99');
    cy.get('.order').should('contain', 'SUMMER2024');
    cy.get('.order').should('contain', 'May 24, 2024, 12:00:00 PM');

    cy.get('.product').within(() => {
      cy.get('.product-name').should('contain', "Tom Clancy's Rainbow Six Siege");
      cy.get('.product-price').should('contain', '15.99');
    });
  });
});
