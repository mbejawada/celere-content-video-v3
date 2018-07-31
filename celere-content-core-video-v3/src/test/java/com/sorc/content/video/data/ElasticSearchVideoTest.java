/**
 * 
 */
package com.sorc.content.video.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.sorc.content.core.util.test.GettersSettersTestHelper;
import com.sorc.content.video.dao.data.AccessControl;
import com.sorc.content.video.dao.data.Category;
import com.sorc.content.video.dao.data.ElasticSearchVideo;
import com.sorc.content.video.dao.data.KalturaAdCuePointObj;
import com.sorc.content.video.dao.data.Link;
import com.sorc.content.video.dao.data.LiveEvents;
import com.sorc.content.video.dao.data.Meta;
import com.sorc.content.video.dao.data.MetaMediaBasicFields;
import com.sorc.content.video.dao.data.VehicleTag;
import com.sorc.content.video.dao.data.Video;

/**
 * @author rakesh.moradiya
 *
 */
public class ElasticSearchVideoTest {

	@Test
	public void testGetSetEquals() throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			InstantiationException, IntrospectionException {
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						ElasticSearchVideo.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						AccessControl.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						Category.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						KalturaAdCuePointObj.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						Link.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						LiveEvents.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						Meta.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						MetaMediaBasicFields.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						VehicleTag.class, false));
		assertTrue(GettersSettersTestHelper
				.assertBasicGetterSetterEqualsHashCodeBehavior(
						Video.class, false));		
	}
	
	@Test
	public void testListing() {
		String id = "0_kvr8t1ug";					
		
		ElasticSearchVideo elasticSearchVideo = new ElasticSearchVideo();
		elasticSearchVideo.setId(id);		

		assertEquals(id, elasticSearchVideo.getId());
	}
}
