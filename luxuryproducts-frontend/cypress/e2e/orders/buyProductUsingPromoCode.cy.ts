import {setupInterceptions} from "../../support/interceptions";

describe('Promo Code Test in Cart Page with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions();
  });

  it('should login, navigate to products page, add a product to the cart and apply a promo code', () => {

    cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
    cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
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

    cy.get('.lead').contains('Discounted Price:').should('exist');

    cy.contains('button', 'Buy').click();
    cy.url().should('eq', 'http://localhost:4200/orders');

    cy.get('input[id="form1.1"]').type('John');
    cy.get('input[id="form1.2"]').type('van');
    cy.get('input[id="form1.3"]').type('Doe');
    cy.get('input[id="form2.1"]').type('1234AB');
    cy.get('input[id="form2.2"]').type('10');
    cy.get('textarea[id="form3.2"]').type('Please deliver between 9 AM and 5 PM');

    cy.get('button').contains('Go To Payment').click();
  });
});
