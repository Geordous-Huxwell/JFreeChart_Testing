import org.junit.jupiter.api.Test;
//package src.org.jfree.data;

import static org.junit.Assert.assertTrue;
//import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;

import org.jfree.data.Range;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.security.InvalidParameterException;

/**
 * COMP 3505 - Assignment 2: Black Box Testing
 * @author Joel Conley, Ryan Darby, Colin MacLean
 *
 * Difficulties, Challenges overcome, Lessons learned:
 * 
 * - Found that Black Box testing can be particularly difficult with
 * respect to not always being able to parse whether your test misunderstands
 * the specifications vs when you have found a valid bug.
 * 
 * - Testing the DataUtilities class was especially challenging, first in just 
 * how much effort was required to understand the method specifications, 
 * but also understanding the input objects and what methods need to be mocked. 
 * Being able to see the code would have made this so much easier. 
 * 
 *  - When and how to test for "nullness" was also a recurring issue. 
 *  We landed on only testing for it when the method specifications explicitly
 *  mentioned it was meant to be handled. Any test that we found to be yielding 
 *  a NullPointerException was determined not to be a valid test as this would
 *  be considered a user-progamming error, not a designer-programming error. 
 *  
 *  - It was tricky deciding when and where to trim down redundant tests. 
 *  We decided to remove tests that were testing different row numbers since
 *  the size of the input object is arbitrary and potentially infinite so testing 
 *  2 or 3 rows is no more beneficial than testing 300 different row numbers. 
 *  There was also discussion about when it is useful to use a second test with 
 *  negative numbers. One point made was that addition with positive numbers ought 
 *  to be the same as addition with negative numbers. The counterpoint was that since 
 *  it was Black Box testing, we can't know for sure.   
 */

class RangeTest {

	private Range range;
		
	//CentralValue Tests
	
	@Test
	void centralValueTest() {
		range = new Range(-1,1);
		assertEquals(0, range.getCentralValue(), 0.1d, "The central value of (-1,1) is 0.");
	}
	
	@Test
	void centralValueSameTest() {
		range = new Range(1,1);
		assertEquals(1, range.getCentralValue(), 0.1d, "The central value of (1,1) is 1.");
	}
	
	
	//Combine Test
	
	@Test
	void combineTest() {
		range = new Range(1,3);
		Range range2 = new Range(2,4);
		assertEquals(new Range(1,4), range.combine(range2, range));
	}
	
	@Test
	void combineTestReverseArgumentOrder() {
		range = new Range(1,3);
		Range range2 = new Range(2,4);
		assertEquals(new Range(1,4), range.combine(range, range2));
	}
	
	@Test
	void combineTestIdenticalInputs() {
		range = new Range(1,3);
		Range range2 = new Range(1,3);
		assertEquals(new Range(1,3), range.combine(range2, range));
	}
	
	@Test
	void combineTestWithOneNull() {
		range = new Range(1,4);
		assertEquals(new Range(1,4), range.combine(range, null));
	}
	
	@Test
	void combineTestWithBothNull() {
		assertEquals(null, range.combine(null,null));
	}
	
	//Intersects Tests
	@Test
	void innerIntersectsTest() {
		range = new Range(1,4);
		assertTrue(range.intersects(2,3));
	}
	
	@Test
	void lowerOutUpperInIntersectsTest() {
		range = new Range(1,4);
		assertTrue(range.intersects(0,3));
	}
	
	@Test
	void lowerInUpperOutIntersectsTest() {
		range = new Range(1,4);
		assertTrue(range.intersects(3,5));
	}
	
	@Test
	void outerIntersectsTest() {
		range = new Range(1,4);
		assertTrue(range.intersects(0,5));
	}
	
	@Test
	void equalIntersectsTest() {
		range = new Range(1,4);
		assertTrue(range.intersects(1,4));
	}
	
	@Test
	void belowLowerBoundIntersectsTest() {
		range = new Range(1,4);
		assertFalse(range.intersects(-1,0));
	}
	
	@Test
	void upperEqualsLowerIntersectsTest() {
		range = new Range(1,4);
		assertTrue(range.intersects(-1,1));
	}
	
	@Test
	void lowerEqualsUpperIntersectsTest() {
		range = new Range(1,4);
		assertTrue(range.intersects(4,6));
	}
	
