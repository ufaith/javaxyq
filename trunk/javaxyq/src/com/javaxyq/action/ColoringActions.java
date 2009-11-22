/*
 * JavaXYQ Engine 
 * 
 * javaxyq@2008 all rights. 
 * http://www.javaxyq.com
 */

package com.javaxyq.action;

import javax.swing.ButtonGroup;

import com.javaxyq.core.GameMain;
import com.javaxyq.core.SpriteFactory;
import com.javaxyq.graph.Button;
import com.javaxyq.graph.Label;
import com.javaxyq.graph.Panel;
import com.javaxyq.graph.ToggleButton;
import com.javaxyq.util.ColorationUtil;
import com.javaxyq.widget.Animation;
import com.javaxyq.widget.Player;
import com.javaxyq.widget.Sprite;

/**
 * ������ɫ������¼�
 * 
 * @author ����ΰ
 * @history 2009-10-16 ����ΰ �½�
 */
public class ColoringActions extends BaseAction {

    private Label lblCharacter;

    private Label lblWeapon;

    private Label lblShadow;

    private int selectionPart;

    private Panel colorDialog;

    private Sprite character;

    private ToggleButton btn1;

    private ToggleButton btn2;

    private ToggleButton btn3;

    private Label textCost;

    private int[] costs = new int[3];

    private Label textMaterial;

    private String material;

    private int[] originColorations;

    private int animIndex;

    @Override
    public void doAction(ActionEvent e) {
        String cmd = e.getCommand();
        System.out.println("coloringAction: " + cmd);
        Player player = GameMain.getPlayer();
        Sprite weapon = player.getWeapon();
		if (cmd.endsWith(".initial")) {//��ʼ����ɫ���
            if (colorDialog == null) {//�����ظ���ʼ��
                colorDialog = (Panel) e.getSource();
                textMaterial = (Label) colorDialog.getComponentByName("textMaterial");
                textCost = (Label) colorDialog.getComponentByName("textCost");
                btn1 = (ToggleButton) colorDialog.getComponentByName("btn1");
                btn2 = (ToggleButton) colorDialog.getComponentByName("btn2");
                btn3 = (ToggleButton) colorDialog.getComponentByName("btn3");
                ButtonGroup bg = new ButtonGroup();
                bg.add(btn1);
                bg.add(btn2);
                bg.add(btn3);
            }
            animIndex = 0;
            costs[0] = costs[1] = costs[2] = 0;
            material = "�ʹ�";
            btn1.setSelected(true);
            this.selectionPart = 0;
            character = new Sprite(player.getPerson());
    		int[] colorations = character.getColorations();
            originColorations = colorations;
            Animation currCharacter = character.getAnimation(animIndex);
            Animation currWeapon = weapon==null?null: weapon.getAnimation(animIndex);
            Animation shadow = player.getShadow().getAnimation(0);
            preview(currCharacter, currWeapon, shadow);
        } else if (cmd.endsWith(".coloring")) {
            //color
            int count = player.getColorationCount(this.selectionPart);
            int coloration = character.getColoration(selectionPart);
            coloration = (coloration + 1) % count;
            character.setColoration(selectionPart, coloration);
            //FIXME Ⱦɫ
            //ColorationUtil.recreate(character, "shape/"+player.getCharacter()+"/00.pp");
            int[] colorations = character.getColorations();
            character = SpriteFactory.loadSprite("/shape/char/"+player.getCharacter()+"/stand.tcp",colorations);
            
            Animation currCharacter = character.getAnimation(animIndex);
            Animation currWeapon = weapon==null?null:weapon.getAnimation(animIndex);
            Animation shadow = player.getShadow().getAnimation(0);
            preview(currCharacter, currWeapon, shadow);

            costs[selectionPart] = (1 + (coloration + 1) / 3) * 10;
        } else if (cmd.endsWith(".rotate")) {
            int count = character.getAnimationCount();
            if (animIndex == count - 1) {
                animIndex = 0;
            } else if (animIndex < 4) {
                animIndex += 4;
            } else {
                animIndex += -4 + 1;
            }
            animIndex %= count;
            Animation currCharacter = character.getAnimation(animIndex);
            Animation currWeapon = weapon==null?null:weapon.getAnimation(animIndex);
            Animation shadow = player.getShadow().getAnimation(0);
            preview(currCharacter, currWeapon, shadow);
        } else if (cmd.endsWith(".section1")) {
            material = "�ʹ�";
            this.selectionPart = 0;
        } else if (cmd.endsWith(".section2")) {
            material = "����";
            this.selectionPart = 1;
        } else if (cmd.endsWith(".section3")) {
            material = "�ʹ�";
            this.selectionPart = 2;
        } else if (cmd.endsWith(".save")) {
            Button btnSave = (Button) e.getSource();
            GameMain.hideDialog((Panel) btnSave.getParent());
    		int[] colorations = character.getColorations();
            player.setColorations(colorations,true);
            System.out.println("coloring: {" + colorations[0] + "," + colorations[1]+ "," + colorations[2] + "}");
        }
        textMaterial.setText("����" + material);
        textCost.setText("" + costs[selectionPart]);
    }

    private void preview(Animation currCharacter, Animation currWeapon, Animation shadow) {
        if (lblCharacter != null) {
            colorDialog.remove(lblCharacter);
        }
        if (lblWeapon != null) {
            colorDialog.remove(lblWeapon);
        }
        if (lblShadow != null) {
            colorDialog.remove(lblShadow);
        }
        lblCharacter = new Label(currCharacter);
        lblShadow = new Label(shadow);
        if(currWeapon!=null) {       	
        	lblWeapon = new Label(currWeapon);
        	lblWeapon.setLocation(lblCharacter.getX() + currCharacter.getCenterX() - currWeapon.getCenterX(),
        			lblCharacter.getY() + currCharacter.getCenterY() - currWeapon.getCenterY());
        	colorDialog.add(lblWeapon);
        }
        lblCharacter.setLocation((colorDialog.getWidth() - lblCharacter.getWidth()) / 2, 80);
        lblShadow.setLocation(lblCharacter.getX() + currCharacter.getCenterX() - shadow.getCenterX(),
            lblCharacter.getY() + currCharacter.getCenterY() - shadow.getCenterY());
        colorDialog.add(lblCharacter);
        colorDialog.add(lblShadow);
    }

}
