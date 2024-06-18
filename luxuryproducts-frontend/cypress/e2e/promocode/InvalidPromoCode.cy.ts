describe('Apply Promo code', () => {
    it('Should login succesfully, buy a product and apply the promo code to it', () => {
        cy.visit('/');

        cy.contains('Login').click();
        cy.contains('label', 'Email').siblings('input').type('admin@mail.com');
        cy.contains('label', 'Password').siblings('input').type('Test123!');
        cy.get('button').contains('Login').click();

        cy.contains('Products').click();
        cy.contains('Products').click();
        cy.get('.container-fluid.bg-light.py-5 .row.align-items-center').eq(0).within(() => {
        cy.get('a.btn.btn-primary').contains('Details').click();
        });

        cy.contains('Add to Cart').click();
        cy.contains('Cart').click()

        cy.get('input[name="promoCode"]').type('SAMPLE_DISCOUNT');
        cy.contains('Apply').click();
       


    });



});