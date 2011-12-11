/**
 * 
 */
package ui;

import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.javaxyq.core.DataManager;
import com.javaxyq.core.DataStore;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.data.ItemInstance;
import com.javaxyq.event.ActionEvent;
import com.javaxyq.event.PanelEvent;
import com.javaxyq.event.PanelHandler;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.profile.Profile;
import com.javaxyq.profile.ProfileException;
import com.javaxyq.profile.ProfileManager;
import com.javaxyq.profile.impl.ProfileManagerImpl;
import com.javaxyq.ui.Label;
import com.javaxyq.ui.Panel;
import com.javaxyq.ui.TextField;
import com.javaxyq.widget.Animation;

/**
 * ��Ϸ���˵�
 * @author gongdewei
 * @date 2011-5-2 create
 */
public class create_role extends PanelHandler {
	
	private String character = "0003";
	private String roleName;
	private HashMap<String, String> charNames;
	
	public void initial(PanelEvent evt) {
		super.initial(evt);
		displayRoleInfo();
	}	

	public void dispose(PanelEvent evt) {
		System.out.println("dispose: select_role ");
	}	
	
	public void selectRole(ActionEvent evt) {
		character = evt.getArgumentAsString(0);
		displayRoleInfo();
	}
	
	public void goback(ActionEvent evt) {
		Panel dlg = helper.getDialog("select_role");
		helper.hideDialog(panel);
		helper.showDialog(dlg);
	}
	
	public void gonext(ActionEvent evt) {
		//TODO create profile
		TextField field = (TextField) panel.findCompByName("role_name");
		roleName = field.getText();
		if(roleName.trim().length() == 0) {
			helper.prompt("�������½����������д�����������", 3000);
			return;
		}
		try {
			ProfileManager profileManager = application.getProfileManager();
			DataManager dataManager = application.getDataManager();
			String name = newProfileName();
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
			
			helper.prompt("���ﴴ���ɹ���", 3000);
			try {
				String profileName = profile.getName();
				application.loadProfile(profileName);
				application.enterScene();
			} catch (ProfileException e) {
				System.err.println("������Ϸ�浵ʧ��!");
				e.printStackTrace();
				helper.prompt("������Ϸ�浵ʧ��!", 3000);
			}
			
		} catch (ProfileException e) {
			e.printStackTrace();
		}
	}

	private String newProfileName() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssS");
		return sdf.format(new Date());
	}

	private void displayRoleInfo() {
		Label label = (Label) panel.findCompByName("role_head");
		Animation anim = SpriteFactory.loadAnimation("/wzife/login/photo/selected/"+getCharacterName(character)+".tcp");
		label.setAnim(anim);
		//TODO ����������Ϣ
		
	}	
	
	private String getCharacterName(String character) {
		if(charNames == null) {
			charNames= new HashMap<String, String>();
			charNames.put("0001", "��ң��");
			charNames.put("0002", "������");
			charNames.put("0003", "����Ů");
			charNames.put("0004", "ӢŮ��");
			charNames.put("0005", "��ħ��");
			charNames.put("0006", "��ͷ��");
			charNames.put("0007", "������");
			charNames.put("0008", "�Ǿ���");
			charNames.put("0009", "�����");
			charNames.put("0010", "��̫��");
			charNames.put("0011", "���켧");
			charNames.put("0012", "���ʶ�");
		}
		return charNames.get(character);
	}
}
