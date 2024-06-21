describe('Apply Promo code', () => {
    it('Should login succesfully, buy a product and apply the promo code to it', () => {
        cy.visit('/');

        cy.contains('Login').click();
        cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
        cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
        cy.get('button').contains('Login').click();

        cy.contains('Products').click();

        cy.contains('Products').click();
        cy.get('.container-fluid.bg-light.py-5 .row.align-items-center').eq(3).within(() => {
        cy.get('a.btn.btn-primary').contains('Details').click();
        });
        cy.contains('Add to Cart').click();


        cy.contains('Products').click();
        cy.get('.container-fluid.bg-light.py-5 .row.align-items-center').eq(7).within(() => {
            cy.get('a.btn.btn-primary').contains('Details').click();
        });
        cy.contains('Add to Cart').click();

        cy.contains('Cart').click();
        cy.get('input[name="promoCode"]').type('ACTION_DISCOUNT');
        cy.contains('Apply').click();

        cy.contains('Buy').click();

        cy.get('input[id="form1.1"]').type('John');
        cy.get('input[id="form1.2"]').type('van');
        cy.get('input[id="form1.3"]').type('Doe');
        cy.get('input[id="form2.1"]').type('1234AB');
        cy.get('input[id="form2.2"]').type('10');
        cy.get('textarea[id="form3.2"]').type('Please deliver between 9 AM and 5 PM');

        cy.get('button').contains('Go To Payment').click();
        cy.get('button').contains('Go to my orders').click();


    });



});
