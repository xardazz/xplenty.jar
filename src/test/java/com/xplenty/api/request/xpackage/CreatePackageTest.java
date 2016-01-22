/**
 * 
 */
package com.xplenty.api.request.xpackage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.xplenty.api.Xplenty;
import com.xplenty.api.exceptions.XplentyAPIException;
import com.xplenty.api.http.Http;
import com.xplenty.api.http.JsonMapperFactory;
import com.xplenty.api.http.Response;
import com.xplenty.api.model.MemberTest;
import com.xplenty.api.model.Package;
import com.xplenty.api.model.PackageTest;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author xardas
 *
 */
public class CreatePackageTest extends TestCase {
	
	@Before
	public void setUp() {
		
	}
	
	@Test
	public void testIntegrity() {

        final Date now = new Date();
        CreatePackage cc = new CreatePackage(PackageTest.createMockPackageForCreation());
		assertEquals(Xplenty.Resource.CreatePackage.value, cc.getEndpoint());
		assertEquals(Xplenty.Resource.CreatePackage.name, cc.getName());
		assertEquals(Http.Method.POST, cc.getHttpMethod());
		assertEquals(Http.MediaType.JSON, cc.getResponseType());
		assertTrue(cc.hasBody());
		assertNotNull(cc.getBody());
	}
	
	@Test
	public void testValidResponseHandling() throws JsonProcessingException, UnsupportedEncodingException {
		Date now = new Date(); 
		Package c = PackageTest.createMockPackage(now);

		String json = JsonMapperFactory.getInstance().writeValueAsString(MemberTest.createMockMemberForCreation());
        assertNotNull(json);

		json = JsonMapperFactory.getInstance().writeValueAsString(c);

        CreatePackage cc = new CreatePackage(PackageTest.createMockPackageForCreation());
		c = cc.getResponse(Response.forContentType(Http.MediaType.JSON,
                json,
                Status.CREATED.getStatusCode(),
                new HashMap<String, String>()));
		
		assertNotNull(c);
		assertEquals(new Long(666), c.getId());
        assertEquals("TestPack", c.getName());
        assertEquals("TestPack Description", c.getDescription());
        assertEquals(Xplenty.PackageFlowType.workflow, c.getFlowType());
        assertEquals("https://testapi.xplenty.com/api/package/666", c.getUrl());
        assertEquals("https://testapi.xplenty.com/package/666", c.getHtmlUrl());
        assertEquals(Xplenty.PackageStatus.active, c.getStatus());
        assertEquals(111, c.getOwnerId().longValue());
        assertTrue(Math.abs(now.getTime() - c.getCreatedAt().getTime()) < 1000); //fractions of second are not serialized
        assertTrue(Math.abs(now.getTime() - c.getUpdatedAt().getTime()) < 1000); //fractions of second are not serialized

        Map<String, String> packVars = c.getVariables();
        assertNotNull(packVars);
        assertEquals("val1", packVars.get("var_1"));
        assertEquals("super$$$\"complex'val\n\t", packVars.get("var_2"));
	}
	
	@Test
	public void testInvalidResponseHandling() throws JsonProcessingException, UnsupportedEncodingException {
        Date now = new Date();
        Package c = PackageTest.createMockPackage(now);
        String json = JsonMapperFactory.getInstance().writeValueAsString(c).replace("{", "[");

		try {
            CreatePackage cc = new CreatePackage(PackageTest.createMockPackageForCreation());
            c = cc.getResponse(Response.forContentType(Http.MediaType.JSON,
                    json,
                    Status.CREATED.getStatusCode(),
                    new HashMap<String, String>()));
			assertTrue(false);
		} catch (XplentyAPIException e) {
			assertEquals(Xplenty.Resource.CreatePackage.name + ": error parsing response object", e.getMessage());
		}

	}
}