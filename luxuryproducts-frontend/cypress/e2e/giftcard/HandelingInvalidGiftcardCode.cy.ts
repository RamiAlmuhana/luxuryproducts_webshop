describe('Invalid Giftcard code', () => {
    it('Should show a message when a invalid giftcard code is being used', () => {
        cy.visit('/');

        cy.contains('Login').click();
      cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
      cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
        cy.get('button').contains('Login').click();

        cy.contains('Home').click();
        cy.contains('Products').click();
        cy.contains('Details').click();

        cy.contains('Add to Cart').click();
        cy.contains('Cart').click();

        cy.get('input[placeholder="Enter giftcard code"]').should('be.visible').type('SampleCode');
        cy.get('input[placeholder="Enter giftcard code"]').siblings('button').contains('Apply').click();

        cy.get('input[placeholder="Enter giftcard code"]').siblings('button').contains('Apply').click();
        cy.on('window:alert', (message) => {
            expect(message).to.equal('Cypress test: Dit is geen geldige Giftcard Code');
        });
    });
});
