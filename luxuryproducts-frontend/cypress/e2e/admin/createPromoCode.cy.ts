import { setupInterceptions } from '../../support/interceptions';

describe('Create Promo Code Test with Mock Data', () => {
  beforeEach(() => {
    setupInterceptions();
    cy.visit('http://localhost:4200/auth/login');
  });

  it('should login as admin and create a new promo code', () => {
    // Admin login
    cy.contains('label', 'Email').siblings('input').type('admin@mail.com');
    cy.contains('label', 'Password').siblings('input').type('Admin123!');
    cy.get('button').contains('Login').click();

    cy.wait('@login').its('response.statusCode').should('eq', 200);
    cy.wait('@getUser').its('response.statusCode').should('eq', 200);

    cy.url().should('eq', 'http://localhost:4200/');

    cy.contains('a', 'Admin Page').click();
    cy.url().should('include', '/admin');

    cy.contains('a', "PromoCode").click();
    cy.url().should('include', '/admin/promocode-list');

    cy.contains('a', "+ Add New Promo").click();
    cy.url().should('include', '/admin/promocode-list/promocode-add');

    cy.get('input[formControlName="code"]').type('SUMMER2024');
    cy.get('input[formControlName="discount"]').type('10');
    cy.get('input[formControlName="startDate"]').type('2024-01-01T00:00:00');
    cy.get('input[formControlName="expiryDate"]').type('2024-12-31T23:59:59');
    cy.get('input[formControlName="maxUsageCount"]').type("1");
    cy.get('select[formControlName="type"]').select('FIXED_AMOUNT');
    cy.get('input[formControlName="minSpendAmount"]').type('100');


    cy.get('button').contains('Create Promo Code').click();

  });
});
