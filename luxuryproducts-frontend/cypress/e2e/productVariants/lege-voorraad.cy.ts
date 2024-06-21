describe('Product stock', () => {
  it('should show message and not alter quantity, when attempting to add an amount of products in the cart that is greater than the stock ', () => {
    cy.visit('/');

    cy.contains('Login').click();
    cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
    cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
    cy.get('button').contains('Login').click();

    cy.contains('Products').click();

    cy.contains('Products').click();
    cy.get('.container-fluid.bg-light.py-5 .row.align-items-center')
      .eq(7)
      .within(() => {
        cy.get('a.btn.btn-primary').contains('Details').click();
      });

    for (let index = 0; index < 5; index++) {
      cy.get('.form-group > #quantity').select('10');
      cy.contains('Add to Cart').click();
    }

    cy.contains('Cart').click();
    cy.get('.card-body > .row > :nth-child(2) > :nth-child(2)').contains('50');

    cy.contains('Products').click();

    cy.contains('Products').click();
    cy.get('.container-fluid.bg-light.py-5 .row.align-items-center')
      .eq(7)
      .within(() => {
        cy.get('a.btn.btn-primary').contains('Details').click();
      });
    cy.contains('Add to Cart').click();
    cy.get('.buyProduct-overlay > h6').should('exist');
    cy.contains('Add to Cart').click();
    cy.contains('Cart').click();
    cy.get('.card-body > .row > :nth-child(2) > :nth-child(2)').contains('50');
  });
});
