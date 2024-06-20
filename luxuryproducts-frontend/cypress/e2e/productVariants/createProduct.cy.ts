import { last } from 'cypress/types/lodash';

describe('Create Product', () => {
  it('user should be able to order a new product created by the admin', () => {
    cy.visit('/');

    cy.contains('Login').click();
    cy.contains('label', 'Email').siblings('input').type('admin@mail.com');
    cy.contains('label', 'Password').siblings('input').type('Test123!');
    cy.get('button').contains('Login').click();

    cy.contains('Admin Page').click();
    cy.contains('Product-Variants').click();
    cy.get('[ng-reflect-router-link="add-product"]').click();
    cy.get('#name').type('test watch');
    cy.get('#brandId').select(4);
    cy.get('#categoryId').select(0);
    cy.get('#country').type('made in Russia');
    cy.get('.btn').click();

    cy.get('[ng-reflect-router-link="add-variant"]').click();

    cy.get('#product').select(10);
    cy.get('#description').type(
      'this is one of the best test watch in it its category. truly a sublime product'
    );
    cy.get('#price').clear().type('120000');
    cy.get('#color').select(4);
    cy.get('#sizeAndFit').select(0);
    cy.get('#imageUrl1').type(
      'https://i0.wp.com/www.bowtiedlife.com/wp-content/uploads/2023/07/Breitling-Endurance-Ironman.png?resize=600%2C400&ssl=1'
    );
    cy.get('#imageUrl2').type(
      'https://cdn4.ethoswatches.com/the-watch-guide/wp-content/uploads/2021/06/Breitling-Professional-Endurance-Pro-Masthead-Mobile@2x-min.jpg'
    );
    cy.get('#imageUrl3').type(
      'https://cdn4.ethoswatches.com/the-watch-guide/wp-content/uploads/2019/12/5-minBreitling-Professional-Edurance-Pro-.jpg?tr=q-70'
    );

    cy.get('.ng-untouched > .btn').click();
    cy.get('#size-0').type('10');
    cy.get('#stock-0').clear().type('30');
    cy.get(':nth-child(10) > .btn');
    cy.contains('Products').click();
    cy;
  });
});
