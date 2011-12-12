/**
 * 
 */
package com.javaxyq.widget;

/**
 * �˶�������
 * @author gongdewei
 * @date 2011-7-24 create
 */
public interface MotionController {

	void walkTo(Character character, int x, int y);
	
	void runTo(Character character, int x, int y);
	
	void transferTo(Character character, int x, int y);
	
	void stop(Character character);
}
