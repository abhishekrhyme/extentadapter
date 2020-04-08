Feature: Test Facebook smoke scenario

  Scenario: Test login
    Given open facebook
    When enter creds
    Then user login
