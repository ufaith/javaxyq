/**
 * 
 */
package com.javaxyq.profile;

import java.awt.Point;
import java.util.List;

import com.javaxyq.core.DataManager;
import com.javaxyq.core.DataStore;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.profile.impl.ProfileManagerImpl;

/**
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class ProfileTest {

	public static void main(String[] args) throws ProfileException {
		//testLoadProfile();
		//testListProfiles();
		//testSaveProfile();
		testNewProfile();
	}

	public static void testListProfiles() throws ProfileException {
		ProfileManager profileManager = new ProfileManagerImpl();
		long startTime = System.currentTimeMillis();
		System.out.println("Listing Profiles: "+new java.util.Date());
		List<Profile> profiles = profileManager.listProfiles();
		for (Profile profile : profiles) {
			System.out.println(profile);
		}
		long endTime = System.currentTimeMillis();
		System.out.println("Listed Profiles: "+new java.util.Date()+", cost "+(endTime-startTime));
	}

	public static void testLoadProfile() throws ProfileException {
		ProfileManager profileManager = new ProfileManagerImpl();
		long startTime = System.currentTimeMillis();
		System.out.println("loading Profile: "+new java.util.Date());
		Profile profile = profileManager.loadProfile("0");
		System.out.println(profile);
		long endTime = System.currentTimeMillis();
		System.out.println("loaded Profiles: "+new java.util.Date()+", cost "+(endTime-startTime));
	}
	
	public static void testSaveProfile() throws ProfileException {
		ProfileManager profileManager = new ProfileManagerImpl();
		Profile profile = profileManager.loadProfile("0");
		String name = "save-123";
		profile.setName(name);
		System.out.println("saving Profile: "+new java.util.Date());
		System.out.println(profile);
		long startTime = System.currentTimeMillis();
		profileManager.saveProfile(profile);
		long endTime = System.currentTimeMillis();
		profile = profileManager.loadProfile(name);
		System.out.println(profile);
		System.out.println("saved Profiles: "+new java.util.Date()+", cost "+(endTime-startTime));
		
	}
	
	public static void testNewProfile() throws ProfileException {
		long startTime = System.currentTimeMillis();
		System.out.println("new Profile: "+new java.util.Date());

		ProfileManager profileManager = new ProfileManagerImpl();
		DataManager dataManager = new DataStore(null);
		String name = "4";
		String character = "0002";
		String roleName = "œ¿øÕ’ﬂ";
		String sceneId = "1506";
		ItemInstance[] items = null;
		Profile profile = profileManager.newProfile(name);
		PlayerVO data = dataManager.createPlayerData(character);
		data.setName(roleName);
		data.setSceneLocation(new Point(38, 20));
		profile.setPlayerData(data);
		profile.setSceneId(sceneId);
		profile.setItems(items);
		profileManager.saveProfile(profile);
		
		long endTime = System.currentTimeMillis();
		System.out.println(profile);
		profile = profileManager.loadProfile(name);
		System.out.println(profile);
		System.out.println("saved Profiles: "+new java.util.Date()+", cost "+(endTime-startTime));
		
	}

}
