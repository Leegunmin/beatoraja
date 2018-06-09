package bms.player.beatoraja.result;

import bms.player.beatoraja.Resolution;
import bms.player.beatoraja.skin.Skin;


/**
 * �꺁�궢�꺂�깉�궧�궘�꺍
 */
public class MusicResultSkin extends Skin {
	
	int ranktime;
	
	public MusicResultSkin(Resolution src, Resolution dst) {
		super(src, dst);
	}
	
	public int getRankTime() {
		return ranktime;
	}

	public void setRankTime(int ranktime) {
		this.ranktime = ranktime;
	}

}
