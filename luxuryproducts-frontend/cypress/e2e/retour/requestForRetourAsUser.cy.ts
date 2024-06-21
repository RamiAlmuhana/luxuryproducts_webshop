describe('Make a retour request', () => {
  it('Should login to your account, buy a products then make request for retour', () => {
    cy.visit('/', {
      //   onBeforeLoad(win) {
      //     cy.stub(win, 'prompt').as('prompt').returns('Not a good product');
      //   },
    });

    cy.contains('Login').click();
    cy.contains('label', 'Email').siblings('input').type('test@mail.com');
    cy.contains('label', 'Password').siblings('input').type('Test123!');
    cy.get('button').contains('Login').click();

    cy.contains('Products').click();

    cy.contains('Products').click();

    cy.get('.container-fluid.bg-light.py-5 .row.align-items-center')
      .eq(6)
      .within(() => {
        cy.get('a.btn.btn-primary').contains('Details').click();
      });

    cy.get('button').contains('Add to Cart').click();

    cy.contains('Cart').click();
    cy.contains('Buy').click();

    cy.get('input[id="form1.1"]').type('John');
    cy.get('input[id="form1.2"]').type('van');
    cy.get('input[id="form1.3"]').type('Doe');
    cy.get('input[id="form2.1"]').type('1234AB');
    cy.get('input[id="form2.2"]').type('10');

    cy.contains('Go To Payment').click();
    cy.contains('Go to my order').click();

    cy.get('button').contains('Retour').eq(0).click();

    // cy.get('@prompt').should('have.always.returned', 'Not a good product');
    // cy.get('@prompt').should('have.been.calledOnce');

    cy.contains('Products').click();
    cy.contains('Products').click();
    cy.contains('Order History').click();
  });
});
