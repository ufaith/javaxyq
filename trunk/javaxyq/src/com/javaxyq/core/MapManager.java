/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.core;

import java.awt.Point;
import java.util.Map;
import java.util.WeakHashMap;

import com.javaxyq.graph.Canvas;
import com.javaxyq.graph.SceneCanvas;
import com.javaxyq.resources.DefaultTileMapProvider;
import com.javaxyq.widget.TileMap;

/**
 * ��ͼ������
 * 
 * @author ����ΰ
 * @history 2008-5-30 ����ΰ �½�
 */
public class MapManager {
	private static MapManager manager = new MapManager();

	//private Map<String, TileMap> maps = new WeakHashMap<String, TileMap>();

	private String currentScene;

	private MapManager() {
	}

	public static MapManager getInstance() {
		return manager;
	}

	public void fadeToMap(String id) {
		this.fadeToMap(id, GameMain.getPlayer().getSceneLocation());
	}

	public TileMap getMap(String id) {
//		TileMap m = maps.get(id);
//		if (m == null) {
			DefaultTileMapProvider tileMapProvider = new DefaultTileMapProvider();
			TileMap m = tileMapProvider.getResource(id);
//			maps.put(id, m);
//		}
		m.setAlpha(1.0f);
		return m;
	}

	/**
	 * ������ͼ��ʾ������
	 * 
	 * @param canvas
	 * @param map
	 * @param p
	 *            player's scene coordinate
	 */
	private Point reviseViewport(SceneCanvas canvas, TileMap map, Point p) {
		Point vp = new Point(p.x * GameMain.STEP_DISTANCE, map.getHeight() - p.y * GameMain.STEP_DISTANCE);
		Point viewPosition = new Point(vp.x - 320, vp.y - 240);

		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();
		int mapWidth = map.getWidth();
		int mapHeight = map.getHeight();
		if (viewPosition.x + canvasWidth > mapWidth) {
			viewPosition.x = mapWidth - canvasWidth;
		} else if (viewPosition.x < 0) {
			viewPosition.x = 0;
		}
		if (viewPosition.y + canvasHeight > mapHeight) {
			viewPosition.y = mapHeight - canvasHeight;
		} else if (viewPosition.y < 0) {
			viewPosition.y = 0;
		}
		return viewPosition;
	}

	public void fadeToMap(String id, Point p) {
		SceneCanvas canvas = GameMain.getSceneCanvas();
		// ����
		canvas.fadeOut(400);
		// prepare map
		TileMap newmap = this.getMap(id);
		Point vp = reviseViewport(canvas, newmap, p);
		newmap.prepare(vp.x, vp.y, canvas.getWidth(), canvas.getHeight());
		// �ȴ���������
		synchronized (Canvas.FADE_LOCK) {
			// ������ͼ
			synchronized (Canvas.UPDATE_LOCK) {
				canvas.setMap(newmap);
				canvas.setPlayerSceneLocation(p);
			}
		}
		// ����
		canvas.fadeIn(300);
		this.currentScene = id;
	}

	public String getCurrentScene() {
		return currentScene;
	}

}
