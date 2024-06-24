describe('Buy and apply Giftcard code', () => {
    it('should login successfully, buy a giftcard, reveice your giftcard code, buy a product and apply on that product the giftcard code', () => {
      cy.visit('/');

      cy.contains('Login').click();
      cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
      cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
      cy.get('button').contains('Login').click();


      cy.contains('Giftcard: €1000').parents('.card').find('button').contains('Buy').click();
      cy.contains('Cart').click();
      cy.contains('Buy').click();


      cy.get('#form1\\.1').type('Testnaam');
      cy.get('#form1\\.2').type('Testnaam');
      cy.get('#form1\\.3').type('Testnaam');
      cy.get('#form2\\.1').type('1049AB');
      cy.get('#form2\\.2').type('5');


      cy.get('button').contains('Go To Payment').click();
      cy.get('button').contains('Go to my orders').click();

      cy.get('.cards_item')
        .first()
        .find('.card_title')
        .invoke('text')
        .then((giftcardCode) => {
          const code = giftcardCode.trim();
          cy.log(`Giftcard code found: ${code}`);


          cy.contains('Products').click();
          cy.get('.container-fluid.bg-light.py-5 .row.align-items-center').eq(8).within(() => {
          cy.get('a.btn.btn-primary').contains('Details').click();
          });

          cy.contains('Add to Cart').click();
          cy.contains('Cart').click();

          cy.get('input[placeholder="Enter giftcard code"]').should('be.visible').type(code);
          cy.get('input[placeholder="Enter giftcard code"]').siblings('button').contains('Apply').click();

          cy.contains('Gift Card Applied');

          cy.contains('Buy').click();

        cy.get('#form1\\.1').type('Testnaam');
        cy.get('#form1\\.2').type('Testnaam');
        cy.get('#form1\\.3').type('Testnaam');
        cy.get('#form2\\.1').type('1049AB');
        cy.get('#form2\\.2').type('5');


        cy.get('button').contains('Go To Payment').click();
        cy.get('button').contains('Go to my orders').click();

        cy.contains('Products').click();
        cy.contains('Products').click();
        cy.get('.container-fluid.bg-light.py-5 .row.align-items-center').eq(3).within(() => {
            cy.get('a.btn.btn-primary').contains('Details').click();
          });

        cy.contains('Add to Cart').click();
        cy.contains('Cart').click();

        cy.get('input[placeholder="Enter giftcard code"]').should('be.visible').type(code);
        cy.get('input[placeholder="Enter giftcard code"]').siblings('button').contains('Apply').click();

        cy.contains('Gift Card Applied');

        cy.contains('Buy').click();

        cy.get('#form1\\.1').type('Testnaam');
        cy.get('#form1\\.2').type('Testnaam');
        cy.get('#form1\\.3').type('Testnaam');
        cy.get('#form2\\.1').type('1049AB');
        cy.get('#form2\\.2').type('5');


        cy.get('button').contains('Go To Payment').click();
        cy.get('button').contains('Go to my orders').click();



        });
    });
  });



