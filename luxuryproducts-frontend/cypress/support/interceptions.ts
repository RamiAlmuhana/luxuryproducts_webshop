
export const setupInterceptions = (promoCodeId: number = 1, invalidPromo: boolean = false) => {
  cy.intercept('POST', '/api/auth/login', (req) => {
    if (req.body.email === 'invalid@mail.com') {
      req.reply({
        statusCode: 403,
        body: { response: 'FORBIDDEN' }
      });
    } else {
      req.reply({ fixture: 'loginSuccess.json' });
    }
  }).as('login');

  cy.intercept('GET', '/api/auth/user', { fixture: 'user.json' }).as('getUser');
  cy.intercept('GET', '/api/products', { fixture: 'products.json' }).as('getProducts');

  cy.fixture('promoCodes.json').then((data) => {
    const promoCode = data.promoCodes.find((code: { id: number; }) => code.id === promoCodeId);
    if (invalidPromo || new Date(promoCode.expiryDate) < new Date()) {
      cy.intercept('GET', `/api/promocodes/validate?code=SUMMER2024`, {
        statusCode: 400,
        body: { error: 'Invalid or expired promo code!' }
      }).as('validatePromoCode');
    } else {
      cy.intercept('GET', `/api/promocodes/validate?code=SUMMER2024`, { body: promoCode }).as('validatePromoCode');
    }
  });

  cy.intercept('POST', '/api/orders', { fixture: 'orderSuccess.json' }).as('createOrder');
  cy.intercept('GET', '/api/orders/myOrders', { fixture: 'orderHistory.json' }).as('getOrderHistory');
  cy.visit('http://localhost:4200/auth/login');
};
