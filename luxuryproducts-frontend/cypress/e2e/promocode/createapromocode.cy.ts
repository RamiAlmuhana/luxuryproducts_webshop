describe('Create a promo code as admin', () => {
    it('Should login as a admin, can go to admin panel and make a promo code', () => {
        cy.visit('/');

        cy.contains('Login').click();
        cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
        cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
        cy.get('button').contains('Login').click();

        cy.contains('Admin Page').click();
        cy.contains('PromoCode').click();
        cy.contains('+ Add New Promo').click();

        cy.get('input[formControlName="code"]').type('TestCode');
        cy.get('input[formControlName="discount"]').type('5');
        cy.get('input[formControlName="startDate"]').type('2024-01-01T00:00:00');
        cy.get('input[formControlName="expiryDate"]').type('2024-12-31T23:59:59');
        cy.get('input[formControlName="maxUsageCount"]').type("1");
        cy.get('select[formControlName="type"]').select('Fixed Amount');
        cy.get('input[formControlName="minSpendAmount"]').type('50');
        cy.get('select[formControlName="categoryId"]').select('Bags');

        cy.contains('Create Promo Code').click();







    });
});
