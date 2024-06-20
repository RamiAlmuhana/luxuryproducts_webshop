describe('Productionstop', () => {
  it('when admin stops the production of a product variant a client shouldn`t be able to add the product variant to the cart', () => {
    cy.visit('/');

    cy.contains('Login').click();
    cy.contains('label', 'Email').siblings('input').type('admin@mail.com');
    cy.contains('label', 'Password').siblings('input').type('Test123!');
    cy.get('button').contains('Login').click();

    cy.get('.navbar').contains('Products').click();
    cy.contains('a', 'Product').click();

    cy.contains('button', 'Color').click();
    cy.contains('a', 'White').click();
    cy.contains('a', 'Details').click();

    cy.get('[style="margin-bottom: 5%;"]').select(1).contains('M - (50)');

    cy.contains('Admin Page').click();
    cy.contains('Product-Variants').click();
    cy.get('[ng-reflect-router-link="productionstop"]').click();
    cy.get('#product').select('CAMERA TAS');
    cy.get('#productVariantIndex').select(0);
    cy.get('#productVariatieIndex').select(1);
    cy.get('#productionStop').select(0);
    cy.get('.btn').click();
    cy.contains('a', 'Product').click();

    cy.contains('button', 'Color').click();
    cy.contains('a', 'White').click();
    cy.contains('a', 'Details').click();
    cy.get('[style="margin-bottom: 5%;"]')
      .find('option')
      .eq(1)
      .should('be.disabled');
  });
});
