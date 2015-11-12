package ca.uhn.fhir.rest.param;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import ca.uhn.fhir.rest.method.QualifiedParamList;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;

public class DateRangeParamTest {

	private static SimpleDateFormat ourFmt;

	static {
		ourFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSSS");
	}

	private DateRangeParam create(String theString) {
		return new DateRangeParam(new DateParam(theString));
	}

	@Test
	public void testAddAnd() {
		assertEquals(1, new DateAndListParam().addAnd(new DateOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new NumberAndListParam().addAnd(new NumberOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new ReferenceAndListParam().addAnd(new ReferenceOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new QuantityAndListParam().addAnd(new QuantityOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new UriAndListParam().addAnd(new UriOrListParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new StringAndListParam().addAnd(new StringOrListParam()).getValuesAsQueryTokens().size());
	}

	@Test
	public void testAndList() {
		assertNotNull(new DateAndListParam().newInstance());
		assertNotNull(new NumberAndListParam().newInstance());
		assertNotNull(new ReferenceAndListParam().newInstance());
		assertNotNull(new QuantityAndListParam().newInstance());
		assertNotNull(new UriAndListParam().newInstance());
		assertNotNull(new StringAndListParam().newInstance());
	}

	@Test
	public void testAndOr() {
		assertEquals(1, new DateOrListParam().addOr(new DateParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new NumberOrListParam().addOr(new NumberParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new ReferenceOrListParam().addOr(new ReferenceParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new QuantityOrListParam().addOr(new QuantityParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new UriOrListParam().addOr(new UriParam()).getValuesAsQueryTokens().size());
		assertEquals(1, new StringOrListParam().addOr(new StringParam()).getValuesAsQueryTokens().size());
	}

	@Test
	public void testDay() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create(">=2011-01-01", "<2011-01-02").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-02 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-03 00:00:00.0000"), create(">2011-01-01", "<=2011-01-02").getUpperBoundAsInstant());
	}

	@Test
	public void testFromQualifiedDateParam() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01-01").getLowerBoundAsInstant());
		assertEquals(null, create(">=2011-01-01").getUpperBoundAsInstant());

		assertEquals(null, create("<=2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("<=2011-01-01").getUpperBoundAsInstant());
	}

	@Test
	public void testMonth() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01", "<2011-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-02-01 00:00:00.0000"), create(">=2011-01", "<2011-02").getUpperBoundAsInstant());

		assertEquals(parse("2011-02-01 00:00:00.0000"), create(">2011-01", "<=2011-02").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-03-01 00:00:00.0000"), create(">2011-01", "<=2011-02").getUpperBoundAsInstant());
	}

	@Test
	public void testOnlyOneParam() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create("2011-01-01").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-02 00:00:00.0000"), create("2011-01-01").getUpperBoundAsInstant());
	}

	@Test
	public void testOrList() {
		assertNotNull(new DateOrListParam().newInstance());
		assertNotNull(new NumberOrListParam().newInstance());
		assertNotNull(new ReferenceOrListParam().newInstance());
		assertNotNull(new QuantityOrListParam().newInstance());
		assertNotNull(new UriOrListParam().newInstance());
		assertNotNull(new StringOrListParam().newInstance());
	}

	@Test
	public void testSecond() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011-01-01T00:00:00", "<2011-01-01T01:00:00").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-01 02:00:00.0000"), create(">=2011-01-01T00:00:00", "<2011-01-01T02:00:00").getUpperBoundAsInstant());

		assertEquals(parse("2011-01-01 00:00:01.0000"), create(">2011-01-01T00:00:00", "<=2011-01-01T02:00:00").getLowerBoundAsInstant());
		assertEquals(parseM1("2011-01-01 02:00:01.0000"), create(">2011-01-01T00:00:00", "<=2011-01-01T02:00:00").getUpperBoundAsInstant());
	}

	@Test
	public void testYear() throws Exception {
		assertEquals(parse("2011-01-01 00:00:00.0000"), create(">=2011", "<2012").getLowerBoundAsInstant());
		assertEquals(parseM1("2012-01-01 00:00:00.0000"), create(">=2011", "<2012").getUpperBoundAsInstant());

		assertEquals(parse("2012-01-01 00:00:00.0000"), create(">2011", "<=2012").getLowerBoundAsInstant());
		assertEquals(parseM1("2014-01-01 00:00:00.0000"), create(">2011", "<=2013").getUpperBoundAsInstant());
	}

	private static DateRangeParam create(String theLower, String theUpper) throws InvalidRequestException {
		DateRangeParam p = new DateRangeParam();
		List<QualifiedParamList> tokens = new ArrayList<QualifiedParamList>();
		tokens.add(QualifiedParamList.singleton(null, theLower));
		if (theUpper != null) {
			tokens.add(QualifiedParamList.singleton(null, theUpper));
		}
		p.setValuesAsQueryTokens(tokens);
		return p;
	}

	public static Date parse(String theString) throws ParseException {
		return ourFmt.parse(theString);
	}

	public static Date parseM1(String theString) throws ParseException {
		return new Date(ourFmt.parse(theString).getTime() - 1L);
	}

}
