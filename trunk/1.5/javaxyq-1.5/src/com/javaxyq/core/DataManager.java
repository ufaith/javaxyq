package com.javaxyq.core;

import java.util.List;
import java.util.Map;

import com.javaxyq.data.ItemInstance;
import com.javaxyq.data.Scene;
import com.javaxyq.data.SceneNpc;
import com.javaxyq.data.SceneTeleporter;
import com.javaxyq.model.Item;
import com.javaxyq.model.PlayerVO;
import com.javaxyq.widget.Player;

public interface DataManager {

	public void addExp(Player player, int exp);

	public void addHp(Player player, int hp);

	/**
	 * ��������ĳ��Ʒ
	 * @return ����ɹ�����true
	 */
	public boolean addItemToPlayer(Player player, ItemInstance item);

	public void addMoney(Player player, int money);

	public void addMp(Player player, int mp);

	/**
	 * ����ĳ���Ե�ֵ
	 * @param player
	 * @param prop
	 * @param val
	 */
	public void addProp(Player player, String prop, int val);

	public ItemInstance createItem(String name);

	public boolean existItem(String name, int amount);

	/**
	 * ����NPC��������
	 * @param npcId
	 * @return
	 */
	public String findChat(String npcId);

	/**
	 * ����ĳ���͵���Ʒ
	 * @param player
	 * @param type ��Ʒ���ͣ��ο�ItemTypes
	 * @return
	 */
	public ItemInstance[] findItems(Player player, int type);

	public List<SceneNpc> findNpcsBySceneId(int sceneId);

	/**
	 * @return 
	 * 
	 */
	public Scene findScene(int sceneId);

	public SceneNpc findSceneNpc(Integer id);

	public SceneTeleporter findSceneTeleporter(Integer id);

	public List<SceneTeleporter> findTeleportersBySceneId(int sceneId);

	public ItemInstance getItemAt(Player player, int index);

	/**
	 * ��Ʒ���Ե��ӵ��������
	 * @param item
	 * @return
	 */
	public int getOverlayAmount(Item item);

	/**
	 * ��ȡ��Ϸ��������б�
	 */
	public ItemInstance[] getItems(Player player);

	/**
	 * ��ʼ��npc���ԣ����
	 * @param vo
	 */
	public void initNPC(Player player);

	public void initPlayerData(PlayerVO vo);

	public PlayerVO createPlayerData(String character);
	
	/**
	 * ����Ĭ�ϴ浵
	 */
	public void loadData();

	public void loadDataFromFile(String filename);

	/**
	 * ������Ʒ
	 * @param srcItem Դ��Ʒ
	 * @param destItem Ŀ����Ʒ
	 * @return ���ӳɹ�����true
	 */
	public boolean overlayItems(ItemInstance srcItem,
			ItemInstance destItem);

	/**
	 * ������������ֵ
	 * 
	 * @param vo
	 */
	public void recalcElfProps(PlayerVO vo);

	/**
	 * �������������ֵ
	 * @param vo
	 */
	public void recalcProperties(PlayerVO vo);

	public boolean removeItem(String name, int amount);

	public boolean removeItemFromPlayer();

	public boolean removePlayerItem(Player player, int index);

	public void removePlayerItem(Player player, ItemInstance item);

	/**
	 * ������Ϸ���ݵ��浵
	 */
	public void saveData();

	/**
	 * ��������ĵ���
	 * @param index ����λ����ţ����϶��£������������� in 5*4 = [0,20) 
	 */
	public void setItem(Player player, int index,
			ItemInstance item);

	public void setItemByName(Player player, int index,
			String itemName);

	public void storeDataToFile(String filename);

	public void swapItem(Player player, int srcIndex, int destIndex);

	public Player createNPC(SceneNpc _npc);
	
	Player createElf(String type,String name,int level);
	
	/**
	 * ��ȡ������ݼ�ֵ��
	 * @param player
	 * @return
	 */
	public Map<String, Object> getProperties(Player player);
	
	public long getLevelExp(int level);

	public Player createPlayer(PlayerVO playerData);

	public void setItems(Player player, ItemInstance[] items);
}