import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;
import org.apache.commons.jexl2.UnifiedJEXL;


public class JEXLTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
        JexlContext context = new MapContext();
        context.set("user", new User());
        
		JexlEngine jexl = new JexlEngine();
        UnifiedJEXL ujexl = new UnifiedJEXL(jexl);
        UnifiedJEXL.Expression expr = ujexl.parse("Hello ${(user.money+40)/3.50}%");
        String hello = expr.evaluate(context).toString();
        System.out.println(hello);
	}

	public static class User{
		private int money = 100;
		private int level = 30;
		private int skill=40;
		public int getMoney() {
			return money;
		}
		public void setMoney(int money) {
			this.money = money;
		}
		public int getLevel() {
			return level;
		}
		public void setLevel(int level) {
			this.level = level;
		}
		public int getSkill() {
			return skill;
		}
		public void setSkill(int skill) {
			this.skill = skill;
		}
		
	}
}
