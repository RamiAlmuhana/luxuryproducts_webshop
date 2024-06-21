describe('Edit and Delete a promo code', () => {
    it('Should login as a admin, can go to admin pane;, edit a promo code and delete promo code', () => {
        cy.visit('/');

        cy.contains('Login').click();
        cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
        cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
        cy.get('button').contains('Login').click();

        cy.contains('Admin Page').click();
        cy.contains('Promo-stats').click();




    });
});
