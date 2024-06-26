package ee.taltech.inbankbackend.service;

import ee.taltech.inbankbackend.config.DecisionEngineConstants;
import ee.taltech.inbankbackend.exceptions.InvalidLoanAmountException;
import ee.taltech.inbankbackend.exceptions.InvalidAgeException;
import ee.taltech.inbankbackend.exceptions.InvalidLoanPeriodException;
import ee.taltech.inbankbackend.exceptions.InvalidPersonalCodeException;
import ee.taltech.inbankbackend.exceptions.NoValidLoanException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class DecisionEngineTest {

    @InjectMocks
    private DecisionEngine decisionEngine;

    private String debtorPersonalCode;
    private String segment1PersonalCode;
    private String segment2PersonalCode;
    private String segment3PersonalCode;

    // Example personal codes for each segment
    @BeforeEach
    void setUp() {
        debtorPersonalCode = "37605030299";
        segment1PersonalCode = "50307172740";
        segment2PersonalCode = "38411266610";
        segment3PersonalCode = "35006069515";
    }
    
    // Testing case when requester has debt
    @Test
    void testDebtorPersonalCode() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 18, 4000L, 12));
    }

    // Testing results when the requester is from the first segment
    @Test
    void testSegment1PersonalCode() throws InvalidLoanPeriodException, InvalidAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment1PersonalCode, 18, 4000L, 12);
        assertEquals(2000, decision.getLoanAmount());
        assertEquals(20, decision.getLoanPeriod());
    }

    // Testing results when the requester is from the second segment
    @Test
    void testSegment2PersonalCode() throws InvalidLoanPeriodException, InvalidAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 18, 4000L, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    // Testing results when the requester is from the third segment
    @Test
    void testSegment3PersonalCode() throws InvalidLoanPeriodException, InvalidAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment3PersonalCode, 18, 4000L, 12);
        assertEquals(10000, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    //Testing case when personal code is invalid
    @Test
    void testInvalidPersonalCode() {
        String invalidPersonalCode = "12345678901";
        assertThrows(InvalidPersonalCodeException.class,
                () -> decisionEngine.calculateApprovedLoan(invalidPersonalCode, 18, 4000L, 12));
    }

    //Testing case when age is invalid
    @Test
    void testInvalidAge() {
        int invalidAge = 17;
        assertThrows(InvalidAgeException.class,
                () -> decisionEngine.calculateApprovedLoan("50307172740", invalidAge, 4000L, 12));
    }

    //Testing case when loan amount is invalid
    @Test
    void testInvalidLoanAmount() {
        Long tooLowLoanAmount = DecisionEngineConstants.MINIMUM_LOAN_AMOUNT - 1L;
        Long tooHighLoanAmount = DecisionEngineConstants.MAXIMUM_LOAN_AMOUNT + 1L;

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 18, tooLowLoanAmount, 12));

        assertThrows(InvalidLoanAmountException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 18, tooHighLoanAmount, 12));
    }

    //Testing case when loan period is invalid
    @Test
    void testInvalidLoanPeriod() {
        int tooShortLoanPeriod = DecisionEngineConstants.MINIMUM_LOAN_PERIOD - 1;
        int tooLongLoanPeriod = DecisionEngineConstants.MAXIMUM_LOAN_PERIOD + 1;

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 18, 4000L, tooShortLoanPeriod));

        assertThrows(InvalidLoanPeriodException.class,
                () -> decisionEngine.calculateApprovedLoan(segment1PersonalCode, 18, 4000L, tooLongLoanPeriod));
    }

    //Testing case when person requested smaller amount than the maximum available for him
    @Test
    void testFindSuitableLoanPeriod() throws InvalidLoanPeriodException, InvalidAgeException, NoValidLoanException,
            InvalidPersonalCodeException, InvalidLoanAmountException {
        Decision decision = decisionEngine.calculateApprovedLoan(segment2PersonalCode, 18, 2000L, 12);
        assertEquals(3600, decision.getLoanAmount());
        assertEquals(12, decision.getLoanPeriod());
    }

    //Testing case when suitable amount and period couldn't be found
    @Test
    void testNoValidLoanFound() {
        assertThrows(NoValidLoanException.class,
                () -> decisionEngine.calculateApprovedLoan(debtorPersonalCode, 18, 10000L, 60));
    }

}

