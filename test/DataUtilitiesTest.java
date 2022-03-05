//package org.jfree.data;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.InvalidParameterException;
import java.util.ArrayList;

import org.jfree.data.DataUtilities;
import org.jfree.data.KeyedValues;
import org.jfree.data.Values2D;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DataUtilitiesTest {

	private Values2D value;
	
	@BeforeEach
	void setup() throws Exception {
		value = mock(Values2D.class);
		when(value.getColumnCount()).thenReturn(4);
		when(value.getRowCount()).thenReturn(3);
		when(value.getValue(0,2)).thenReturn(5);
		when(value.getValue(1,2)).thenReturn(7);
		when(value.getValue(2,2)).thenReturn(1);
		
	}

	// CalculateColumnTotal Tests
	
	@Test
	void testCalculateColumnTotal() {
		assertEquals(13, DataUtilities.calculateColumnTotal(value, 2), .01d);
		verify(value, times(3)).getValue(anyInt(), anyInt());
	}		
	
	@Test
	void calculateColumnTotalTestUndefinedColumn() {
		assertEquals(0, DataUtilities.calculateColumnTotal(value, 1));
		verify(value, times(3)).getValue(anyInt(), anyInt());
	}
	
	@Test
	void calculateColumnTotalTestInvalidColumn() {
		assertEquals(0, DataUtilities.calculateColumnTotal(value, -1));
	}
	
	@Test
	void calculateColumnTotalTestNullData() {
		assertThrows(InvalidParameterException.class, () -> DataUtilities.calculateColumnTotal(null, 2));
	}
	

	// CalcluateRowTotal Tests
	
	@Test
	void calculateRowTotal() {
		assertEquals(5, DataUtilities.calculateRowTotal(value, 0));
		verify(value, times(3)).getValue(anyInt(), anyInt());
	}
	
	@Test
	void calculateRowTotalUndefined() {
		assertEquals(0, DataUtilities.calculateRowTotal(value, 5));
		verify(value, times(3)).getValue(anyInt(), anyInt());
	}
	
	@Test
	void calculateRowTotalInvalidItem() {
		assertEquals(0, DataUtilities.calculateRowTotal(value, -8));
		verify(value, times(3)).getValue(anyInt(), anyInt());
	}
	
	
	@Test
	void calculateRowTotalTestNullData() {
		assertThrows(InvalidParameterException.class, () -> DataUtilities.calculateRowTotal(null, 2));
	}
	
	// CreateNumebrArray Tests
	
	@Test
	void createNumberArrayTest() {
		double[] data = {1.2, 2.3, 3.4};
		assertEquals("Number[]", DataUtilities.createNumberArray(data).getClass().getSimpleName());
	}
	
	@Test
	void createNumberArrayCorrectlyConvertsElement() {
		double[] data = {1.2, 2.3, 3.4};
		assertEquals(2.3, DataUtilities.createNumberArray(data)[1].doubleValue());
	}
	
	@Test
	void createNumberArrayCorrectlyConvertsWholeArray() {
		double[] data = {1.2, 2.3, 3.4};
		Double[] expected = {1.2, 2.3, 3.4};
		assertArrayEquals(expected, DataUtilities.createNumberArray(data));
	}
	
	@Test
	void createNumberArrayTestNull() {
		double[] data = null;
		assertThrows(InvalidParameterException.class, () -> DataUtilities.createNumberArray(data));
	}
	
	@Test
	void createNumberArraySingleElement() {
		double[] data = {1.2};
		assertEquals(1, DataUtilities.createNumberArray(data).length);
	}
	
	// Create2DNumberArray Test
	
	@Test
    void createNumberArray2DTest() {
        double[] data = {1,2,3,4};
        double[] data2 = {5,6,7,8};
        double[][] data3 = {data,data2};
        double[][] expected = {{(Double) 1.0,(Double) 2.0,(Double) 3.0, (Double) 4.0}, 
        						{(Double) 5.0, (Double) 6.0, (Double) 7.0, (Double) 8.0}};
        assertArrayEquals(expected, DataUtilities.createNumberArray2D(data3));
    }
	
	// GetCumulativePercentages Tests
	
	@Test
    void testGetCumulativePercentages() {

		KeyedValues list = mock(KeyedValues.class);
        when(list.getValue(0)).thenReturn(5);
        when(list.getValue(1)).thenReturn(9);
        when(list.getValue(2)).thenReturn(2);
        when(list.getKey(0)).thenReturn(0);
        when(list.getKey(1)).thenReturn(1);
        when(list.getKey(2)).thenReturn(2);
        when(list.getIndex(5)).thenReturn(0);
        when(list.getIndex(9)).thenReturn(1);
        when(list.getIndex(2)).thenReturn(2);
        when(list.getItemCount()).thenReturn(3);
        ArrayList<Integer> keys = new ArrayList();
        keys.add(0);
        keys.add(1);
        keys.add(2);
        when(list.getKeys()).thenReturn(keys);

        double[] expectedValues = {0.3125, 0.875, 1.0};
        
        KeyedValues actual = DataUtilities.getCumulativePercentages(list);
        double[] actualValues = new double[3];
        
        for (int i=0; i<actual.getItemCount(); i++){
        	actualValues[i] = (double) actual.getValue(i);
        }
        
        assertArrayEquals(expectedValues,actualValues);
        
    }

	@Test
    void testGetCumulativePercentagesWithZero() {
        
        KeyedValues list = mock(KeyedValues.class);
        when(list.getValue(0)).thenReturn(0);
        when(list.getValue(1)).thenReturn(1);
        when(list.getKey(0)).thenReturn(0);
        when(list.getKey(1)).thenReturn(1);
        when(list.getIndex(0)).thenReturn(0);
        when(list.getIndex(1)).thenReturn(1);
        when(list.getItemCount()).thenReturn(2);
        ArrayList<Integer> keys = new ArrayList();
        keys.add(0);
        keys.add(1);
        when(list.getKeys()).thenReturn(keys);

        double[] expectedValues = {0.0, 1.0};
        
        KeyedValues actual = DataUtilities.getCumulativePercentages(list);
        double[] actualValues = new double[2];
        
        for (int i=0; i<actual.getItemCount(); i++){
        	actualValues[i] = (double) actual.getValue(i);
        }
        
        assertArrayEquals(expectedValues,actualValues);
        
    }
	
	@Test
    void testGetCumulativePercentagesWithSomeNeg() {
        
        KeyedValues list = mock(KeyedValues.class);
        when(list.getValue(0)).thenReturn(-5);
        when(list.getValue(1)).thenReturn(5);
        when(list.getValue(2)).thenReturn(5);
        when(list.getKey(0)).thenReturn(0);
        when(list.getKey(1)).thenReturn(1);
        when(list.getKey(2)).thenReturn(2);
        when(list.getIndex(-5)).thenReturn(0);
        when(list.getIndex(5)).thenReturn(1);
        when(list.getIndex(5)).thenReturn(2);
        when(list.getItemCount()).thenReturn(3);
        ArrayList<Integer> keys = new ArrayList();
        keys.add(0);
        keys.add(1);
        keys.add(2);
        when(list.getKeys()).thenReturn(keys);

        double[] expectedValues = {-1.0, 0.0, 1.0};
        
        KeyedValues actual = DataUtilities.getCumulativePercentages(list);
        double[] actualValues = new double[3];
        
        for (int i=0; i<actual.getItemCount(); i++){
        	actualValues[i] = (double) actual.getValue(i);
        }
        
        assertArrayEquals(expectedValues,actualValues);
        
    }

	@Test
    void testGetCumulativePercentagesWithAllNeg() {
        
        KeyedValues list = mock(KeyedValues.class);
        when(list.getValue(0)).thenReturn(-5);
        when(list.getValue(1)).thenReturn(-5);
        when(list.getKey(0)).thenReturn(0);
        when(list.getKey(1)).thenReturn(1);
        when(list.getIndex(-5)).thenReturn(0);
        when(list.getIndex(-5)).thenReturn(1);
        when(list.getItemCount()).thenReturn(2);
        ArrayList<Integer> keys = new ArrayList();
        keys.add(0);
        keys.add(1);
        when(list.getKeys()).thenReturn(keys);

        double[] expectedValues = {0.5, 1.0};
        
        KeyedValues actual = DataUtilities.getCumulativePercentages(list);
        double[] actualValues = new double[2];
        
        for (int i=0; i<actual.getItemCount(); i++){
        	actualValues[i] = (double) actual.getValue(i);
        }
        
        assertArrayEquals(expectedValues,actualValues);
        
    }
	
	@Test
    void testGetCumulativePercentagesWithZeroDivision() {
/**
 * Presumably if the sum of the provided values is 0, 
 * there would be an attempt at zero-division when getting the percentages.        
 */
        KeyedValues list = mock(KeyedValues.class);
        when(list.getValue(0)).thenReturn(-5);
        when(list.getValue(1)).thenReturn(5);
        when(list.getKey(0)).thenReturn(0);
        when(list.getKey(1)).thenReturn(1);
        when(list.getIndex(-5)).thenReturn(0);
        when(list.getIndex(5)).thenReturn(1);
        when(list.getItemCount()).thenReturn(2);
        ArrayList<Integer> keys = new ArrayList();
        keys.add(0);
        keys.add(1);
        when(list.getKeys()).thenReturn(keys);

        assertThrows(ArithmeticException.class,() -> DataUtilities.getCumulativePercentages(list));
        
    }

}
