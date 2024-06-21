describe('Buy and apply Giftcard codes', () => {
    it('should login successfully, buy two giftcards, buy a product, and apply giftcard codes in the cart', () => {
      cy.visit('/');

      cy.contains('Login').click();
      cy.contains('label', 'Email').siblings('input').type('bob@hotmail.com');
      cy.contains('label', 'Password').siblings('input').type('913enm\"D0DylT?k');
      cy.get('button').contains('Login').click();


      cy.get('button').contains('Buy').click();
      cy.get('button').contains('Buy').click();
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
        .then((giftcardCode1) => {
          const code1 = giftcardCode1.trim();
          cy.log(`First Giftcard code found: ${code1}`);

          cy.get('.cards_item')
            .eq(1)
            .find('.card_title')
            .invoke('text')
            .then((giftcardCode2) => {
              const code2 = giftcardCode2.trim();
              cy.log(`Second Giftcard code found: ${code2}`);


              cy.contains('Products').click();
              cy.get('.btn-primary').contains('Details').first().click();
              cy.contains('Add to Cart').click();
              cy.contains('Cart').click();


              cy.get('input[placeholder="Enter giftcard code"]').should('be.visible').type(code1);
              cy.get('input[placeholder="Enter giftcard code"]').siblings('button').contains('Apply').click();
              cy.contains('Gift Card Applied');

              cy.get('input[placeholder="Enter giftcard code"]').clear().type(code2);
              cy.get('input[placeholder="Enter giftcard code"]').siblings('button').contains('Apply').click();
              cy.contains('Gift Card Applied');
            });
        });
    });
  });


