package com.github.albfernandez.cleangpstrace;
/*
(C) Copyright 2014-2015 Alberto Fernández <infjaf@gmail.com>

This file is part of cleangpstrace.

Foobar is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Foobar is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
*/


import org.junit.Assert;
import org.junit.Test;

public class NMEAChecksumTest {

	@Test
	public void testCalculateChecksum () {
		Assert.assertEquals("45", NMEAChecksum.calculateCheckSum("GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A"));
	}
	
	@Test
	public void testValidChecksum () {
	    Assert.assertTrue(NMEAChecksum.isValidCheckSum("$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*45"));
	    Assert.assertTrue(NMEAChecksum.isValidCheckSum("$GPRMC,081329,V,4221.870,N,00457.240,W,4.86,337.14,150719,,*10"));
	}
	@Test
	public void testInvalidChecksum () {
		Assert.assertFalse(NMEAChecksum.isValidCheckSum("$GPRMC,092751.000,A,5321.6802,N,00630.3371,W,0.06,31.66,280511,,,A*47"));
	}
}
