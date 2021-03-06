package bms.player.beatoraja.skin.lr2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bms.player.beatoraja.MainState;
import bms.player.beatoraja.skin.SkinLoader;

public class LR2SkinLoader extends SkinLoader {

	private List<Command> commands = new ArrayList<Command>();

	protected Map<Integer, Boolean> op = new HashMap<Integer, Boolean>();

	protected void addCommandWord(Command cm) {
		commands.add(cm);
	}

	protected void addCommandWord(Command... cm) {
		commands.addAll(Arrays.asList(cm));
	}

	boolean skip = false;
	boolean ifs = false;

	protected void processLine(String line, MainState state) {
		if (!line.startsWith("#") ) {
			return;
		}
		String[] str = line.split(",", -1);
		if (str.length > 0) {
			if (str[0].equals("#IF")) {
				ifs = true;
				for (int i = 1; i < str.length; i++) {
					boolean b = false;
					if (str[i].length() == 0) {
						continue;
					}
					try {
						int opt = Integer.parseInt(str[i].replace('!', '-').replaceAll("[^0-9-]", ""));
						if(opt >=  0) {
							if(op.containsKey(opt) && op.get(opt)) {
								b = true;
							}
						} else {
							if(op.containsKey(-opt) && !op.get(-opt)) {
								b = true;
							}
						}
						if (!b && !op.containsKey(Math.abs(opt)) && state != null) {
							if(opt >= 0) {
								b = state.getBooleanValue(opt);
							} else {
								b = !state.getBooleanValue(-opt);
							}
						}
						if (!b) {
							ifs = false;
							break;
						}
					} catch (NumberFormatException e) {
						e.printStackTrace();
						break;
					}
				}

				skip = !ifs;
			} else if (str[0].equals("#ELSEIF")) {
				if (ifs) {
					skip = true;
				} else {
					ifs = true;
					for (int i = 1; i < str.length; i++) {
						boolean b = false;
						try {
							int opt = Integer.parseInt(str[i].replace('!', '-').replaceAll("[^0-9-]", ""));
							if(opt >=  0) {
								if(op.containsKey(opt) && op.get(opt)) {
									b = true;
								}
							} else {
								if(op.containsKey(-opt) && !op.get(-opt)) {
									b = true;
								}
							}
							if (!b && !op.containsKey(Math.abs(opt)) && state != null) {
								if(opt >= 0) {
									b = state.getBooleanValue(opt);
								} else {
									b = !state.getBooleanValue(-opt);
								}
							}
							if (!b) {
								ifs = false;
								break;
							}
						} catch (NumberFormatException e) {
							break;
						}
					}

					skip = !ifs;
				}
			} else if (str[0].equals("#ELSE")) {
				skip = ifs;
			} else if (str[0].equals("#ENDIF")) {
				skip = false;
				ifs = false;
			}
			if (!skip) {
				if (str[0].equals("#SETOPTION")) {
					int index = Integer.parseInt(str[1]);
					op.put(index, Integer.parseInt(str[2]) >= 1);
				}

				for (Command cm : commands) {
					if (str[0].equals("#" + cm.name())) {
						cm.execute(this, str);
					}
				}
			}
		}
	}

	public Map<Integer, Boolean> getOption() {
		return op;
	}

	public abstract class CommandWord implements Command<LR2SkinLoader> {

		public final String str;

		public String name() {
			return str;
		}
		
		public CommandWord(String str) {
			this.str = str;
		}

		public void execute(LR2SkinLoader loader, String[] values) {
			execute(values);
		}

		public abstract void execute(String[] values);

	}
	
	public interface Command<T extends LR2SkinLoader> {
		
		public abstract String name();
		public abstract void execute(T loader, String[] values);		
	}
}
