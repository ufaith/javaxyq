# Introduction #

使用SwingWorker实现灵活的组合式的动画效果。


# Details #

# 怪物死亡时，清出战斗场景，使用了两个动画效果，先闪动，然后渐隐。

闪动（blink）的代码
```
	private class BlinkWorker extends SwingWorker{
		private Player player;
		private long duration;
		public BlinkWorker(Player player, long duration) {
			super();
			this.player = player;
			this.duration = duration;
		}
		@Override
		protected Object doInBackground() throws Exception {
			long minShow = 50;
			long interval = (this.duration - minShow*2)/2;
			try {
				this.player.setAlpha(0);
				Thread.sleep(interval);
				this.player.setAlpha(1.0f);
				Thread.sleep(minShow);
				this.player.setAlpha(0);
				Thread.sleep(interval);
				this.player.setAlpha(1.0f);
				Thread.sleep(minShow);
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				this.player.setAlpha(1.0f);
			}
			return null;
		}
	}
```


渐隐的动画效果：
```
	private class FadeOutWorker extends SwingWorker<Float, Float>{
		private Player player;
		private long duration;

		public FadeOutWorker(Player player,long duration) {
			this.player = player;
			this.duration = duration;
		}
		
		@Override
		protected Float doInBackground() throws Exception {
			long passTime = 0;
			long interval = 50; 
			float alpha = 1.0f;
			while (passTime < duration) {
				// System.out.println(this.getId()+" "+this.getName());
				passTime += interval;
				alpha = (float) (1 - (1.0 * passTime / duration));
				if (alpha < 0) {
					alpha = 0;
				}
				if (alpha > 1) {
					alpha = 1;
				}
				player.setAlpha(alpha);
				publish(alpha);
				try {
					Thread.sleep(interval);
				} catch (InterruptedException e) {
				}
			}
			removePlayerFromTeam(player);
			player.setAlpha(1.0f);
			System.out.println("将"+player.getName()+"移出队伍。");
			return alpha;
		}
		
		@Override
		protected void done() {
			super.done();
		}
				
	}

```


清除怪物的代码：
```
	public void cleanPlayer(Player player) {
		try {
			BlinkWorker blinker = new BlinkWorker(player, 400);
			blinker.execute();
			blinker.get();//等待闪动执行完毕
			FadeOutWorker worker = new FadeOutWorker(player, 200);
			worker.execute();
			worker.get();//等待渐隐执行完毕
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

```

以上介绍了SwingWorker的组合式使用，execute()方法启动worker执行，这个是异步的方法，当我们需要等待它完成时可以调用worker.get()，等待到worker退出doInBackground()方法。

前期设计动画效果时比较凌乱，如今再看时，觉得可以将动作分解成典型的动画，然后根据需要进行组合，岂不美哉！