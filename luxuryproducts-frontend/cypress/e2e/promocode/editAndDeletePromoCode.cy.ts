describe('Edit and Delete a promo code', () => {
    it('Should login as a admin, can go to admin pane;, edit a promo code and delete promo code', () => {
        cy.visit('/');

        cy.contains('Login').click();
        cy.contains('label', 'Email').siblings('input').type('admin@mail.com');
        cy.contains('label', 'Password').siblings('input').type('Test123!');
        cy.get('button').contains('Login').click();

        cy.contains('Admin Page').click();
        cy.contains('PromoCode').click();

        cy.get('.card').eq(2).within(() => {
            cy.contains('Edit').click();
        });

        
        cy.get('input[formControlName="code"]').type('SUMMER2024');
        cy.get('input[formControlName="code"]').clear().type('UpdateTestCode');
        cy.get('input[formControlName="discount"]').type('500');
        cy.get('input[formControlName="discount"]').clear().type('300');
        cy.get('input[formControlName="startDate"]').type('2024-01-01T00:00:00');
        cy.get('input[formControlName="expiryDate"]').type('2024-12-31T23:59:59');
        cy.get('input[formControlName="maxUsageCount"]').type("100");
        cy.get('input[formControlName="maxUsageCount"]').clear().type('50');
        cy.get('select[formControlName="type"]').select('Fixed Amount');
        cy.get('input[formControlName="minSpendAmount"]').type('10000');
        cy.get('input[formControlName="minSpendAmount"]').clear().type('5000');
        cy.get('select[formControlName="categoryId"]').select('Bags');

        cy.contains('Edit Promo Code').click();

        cy.contains('UpdateTestCode').should('exist').parents('.card').within(() => {
            cy.contains('Delete').click();
        });



    });
});