describe('Default Test Suite', () => {
  it('Visits the Cypress Documentation', () => {
    cy.visit('https://docs.cypress.io')
    cy.contains('Get Started').should('be.visible')
  })

  it('Performs a Search on Cypress Documentation', () => {
    cy.visit('https://docs.cypress.io')
    cy.get('input[placeholder="Search"]').type('network requests{enter}')
    cy.contains('Intercept HTTP requests').should('be.visible')
  })
})