	@Test
	void aboveUpperBoundIntersectsTest() {
		range = new Range(1,4);
		assertFalse(range.intersects(5,7));
	}
	
	//GetLength Tests
	
	@Test
	void getLengthTest() {
		range = new Range(1,4);
		assertEquals(3, range.getLength()); 
	}
	
	@Test
	void getLengthEqualBoundsTest() {
		range = new Range(1,1);
		assertEquals(0, range.getLength()); 
	}
	
	@Test
	void getLengthNegLowBoundTest() {
		range = new Range(-1,1);
		assertEquals(2, range.getLength()); 
	}
	
	
	//To String Test
	
	@Test
	void toStringTest() {
		range = new Range(1,4);
		assertEquals("Range[1.0,4.0]", range.toString());
	}
	
	//Contains Tests
	
	@Test
	void containsTest() {
		range = new Range(1,4);
		assertTrue(range.contains(3));
	}
	
	@Test
	void containsLowerBoundTest() {
		range = new Range(1,4);
		assertTrue(range.contains(1));
	}
	
	@Test
	void containsUpperBoundTest() {
		range = new Range(1,4);
		assertTrue(range.contains(4));
	}
	
	@Test
	void containsBelowLowerBoundTest() {
		range = new Range(1,4);
		assertFalse(range.contains(0));
	}
	
	@Test
	void containsAboveUpperBoundTest() {
		range = new Range(1,4);
		assertFalse(range.contains(5));
	}
	
	
	//Expand Test
	
	@Test
	void expandTest() {
		range = new Range(2,6);
		double lowerMargin = 0.25;
		double upperMargin = 0.5;
		assertEquals(new Range(1,8), range.expand(range,lowerMargin,upperMargin));
	}
	
	@Test
	void zeroExpandTest() {
		range = new Range(2,6);
		double lowerMargin = 0;
		double upperMargin = 0;
		assertEquals(new Range(2,6), range.expand(range,lowerMargin,upperMargin));
	}
	
	@Test
	void expandLowerMarginTest() {
		range = new Range(2,6);
		double lowerMargin = 0.25;
		double upperMargin = 0;
		assertEquals(new Range(1,6), range.expand(range,lowerMargin,upperMargin));
	}
	
	@Test
	void expandUpperMarginTest() {
		range = new Range(2,6);
		double lowerMargin = 0;
		double upperMargin = 0.5;
		assertEquals(new Range(2,8), range.expand(range,lowerMargin,upperMargin));
	}
	
	@Test
	void nullExpandInputTest() {
		range = null;
		double lowerMargin = 0.25;
		double upperMargin = 0.5;
		assertThrows(InvalidParameterException.class, 
				() -> range.expand(range, lowerMargin, upperMargin));
	}
		
	//Lower Bound Test
	
	@Test
	void lowerBoundTest() {
		range = new Range(2,6);
		assertEquals(2,range.getLowerBound());
	}
	
	@Test
	void sameLowerBoundTest() {
		range = new Range(2,2);
		assertEquals(2,range.getLowerBound());
	}

	
	//Equals Test

	@Test
	void identityEqualsTest() {
		range = new Range(1,6);
		assertTrue(range.equals(range));
	}
	
	@Test
	void valueEqualsTest() {
		range = new Range(1,6);
		Range range2 = new Range(1,6);
		assertTrue(range.equals(range2));
	}
	
	@Test
	void falseEqualsTest() {
		range = new Range(1,6);
		Range range2 = new Range(2,5);
		assertFalse(range.equals(range2));
	}
	
	
	//Shift Test
	
	@Test
	void shiftTest() {
		range = new Range(1,6);
		double delta = 5;
		assertEquals(new Range(6,11),range.shift(range,delta));
	}
	
	@Test
	void shiftZeroTest() {
		range = new Range(1,6);
		double delta = 0;
		assertEquals(new Range(1,6),range.shift(range,delta));
	}
	
	@Test
	void shiftNegativeTest() {
		range = new Range(1,6);
		double delta = -1;
		assertEquals(new Range(0,5),range.shift(range,delta));
	}
	
	
	@Test
	void shiftTestInvalidInput() {
		range = null;
		double delta = 5;
		assertThrows(InvalidParameterException.class, 
				() -> range.shift(range,delta));
	}
	
	//Constrain Test
	
	@Test
	void constrainTestInRange() {
		range = new Range(1,3);
		assertEquals(2, range.constrain(2));
	}
	
