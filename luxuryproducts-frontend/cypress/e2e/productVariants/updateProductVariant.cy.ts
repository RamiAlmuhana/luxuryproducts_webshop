describe('ProductVariant Update', () => {
  it('should display updated data when product info gets updated', () => {
    cy.visit('/');

    cy.contains('Login').click();
    cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
    cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
    cy.get('button').contains('Login').click();

    cy.get('.navbar').contains('Products').click();
    cy.get('.navbar').contains('Products').click();

    cy.get('.container-fluid.bg-light.py-5 .row.align-items-center')
      .eq(4)
      .within(() => {
        cy.get('a.btn.btn-primary').contains('Details').click();
      });

    cy.get(
      '[src="https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-19186555_1039864_ED.jpg?&op_usm=1.75,1.0,6.0&$cropN=0.1,0.1,0.8,0.8&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp"]'
    ).click();

    cy.get('.buyProduct-overlay > :nth-child(2) > span').contains('16,000.00');
    cy.get(':nth-child(7) > p').contains('Luxembourg');

    cy.contains('Admin Page').click();
    cy.contains('Product-Variants').click();
    cy.get('[ng-reflect-router-link="update-variant"]').click();

    cy.get('#product').select(4);
    cy.get('#productVariantIndex').select(1);
    cy.get('#price').clear().type('3200000');
    cy.get(':nth-child(3) > .btn').click();
    cy.get('#size-3').type('10');
    cy.get('#stock-3').clear().type('30');
    cy.get('.btn-secondary').click();

    cy.contains('Products').click();
    cy.get('.container-fluid.bg-light.py-5 .row.align-items-center')
      .eq(4)
      .within(() => {
        cy.get('a.btn.btn-primary').contains('Details').click();
      });

    cy.get(
      '[src="https://media.tiffany.com/is/image/Tiffany/EcomItemL2/tiffany-co-schlumbergersixteen-stone-ring-19186555_1039864_ED.jpg?&op_usm=1.75,1.0,6.0&$cropN=0.1,0.1,0.8,0.8&defaultImage=NoImageAvailableInternal&&defaultImage=NoImageAvailableInternal&fmt=webp"]'
    ).click();

    cy.get('.buyProduct-overlay > :nth-child(2) > span').should(
      'not.equal',
      '16,000.00'
    );
    cy.get('[style="margin-bottom: 5%;"]').select(3).contains('10 - (30)');
  });
});
