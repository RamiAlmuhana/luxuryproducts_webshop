describe('filtering products', () => {
  it('the user uses the filters and selects a product. the products has the right content ', () => {
    cy.visit('/');

    cy.contains('Login').click();
    cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
    cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
    cy.get('button').contains('Login').click();

    cy.url().should('eq', 'http://localhost:4200/');
    cy.contains('a', 'Product').click();
    cy.url().should('eq', 'http://localhost:4200/products');
    cy.contains('button', 'Brand').click();
    cy.contains('a', 'Gucci').click();
    cy.contains('a', 'Details').click();

    cy.get('.flexbox-extra-information > :nth-child(3) > p').contains('Gucci');
  });
});