	@Test
	void constrainTestGreaterThan() {
		range = new Range(1,3);
		assertEquals(3, range.constrain(4));
	}
	
	@Test
	void constrainTestLessThan() {
		range = new Range(1,3);
		assertEquals(1, range.constrain(0));
	}
	
	@Test
	void constrainTestDecimalRoundDown() {
		range = new Range(1,3);
		assertEquals(1, range.constrain(1.2));
	}
	
	@Test
	void constrainTestDecimalRoundUp() {
		range = new Range(1,3);
		assertEquals(1, range.constrain(0.8));
	}
	
	@Test
	void constrainTestNegativeNumber() {
		range = new Range(1,3);
		assertEquals(1, range.constrain(-1));
	}

	//ExpandtoInclude Test
	
	@Test
	void expandToIncludeTestExpandUp() {
		range = new Range(1,3);
		assertEquals(new Range(1,5), Range.expandToInclude(range, 5));
	}
	
	@Test
	void expandToIncludeTestExpandDown() {
		range = new Range(2,4);
		assertEquals(new Range(1,4), Range.expandToInclude(range, 1));
	}
	
	@Test
	void expandToIncludeTestInRange() {
		range = new Range(1,3);
		assertEquals(new Range(1,3), Range.expandToInclude(range, 2));
	}
	
	@Test
	void expandToIncludeTestInitalNull() {
		assertEquals(new Range(1,1), Range.expandToInclude(null, 1));
	}
	
	@Test
	void expandToIncludeTestInRangeDouble() {
		range = new Range(1,3);
		assertEquals(new Range(1,3), Range.expandToInclude(range, 1.2));
	}
	
	@Test
	void expandToIncludeTestExpandUpDouble() {
		range = new Range(1,3);
		assertEquals(new Range(1,5.2), Range.expandToInclude(range, 5.2));
	}
	
	@Test
	void expandToIncludeTestExpandDownDouble() {
		range = new Range(2,4);
		assertEquals(new Range(0.8,4), Range.expandToInclude(range, 0.8));
	}
	
	//getUpperBound Test
	
	@Test
	void getUpperBoundTest() {
		range = new Range(1,3);
		assertEquals(3, range.getUpperBound());
	}
	
	@Test
	void getUpperBoundTestSingleElement() {
		range = new Range(1,1);
		assertEquals(1, range.getUpperBound());
	}
	
	@Test
	void getUpperBoundTestNull() {
		range = null;
		assertThrows(NullPointerException.class, () -> range.getUpperBound());
	}
	
	//shift Test
	
	@Test
	void shiftTestRight() {
		range = new Range(1,3);
		assertEquals(new Range(6,8), Range.shift(range, 5, false));
	}
	
	@Test
	void shiftTestLeft() {
		range = new Range(2,4);
		assertEquals(new Range(1,3), Range.shift(range, -1, false));
	}
	
	@Test
	void shiftTest0() {
		range = new Range(1,3);
		assertEquals(new Range(1,3), Range.shift(range, 0, false));
	}
	
	@Test
	void shiftTestLeftDisallowZeroCrossing() {
		range = new Range(1,3);
		assertEquals(new Range(0,1), Range.shift(range, -2, false));
	}
	
	@Test
	void shiftTestLeftAllowZeroCrossing() {
		range = new Range(1,3);
		assertEquals(new Range(-1,1), Range.shift(range, -2, true));
	}
	
	@Test
	void shiftTestRightDisallowZeroCrossing() {
		range = new Range(-3,-1);
		assertEquals(new Range(-1,0), Range.shift(range, 2, false));
	}
	
	@Test
	void shiftTestRightAllowZeroCrossing() {
		range = new Range(-3,-1);
		assertEquals(new Range(-1,1), Range.shift(range, 2, true));
	}
	
	@Test
	void shiftTestRightDisallowZeroCrossingFullyOver0() {
		range = new Range(-3,-1);
		assertEquals(new Range(0,0), Range.shift(range, 5, false));
	}
	
	@Test
	void shiftTestLeftDisallowZeroCrossingFullyOver0() {
		range = new Range(1,3);
		assertEquals(new Range(0,0), Range.shift(range, -5, false));
	}

	@Test
	void shiftTestNull() {
		range = null;
		assertThrows(InvalidParameterException.class, () -> Range.shift(range, 1, false));
	}
	
}